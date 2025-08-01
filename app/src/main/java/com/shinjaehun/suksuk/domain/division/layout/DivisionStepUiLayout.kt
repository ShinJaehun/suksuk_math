package com.shinjaehun.suksuk.domain.division.layout

import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.domain.division.model.DivisionPhase
import com.shinjaehun.suksuk.domain.division.model.InputCell

data class DivisionStepUiLayout(
    val phase: DivisionPhase,
    val cells: Map<CellName, InputCell> = emptyMap(),
    val inputIndices: Map<CellName, Int?> = emptyMap(),
    val showSubtractLine: Boolean = false,
    val feedback: String? = null,
)
