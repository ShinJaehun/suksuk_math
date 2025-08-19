package com.shinjaehun.suksuk.domain.division.sequence

import com.shinjaehun.suksuk.domain.division.model.DivisionCell
import com.shinjaehun.suksuk.domain.division.model.DivisionPhase

data class DivisionPhaseStep(
    val phase: DivisionPhase,
    val editableCells: List<DivisionCell> = emptyList(),
    val highlightCells: List<DivisionCell> = emptyList(),
    val needsBorrow: Boolean = false,
    val needsCarry: Boolean = false,
    val subtractLineTargets: Set<DivisionCell> = emptySet(),
    val presetValues: Map<DivisionCell, String> = emptyMap(),
    val strikeThroughCells: List<DivisionCell> = emptyList(),
    val clearCells: Set<DivisionCell> = emptySet(),
)
