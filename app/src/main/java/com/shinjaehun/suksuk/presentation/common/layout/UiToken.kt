package com.shinjaehun.suksuk.presentation.common.layout

import androidx.compose.ui.unit.dp

object UiTokens {
    val DesignBoardW = 360.dp    // 보드 기준 폭
    val DesignBoardH = 560.dp    // 보드 기준 높이
    const val MinScale = 0.45f   // 이보다 작아지면 안내(가로 극단 폼팩터 방어)

    val Outer = 20.dp            // 바깥 여백
    val Gutter = 16.dp           // 두 패널 사이 간격
    val PanelBottom = 28.dp      // 패널 하단 여유
}