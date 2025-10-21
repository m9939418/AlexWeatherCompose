package com.alex.yang.weather.demo.feature.today_detail.component

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alex.yang.weather.demo.R
import com.alex.yang.weather.demo.ui.theme.AlexWeatherDemoTheme
import com.alex.yang.weather.demo.ui.theme.Brand16Medium
import com.alex.yang.weather.demo.ui.theme.Brand18Medium
import com.alex.yang.weather.demo.ui.theme.Brand24Medium
import com.alex.yang.weather.demo.ui.theme.RShape16
import com.alex.yang.weather.domain.model.Hour

/**
 * Created by AlexYang on 2025/10/19.
 *
 *
 */
@Composable
fun HourRow(item: Hour) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceContainer, shape = RShape16)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.Top
    ) {
        // 左側（時間/狀態 + 溫度，佔 0.3 權重）
        Column(
            modifier = Modifier.weight(0.3f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 20:30:00
            Text(
                text = item.datetime,
                style = Brand18Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(end = 8.dp)
            )

            Spacer(Modifier.height(8.dp))

            // 天氣圖示
            Image(
                modifier = Modifier
                    .size(48.dp)
                    .padding(top = 8.dp, bottom = 6.dp),
                painter = painterResource(id = item.icon),
                contentDescription = null
            )

            Spacer(Modifier.height(8.dp))

            // 26.0°
            Text(
                text = "${item.temp}°",
                style = Brand24Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // 右側指標（佔 0.7 權重）
        Column(
            modifier = Modifier.weight(0.7f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // 雨, 部分多云
            Text(
                text = item.conditions,
                style = Brand16Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(4.dp))

            // 體感溫度
            MetricRow(
                icon = R.drawable.ic_feellike,
                label = "體感溫度",
                value = "${item.feelslike}°"
            )

            // 降雨
            MetricRow(
                icon = R.drawable.ic_precipprob,
                label = "降雨",
                value = "${item.precipprob}%"
            )

            // 風速
            MetricRow(
                icon = R.drawable.ic_windgust,
                label = "風速",
                value = "${item.windspeed} km/h"
            )

            // 雲蓋
            MetricRow(
                icon = R.drawable.ic_cloudcover,
                label = "雲蓋",
                value = "${item.cloudcover}%"
            )
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
fun HourRowPreview() {
    AlexWeatherDemoTheme {
        Scaffold { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .background(color = Color.Cyan)
            ) {
                HourRow(
                    item = Hour(
                        datetime = "20:30:00",
                        icon = 0,
                        temp = 26,
                        feelslike = 25,
                        windspeed = 15.0,
                        datetimeEpoch = 1760630400,
                        humidity = 72.9,
                        dew = 22.8,
                        precipprob = 11,
                        windgust = 25.2,
                        pressure = 1000.0,
                        cloudcover = 44,
                        visibility = 16.9,
                        uvindex = 8.0,
                        sunrise = "05:52:50",
                        sunset = "17:52:50",
                        conditions = "雨, 部分多云",
                    )
                )
            }
        }
    }
}