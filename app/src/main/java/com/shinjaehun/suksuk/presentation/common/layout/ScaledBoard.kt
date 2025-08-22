package com.shinjaehun.suksuk.presentation.common.layout

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.shinjaehun.suksuk.presentation.common.notice.LandscapeNotSupportedPanel
import kotlin.math.min

@Composable
fun ScaledBoard(
    designWidth: Dp,          // 보드의 기준 크기(예: 360.dp)
    designHeight: Dp,         // 보드의 기준 크기(예: 560.dp)
    minScale: Float = 0.60f,  // 이 값보다 작게 줄여야 하면 "가로 지원 불가"로 판단

//    maxScale: Float = 1f,
    maxScale: Float = 1.5f,
    shrink: Float = 1f,

    content: @Composable BoxScope.() -> Unit
) {
//    BoxWithConstraints(Modifier.fillMaxSize()) {
//        val scaleX = (maxWidth / designWidth).toFloat()
//        val scaleY = (maxHeight / designHeight).toFloat()
//
//        val base = minOf(scaleX, scaleY).coerceAtMost(maxScale)
//        val scale = base * shrink
//        Log.d("ScaledBoard", "scaleX=$scaleX scaleY=$scaleY scale=$scale")
//
//        if (scale < minScale) {
//            // 폴백: 스케일이 임계치보다 작으면 안내 메시지 표시
//            LandscapeNotSupportedPanel()
//            return@BoxWithConstraints
//
//        }
//
////        val scaledW = designWidth * scale
////        val scaledH = designHeight * scale
////        val dx = (paneW - scaledW) / 2
////        val dy = (paneH - scaledH) / 2
////
////        val density = LocalDensity.current
////        Box(
////            modifier = Modifier
////                .requiredSize(designWidth, designHeight)
////                .graphicsLayer(
////                    // 좌상단 기준으로 축소한 뒤, 빈 여백만큼 가운데로 이동
////                    transformOrigin = TransformOrigin(0f, 0f),
////                    scaleX = scale,
////                    scaleY = scale,
////                    translationX = with(density) { dx.toPx() },
////                    translationY = with(density) { dy.toPx() }
////                ),
////            content = content
////        )
//
////        Box(
////            modifier = Modifier.size(designWidth * scale, designHeight * scale),
////            content = content
////        )
//
//        val density = LocalDensity.current
//        val scaledDensity = Density(
//            density = density.density * scale,  // ← 핵심: 밀도 스케일
//            fontScale = density.fontScale   // 폰트는 원하면 *k 해도 됨
//        )
//
//        CompositionLocalProvider(LocalDensity provides scaledDensity) {
//            // 외곽 박스도 스케일된 논리 크기로 맞춰주면 깔끔
//            Box(
//                modifier = Modifier
//                    .size(designWidth * scale, designHeight * scale)
//                    .align(Alignment.TopCenter),
//                content = content
//            )
//        }
//    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val scaleX = (maxWidth / designWidth).toFloat()
        val scaleY = (maxHeight / designHeight).toFloat()

        // 화면 비율에 맞춰 스케일 계산
        val base = minOf(scaleX, scaleY).coerceIn(minScale, maxScale)
        val scale = base * shrink
        Log.d("ScaledBoard", "scaleX=$scaleX scaleY=$scaleY scale=$scale")

        if (scale < minScale) {
            LandscapeNotSupportedPanel()
            return@BoxWithConstraints
        }

        val density = LocalDensity.current
        val scaledDensity = Density(
            density = density.density * scale,  // ← 크기 스케일
            fontScale = density.fontScale       // ← 글꼴은 원하면 별도 조정
        )

        CompositionLocalProvider(LocalDensity provides scaledDensity) {
            Box(
                modifier = Modifier
                    .size(designWidth * scale, designHeight * scale)
                    .align(Alignment.TopCenter),
                content = content
            )
        }
    }

}