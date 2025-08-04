package com.shinjaehun.suksuk.domain.division

import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.domain.division.model.CrossOutColor
import com.shinjaehun.suksuk.domain.division.model.Highlight

// InputCellV2.kt
data class InputCellV2(
    val cellName: CellName,
    val value: String? = null,
    val editable: Boolean = false,
    val highlight: Highlight = Highlight.None,
    val crossOutColor: CrossOutColor = CrossOutColor.None
)
