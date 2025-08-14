package com.shinjaehun.suksuk.presentation.multiplication.model

import com.shinjaehun.suksuk.domain.multiplication.model.MulCellName
import com.shinjaehun.suksuk.domain.multiplication.model.MulPattern

data class MulUiState(
    val cells: Map<MulCellName, MulInputCell> = emptyMap(),
    val currentState: Int = 0,
    val isCompleted: Boolean = false,
    val feedback: String? = null,
    val pattern: MulPattern = MulPattern.TwoByTwo
)
