package com.shinjaehun.suksuk.presentation.common.layout

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.shinjaehun.suksuk.presentation.common.notice.LandscapeNotSupportedPanel

@Composable
fun ScaledBoardBox(
    designWidth: Dp,
    designHeight: Dp,
    minScale: Float = UiTokens.MinScale,
    content: @Composable BoxScope.() -> Unit
) {
    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing) // 시스템 바 회피
    ) {
        val sW = (maxWidth / designWidth).toFloat()
        val sH = (maxHeight / designHeight).toFloat()
        val scale = minOf(sW, sH)

        if (scale < minScale) {
            LandscapeNotSupportedPanel()   // 너무 작아지는 폼팩터 방어
            return@BoxWithConstraints
        }
        Log.d("ScaledBoard", "scale=$scale, minScale=$minScale")

        Box(
            Modifier
                .size(designWidth * scale, designHeight * scale),
            content = content
        )
    }
}