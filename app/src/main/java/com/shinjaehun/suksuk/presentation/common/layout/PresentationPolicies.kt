package com.shinjaehun.suksuk.presentation.common.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.shinjaehun.suksuk.presentation.common.device.LocalDeviceClasses


@Composable
fun rememberLayoutSpec(
    // 필요시 화면별 미세조정을 위해 오버라이드 Hook 제공
    override: (LayoutSpec) -> LayoutSpec = { it }
): LayoutSpec {
    val dc = LocalDeviceClasses.current

    val mode = when {
        dc.isLarge && dc.isLandscape -> LayoutMode.LargeLandscape
        dc.isLarge && !dc.isLandscape -> LayoutMode.LargePortrait
        !dc.isLarge && dc.isLandscape -> LayoutMode.SmallLandscape
        else -> LayoutMode.SmallPortrait
    }

    val outer = UiTokens.OuterPaddingH       // 예: 20.dp
    val gutter = UiTokens.InnerGutter        // 예: 16.dp
    val maxW = if (dc.isLarge) 1400.dp else null

    val base = when (mode) {
        LayoutMode.LargeLandscape -> LayoutSpec(
            mode, isRow = true,
            boardWeight = 1.6f, panelWeight = 1.4f,
            outerPaddingH = outer, innerGutter = gutter,
            topOffset = 40.dp,
            useScaledBoard = true,
            scaleBounds = ScaleBounds(min=0.60f, max=1.35f, shrink=1f, alignTop=true),
            maxContentWidth = maxW
        )
        LayoutMode.SmallLandscape -> LayoutSpec(
            mode, isRow = true,
            boardWeight = 1.6f, panelWeight = 1.4f,
            outerPaddingH = outer, innerGutter = gutter,
            topOffset = 0.dp,
            useScaledBoard = true,
            scaleBounds = ScaleBounds(min=0.45f, max=1.00f, shrink=0.90f, alignTop=true),
            maxContentWidth = null
        )
        LayoutMode.LargePortrait -> LayoutSpec(
            mode, isRow = false,
            boardWeight = 1.2f, panelWeight = 0.8f,
            outerPaddingH = outer, innerGutter = gutter,
            topOffset = 48.dp,
            useScaledBoard = true,
            scaleBounds = ScaleBounds(min=0.70f, max=1.35f, shrink=1f, alignTop=true),
            maxContentWidth = maxW
        )
        LayoutMode.SmallPortrait -> LayoutSpec(
            mode, isRow = false,
            boardWeight = 1.0f, panelWeight = 0f,   // panel은 wrap
            outerPaddingH = outer, innerGutter = gutter,
            topOffset = (-8).dp,
            useScaledBoard = false, // 필요하면 true로 바꿔서 소형 세로도 스케일 적용
            scaleBounds = ScaleBounds(min=0.60f, max=1.00f, shrink=1f, alignTop=true),
            maxContentWidth = null
        )
    }
    return override(base)
}