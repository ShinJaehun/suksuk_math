package com.shinjaehun.suksuk.presentation.common.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PresentationScaffoldV2(
    board: @Composable BoxScope.() -> Unit,
    panel: @Composable BoxScope.() -> Unit,
    specOverride: (LayoutSpec) -> LayoutSpec = { it } // 특정 화면만 미세조정 가능
) {
    val spec = rememberLayoutSpec(override = specOverride)

    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing) // 시스템 인셋 대응
    ) {
        val rowWidth = spec.maxContentWidth?.let { minOf(maxWidth, it) } ?: maxWidth
        val designW = UiTokens.DesignBoardWidth    // 예: 360.dp
        val designH = UiTokens.DesignBoardHeight   // 예: 560.dp

        if (spec.isRow) {
            // 가로 2패널
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = spec.outerPaddingH),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    Modifier
                        .width(rowWidth)
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.spacedBy(spec.innerGutter),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // BOARD
                    Box(
                        Modifier
//                            .background(Color.Red.copy(alpha = 0.2f))
                            .weight(spec.boardWeight)
                            .fillMaxHeight()
                            .padding(top = spec.topOffset),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        if (spec.useScaledBoard) {
                            ScaledBoardInSlot(
                                designWidth = designW,
                                designHeight = designH,
                                minScale = spec.scaleBounds.min,
                                maxScale = spec.scaleBounds.max,
                                shrink = spec.scaleBounds.shrink,
                                alignTop = spec.scaleBounds.alignTop
                            ) { board() }
                        } else {
                            Box(Modifier.align(Alignment.TopCenter), content = board)
                        }
                    }

                    // PANEL
                    Box(
                        Modifier
//                            .background(Color.Blue.copy(alpha = 0.2f))
                            .weight(spec.panelWeight)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) { panel() }
                }
            }
        } else {
            // 세로 2스택
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = spec.outerPaddingH),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    Modifier
                        .width(rowWidth)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // BOARD
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)  // 필요 시 spec.boardWeight로 바꿔도 됨
                            .offset(y = spec.topOffset),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        if (spec.useScaledBoard) {
                            ScaledBoardInSlot(
                                designWidth = designW,
                                designHeight = designH,
                                minScale = spec.scaleBounds.min,
                                maxScale = spec.scaleBounds.max,
                                shrink = spec.scaleBounds.shrink,
                                alignTop = spec.scaleBounds.alignTop
                            ) { board() }
                        } else {
                            board()
                        }
                    }

                    Spacer(Modifier.height(spec.innerGutter))

                    // PANEL
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(), // LargePortrait만 weight 주고 싶으면 spec.mode로 분기
                        contentAlignment = Alignment.Center
                    ) { panel() }

                    Spacer(Modifier.height(UiTokens.PanelBottomSpacing)) // 예: 28.dp
                }
            }
        }
    }
}

