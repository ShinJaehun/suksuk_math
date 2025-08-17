package com.shinjaehun.suksuk.presentation.multiplication.model

import com.shinjaehun.suksuk.common.ui.Highlight
import com.shinjaehun.suksuk.domain.multiplication.model.MulCell

data class MulInputCell (
    val cellName: MulCell,
    val value: String? = null,
    val editable: Boolean = false,
    val highlight: Highlight = Highlight.None,
    val hidden: Boolean = false,
    val totalLineType: TotalLineType = TotalLineType.None
)

enum class TotalLineType {
    None,
    Confirmed
}