package com.shinjaehun.suksuk.presentation.common.layout

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isFinite
import com.shinjaehun.suksuk.presentation.common.notice.LandscapeNotSupportedPanel

//@Composable
//fun PresentationScaffold(
//    isLandscape: Boolean,
//    board: @Composable BoxScope.() -> Unit,
//    panel: @Composable BoxScope.() -> Unit,
//    // landscape tuning(세 화면에서 같은 값 사용)
//    designWidth: Dp = 360.dp,
//    designHeight: Dp = 560.dp,
//    minScale: Float = 0.45f,
////    boardWeight: Float = 1.2f,
////    panelWeight: Float = 1.5f,
//    boardWeight: Float = 3f,
//    panelWeight: Float = 1f,
//
////    contentWidthFraction: Float = 0.90f,
////    maxContentWidth: Dp = 1400.dp,
//
//    contentWidthFraction: Float = 0.96f,  // ← 0.90 → 0.96 (~+6.6%)
//    maxContentWidth: Dp = 1800.dp,        // ← 1400 → 1800 (혹은 null)
//
//    outerPadding: Dp = 16.dp,
//    innerGutter: Dp = 12.dp,
//) {
//    if (isLandscape) {
//        DualPaneBoardScaffold(
//            designWidth = designWidth,
//            designHeight = designHeight,
//            minScale = minScale,
//            boardWeight = boardWeight,
//            panelWeight = panelWeight,
//            contentWidthFraction = contentWidthFraction,
//            maxContentWidth = maxContentWidth,
//            outerPadding = outerPadding,
//            innerGutter = innerGutter,
//            board = { Box(Modifier.align(Alignment.TopCenter).fillMaxWidth()) { board() } },
//            panel = { Box(Modifier.fillMaxSize()) { panel() } }
//        )
//    } else {
//        Box(Modifier.fillMaxSize()) {
//            Box(Modifier.align(Alignment.TopCenter).fillMaxWidth()) { board() }
//            Box(Modifier.align(Alignment.BottomCenter).fillMaxWidth()) { panel() }
//        }
//    }
//}

//@Composable
//fun PresentationScaffold(
//    isLandscape: Boolean,
//    board: @Composable BoxScope.() -> Unit,
//    panel: @Composable BoxScope.() -> Unit,
//    designWidth: Dp = UiTokens.DesignBoardW,
//    designHeight: Dp = UiTokens.DesignBoardH,
//    minScale: Float = UiTokens.MinScale,
//    boardWeight: Float = 2f,   // 좌/우 비율
//    panelWeight: Float = 1f
//) {
//    if (isLandscape) {
//        // ---- 가로: 좌 보드 / 우 패드 ----
//        Row(
//            Modifier
//                .fillMaxSize()
//                .windowInsetsPadding(WindowInsets.safeDrawing)
//                .padding(horizontal = UiTokens.Outer),
//            horizontalArrangement = Arrangement.spacedBy(UiTokens.Gutter),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(
//                Modifier
//                    .weight(boardWeight)
//                    .fillMaxHeight(),
//                contentAlignment = Alignment.Center
//            ) {
//                ScaledBoardBox(designWidth, designHeight, minScale) { board() }
//            }
//            Box(
//                Modifier
//                    .weight(panelWeight)
//                    .fillMaxHeight(),
//                contentAlignment = Alignment.Center
//            ) {
//                // 패드 폭은 “가용폭 내”에서 스스로 정해지게(내부에서 계산)
//                Box(Modifier.fillMaxWidth()) { panel() }
//            }
//        }
//    } else {
//        // ---- 세로: 위 보드 / 아래 패드 ----
//        Column(
//            Modifier
//                .fillMaxSize()
//                .windowInsetsPadding(WindowInsets.safeDrawing)
//                .padding(horizontal = UiTokens.Outer),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Box(
//                Modifier
//                    .weight(1f)
//                    .fillMaxWidth(),
//                contentAlignment = Alignment.Center
//            ) {
//                ScaledBoardBox(designWidth, designHeight, minScale) { board() }
//            }
//
//            Spacer(Modifier.height(UiTokens.Gutter))
//
//            // 패드: 화면 폭의 70% 정도에서 자동 배치(과대/과소 방지)
//            Box(
//                Modifier
//                    .fillMaxWidth(0.7f)
//                    .wrapContentHeight(),
//                contentAlignment = Alignment.Center
//            ) {
//                panel()
//            }
//
//            Spacer(Modifier.height(UiTokens.PanelBottom))
//        }
//    }
//}


