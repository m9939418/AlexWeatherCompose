package com.alex.yang.weather.demo

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.alex.yang.core_ui.component.NoNetworkDialog
import com.alex.yang.weather.demo.navigation.AppNavGraph
import com.alex.yang.core_ui.ui.theme.AlexWeatherDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition { !viewModel.uiState.value.isReady }

            setOnExitAnimationListener { provider ->
                val target = runCatching { provider.iconView }.getOrNull() ?: provider.view

                target.animate()
                    .rotation(180f)
                    .scaleX(0f)
                    .scaleY(0f)
                    .alpha(0f)
                    .setInterpolator(FastOutSlowInInterpolator())
                    .setDuration(360L)
                    .withEndAction { provider.remove() }
                    .start()
            }
        }

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT,
                detectDarkMode = { resources ->
                    (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
                }
            ),
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT,
                detectDarkMode = { resources ->
                    (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
                }
            )
        )

        setContent {
            AlexWeatherDemoTheme {
                val navController = rememberNavController()
                val state by viewModel.uiState.collectAsStateWithLifecycle()

                BackHandler(enabled = navController.previousBackStackEntry == null) {
                    finish()
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets(0),
                ) { innerPadding ->
                    AppNavGraph(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                    )
                }

                NoNetworkDialog(
                    visible = state.showDialog,
                    title = state.error?.first ?: "沒有網路連線",
                    message = state.error?.second ?: "請檢查您的網路狀態",
                    onRetry = { viewModel.fetchConfig() },
                )
            }
        }
    }
}