package com.shinjaehun.suksuk.presentation.common.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.shinjaehun.suksuk.presentation.common.notice.LandscapeNotSupportedPanel

@Composable
fun ScaledBoardInSlot(
    designWidth: Dp,
    designHeight: Dp,
    minScale: Float = 0.60f,
    maxScale: Float = 1.35f,
    shrink: Float = 1f,
    alignTop: Boolean = true,
    modifier: Modifier = Modifier.fillMaxSize(),
    content: @Composable BoxScope.() -> Unit
) {
    val slotAlignment = if (alignTop) Alignment.TopCenter else Alignment.Center

    Box(
        modifier = modifier,
        contentAlignment = slotAlignment
    ) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val scaleX = (maxWidth / designWidth).toFloat()
            val scaleY = (maxHeight / designHeight).toFloat()

            val base = minOf(scaleX, scaleY).coerceIn(minScale, maxScale)
            val scale = base * shrink
//            Log.d("ScaledBoardInSlot", "scaleX=$scaleX scaleY=$scaleY scale=$scale")

            if (scale < minScale) {
                LandscapeNotSupportedPanel()
                return@BoxWithConstraints
            }

            val density = LocalDensity.current
            val scaledDensity = Density(
                density = density.density * scale,
                fontScale = density.fontScale
            )

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = slotAlignment
            ) {
                CompositionLocalProvider(LocalDensity provides scaledDensity) {
                    Box(
                        modifier = Modifier
                            .size(designWidth * scale, designHeight * scale),
                        contentAlignment = slotAlignment

                    ) {
                        content()
                    }
                }
            }
        }
    }
}