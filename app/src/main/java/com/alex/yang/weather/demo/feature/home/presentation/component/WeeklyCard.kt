package com.alex.yang.weather.demo.feature.home.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alex.yang.weather.demo.R
import com.alex.yang.weather.demo.feature.home.presentation.HomeViewModel
import com.alex.yang.weather.demo.ui.theme.AlexWeatherDemoTheme
import com.alex.yang.weather.demo.ui.theme.RShape16

/**
 * Created by AlexYang on 2025/10/18.
 *
 * 本週天氣預報 Card
 */
@Composable
fun WeeklyCard(items: List<HomeViewModel.DayUiData>, onItemClick: (Int) -> Unit) {
//    val todayIndex = remember(items) {
//        items.indexOfFirst { it.isToday }.let { if (it < 0) 0 else it }
//    }
//    val listState = rememberLazyListState(initialFirstVisibleItemIndex = todayIndex)

//    LaunchedEffect(todayIndex) {
//        if (todayIndex > 0) {
//            listState.scrollToItem(todayIndex)
//        }
//    }

    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceContainer, shape = RShape16)
            .padding(vertical = 8.dp)
    ) {
        WeatherCardCaptionWithIcon(
            iconRes = R.drawable.ic_calendar,
            caption = "本週天氣預報"
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            state = listState,
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            itemsIndexed(
                items = items,
                key = { _, item -> item.key },
                contentType = { _, _ -> "weekly_item" }
            ) { index, item ->
                WeeklyItem(
                    data = item,
                    onClick = { onItemClick(index) }
                )
            }
        }
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
fun WeeklyCardPreview() {
    AlexWeatherDemoTheme {
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                WeeklyCard(
                    items = listOf(
                        HomeViewModel.DayUiData(
                            isToday = true,
                            topLabel = "今天",
                            bottomLabel = "10/20",
                            iconRes = R.drawable.ic_precipprob,
                            tempMax = "30°C",
                            tempMin = "22°C",
                            precipProb = "10%",
                            key = 1
                        ),
                        HomeViewModel.DayUiData(
                            isToday = false,
                            topLabel = "週二",
                            bottomLabel = "10/21",
                            iconRes = R.drawable.ic_precipprob,
                            tempMax = "28°C",
                            tempMin = "21°C",
                            precipProb = "60%",
                            key = 2
                        ),
                        HomeViewModel.DayUiData(
                            isToday = false,
                            topLabel = "週三",
                            bottomLabel = "10/22",
                            iconRes = R.drawable.ic_precipprob,
                            tempMax = "31°C",
                            tempMin = "23°C",
                            precipProb = "0%",
                            key = 3
                        )
                    ),
                    onItemClick = {}
                )
            }
        }
    }
}

