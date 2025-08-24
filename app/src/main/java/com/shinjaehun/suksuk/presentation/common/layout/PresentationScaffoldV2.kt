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
import androidx.compose.ui.draw.clipToBounds
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

@Composable
fun rememberButtonSizes(): Pair<Dp, Dp> {
    val cfg = LocalConfiguration.current
    val isLarge = cfg.smallestScreenWidthDp >= 600
    return if (isLarge) 68.dp to 12.dp else 56.dp to 8.dp
}