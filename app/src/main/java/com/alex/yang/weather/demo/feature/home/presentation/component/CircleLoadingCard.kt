package com.alex.yang.weather.demo.feature.home.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alex.yang.weather.demo.ui.theme.AlexWeatherDemoTheme
import com.alex.yang.weather.demo.ui.theme.Brand18Medium
import com.alex.yang.weather.demo.ui.theme.Purple40
import com.alex.yang.weather.demo.ui.theme.RShape16

/**
 * Created by AlexYang on 2025/10/18.
 *
 * Screen 專用 - Loading View
 */
@Composable
fun CircleLoadingCard(
    modifier: Modifier = Modifier,
    message: String = "載入中...."
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.outline, shape = RShape16)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = Purple40,
                strokeWidth = 6.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                style = Brand18Medium,
                color = MaterialTheme.colorScheme.surface,
                textAlign = TextAlign.Center,
                text = message
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
fun CircleLoadingViewPreview() {
    AlexWeatherDemoTheme {
        Scaffold { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                CircleLoadingCard()
            }
        }
    }
}