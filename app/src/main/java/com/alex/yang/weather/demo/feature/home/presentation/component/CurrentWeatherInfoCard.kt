package com.alex.yang.weather.demo.feature.home.presentation.component

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alex.yang.weather.demo.R
import com.alex.yang.weather.demo.ui.theme.AlexWeatherDemoTheme
import com.alex.yang.weather.demo.ui.theme.Brand20Medium
import com.alex.yang.weather.demo.ui.theme.Brand80Medium
import com.alex.yang.weather.domain.model.Day

/**
 * Created by AlexYang on 2025/10/18.
 *
 * 當前天氣資訊 Card
 */
@Composable
fun CurrentWeatherInfoCard(modifier: Modifier = Modifier, data: Day) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // 天氣圖示
            Image(
                modifier = Modifier.size(48.dp),
                painter = painterResource(data.icon),
                contentDescription = null
            )
            // 部分多雲
            Text(
                modifier = Modifier.padding(start = 8.dp),
                style = Brand20Medium,
                color = MaterialTheme.colorScheme.onSurface,
                text = data.conditions
            )
        }

        // 32°
        Text(
            modifier = Modifier.padding(vertical = 16.dp),
            style = Brand80Medium,
            color = MaterialTheme.colorScheme.onSurface,
            text = "${data.temp}°"
        )

        // 體感溫度: 32°
        // 最高溫: 32°・最低溫:24°
        Text(
            style = Brand20Medium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            text = "體感溫度:${data.feelslike}\n最高溫:${data.tempmax}°・最低溫:${data.tempmin}°"
        )

        Spacer(modifier = Modifier.height(16.dp))
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
fun CurrentWeatherInfoCardPreview() {
    AlexWeatherDemoTheme {
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                CurrentWeatherInfoCard(
                    data = Day(
                        temp = 32,
                        feelslike = 32,
                        tempmax = 32,
                        tempmin = 24,
                        icon = R.drawable.ic_precipprob,
                        conditions = "部分多雲",
                        datetime = "TODO()",
                        datetimeEpoch = 100,
                        humidity = 20.3,
                        dew = 20.3,
                        precipprob = 20.3,
                        windgust = 20.3,
                        windspeed = 20.3,
                        pressure = 20.3,
                        cloudcover = 20.3,
                        visibility = 20.3,
                        uvindex = 20.3,
                        sunrise = "TODO()",
                        sunset = "TODO()",
                        hours = emptyList()
                    )
                )
            }
        }
    }
}