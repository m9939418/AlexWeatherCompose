package com.alex.yang.weather.demo.feature.web

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.alex.yang.weather.core.extension.setUpSettings
import com.alex.yang.weather.core.extension.startShareIntent
import com.alex.yang.weather.demo.ui.theme.AlexWeatherDemoTheme
import com.alex.yang.weather.demo.ui.theme.Brand20Medium

/**
 * Created by AlexYang on 2025/10/19.
 *
 * Webview 網頁瀏覽 Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebScreen(
    modifier: Modifier,
    navController: NavController,
    caption: String,
    url: String,
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var progress by remember { mutableIntStateOf(0) } // 0..100
    val webView = remember { WebView(context) }
    val goBackOrClose: () -> Unit = {
        if (webView.canGoBack()) webView.goBack()
        else navController.popBackStack()
    }

    BackHandler(enabled = true) { goBackOrClose() }

    DisposableEffect(webView) {
        webView.setUpSettings()
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return handleUrl(view, request?.url?.toString())
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                isLoading = true
                progress = 0
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progress = 100
                isLoading = false
            }
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progress = newProgress.coerceIn(0, 100)
            }
        }

        // 初始載入
        if (webView.url != url) {
            isLoading = true
            progress = 0
            webView.loadUrl(url)
        }

        onDispose {
            runCatching {
                webView.apply {
                    stopLoading()
                    webChromeClient = null
                    loadUrl("about:blank")
                    destroy()
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        if (webView.canGoBack()) webView.goBack()
                        else navController.popBackStack()
                    }) {
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
                        text = caption
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                actions = {
                    IconButton(
                        modifier = Modifier.size(36.dp),
                        onClick = {
                            (context as Activity).startShareIntent("AlexYangWeather", url)
                        }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { webView },
                update = { view ->
                    if (view.url != url) {
                        isLoading = true
                        progress = 0
                        view.loadUrl(url)
                    }
                }
            )

            if (isLoading || progress in 0..99) {
                LinearProgressIndicator(
                    progress = { progress / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .align(Alignment.TopStart),
                    color = MaterialTheme.colorScheme.surfaceContainer,
                )
            }
        }
    }
}

private fun handleUrl(view: WebView?, url: String?): Boolean {
    val webView = view ?: return false
    val uri = runCatching { url?.trim().orEmpty().toUri() }.getOrNull() ?: return false
    val scheme = uri.scheme?.lowercase() ?: return false

    val isExternal = scheme in setOf("tel", "mailto", "intent", "market", "geo", "sms")

    return if (isExternal) {
        val ctx = webView.context
        runCatching {
            Intent(Intent.ACTION_VIEW, uri).apply {
                if (ctx !is Activity) addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }.let(ctx::startActivity)
        }.isSuccess
    } else {
        runCatching { webView.loadUrl(uri.toString()) }.isSuccess
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
fun WebScreenPreview() {
    AlexWeatherDemoTheme {
        WebScreen(
            modifier = Modifier,
            navController = NavController(LocalContext.current),
            caption = "OpenNet",
            url = "https://opennet.tw/",
        )
    }
}