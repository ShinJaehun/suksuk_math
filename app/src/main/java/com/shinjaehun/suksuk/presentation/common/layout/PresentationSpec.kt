package com.shinjaehun.suksuk.presentation.common.layout

import androidx.compose.ui.unit.Dp

enum class LayoutMode { SmallPortrait, SmallLandscape, LargePortrait, LargeLandscape }

data class ScaleBounds(
    val min: Float,
    val max: Float,
    val shrink: Float,
    val alignTop: Boolean
)

data class LayoutSpec(
    val mode: LayoutMode,
    val isRow: Boolean,          // Row(가로 2패널) vs Column(세로 2스택)
    val boardWeight: Float,
    val panelWeight: Float,
    val outerPaddingH: Dp,
    val innerGutter: Dp,
    val topOffset: Dp,
    val useScaledBoard: Boolean,
    val scaleBounds: ScaleBounds,
    val maxContentWidth: Dp? = null
)