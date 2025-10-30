@file:Suppress("SpellCheckingInspection")

package com.alex.yang.home.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.yang.home.R
import com.alex.yang.weather.core.extension.asComma
import com.alex.yang.weather.core.network.NetworkManager
import com.alex.yang.weather.core.network.Resource
import com.alex.yang.weather.core.utils.DayLabel
import com.alex.yang.weather.core.utils.HourLabel
import com.alex.yang.weather.domain.model.County
import com.alex.yang.weather.domain.model.Day
import com.alex.yang.weather.domain.model.Hour
import com.alex.yang.weather.domain.model.Timeline
import com.alex.yang.weather.domain.repository.ConfigRepository
import com.alex.yang.weather.domain.repository.DataStoreRepository
import com.alex.yang.weather.domain.usecase.FindTodayDayUseCase
import com.alex.yang.weather.domain.usecase.GetWeatherUseCase
import com.alex.yang.weather.domain.usecase.IsCurrentHourUseCase
import com.alex.yang.weather.domain.usecase.IsTodayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import javax.annotation.concurrent.Immutable
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * Created by AlexYang on 2025/10/16.
 *
 * 首頁 ViewModel
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val findTodayDayUseCase: FindTodayDayUseCase,
    private val isCurrentHourUseCase: IsCurrentHourUseCase,
    private val isTodayUseCase: IsTodayUseCase,
