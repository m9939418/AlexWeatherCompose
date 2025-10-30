package com.alex.yang.home.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alex.yang.weather.core.aboutMe
import com.alex.yang.core_ui.ui.theme.AlexWeatherDemoTheme
import com.alex.yang.core_ui.ui.theme.Brand16Medium
import com.alex.yang.core_ui.ui.theme.Brand20Medium

/**
 * Created by AlexYang on 2025/10/18.
 *
 *
 */
@Composable
fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .padding(start = 24.dp, top = 24.dp, bottom = 24.dp)
    ) {
        // Avatar
        Icon(
            modifier = Modifier.size(48.dp),
            imageVector = Icons.Rounded.AccountCircle,
            contentDescription = null
        )
        // Name
        Text(
            modifier = Modifier.padding(top = 8.dp),
            style = Brand20Medium,
            color = MaterialTheme.colorScheme.onSurface,
            text = aboutMe.first
        )
        // Email
        Text(
            style = Brand16Medium,
            color = MaterialTheme.colorScheme.outline,
            text = aboutMe.second
        )
    }
    HorizontalDivider(thickness = 0.5.dp)
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
fun DrawerHeaderPreview() {
    AlexWeatherDemoTheme {
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                DrawerHeader()
            }
        }
    }
}