//@Composable
//fun PresentationScaffoldV2(
//    board: @Composable BoxScope.() -> Unit,
//    panel: @Composable BoxScope.() -> Unit,
//    specOverride: (LayoutSpec) -> LayoutSpec = { it },
//    debugColors: Boolean = true
//) {
//    val spec = rememberLayoutSpec(override = specOverride)
//
//    // ⬇️ 인셋은 세로만 적용 (좌우 비대칭 인셋으로 중앙이 어긋나는 걸 방지)
//    val verticalInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Bottom)
//
//    BoxWithConstraints(
//        Modifier
//            .fillMaxSize()
////            .windowInsetsPadding(verticalInsets)
//            .then(if (debugColors) Modifier.background(Color(0x11_FFFFFF)) else Modifier) // 화면 전체
//    ) {
//        val rowWidth = spec.maxContentWidth?.let { minOf(maxWidth, it) } ?: maxWidth
//        val designW = UiTokens.DesignBoardWidth
//        val designH = UiTokens.DesignBoardHeight
//
//        Box(
//            Modifier
//                .fillMaxHeight()
//                .width(2.dp)
//                .align(Alignment.Center) // 화면 정중앙
//                .background(Color.Black.copy(alpha = 0.25f))
//        )
//
//        if (spec.isRow) {
//            // ▶ 가로 2패널
//            Box(
//                Modifier
//                    .fillMaxSize()
//                    .padding(horizontal = spec.outerPaddingH)
//                    .then(if (debugColors) Modifier.background(Color(0x11_FF0000)) else Modifier), // 바깥 컨테이너
//            ) {
//                Row(
//                    modifier = Modifier
//                        .align(Alignment.Center)   // ★ 부모 기준 '진짜' 중앙 고정
//                        .width(rowWidth)
//                        .fillMaxHeight()
//                        .then(if (debugColors) Modifier.background(Color(0x11_0000FF)) else Modifier), // Row 영역
//                    horizontalArrangement = Arrangement.spacedBy(spec.innerGutter),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    // BOARD
//                    Box(
//                        Modifier
//                            .weight(spec.boardWeight)
//                            .fillMaxHeight()
//                            .padding(top = spec.topOffset)
//                            .then(if (debugColors) Modifier.background(Color(0x22_FFC0CB)) else Modifier), // 보드 슬롯
//                        contentAlignment = Alignment.TopCenter
//                    ) {
//                        if (spec.useScaledBoard) {
//                            ScaledBoardInSlot(
//                                designWidth = designW,
//                                designHeight = designH,
//                                minScale = spec.scaleBounds.min,
//                                maxScale = spec.scaleBounds.max,
//                                shrink = spec.scaleBounds.shrink,
//                                alignTop = spec.scaleBounds.alignTop,
//                                modifier = Modifier.fillMaxSize()
//                            ) { board() }
//                        } else {
//                            // 좌측 쏠림 방지: 슬롯 전체를 한 번 감싸고 TopCenter에 다시 배치
//                            Box(
//                                Modifier.matchParentSize(),
//                                contentAlignment = Alignment.TopCenter
//                            ) { board() }
//                        }
//                    }
//
//                    // PANEL
//                    Box(
//                        Modifier
//                            .weight(spec.panelWeight)
//                            .fillMaxHeight()
//                            .then(if (debugColors) Modifier.background(Color(0x22_CCCBFF)) else Modifier), // 패널 슬롯
//                        contentAlignment = Alignment.Center
//                    ) { panel() }
//                }
//            }
//        } else {
//            // ▶ 세로 2스택
//            Box(
//                Modifier
//                    .fillMaxSize()
//                    .padding(horizontal = spec.outerPaddingH)
//                    .then(if (debugColors) Modifier.background(Color(0x11_00FF00)) else Modifier), // 바깥 컨테이너
//            ) {
//                Column(
//                    modifier = Modifier
//                        .align(Alignment.Center)   // ★ 부모 기준 중앙 고정
//                        .width(rowWidth)
//                        .fillMaxHeight()
//                        .then(if (debugColors) Modifier.background(Color(0x11_0000FF)) else Modifier), // Column 영역
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    // BOARD
//                    Box(
//                        Modifier
//                            .fillMaxWidth()
//                            .weight(1f)
//                            .offset(y = spec.topOffset)
//                            .then(if (debugColors) Modifier.background(Color(0x22_FFC0CB)) else Modifier), // 보드 슬롯
//                        contentAlignment = Alignment.TopCenter
//                    ) {
//                        if (spec.useScaledBoard) {
//                            ScaledBoardInSlot(
//                                designWidth = designW,
//                                designHeight = designH,
//                                minScale = spec.scaleBounds.min,
//                                maxScale = spec.scaleBounds.max,
//                                shrink = spec.scaleBounds.shrink,
//                                alignTop = spec.scaleBounds.alignTop,
//                                modifier = Modifier.fillMaxSize()
//                            ) { board() }
//                        } else {
//                            Box(
//                                Modifier.matchParentSize(),
//                                contentAlignment = Alignment.TopCenter
//                            ) { board() }
//                        }
//                    }
//
//                    Spacer(Modifier.height(spec.innerGutter))
//
//                    // PANEL
//                    Box(
//                        Modifier
//                            .fillMaxWidth()
//                            .wrapContentHeight()
//                            .then(if (debugColors) Modifier.background(Color(0x22_CCCBFF)) else Modifier), // 패널 슬롯
//                        contentAlignment = Alignment.Center
//                    ) { panel() }
//
//                    Spacer(Modifier.height(UiTokens.PanelBottomSpacing))
//                }
//            }
//        }
//    }
//}


