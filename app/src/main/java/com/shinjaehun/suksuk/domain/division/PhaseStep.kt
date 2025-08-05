package com.shinjaehun.suksuk.domain.division

import com.shinjaehun.suksuk.domain.division.model.CellName

data class PhaseStep(
    val phase: DivisionPhaseV2,
    val editableCells: List<CellName> = emptyList(),
    val highlightCells: List<CellName> = emptyList(),
    val needsBorrow: Boolean = false,
    val needsCarry: Boolean = false,
    val subtractLineTargets: Set<CellName> = emptySet(),
    val presetValues: Map<CellName, String> = emptyMap(),
    val strikeThroughCells: List<CellName> = emptyList()
)
