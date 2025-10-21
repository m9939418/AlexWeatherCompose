package com.alex.yang.weather.core.extension

import java.text.DecimalFormat

/**
 * Created by AlexYang on 2025/10/17.
 *
 *
 */
val Double.asComma: String
    get() = DecimalFormat("#,###").format(this)