package com.shinjaehun.suksuk.domain.division.legacy.model

import com.shinjaehun.suksuk.domain.division.model.DivisionCell

data class InputCell(
    val divisionCell: DivisionCell = DivisionCell.None,
    val inputIdx: Int = -1,
    val value: String? = null,
    val editable: Boolean = false,
    val highlight: Highlight = Highlight.None,
    val crossOutColor: CrossOutColor = CrossOutColor.None,
)

enum class Highlight {
    None,      // 일반
    Editing,   // 현재 입력 중(빨간색 ?)
    Related    // 연관 강조(파란색)
}

enum class CrossOutColor {
    None,          // 취소선 없음
    Pending,       // 입력 중
    Confirmed      // 입력 후
}