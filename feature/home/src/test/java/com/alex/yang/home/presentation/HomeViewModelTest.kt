package com.alex.yang.home.presentation

import com.alex.yang.home.util.MainDispatcherRule
import com.alex.yang.home.util.MockNetworkStateDetectImpl
import com.alex.yang.home.util.MockWeatherRepository
import com.alex.yang.home.util.fakeCounties
import com.alex.yang.home.util.fakeCounty
import com.alex.yang.home.util.fakeTimelineForToday
import com.alex.yang.weather.core.network.NetworkStateDetect
import com.alex.yang.weather.core.network.Resource
import com.alex.yang.weather.domain.model.County
import com.alex.yang.weather.domain.repository.ConfigRepository
import com.alex.yang.weather.domain.repository.DataStoreRepository
import com.alex.yang.weather.domain.usecase.FindTodayDayUseCase
import com.alex.yang.weather.domain.usecase.GetWeatherUseCase
import com.alex.yang.weather.domain.usecase.IsCurrentHourUseCase
import com.alex.yang.weather.domain.usecase.IsTodayUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

/**
 * Created by AlexYang on 2025/10/31.
 *
 * 單元測試：針對 HomeViewModel 進行測試
 *
 * 測試重點：
 * - 以「可測 + 可替換」為原則，不 mock operator invoke 的 UseCase，改用真實 UseCase + 假 Repository。
 * - 以「可控網路狀態」為前提，透過 MockNetworkStateDetectImpl.online 切換 available / lost 分支。
 * - 以 runTest + StandardTestDispatcher + advanceUntilIdle() 收斂所有非同步工作（init → fetch → 映射 → setState）。
 *
 * 覆蓋情境（EC）：
 *  EC1  fetchWeather 成功 → UI 正確更新
 *  EC2  fetchWeather 失敗 → errorMessage 設定、停止 loading
 *  EC3  網路中斷 → showDialog 為 true、停止 loading
 *  EC4  refresh(newCity) → 更新 selectedCity、呼叫 prefs.saveSelectedCounty(newCity)
 *  EC5  updateShowCitySheet(true) → showCitySheet 為 true
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var dataStoreRepository: DataStoreRepository
    private lateinit var configRepository: ConfigRepository

    private lateinit var networkStateDetect: NetworkStateDetect
    private lateinit var mockWeatherRepository: MockWeatherRepository

    private lateinit var getWeatherUseCase: GetWeatherUseCase
    private lateinit var findTodayDayUseCase: FindTodayDayUseCase
    private lateinit var isCurrentHourUseCase: IsCurrentHourUseCase
    private lateinit var isTodayUseCase: IsTodayUseCase

    /**
     * 測試前置：
     * - 假 DataStore / Config：回傳固定 Flow 與資料
     * - MockNetworkStateDetectImpl：以 online=true 為預設
     * - MockWeatherRepository：由個別測試指定 nextResult（Success/Error）
     * - 真實 UseCase：確保映射與時間判斷走真實邏輯
     */
    @Before
    fun setUp() {
        dataStoreRepository = mockk(relaxed = true)
        configRepository = mockk(relaxed = true)

        every { dataStoreRepository.getSelectedCounty() } returns flowOf(fakeCounty())
        every { dataStoreRepository.getApiKey() } returns flowOf("API_KEY")
        coEvery { dataStoreRepository.saveSelectedCounty(any()) } returns Unit
        coEvery { configRepository.getCounties() } returns fakeCounties()

        networkStateDetect = MockNetworkStateDetectImpl(online = true)
        mockWeatherRepository = MockWeatherRepository()

        getWeatherUseCase = GetWeatherUseCase(mockWeatherRepository)
        findTodayDayUseCase = FindTodayDayUseCase()
        isCurrentHourUseCase = IsCurrentHourUseCase()
        isTodayUseCase = IsTodayUseCase()
    }

    private fun setUpViewModel() = HomeViewModel(
        getWeatherUseCase = getWeatherUseCase,
        findTodayDayUseCase = findTodayDayUseCase,
        isCurrentHourUseCase = isCurrentHourUseCase,
        isTodayUseCase = isTodayUseCase,
        preferences = dataStoreRepository,
        configRepository = configRepository,
        networkStateDetect = networkStateDetect
    )

    /**
     * EC1：fetchWeather 成功 → UI 正確更新
     *
     * Given：online = true、WeatherRepository 回傳 Success(fakeTimelineForToday())
     * When ：建立 ViewModel（init→fetch→映射），advanceUntilIdle() 收斂非同步
     * Then ：isLoading=false、errorMessage=null，UI 各區塊清單/欄位應有資料
     */
    @Test
    fun `EC1 - success updates UI`() = runTest {
        // Given
        (networkStateDetect as MockNetworkStateDetectImpl).online = true
        mockWeatherRepository.nextResult = Resource.Success(fakeTimelineForToday())

        // When
        val viewModel = setUpViewModel()
        advanceUntilIdle()

        // Then
        val result = viewModel.uiState.value
        assertThat(result.isLoading).isFalse()
        assertThat(result.errorMessage).isNull()
        assertThat(result.hourUiDataList).isNotEmpty()
        assertThat(result.dayUiDataList).isNotEmpty()
        assertThat(result.otherUiDataList).isNotEmpty()
        assertThat(result.counties).isNotEmpty()
    }

    /**
     * EC2：fetchWeather 失敗 → errorMessage 設定、停止 loading
     *
     * Given：online = true、WeatherRepository 回傳 Error("boom")
     * When ：建立 ViewModel，advanceUntilIdle()
     * Then ：errorMessage="boom"、isLoading=false
     */
    @Test
    fun `EC2 - error sets message`() = runTest {
        // Given
        (networkStateDetect as MockNetworkStateDetectImpl).online = true
        mockWeatherRepository.nextResult = Resource.Error("boom")

        // When
        val viewModel = setUpViewModel()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.errorMessage).isEqualTo("boom")
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    /**
     * EC3：網路中斷 → showDialog=true、isLoading=false
     *
     * Given：online = false（觸發 lost 分支）
     * When ：建立 ViewModel，advanceUntilIdle()
     * Then ：showDialog=true、isLoading=false
     */
    @Test
    fun `EC3 - offline shows dialog`() = runTest {
        // Given
        (networkStateDetect as MockNetworkStateDetectImpl).online = false

        // When
        val viewModel = setUpViewModel()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.showDialog).isTrue()
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    /**
     * EC4：refresh(newCity) → 更新 selectedCity、呼叫 prefs.saveSelectedCounty(newCity)
     *
     * Given：online = true、初次 fetch 成功
     * When ：呼叫 refresh(newCity)，advanceUntilIdle()
     * Then ：selectedCity==newCity，並 verify saveSelectedCounty(newCity) 被呼叫一次
     */
    @Test
    fun `EC4 - refresh saves and refetch`() = runTest {
        // Given
        (networkStateDetect as MockNetworkStateDetectImpl).online = true
        mockWeatherRepository.nextResult = Resource.Success(fakeTimelineForToday())

        val viewModel = setUpViewModel()
        advanceUntilIdle()  // 等待 init → 第一次 fetch 完成

        // When
        val newCity = County(zh = "台北市", en = "Taipei")
        viewModel.refresh(newCity)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.selectedCity).isEqualTo(newCity)
        coVerify(exactly = 1) { dataStoreRepository.saveSelectedCounty(newCity) }
    }

    /**
     * EC5：updateShowCitySheet(true) → UI 旗標變更
     *
     * Given：任意初始狀態
     * When ：呼叫 updateShowCitySheet(true)
     * Then ：showCitySheet=true
     */
    @Test
    fun `EC5 - updateShowCitySheet sets true`() = runTest {
        // Given
        (networkStateDetect as MockNetworkStateDetectImpl).online = true
        mockWeatherRepository.nextResult = Resource.Success(fakeTimelineForToday())

        val viewModel = setUpViewModel()
        advanceUntilIdle()

        // When
        viewModel.updateShowCitySheet(true)

        // Then
        assertThat(viewModel.uiState.value.showCitySheet).isTrue()
    }
}