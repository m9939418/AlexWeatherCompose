package com.alex.yang.weather.demo.feature.today_detail

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alex.yang.mock.fakeWeeklyDaysForPreview
import com.alex.yang.weather.core.utils.HourLabel
import com.alex.yang.weather.demo.feature.today_detail.component.HourRow
import com.alex.yang.weather.demo.ui.theme.AlexWeatherDemoTheme
import com.alex.yang.weather.demo.ui.theme.Brand20Medium
import com.alex.yang.weather.domain.model.Hour

/**
 * Created by AlexYang on 2025/10/19.
 *
 * 當日詳細天氣預報 Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    index: Int,
    list: List<Hour>
) {
    val state = rememberLazyListState(0)

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
                        text = HourLabel.todayLabel
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            state = state,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            itemsIndexed(
                items = list,
                key = { index, item -> "hour-${item.datetime}-${index}" },
                contentType = { _, _ -> "hourly_item" }
            ) { index, item ->
                HourRow(item)
            }
            item {
                Spacer(modifier = Modifier.height(100.dp))
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
fun TodayDetailScreenPreview() {
    val fakeDays = fakeWeeklyDaysForPreview()

    AlexWeatherDemoTheme {
        Scaffold { innerPadding ->
            TodayDetailScreen(
                modifier = Modifier.padding(innerPadding),
                navController = NavController(LocalContext.current),
                index = 0,
                list = fakeDays[2].hours
            )
        }
    }
}