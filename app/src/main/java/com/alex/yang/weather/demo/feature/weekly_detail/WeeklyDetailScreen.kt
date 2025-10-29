package com.alex.yang.weather.demo.feature.weekly_detail

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alex.yang.mock.fakeWeeklyDaysForPreview
import com.alex.yang.weather.core.utils.DayLabel
import com.alex.yang.weather.demo.R
import com.alex.yang.weather.demo.feature.home.presentation.component.WeatherCardCaptionWithIcon
import com.alex.yang.weather.demo.feature.today_detail.component.HourRow
import com.alex.yang.weather.demo.ui.theme.AlexWeatherDemoTheme
import com.alex.yang.weather.demo.ui.theme.Brand14Medium
import com.alex.yang.weather.demo.ui.theme.Brand20Medium
import com.alex.yang.weather.demo.ui.theme.Brand24Medium
import com.alex.yang.weather.demo.ui.theme.Brand50Medium
import com.alex.yang.weather.demo.ui.theme.RShape16
import com.alex.yang.weather.domain.model.Day
import kotlinx.coroutines.launch

/**
 * Created by AlexYang on 2025/10/20.
 *
 * 本週詳細天氣預報 Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    index: Int,
    city: String,
    list: List<Day>
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = index.coerceIn(0, (list.size - 1).coerceAtLeast(0)),
        pageCount = { list.size }
    )

    val currentTitle by remember(list, pagerState.currentPage) {
        derivedStateOf {
            val epoch = list.getOrNull(pagerState.currentPage)?.datetimeEpoch
            epoch?.let { DayLabel.fmtWeeklyDetailTabLabel(it).replace("\n", " ") } ?: ""
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                title = {
                    Text(
                        style = Brand20Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        text = currentTitle
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // --- TabRow (上方) ---
            PrimaryScrollableTabRow(
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent,
                contentColor = Color.Transparent,
                selectedTabIndex = pagerState.currentPage,
                indicator = {},
                divider = {},
                edgePadding = 8.dp
            ) {
                list.forEachIndexed { i, day ->
                    val isSelected = pagerState.currentPage == i

                    Tab(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 10.dp)
                            .background(
                                color = if (isSelected) {
                                    MaterialTheme.colorScheme.onSurface
                                } else {
                                    MaterialTheme.colorScheme.surface
                                },
                                shape = RShape16
                            ),
                        selected = isSelected,
                        text = {
                            Text(
                                style = Brand14Medium,
                                color = if (isSelected) {
                                    MaterialTheme.colorScheme.surface
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                },
                                textAlign = TextAlign.Center,
                                text = DayLabel.fmtWeeklyDetailTabLabel(day.datetimeEpoch)
                            )
                        },
                        onClick = { scope.launch { pagerState.animateScrollToPage(i) } },
                    )
                }
            }

            // --- Pager (下方撐滿) ---
            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                state = pagerState
            ) { page ->
                DayHoursList(day = list[page], city = city)
            }
        }
    }
}

@Composable
private fun DayHoursList(modifier: Modifier = Modifier, day: Day, city: String) {
    val state = rememberLazyListState(0)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        state = state,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item(
            key = "today_summary_card",
            contentType = "today_summary_card"
        ) {
            TodaySummaryCard(day, city)
        }

        item(
            key = "hourly_caption",
            contentType = "hourly_caption"
        ) {
            WeatherCardCaptionWithIcon(
                iconRes = R.drawable.ic_hours,
                caption = "每小時天氣預報"
            )
        }

        itemsIndexed(
            items = day.hours,
            key = { index, item -> "hourly_card_${item.datetimeEpoch}$index" },
            contentType = { _, _ -> "hourly_card" }
        ) { _, hour ->
            HourRow(item = hour)
        }

        item { Spacer(modifier = Modifier.height(100.dp)) }
    }
}

@Composable
fun TodaySummaryCard(day: Day, city: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 40.dp)
    ) {
        // 台北市
        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            style = Brand24Medium,
            color = MaterialTheme.colorScheme.onSurface,
            text = city
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 25°
            Text(
                style = Brand50Medium,
                color = MaterialTheme.colorScheme.onSurface,
                text = "${day.tempmax}°"
            )
            // 22°
            Text(
                modifier = Modifier.padding(start = 16.dp),
                style = Brand50Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                text = "${day.tempmin}°"
            )
            Image(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(64.dp),
                painter = painterResource(day.icon),
                contentDescription = null,
            )
        }
        // 多雲
        Text(
            modifier = Modifier.padding(top = 16.dp),
            style = Brand24Medium,
            color = MaterialTheme.colorScheme.onSurface,
            text = day.conditions
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Light Mode"
)
@Preview(
    showBackground = true,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
fun WeeklyDetailScreenPreview() {
    val fakeDays = fakeWeeklyDaysForPreview()

    AlexWeatherDemoTheme {
        Scaffold { innerPadding ->
            WeeklyDetailScreen(
                modifier = Modifier.padding(innerPadding),
                navController = NavController(LocalContext.current),
                index = 0,
                city = "台北市",
                list = fakeDays
            )
        }
    }
}