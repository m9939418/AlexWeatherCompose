package com.alex.yang.weather.demo.feature.home.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.alex.yang.weather.demo.R
import com.alex.yang.weather.demo.feature.home.presentation.HomeViewModel
import com.alex.yang.weather.demo.ui.theme.AlexWeatherDemoTheme
import com.alex.yang.weather.demo.ui.theme.Brand18Medium
import com.alex.yang.weather.demo.ui.theme.Brand45Medium
import com.alex.yang.weather.demo.ui.theme.RShape16
import com.alex.yang.weather.demo.ui.theme.Span20Medium
import com.alex.yang.weather.demo.ui.theme.Span45Medium

/**
 * Created by AlexYang on 2025/10/18.
 *
 * 當日其他天氣資訊 Card
 */
@Composable
fun WeatherOtherInfoCard(info: HomeViewModel.OtherUiData) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val (icon, text) = remember(info.captionRes, info.caption) {
        val icon = mapOf(
            "icon" to InlineTextContent(
                placeholder = Placeholder(
                    width = 1.5.em,
                    height = 1.5.em,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                )
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(info.captionRes),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        )
        val text = buildAnnotatedString {
            appendInlineContent(id = "icon", alternateText = "[icon]")
            append(" ")
            append(info.caption)
        }
        icon to text
    }
    val descAnnotated = remember(info.desc, info.degree) {
        buildAnnotatedString {
            withStyle(style = Span45Medium.copy(color = onSurface)) {
                append(info.desc)
            }
            withStyle(style = Span20Medium.copy(color = onSurface)) {
                append(info.degree)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(color = MaterialTheme.colorScheme.surfaceContainer, shape = RShape16)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 標題
        Text(
            modifier = Modifier.padding(start = 6.dp),
            style = Brand18Medium,
            color = onSurface,
            maxLines = 1,
            inlineContent = icon,
            text = text
        )

        // 數值
        Text(
            style = Brand45Medium,
            maxLines = 1,
            text = descAnnotated
        )

        // 補充說明
        info.desc2?.let {
            Text(
                style = Brand18Medium,
                color = onSurface,
                maxLines = 2,
                textAlign = TextAlign.Center,
                text = it
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
fun WeatherOtherInfoCardPreview() {
    AlexWeatherDemoTheme {
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                WeatherOtherInfoCard(
                    info = HomeViewModel.OtherUiData(
                        captionRes = R.drawable.ic_humidity,
                        caption = "濕度",
                        desc = "78",
                        degree = "%",
                        desc2 = "今天的平均濕度"
                    )
                )
            }
        }
    }
}