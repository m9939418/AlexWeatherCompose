package com.alex.yang.weather.core.extension

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.text.TextUtils
import androidx.core.app.ShareCompat

/**
 * Created by AlexYang on 2025/10/19.
 *
 *
 */

fun Activity.startShareIntent(title: String = "", content: String?) {
    if (TextUtils.isEmpty(content)) return
    ShareCompat.IntentBuilder(this)
        .setType("text/plain")
        .setChooserTitle(title)
        .setText(content)
        .startChooser()
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}