package com.alex.yang.weather.core.extension

import android.annotation.SuppressLint
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView

/**
 * Created by AlexYang on 2025/10/19.
 *
 *
 */
@SuppressLint("SetJavaScriptEnabled")
fun WebView.setUpSettings() {
    settings.run {
        javaScriptEnabled = true
        allowContentAccess = true
        allowFileAccess = true
        loadsImagesAutomatically = true
        blockNetworkImage = false
        builtInZoomControls = true
        displayZoomControls = false
        cacheMode = WebSettings.LOAD_DEFAULT
        domStorageEnabled = true
        javaScriptCanOpenWindowsAutomatically = true
        layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        loadWithOverviewMode = true
        mediaPlaybackRequiresUserGesture = true
        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        setNeedInitialFocus(true)
        setSupportMultipleWindows(false)
        setSupportZoom(true)
        useWideViewPort = true
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(this@setUpSettings, true)
    }
}