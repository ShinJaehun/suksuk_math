package com.shinjaehun.suksuk.presentation.division.model

import com.shinjaehun.suksuk.common.ui.Highlight
import com.shinjaehun.suksuk.domain.division.model.DivisionCell

data class DivisionInputCellV2(
    val cellName: DivisionCell,
    val value: String? = null,
    val editable: Boolean = false,
    val highlight: Highlight = Highlight.None,
    val crossOutType: CrossOutType = CrossOutType.None,
    val subtractLineType: SubtractLineType = SubtractLineType.None,
    val hidden: Boolean = false,
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