package com.shinjaehun.suksuk.presentation.common.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PresentationScaffold(
    isLandscape: Boolean,
    board: @Composable BoxScope.() -> Unit,
    panel: @Composable BoxScope.() -> Unit,
    // landscape tuning(세 화면에서 같은 값 사용)
    designWidth: Dp = 360.dp,
    designHeight: Dp = 560.dp,
    minScale: Float = 0.45f,
    boardWeight: Float = 1.2f,
    panelWeight: Float = 1.5f,
    contentWidthFraction: Float = 0.90f,
    maxContentWidth: Dp = 1400.dp,
    outerPadding: Dp = 16.dp,
    innerGutter: Dp = 12.dp,
) {
    if (isLandscape) {
        DualPaneBoardScaffold(
            designWidth = designWidth,
            designHeight = designHeight,
            minScale = minScale,
            boardWeight = boardWeight,
            panelWeight = panelWeight,
            contentWidthFraction = contentWidthFraction,
            maxContentWidth = maxContentWidth,
            outerPadding = outerPadding,
            innerGutter = innerGutter,
            board = { Box(Modifier.align(Alignment.TopCenter).fillMaxWidth()) { board() } },
            panel = { Box(Modifier.fillMaxSize()) { panel() } }
        )
    } else {
        Box(Modifier.fillMaxSize()) {
            Box(Modifier.align(Alignment.TopCenter).fillMaxWidth()) { board() }
            Box(Modifier.align(Alignment.BottomCenter).fillMaxWidth()) { panel() }
        }
    }
}