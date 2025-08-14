package com.shinjaehun.suksuk.domain.division.sequence

import com.shinjaehun.suksuk.domain.division.model.DivisionCellName
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2

data class DivisionPhaseStep(
    val phase: DivisionPhaseV2,
    val editableCells: List<DivisionCellName> = emptyList(),
    val highlightCells: List<DivisionCellName> = emptyList(),
    val needsBorrow: Boolean = false,
    val needsCarry: Boolean = false,
    val subtractLineTargets: Set<DivisionCellName> = emptySet(),
    val presetValues: Map<DivisionCellName, String> = emptyMap(),
    val strikeThroughCells: List<DivisionCellName> = emptyList()
)
