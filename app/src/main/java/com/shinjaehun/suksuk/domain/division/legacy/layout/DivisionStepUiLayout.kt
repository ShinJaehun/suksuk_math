package com.shinjaehun.suksuk.domain.division.legacy.layout

import com.shinjaehun.suksuk.domain.division.legacy.model.DivisionPhase
import com.shinjaehun.suksuk.domain.division.legacy.model.InputCell
import com.shinjaehun.suksuk.domain.division.model.CellName

data class DivisionStepUiLayout(
    val phase: DivisionPhase,
    val cells: Map<CellName, InputCell> = emptyMap(),
    val inputIndices: Map<CellName, Int?> = emptyMap(),
    val showSubtractLine: Boolean = false,
    val feedback: String? = null,
)
