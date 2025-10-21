package com.alex.yang.weather.demo.feature.home.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.alex.yang.weather.demo.feature.home.presentation.HomeViewModel
import com.alex.yang.weather.demo.ui.theme.AlexWeatherDemoTheme
import com.alex.yang.weather.demo.ui.theme.Brand16Bold
import com.alex.yang.weather.demo.ui.theme.Brand16Medium

/**
 * Created by AlexYang on 2025/10/18.
 *
 *
 */
@Composable
fun HourlyItem(data: HomeViewModel.HourUiData, onClick: () -> Unit) {
    val textStyle = if (data.isNowHour) Brand16Bold else Brand16Medium
    val textColor = if (data.isNowHour) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.outline
    }

    Column(
        modifier = Modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 時間和溫度
        Text(
            style = textStyle,
            color = textColor,
            textAlign = TextAlign.Center,
            text = "${data.label}\n\n${data.temp}"
        )

        // 天氣圖示
        Image(
            modifier = Modifier
                .size(40.dp)
                .padding(top = 8.dp, bottom = 6.dp),
            painter = painterResource(data.icon),
            contentDescription = null
        )

        // 降雨機率
        Text(
            style = textStyle,
            color = textColor,
            text = data.precipProb
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
fun HourlyItemPreview() {
    AlexWeatherDemoTheme {
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                HourlyItem(
                    data = HomeViewModel.HourUiData(
                        isNowHour = true,
                        label = "現在",
                        temp = "25°",
                        icon = R.drawable.ic_precipprob,
                        precipProb = "20%",
                        key = 1234
                    ),
                    onClick = {}
                )
            }
        }
    }
}