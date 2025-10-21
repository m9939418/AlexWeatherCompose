package com.alex.yang.weather.core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest

/**
 * Created by AlexYang on 2025/10/17.
 *
 *
 * @param modifier
 * @param imageUrl              圖片的 URL
 * @param size                  圖片的目標尺寸（px），0 表示不限制。設置此值可以減少內存使用
 * @param contentDescription
 * @param contentScale          圖片的縮放方式，預設為 ContentScale.Fit
 * @param crossfade             是否啟用淡入動畫效果，預設為 false 以提升列表性能
 * @param memoryCachePolicy     預設啟用
 * @param diskCachePolicy       預設啟用
 *
 *
 * 使用範例：
 * ```
 * CachedAsyncImage(
 *     modifier = Modifier.size(48.dp),
 *     imageUrl = "https://alex.yang.com/icon.png",
 *     size = 48,
 *     contentDescription = "天氣圖標"
 * )
 * ```
 */
@Composable
fun CachedAsyncImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    size: Int = 0,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Fit,
    crossfade: Boolean = false,
    memoryCachePolicy: CachePolicy = CachePolicy.ENABLED,
    diskCachePolicy: CachePolicy = CachePolicy.ENABLED,
    showPlaceholder: Boolean = true
) {
    val context = LocalContext.current
    val imageRequest = remember(imageUrl, size, crossfade, memoryCachePolicy, diskCachePolicy) {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .apply {
                if (size > 0) {
                    size(size)
                    allowHardware(true)
                }
                if (crossfade) {
                    crossfade(200)
                } else {
                    crossfade(false)
                }
            }
            .memoryCachePolicy(memoryCachePolicy)
            .diskCachePolicy(diskCachePolicy)
            .diskCacheKey(imageUrl)
            .memoryCacheKey(imageUrl)
            .networkCachePolicy(CachePolicy.ENABLED)
            .build()
    }

    SubcomposeAsyncImage(
        modifier = modifier,
        model = imageRequest,
        contentDescription = contentDescription,
        contentScale = contentScale,
        loading = if (showPlaceholder) {
            { IconPlaceholder() }
        } else null,
        error = if (showPlaceholder) {
            { IconPlaceholder() }
        } else null
    )
}

@Composable
private fun IconPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    )
}