@Composable
fun PresentationScaffold(
    debugColors: Boolean = true,
    board: @Composable () -> Unit,
    panel: @Composable () -> Unit
) {
    val (isLarge, isLandscape, isSmallLandscape) = deviceClasses()

    val outer = 20.dp
    val gutter = 16.dp
    val maxContentWidth = if (isLarge) 1400.dp else Dp.Infinity

    // 큰 화면 가로에서 보드/패널 가중치
    val boardWeightLgLand = 1.6f
    val panelWeightLgLand = 1.4f

    val isLargePortrait = !isLandscape && isLarge

    // 큰 화면 세로에서 쓸 가중치(예시 값, 취향대로 조절)
    val boardWeightLgPortrait = 1.2f   // 보드가 차지할 비율
    val panelWeightLgPortrait = 0.8f   // 패널이 차지할 비율

    val topPadLargeLandscape = 40.dp   // 대화면 가로: 위에서 조금 내림
    val topPadLargePortrait  = 48.dp   // 대화면 세로: 위에서 조금 더 내림
    val pullUpSmallPortrait  = (-8).dp // 작은 화면 세로: 살짝 위로 당김 (음수 offset)

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val rowWidth = if (maxContentWidth.isFinite) minOf(maxWidth, maxContentWidth) else maxWidth

        when {
            // ✅ 큰 화면 가로: 2패널, 보드에 ScaledBoard 적용 (확대 허용)
            isLarge && isLandscape -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = outer),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        Modifier
                            .width(rowWidth)
                            .fillMaxHeight(),
                        horizontalArrangement = Arrangement.spacedBy(gutter),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .weight(boardWeightLgLand)
                                .fillMaxHeight()
                                .padding(top = topPadLargeLandscape),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            ScaledBoardInSlot(
                                designWidth = 360.dp,
                                designHeight = 560.dp,
                                minScale = 0.60f,
                                maxScale = 1.35f,  // ← 여기서 확대 상한 조절
                                shrink = 1f,
                                alignTop = true
                            ) { board() }
                        }

                        Box(
                            Modifier
                                .weight(panelWeightLgLand)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) { panel() }
                    }
                }
            }

            // 작은 화면 가로: 보드만 축소용 스케일
            isSmallLandscape -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = outer),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        Modifier
                            .width(rowWidth)
                            .fillMaxHeight(),
                        horizontalArrangement = Arrangement.spacedBy(gutter),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .weight(boardWeightLgLand)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            ScaledBoardInSlot(
                                designWidth = 360.dp,
                                designHeight = 560.dp,
                                minScale = 0.45f,
                                maxScale = 1f,      // 확대 금지
                                shrink = 0.90f,     // 항상 10% 더 작게
                                alignTop = true
                            ) { board() }
                        }

                        Box(
                            Modifier
                                .weight(panelWeightLgLand)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) { panel() }
                    }
                }
            }

            // ✅ 큰 화면 세로: 위 보드 영역에도 확대 적용
            else -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = outer),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        Modifier
                            .width(rowWidth)
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            Modifier.then(
                                if (isLargePortrait) Modifier.weight(boardWeightLgPortrait)
                                else Modifier.weight(1f)
                            )
                                .fillMaxWidth()
                                .then(
                                    if (isLarge) Modifier.padding(top = topPadLargePortrait)
                                    else Modifier.offset(y = pullUpSmallPortrait)
                                ),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            if (isLargePortrait) {
                                ScaledBoardInSlot(
                                    designWidth = 360.dp,
                                    designHeight = 560.dp,
                                    minScale = 0.70f,
                                    maxScale = 1.35f, // 세로에서도 확대 허용
                                    shrink = 1f,
                                    alignTop = true
                                ) { board() }
                            } else {
                                board()
                            }
                        }

                        Spacer(Modifier.height(gutter))

                        Box(
                            Modifier
                                .fillMaxWidth()
                                .then(
                                    if (isLargePortrait) Modifier.weight(panelWeightLgPortrait)
                                    else Modifier.wrapContentHeight()
                                ),
                            contentAlignment = Alignment.Center
                        ) { panel() }

                        Spacer(Modifier.height(28.dp))
                    }
                }
            }
        }
    }


