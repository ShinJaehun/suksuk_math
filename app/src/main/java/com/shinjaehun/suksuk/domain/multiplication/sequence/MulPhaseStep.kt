package com.shinjaehun.suksuk.domain.multiplication.sequence

import com.shinjaehun.suksuk.domain.multiplication.model.MulCellName
import com.shinjaehun.suksuk.domain.multiplication.model.MulPhase

data class MulPhaseStep(
    val phase: MulPhase,
    val editableCells: List<MulCellName> = emptyList(),
    val highlightCells: List<MulCellName> = emptyList(),
    val needsCarry: Boolean = false
)
