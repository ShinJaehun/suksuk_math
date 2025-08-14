package com.shinjaehun.suksuk.presentation.multiplication.model

import com.shinjaehun.suksuk.common.ui.Highlight
import com.shinjaehun.suksuk.domain.multiplication.model.MulCellName

data class MulInputCell (
    val cellName: MulCellName,
    val value: String? = null,
    val editable: Boolean = false,
    val highlight: Highlight = Highlight.None
)