package com.shinjaehun.suksuk.presentation.multiplication.model

import com.shinjaehun.suksuk.domain.multiplication.model.MulCell
import com.shinjaehun.suksuk.domain.pattern.MulPattern

data class MulUiState(
    val cells: Map<MulCell, MulInputCell> = emptyMap(),
    val currentStep: Int = 0,
    val isCompleted: Boolean = false,
    val feedback: String? = null,
    val pattern: MulPattern? = null
)
