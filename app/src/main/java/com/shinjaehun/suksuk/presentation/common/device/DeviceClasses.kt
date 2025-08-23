package com.shinjaehun.suksuk.presentation.common.device

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration


@Immutable
data class DeviceClasses(
    val isLarge: Boolean,           // sw >= 600dp (태블릿)
    val isLandscape: Boolean,       // 가로/세로
    val isSmallLandscape: Boolean,  // 비태블릿 + 가로
    val widthDp: Int,
    val heightDp: Int,
    val widthClass: WidthClass,     // M3 없이 자체 분류
    val heightClass: HeightClass,
) {
    companion object {
        val Default = DeviceClasses(
            isLarge = false, isLandscape = false, isSmallLandscape = false,
            widthDp = 360, heightDp = 640,
            widthClass = WidthClass.Compact,
            heightClass = HeightClass.Medium
        )
    }
}

enum class WidthClass { Compact, Medium, Expanded }
enum class HeightClass { Compact, Medium, Expanded }

@Composable
fun rememberDeviceClasses(): DeviceClasses {
    val cfg = LocalConfiguration.current
    val isLandscape = cfg.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    val isLarge = cfg.smallestScreenWidthDp >= 600
    val isSmallLandscape = !isLarge && isLandscape

    val w = cfg.screenWidthDp
    val h = cfg.screenHeightDp

    val wClass = when {
        w >= 840 -> WidthClass.Expanded
        w >= 600 -> WidthClass.Medium
        else     -> WidthClass.Compact
    }
    val hClass = when {
        h >= 900 -> HeightClass.Expanded
        h >= 480 -> HeightClass.Medium
        else     -> HeightClass.Compact
    }

    // recomposition 최소화
    return remember(w, h, isLandscape, isLarge) {
        DeviceClasses(
            isLarge = isLarge,
            isLandscape = isLandscape,
            isSmallLandscape = isSmallLandscape,
            widthDp = w,
            heightDp = h,
            widthClass = wClass,
            heightClass = hClass
        )
    }
}

/** 전역에서 바로 꺼내 쓰고 싶을 때 */
val LocalDeviceClasses = staticCompositionLocalOf { DeviceClasses.Default }

/** 루트에서 한 번만 제공해두면 하위 어디서든 LocalDeviceClasses.current 로 접근 가능 */
@Composable
fun ProvideDeviceClasses(content: @Composable () -> Unit) {
    val dc = rememberDeviceClasses()
    CompositionLocalProvider(LocalDeviceClasses provides dc) {
        content()
    }
}