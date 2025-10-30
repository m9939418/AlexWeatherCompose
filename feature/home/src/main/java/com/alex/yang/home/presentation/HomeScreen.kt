@file:Suppress("SpellCheckingInspection")

package com.alex.yang.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.alex.yang.home.presentation.component.AppDrawer
import com.alex.yang.home.presentation.component.CircleLoadingCard
import com.alex.yang.home.presentation.component.CityModalSheet
import com.alex.yang.home.presentation.component.CurrentWeatherInfoCard
import com.alex.yang.home.presentation.component.ErrorCard
import com.alex.yang.home.presentation.component.HomeTopAppBar
import com.alex.yang.home.presentation.component.HourlyCard
import com.alex.yang.home.presentation.component.WeatherOtherInfoCard
import com.alex.yang.home.presentation.component.WeeklyCard
import com.alex.yang.core_ui.component.NoNetworkDialog
import com.alex.yang.weather.domain.model.Day
import com.alex.yang.weather.domain.model.Hour
import kotlinx.coroutines.launch

/**
 * Created by AlexYang on 2025/10/16.
 *
 * 首頁 Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    navToTodayDetail: (Int, String, List<Hour>) -> Unit,
    navToWeeklyDetail: (Int, String, List<Day>) -> Unit
) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyStaggeredGridState()
    val citySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer { appRoute ->
                navController.navigate(appRoute)
                scope.launch { drawerState.close() }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            contentWindowInsets = WindowInsets(0),
            topBar = {
                HomeTopAppBar(
                    modifier = modifier,
                    title = state.selectedCity.zh,
                    onOpenClick = { scope.launch { drawerState.open() } },
                    onClick = { viewModel.updateShowCitySheet(true) },
                    onRefresh = viewModel::fetchWeather
                )
            },
        ) { innerPadding ->
            LazyVerticalStaggeredGrid(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
                    .navigationBarsPadding(),
                state = listState,
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 40.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalItemSpacing = 24.dp,
            ) {
                // 當日天氣資訊
                state.currenDay?.let { data ->
                    item(
                        key = "current_weather_info",
                        contentType = "current_weather_info",
                        span = StaggeredGridItemSpan.FullLine
                    ) {
                        CurrentWeatherInfoCard(data = data)
                    }
                }

                // 每小時天氣預報
                state.hourUiDataList.takeIf { it.isNotEmpty() }?.let {
                    item(
                        key = "hourly_weather_info",
                        contentType = "hourly_weather_info",
                        span = StaggeredGridItemSpan.FullLine,
                    ) {
                        HourlyCard(items = state.hourUiDataList) { index ->
                            navToTodayDetail(index, state.city, state.nowAfterHours)
                        }
                    }
                }

                // 本週天氣預報
                state.dayUiDataList.takeIf { it.isNotEmpty() }?.let {
                    item(
                        key = "weekly_weather_info",
                        contentType = "weekly_weather_info",
                        span = StaggeredGridItemSpan.FullLine
                    ) {
                        WeeklyCard(items = state.dayUiDataList) { index ->
                            navToWeeklyDetail(index, state.city, state.days)
                        }
                    }
                }

                // 當前其他天氣資訊
                state.otherUiDataList.takeIf { it.isNotEmpty() }?.let { list ->
                    items(
                        items = list,
                        key = { it.captionRes },
                        contentType = { "current_weather_other_info" },
                        span = { StaggeredGridItemSpan.SingleLane }
                    ) { info ->
                        WeatherOtherInfoCard(info)
                    }

                    item(
                        key = "spacer",
                        contentType = "spacer",
                        span = StaggeredGridItemSpan.FullLine
                    ) {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }

            state.errorMessage?.takeIf { it.isNotEmpty() }?.let { message ->
                ErrorCard(
                    message = message,
                    onRetry = { viewModel.fetchWeather() }
                )
            }

            if (state.isLoading) {
                CircleLoadingCard()
            }
        }
    }

    if (state.showCitySheet) {
        CityModalSheet(
            sheetState = citySheetState,
            list = state.counties,
            onDismissRequest = { viewModel.updateShowCitySheet(false) },
            onItemClick = { city -> viewModel.refresh(city) }
        )
    }

    NoNetworkDialog(
        visible = state.showDialog,
        onRetry = { viewModel.fetchWeather() },
    )
}


