package com.alex.yang.core_ui.ui.model

import com.alex.yang.core_ui.R
import com.alex.yang.navigation.AppRoute

/**
 * Created by AlexYang on 2025/10/20.
 *
 * 側邊選單項目
 *
 * @property label 選單項目顯示的文字標籤
 * @property iconRes 選單項目的圖示資源 ID，預設為 0（無圖示）
 * @property destination 點擊選單項目後的導航目的地，類型為 [AppRoute]
 */
data class DrawerItem(
    val label: String,
    val iconRes: Int = 0,
    val destination: AppRoute
) {
    companion object {
        val ALL = listOf<DrawerItem>(
            DrawerItem(
                label = "Home", iconRes = R.drawable.ic_home,
                destination = AppRoute.Home
            ),
            DrawerItem(
                label = "About API",
                iconRes = R.drawable.ic_api,
                destination = AppRoute.Web(
                    caption = "About API",
                    url = "https://www.visualcrossing.com/"
                )
            ),
            DrawerItem(
                label = "GitHub",
                iconRes = R.drawable.ic_github,
                destination = AppRoute.Web(
                    caption = "GitHub",
                    url = "https://github.com/m9939418?tab=repositories"
                )
            )
        )
    }
}