//    private val preferences: AppPreferences,
//    private val configRepository: ConfigRepository,
    private val preferences: DataStoreRepository,
    private val configRepository: ConfigRepository,

    private val networkManager: NetworkManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState())
    val uiState = _uiState.asStateFlow()

    private val formatter = DateTimeFormat.forPattern("yyyy-MM-dd")
    private val now = DateTime.now(DateTimeZone.getDefault()).withTimeAtStartOfDay()

    private val startDayString = now.toString(formatter)    // 取得今天日期字串
    private val endDayString = now.plusDays(7).toString(formatter)  // 取得未來七天字串

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedCity = preferences.getSelectedCounty().firstOrNull() ?: County.DEFAULT
                )
            }
        }
        fetchWeather()
    }

    fun fetchWeather() {
        viewModelScope.launch {
            networkManager.guardOnline(
                doOnAvailable = {
                    _uiState.update { it.copy(isLoading = true, showDialog = false) }

                    delay(200)

                    when (val result = getWeatherUseCase(
                        location = "${_uiState.value.selectedCity.en},TW",
                        startDay = startDayString,
                        endDay = endDayString,
                        apiKey = preferences.getApiKey().first()
                    )) {
                        is Resource.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = null,
                                    timeline = result.data,
                                    currenDay = findTodayDayUseCase(result.data.days),
                                    hourUiDataList = prepareHourItems(result.data),
                                    dayUiDataList = prepareDayItems(result.data.days),
                                    otherUiDataList = prepareOtherWeatherItems(result.data.currentConditions),
                                    counties = configRepository.getCounties().counties,
                                    days = result.data.days,
                                    city = _uiState.value.selectedCity.zh
                                )
                            }
                        }

                        is Resource.Error -> {
                            _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                            Log.e("DEBUG", "[DEBUG] fetchWeather Error: ${result.message}")
                        }
                    }
                },
                doOnLost = {
                    _uiState.update { it.copy(isLoading = false, showDialog = true) }
                }
            )
        }
    }

    fun refresh(data: County) {
        _uiState.update { it.copy(selectedCity = data, showCitySheet = false) }

        viewModelScope.launch {
            preferences.saveSelectedCounty(data)
        }

        fetchWeather()
    }

    private fun prepareOtherWeatherItems(hour: Hour): List<OtherUiData> {
        return listOf(
            OtherUiData(
                "日出和日落",
                R.drawable.ic_sun,
                "",
                "",
                "日出：${hour.sunrise}\n日落：${hour.sunset}"
            ),
            OtherUiData(
                "紫外線指數",
                R.drawable.ic_uvindex,
                hour.uvindex.roundToInt().toString(),
                "",
                "紫外線指數 (1-10)"
            ),
            OtherUiData(
                "濕度",
                R.drawable.ic_humidity,
                hour.humidity.roundToInt().toString(),
                " %",
                "${hour.dew.roundToInt()}° 露點"
            ),
            OtherUiData(
                "能見度",
                R.drawable.ic_visibility,
                hour.visibility.roundToInt().toString(),
                " km",
                "平均能見度"
            ),
            OtherUiData(
                "風",
                R.drawable.ic_windgust,
                hour.windspeed.roundToInt().toString(),
                " km/h",
                "當日最大平均風速"
            ),
            OtherUiData("氣壓", R.drawable.ic_pressure, hour.pressure.asComma, "", "hPa / mbar")
        )
    }

    private fun prepareHourItems(timeline: Timeline): List<HourUiData> {
        val nowStartEpoch = DateTime.now(DateTimeZone.getDefault())
            .withMinuteOfHour(0)
            .withSecondOfMinute(0)
            .withMillisOfSecond(0)
            .millis / 1000

        // 找到今天在 days 的 index
        val todayIndex = timeline.days.indexOfFirst { it.datetime == startDayString }
        if (todayIndex < 0) return emptyList()

        val today = timeline.days[todayIndex]

        // 今天「當前小時(含)之後」的資料
        val todayFuture = today.hours.filter { it.datetimeEpoch >= nowStartEpoch }

        // 不夠 minCount 就用「明天」補前幾個小時
        val need = (16 - todayFuture.size).coerceAtLeast(0)
        val tomorrow = timeline.days.getOrNull(todayIndex + 1)
        val tomorrowHead = if (need > 0) tomorrow?.hours.orEmpty().take(need) else emptyList()

        val displayHours = todayFuture + tomorrowHead

        _uiState.update { it.copy(nowAfterHours = displayHours) }

        return displayHours.map { hour ->
            HourUiData(
                key = hour.datetimeEpoch,
                label = HourLabel.label(hour.datetimeEpoch),
                temp  = "${hour.temp}°",
                icon  = hour.icon,
                precipProb = "${hour.precipprob}%",
                isNowHour  = isCurrentHourUseCase(hour.datetimeEpoch)
            )
        }
    }

    private fun prepareDayItems(list: List<Day>): List<DayUiData> {
        return list.map { day ->
            val (top, bottom) = DayLabel.label(day.datetimeEpoch)

            DayUiData(
                key = day.datetimeEpoch,
                topLabel = top,
                bottomLabel = bottom,
                tempMax = "${day.tempmax}°",
                tempMin = "${day.tempmin}°",
                iconRes = day.icon,
                precipProb = "${day.precipprob.roundToInt()}%",
                isToday = isTodayUseCase(day.datetimeEpoch)
            )
        }
    }

    fun updateShowCitySheet(show: Boolean) {
        _uiState.update { it.copy(showCitySheet = show) }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val errorMessage: String? = null,

        val timeline: Timeline? = null,
        val currenDay: Day? = null,
        val hourUiDataList: List<HourUiData> = emptyList(),
        val dayUiDataList: List<DayUiData> = emptyList(),
        val otherUiDataList: List<OtherUiData> = emptyList(),
        val counties: List<County> = emptyList(),
        val nowAfterHours: List<Hour> = emptyList(),
        val days: List<Day> = emptyList(),
        val city: String = "",

        val selectedCity: County = County.DEFAULT,
        val showCitySheet: Boolean = false,
        val showDialog: Boolean = false,
    )

    @Immutable
    data class HourUiData(
        val key: Long,
        val label: String,
        val temp: String,
        val icon: Int,
        val precipProb: String,
        val isNowHour: Boolean
    )

    @Immutable
    data class DayUiData(
        val key: Long,
        val topLabel: String,
        val bottomLabel: String,
        val tempMax: String,
        val tempMin: String,
        val iconRes: Int,
        val precipProb: String,
        val isToday: Boolean
    )

    @Immutable
    data class OtherUiData(
        val caption: String,
        val captionRes: Int,
        val desc: String,
        val degree: String,
        val desc2: String? = null
    )
}