//@Composable
//fun PresentationScaffoldV2(
//    board: @Composable BoxScope.() -> Unit,
//    panel: @Composable BoxScope.() -> Unit,
//    specOverride: (LayoutSpec) -> LayoutSpec = { it },
//) {
//    val spec = rememberLayoutSpec(override = specOverride)
//
//    // 세로만 인셋 적용(좌우 비대칭 인셋으로 중앙 어긋나는 것 방지)
//    val verticalInsets = WindowInsets.safeDrawing.only(
//        WindowInsetsSides.Top + WindowInsetsSides.Bottom
//    )
//
//    BoxWithConstraints(
//        Modifier
//            .fillMaxSize()
//            .windowInsetsPadding(verticalInsets)
//    ) {
//        val designW = UiTokens.DesignBoardWidth
//        val designH = UiTokens.DesignBoardHeight
//
//        // 중앙 컨테이너: 패딩과 cap을 '같은 곳'에 적용
//        val contentWidth = maxWidth - spec.outerPaddingH * 2
//        val centerWidth = spec.maxContentWidth?.let { minOf(contentWidth, it) } ?: contentWidth
//
//        Box(
//            Modifier
//                .align(Alignment.Center)      // 화면 기준 중앙 고정
//                .width(centerWidth)           // cap 적용
//                .fillMaxHeight()
//                .padding(horizontal = spec.outerPaddingH) // 좌우 바깥 패딩(중앙 컨테이너에만)
//        ) {
//            if (spec.isRow) {
//                // ▶ 가로 2패널
//                Row(
//                    Modifier.fillMaxSize(),  // 중앙 컨테이너를 꽉 채움 (대칭 유지)
//                    horizontalArrangement = Arrangement.spacedBy(spec.innerGutter),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    // BOARD 슬롯
//                    Box(
//                        Modifier
//                            .background(Color.Red.copy(alpha = 0.2f))
//                            .weight(spec.boardWeight)
//                            .fillMaxHeight()
//                            .padding(top = spec.topOffset),
//                        contentAlignment = Alignment.TopCenter
//                    ) {
//                        if (spec.useScaledBoard) {
//                            ScaledBoardInSlot(
//                                designWidth = designW,
//                                designHeight = designH,
//                                minScale = spec.scaleBounds.min,
//                                maxScale = spec.scaleBounds.max,
//                                shrink = spec.scaleBounds.shrink,
//                                alignTop = spec.scaleBounds.alignTop,
//                                modifier = Modifier.fillMaxSize()
//                            ) { board() }
//                        } else {
//                            // 스케일 미적용 경로(작은 세로 등): 정렬만 보장
//                            Box(
//                                Modifier.matchParentSize(),
//                                contentAlignment = Alignment.TopCenter
//                            ) { board() }
//                        }
//                    }
//
//                    // PANEL 슬롯
//                    Box(
//                        Modifier
//                            .weight(spec.panelWeight)
//                            .fillMaxHeight(),
//                        contentAlignment = Alignment.Center
//                    ) { panel() }
//                }
//            } else {
//                // ▶ 세로 2스택
//                Column(
//                    Modifier.fillMaxSize(),     // 중앙 컨테이너를 꽉 채움
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    // BOARD
//                    Box(
//                        Modifier
//                            .fillMaxWidth()
//                            .weight(1f)
//                            .offset(y = spec.topOffset),
//                        contentAlignment = Alignment.TopCenter
//                    ) {
//                        if (spec.useScaledBoard) {
//                            ScaledBoardInSlot(
//                                designWidth = designW,
//                                designHeight = designH,
//                                minScale = spec.scaleBounds.min,
//                                maxScale = spec.scaleBounds.max,
//                                shrink = spec.scaleBounds.shrink,
//                                alignTop = spec.scaleBounds.alignTop,
//                                modifier = Modifier.fillMaxSize()
//                            ) { board() }
//                        } else {
//                            Box(
//                                Modifier.matchParentSize(),
//                                contentAlignment = Alignment.TopCenter
//                            ) { board() }
//                        }
//                    }
//
//                    Spacer(Modifier.height(spec.innerGutter))
//
//                    // PANEL
//                    Box(
//                        Modifier
//                            .fillMaxWidth()
//                            .wrapContentHeight(),
//                        contentAlignment = Alignment.Center
//                    ) { panel() }
//
//                    Spacer(Modifier.height(UiTokens.PanelBottomSpacing))
//                }
//            }
//
//            // (옵션) 중앙 가이드선 디버깅
//            // Box(Modifier.align(Alignment.Center).fillMaxHeight().width(2.dp))
//        }
//    }
//}

@Composable
fun rememberButtonSizes(): Pair<Dp, Dp> {
    val cfg = LocalConfiguration.current
    val isLarge = cfg.smallestScreenWidthDp >= 600
    return if (isLarge) 68.dp to 12.dp else 56.dp to 8.dp
}