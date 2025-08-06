package com.shinjaehun.suksuk.domain.division

import com.shinjaehun.suksuk.domain.division.model.CellName

// InputCellV2.kt
data class InputCellV2(
    val cellName: CellName,
    val value: String? = null,
    val editable: Boolean = false,
    val highlight: Highlight = Highlight.None,
    val crossOutType: CrossOutType = CrossOutType.None,
    val subtractLineType: SubtractLineType = SubtractLineType.None
)

enum class CrossOutType {
    None,
    Pending,
    Confirmed
}

enum class SubtractLineType {
    None,
    Pending,
    SubtractLine1,
    SubtractLine2,
}

enum class Highlight {
    None,      // 일반
    Editing,   // 현재 입력 중(빨간색 ?)
    Related    // 연관 강조(파란색)
}