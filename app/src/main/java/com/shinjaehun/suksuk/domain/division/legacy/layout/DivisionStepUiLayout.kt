package com.shinjaehun.suksuk.domain.division.legacy.layout

import com.shinjaehun.suksuk.domain.division.legacy.model.DivisionPhase
import com.shinjaehun.suksuk.domain.division.legacy.model.InputCell
import com.shinjaehun.suksuk.domain.division.model.DivisionCellName

data class DivisionStepUiLayout(
    val phase: DivisionPhase,
    val cells: Map<DivisionCellName, InputCell> = emptyMap(),
    val inputIndices: Map<DivisionCellName, Int?> = emptyMap(),
    val showSubtractLine: Boolean = false,
    val feedback: String? = null,
)
