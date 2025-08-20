package com.shinjaehun.suksuk.presentation.common.layout

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.shinjaehun.suksuk.presentation.common.notice.LandscapeNotSupportedPanel
import kotlin.math.min

@Composable
fun ScaledBoard(
    designWidth: Dp,          // 보드의 기준 크기(예: 360.dp)
    designHeight: Dp,         // 보드의 기준 크기(예: 560.dp)
    minScale: Float = 0.60f,  // 이 값보다 작게 줄여야 하면 "가로 지원 불가"로 판단
    content: @Composable BoxScope.() -> Unit
) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val scaleX = (maxWidth / designWidth).toFloat()
        val scaleY = (maxHeight / designHeight).toFloat()
        val scale = min(scaleX, scaleY)
        Log.d("ScaledBoard", "scaleX=$scaleX scaleY=$scaleY scale=$scale")

        if (scale < minScale) {
            // 폴백: 스케일이 임계치보다 작으면 안내 메시지 표시
            LandscapeNotSupportedPanel()
            return@BoxWithConstraints

        }

//        val scaledW = designWidth * scale
//        val scaledH = designHeight * scale
//        val dx = (paneW - scaledW) / 2
//        val dy = (paneH - scaledH) / 2
//
//        val density = LocalDensity.current
//        Box(
//            modifier = Modifier
//                .requiredSize(designWidth, designHeight)
//                .graphicsLayer(
//                    // 좌상단 기준으로 축소한 뒤, 빈 여백만큼 가운데로 이동
//                    transformOrigin = TransformOrigin(0f, 0f),
//                    scaleX = scale,
//                    scaleY = scale,
//                    translationX = with(density) { dx.toPx() },
//                    translationY = with(density) { dy.toPx() }
//                ),
//            content = content
//        )

        Box(
            modifier = Modifier
                .requiredSize(designWidth, designHeight)
                .graphicsLayer(
                    transformOrigin = TransformOrigin(0.5f, 0.5f), // ✅ 중심 기준
                    scaleX = scale,
                    scaleY = scale
                ),
            content = content
        )
    }


}