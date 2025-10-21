package com.alex.yang.weather.demo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.alex.yang.weather.demo.feature.home.presentation.HomeScreen
import com.alex.yang.weather.demo.feature.home.presentation.HomeSharedViewModel
import com.alex.yang.weather.demo.feature.today_detail.TodayDetailScreen
import com.alex.yang.weather.demo.feature.web.WebScreen
import com.alex.yang.weather.demo.feature.weekly_detail.WeeklyDetailScreen

/**
 * Created by AlexYang on 2025/10/16.
 *
 *
 */
@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AppRoute.Home,
    ) {
        /**
         * 首頁 Router
         *
         * 顯示天氣首頁，包含今日和每週天氣概覽。
         * 提供導航到詳細頁面的功能，並透過 SharedViewModel 傳遞資料。
         */
        composable<AppRoute.Home> { backEntry ->
            val sharedViewModel: HomeSharedViewModel = hiltViewModel(backEntry)

            HomeScreen(
                modifier = modifier,
                navController = navController,
                navToTodayDetail = { index, city, list ->
                    sharedViewModel.setHours(list)
                    navController.navigate(AppRoute.TodayDetail(index = index))
                },
                navToWeeklyDetail = { index, city, list ->
                    sharedViewModel.setDays(list)
                    navController.navigate(AppRoute.WeeklyDetail(index = index, city = city))
                }
            )
        }

        /**
         * 網頁瀏覽 Router
         *
         * 在應用內顯示網頁內容。
         * 從路由參數中取得 URL 和標題。
         */
        composable<AppRoute.Web> { backEntry ->
            WebScreen(
                modifier = modifier,
                navController = navController,
                caption = backEntry.toRoute<AppRoute.Web>().caption,
                url = backEntry.toRoute<AppRoute.Web>().url,
            )
        }

        /**
         * 今日天氣詳細 Router
         *
         * 顯示特定時段的詳細天氣資訊。
         * 從 HomeSharedViewModel 取得小時資料列表，
         * 透過 index 參數定位要顯示的具體時段。
         *
         * 注意：使用 remember 和 getBackStackEntry 來取得
         * Home 畫面的 BackStackEntry，確保使用同一個 ViewModel 實例。
         */
        composable<AppRoute.TodayDetail> { backEntry ->
            val homeEntry = remember(backEntry) {
                navController.getBackStackEntry(AppRoute.Home)
            }
            val sharedViewModel: HomeSharedViewModel = hiltViewModel(homeEntry)
            val list by sharedViewModel.hours.collectAsStateWithLifecycle()

            TodayDetailScreen(
                modifier = modifier,
                navController = navController,
                index = backEntry.toRoute<AppRoute.TodayDetail>().index,
                list = list
            )
        }

        /**
         * 每週天氣詳細 Router
         *
         * 顯示特定日期的詳細天氣資訊。
         * 從 HomeSharedViewModel 取得天數資料列表，
         * 透過 index 參數定位要顯示的具體日期，
         * 並使用 city 參數顯示城市資訊。
         *
         * 注意：使用 remember 和 getBackStackEntry 來取得
         * Home 畫面的 BackStackEntry，確保使用同一個 ViewModel 實例。
         */
        composable<AppRoute.WeeklyDetail> { backEntry ->
            val homeEntry = remember(backEntry) {
                navController.getBackStackEntry(AppRoute.Home)
            }
            val sharedViewModel: HomeSharedViewModel = hiltViewModel(homeEntry)
            val list by sharedViewModel.days.collectAsStateWithLifecycle()

            WeeklyDetailScreen(
                modifier = modifier,
                navController = navController,
                index = backEntry.toRoute<AppRoute.WeeklyDetail>().index,
                city = backEntry.toRoute<AppRoute.WeeklyDetail>().city,
                list = list
            )
        }
    }
}
