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
 * 每小時天氣預報 Card
 */
@Composable
fun HourlyCard(items: List<HomeViewModel.HourUiData>, onItemClick: (Int) -> Unit) {
    // 找出當前小時的 index
//    val nowIndex = remember(items) {
//        items.indexOfFirst { it.isNowHour }.let { if (it < 0) 0 else it }
//    }
//    val nowIndex by remember(items) {
//        derivedStateOf {
//            items.indexOfFirst { it.isNowHour }.let {
//                if (it < 0) 0 else it
//            }
//        }
//    }
//    val listState = rememberLazyListState(initialFirstVisibleItemIndex = nowIndex)

//    LaunchedEffect(nowIndex) {
//        if (nowIndex > 0) {
//            listState.scrollToItem(nowIndex)
//        }
//    }

    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceContainer, shape = RShape16)
            .padding(vertical = 8.dp)
    ) {
        // 每小時天氣預報
        WeatherCardCaptionWithIcon(
            iconRes = R.drawable.ic_hours,
            caption = "每小時天氣預報"
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            state = listState,
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(
                items = items,
                key = { _, item -> item.key },
                contentType = { _, _ -> "hourly_item" }
            ) { index, item ->
                HourlyItem(
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
fun HourlyCardPreview() {
    AlexWeatherDemoTheme {
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                HourlyCard(
                    items = listOf(
                        HomeViewModel.HourUiData(
                            isNowHour = true,
                            label = "現在",
                            temp = "25°",
                            icon = R.drawable.ic_precipprob,
                            precipProb = "20%",
                            key = 1234
                        ),
                        HomeViewModel.HourUiData(
                            isNowHour = false,
                            label = "13:00",
                            temp = "26°",
                            icon = R.drawable.ic_precipprob,
                            precipProb = "10%",
                            key = 1235
                        ),
                        HomeViewModel.HourUiData(
                            isNowHour = false,
                            label = "14:00",
                            temp = "27°",
                            icon = R.drawable.ic_precipprob,
                            precipProb = "0%",
                            key = 1236
                        ),
                        HomeViewModel.HourUiData(
                            isNowHour = false,
                            label = "15:00",
                            temp = "28°",
                            icon = R.drawable.ic_precipprob,
                            precipProb = "5%",
                            key = 1237
                        )
                    ),
                    onItemClick = {}
                )
            }
        }
    }
}