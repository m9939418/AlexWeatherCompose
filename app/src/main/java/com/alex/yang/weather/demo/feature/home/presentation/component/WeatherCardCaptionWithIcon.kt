package com.alex.yang.weather.demo.feature.home.presentation.component

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.alex.yang.weather.demo.ui.theme.AlexWeatherDemoTheme
import com.alex.yang.weather.demo.ui.theme.Brand18Medium

/**
 * Created by AlexYang on 2025/10/18.
 *
 *
 */
@Composable
fun WeatherCardCaptionWithIcon(
    @DrawableRes iconRes: Int,
    caption: String
) {
    // 設定 caption icon 及 文字
    val (icon, text) = remember(iconRes, caption) {
        val icon = mapOf(
            "icon" to InlineTextContent(
                placeholder = Placeholder(
                    width = 1.3.em,
                    height = 1.3.em,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                )
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        )
        val text = buildAnnotatedString {
            appendInlineContent(id = "icon", alternateText = "[icon]")
            append(" ")
            append(caption)
        }
        icon to text
    }

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp),
        style = Brand18Medium,
        color = MaterialTheme.colorScheme.onSurface,
        inlineContent = icon,
        text = text
    )
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
fun WeatherCardCaptionWithIconPreview() {
    AlexWeatherDemoTheme {
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .background(color = Color.LightGray)
            ) {
                WeatherCardCaptionWithIcon(
                    iconRes = com.alex.yang.weather.demo.R.drawable.ic_cloudcover,
                    caption = "25°C"
                )
            }
        }
    }
}