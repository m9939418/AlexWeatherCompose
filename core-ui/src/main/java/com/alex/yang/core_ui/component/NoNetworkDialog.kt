package com.alex.yang.core_ui.component

import android.content.res.Configuration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.alex.yang.core_ui.ui.theme.AlexWeatherDemoTheme
import com.alex.yang.weather.core.extension.findActivity

/**
 * Created by AlexYang on 2025/10/20.
 *
 * 無網路連線 Dialog
 *
 * 當應用程式偵測到無網路連線時顯示的對話框，提供重試和離開兩個選項。
 * 此對話框無法透過點擊外部區域關閉，使用者必須選擇其中一個操作。
 *
 * @param visible 控制對話框是否顯示，當為 false 時對話框不會渲染
 * @param title 對話框標題文字，預設為「沒有網路連線」
 * @param message 對話框內容訊息，預設為「請檢查您的網路狀態，再重試一次。」
 * @param onRetry 點擊「重試」按鈕時的回調函式，通常用於重新執行網路請求
 * @param onExitApp 點擊「離開」按鈕時的回調函式。若為 null，則預設行為是關閉整個應用程式（finishAffinity）
 */
@Composable
fun NoNetworkDialog(
    visible: Boolean,
    title: String = "沒有網路連線",
    message: String = "請檢查您的網路狀態，再重試一次。",
    onRetry: () -> Unit,
    onExitApp: (() -> Unit)? = null
) {
    if (!visible) return

    val activity = LocalContext.current.findActivity()

    AlertDialog(
        onDismissRequest = { },
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onRetry) { Text("重試") }
        },
        dismissButton = {
            TextButton(
                onClick = { onExitApp?.invoke() ?: activity?.finishAffinity() }
            ) {
                Text("離開")
            }
        }
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
fun NoNetworkDialogPreview() {
    AlexWeatherDemoTheme {
        NoNetworkDialog(
            visible = true,
            onRetry = {},
            onExitApp = {}
        )
    }
}