//    BoxWithConstraints(Modifier.fillMaxSize()) {
//        val rowWidth = if (maxContentWidth.isFinite) minOf(maxWidth, maxContentWidth) else maxWidth
//
//        when {
//            // 큰 화면 가로: 2패널, 스케일 없음, 중앙 정렬
//            isLarge && isLandscape -> {
//                Box(
//                    Modifier
//                        .fillMaxSize()
//                        .padding(horizontal = outer),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Row(
//                        Modifier
//                            .width(rowWidth)
//                            .fillMaxHeight(),
//                        horizontalArrangement = Arrangement.spacedBy(gutter),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Box(
//                            Modifier
//                                .weight(boardWeightLgLand)
//                                .fillMaxHeight()
//                                .padding(top = topPadLargeLandscape),
////                                .background(if (debugColors) Color(0x332196F3) else Color.Transparent),
//                            contentAlignment = Alignment.TopCenter
//                        ) { board() }
//
//                        Box(
//                            Modifier
//                                .weight(panelWeightLgLand)
//                                .fillMaxHeight(),
////                                .background(if (debugColors) Color(0x33E91E63) else Color.Transparent),
//                            contentAlignment = Alignment.Center
//                        ) { panel() }
//                    }
//                }
//            }
//
//            // ✅ 작은 화면 가로: 2패널, 보드만 스케일
//            isSmallLandscape -> {
//                Box(
//                    Modifier
//                        .fillMaxSize()
//                        .padding(horizontal = outer),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Row(
//                        Modifier
//                            .width(rowWidth)
//                            .fillMaxHeight(),
//                        horizontalArrangement = Arrangement.spacedBy(gutter),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Box(
//                            Modifier
//                                .weight(boardWeightLgLand)
//                                .fillMaxHeight(),
////                                .background(if (debugColors) Color(0x332196F3) else Color.Transparent),
//                            contentAlignment = Alignment.TopCenter
//                        ) {
//                            ScaledBoard(
//                                designWidth = 360.dp,
//                                designHeight = 560.dp,
//                                minScale = 0.45f,
//                                maxScale = 1f,     // 확대 금지
//                                shrink = 0.90f     // ✅ 항상 10% 더 작게
//                            ) { board() }
//                        }
//
//                        Box(
//                            Modifier
//                                .weight(panelWeightLgLand)
//                                .fillMaxHeight(),
////                                .background(if (debugColors) Color(0x33E91E63) else Color.Transparent),
//                            contentAlignment = Alignment.Center
//                        ) { panel() }
//                    }
//                }
//            }
//
//            // 그 외(큰 화면 세로 / 작은 화면 세로): 무스케일, 중앙 정렬 (상하 배치)
//            else -> {
//                Box(
//                    Modifier
//                        .fillMaxSize()
//                        .padding(horizontal = outer),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Column(
//                        Modifier
//                            .width(rowWidth)
//                            .fillMaxHeight(),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Box(
//                            Modifier
////                                .weight(1f)
//                                .then(
//                                    if (isLargePortrait) Modifier.weight(boardWeightLgPortrait)
//                                    else Modifier.weight(1f)
//                                )
//                                .fillMaxWidth()
////                                .background(if (debugColors) Color(0x332196F3) else Color.Transparent)
//                                .then(
//                                    if (isLarge) Modifier.padding(top = topPadLargePortrait)
//                                    else Modifier.offset(y = pullUpSmallPortrait)
//                                ),
//                            contentAlignment = Alignment.TopCenter
//                        ) { board() }
//
//                        Spacer(Modifier.height(gutter))
//
//                        Box(
//                            Modifier
//                                .fillMaxWidth()
////                                .wrapContentHeight(),
////                                .background(if (debugColors) Color(0x33E91E63) else Color.Transparent)
//                                .then(
//                                    if (isLargePortrait) {
//                                        // 큰 화면 세로에서는 패널에도 weight를 줘서 공간을 더 가져가게
//                                        Modifier.weight(panelWeightLgPortrait)
//                                    } else {
//                                        // 작은 화면 세로에서는 기존처럼 필요한 만큼만
//                                        Modifier.wrapContentHeight()
//                                    }
//                                ),
//
//                            contentAlignment = Alignment.Center
//                        ) { panel() }
//
//                        Spacer(Modifier.height(28.dp))
//                    }
//                }
//            }
//        }
//    }
}

@Composable
private fun ScaledBoardInSlot(
    designWidth: Dp,
    designHeight: Dp,
    minScale: Float = 0.60f,
    maxScale: Float = 1.35f,   // ← 태블릿에서 확대 허용
    shrink: Float = 1f,
    alignTop: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        // 이 슬롯(부모 Box)의 실제 가용 크기
        val slotW = maxWidth
        val slotH = maxHeight

        val scaleX = (slotW / designWidth).toFloat()
        val scaleY = (slotH / designHeight).toFloat()

        // 슬롯 기준으로 스케일 계산 (확대/축소 모두 허용)
        val base = minOf(scaleX, scaleY).coerceIn(minScale, maxScale)
        val scale = base * shrink

        if (scale < minScale) {
            LandscapeNotSupportedPanel()
            return@BoxWithConstraints
        }

        val density = LocalDensity.current
        val scaledDensity = Density(
            density = density.density * scale,
            fontScale = density.fontScale      // 폰트 따로 키우려면 * scale
        )

        CompositionLocalProvider(LocalDensity provides scaledDensity) {
            Box(
                modifier = Modifier
                    .size(designWidth * scale, designHeight * scale)
                    .align(if (alignTop) Alignment.TopCenter else Alignment.Center),
                content = content
            )
        }
    }
}

@Composable
fun deviceClasses(): Triple<Boolean, Boolean, Boolean> {
    val cfg = LocalConfiguration.current
    val isLandscape = cfg.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isLarge = cfg.smallestScreenWidthDp  >= 600
    val isSmallLandscape = !isLarge && isLandscape
    return Triple(isLarge, isLandscape, isSmallLandscape)
}

@Composable
fun rememberButtonSizes(): Pair<Dp, Dp> {
    val cfg = LocalConfiguration.current
    val isLarge = cfg.smallestScreenWidthDp >= 600
    return if (isLarge) 68.dp to 12.dp else 56.dp to 8.dp
}
