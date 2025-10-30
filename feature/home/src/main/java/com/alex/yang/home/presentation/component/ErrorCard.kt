package com.alex.yang.home.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alex.yang.core_ui.ui.theme.Brand16Medium

/**
 * Created by AlexYang on 2025/8/15.
 *
 *
 */
@Composable
fun ErrorCard(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                style = Brand16Medium,
                color = MaterialTheme.colorScheme.onSurface,
                text = message
            )
            Button(onClick = onRetry) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    style = Brand16Medium,
                    color = MaterialTheme.colorScheme.surface,
                    text = "重試"
                )
            }
        }
    }
}