package com.alex.yang.home.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alex.yang.navigation.AppRoute
import com.alex.yang.core_ui.ui.model.DrawerItem
import com.alex.yang.core_ui.ui.theme.AlexWeatherDemoTheme
import com.alex.yang.core_ui.ui.theme.Brand14Medium
import com.alex.yang.core_ui.ui.theme.Brand16Medium

/**
 * Created by AlexYang on 2025/10/18.
 *
 *
 */
@Composable
fun AppDrawer(onClose: (AppRoute) -> Unit) {
    var selectedLabel by remember { mutableStateOf("Home") }

    ModalDrawerSheet(modifier = Modifier.fillMaxWidth(0.6f)) {
        DrawerHeader()

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(DrawerItem.ALL) { item ->
                NavigationDrawerItem(
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    icon = {
                        Icon(
                            painter = painterResource(item.iconRes),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    label = {
                        Text(
                            style = Brand14Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            text = item.label
                        )
                    },
                    selected = selectedLabel == item.label,
                    onClick = {
                        selectedLabel = item.label
                        onClose(item.destination)
                    }
                )
            }
        }

        Spacer(Modifier.weight(1f))

        HorizontalDivider(thickness = 0.5.dp)
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
fun AppDrawerPreview() {
    AlexWeatherDemoTheme {
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                AppDrawer(onClose = {})
            }
        }
    }
}
