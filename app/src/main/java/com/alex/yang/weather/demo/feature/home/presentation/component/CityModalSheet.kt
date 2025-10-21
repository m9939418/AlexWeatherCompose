package com.alex.yang.weather.demo.feature.home.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alex.yang.weather.data.model.County
import com.alex.yang.weather.demo.R
import com.alex.yang.weather.demo.ui.theme.AlexWeatherDemoTheme
import com.alex.yang.weather.demo.ui.theme.Brand20Medium
import com.alex.yang.weather.demo.ui.theme.RShape28
import java.util.UUID

/**
 * Created by AlexYang on 2025/10/19.
 *
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityModalSheet(
    sheetState: SheetState,
    list: List<County>,
    onDismissRequest: () -> Unit,
    onItemClick: (County) -> Unit
) {
    val listState = rememberLazyStaggeredGridState()

    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        dragHandle = null,
        contentColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                title = {
                    Text(
                        modifier = Modifier.padding(vertical = 24.dp),
                        style = Brand20Medium,
                        color = MaterialTheme.colorScheme.onBackground,
                        text = "請選擇城市",
                    )
                },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.padding(start = 16.dp),
                        onClick = onDismissRequest
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                },
                actions = {}
            )

            LazyVerticalStaggeredGrid(
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                state = listState,
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalItemSpacing = 16.dp,
            ) {
                itemsIndexed(
                    items = list,
                    key = { index, item -> "${index}_${item.en}" },
                    span = { _, _ -> StaggeredGridItemSpan.SingleLane }
                ) { index, item ->
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceContainer,
                                shape = if (index % 2 == 1) CircleShape else RShape28
                            ),
                        onClick = { onItemClick(item) }
                    ) {
                        Text(
                            style = Brand20Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            text = item.zh,
                        )
                    }
                }
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityModalSheetPreview() {
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LaunchedEffect(Unit) {
        bottomSheetState.show()
    }

    AlexWeatherDemoTheme {
        Scaffold { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                CityModalSheet(
                    sheetState = bottomSheetState,
                    list = List(9) { County("臺北市", "${UUID.randomUUID()}") },
                    onDismissRequest = {},
                    onItemClick = { _ -> }
                )
            }
        }
    }
}