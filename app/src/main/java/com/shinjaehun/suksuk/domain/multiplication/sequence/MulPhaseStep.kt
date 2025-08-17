package com.shinjaehun.suksuk.domain.multiplication.sequence

import com.shinjaehun.suksuk.domain.division.model.DivisionCell
import com.shinjaehun.suksuk.domain.multiplication.model.MulCell
import com.shinjaehun.suksuk.domain.multiplication.model.MulPhase

data class MulPhaseStep(
    val phase: MulPhase,
    val editableCells: List<MulCell> = emptyList(),
    val highlightCells: List<MulCell> = emptyList(),
    val needsCarry: Boolean = false,
    val clearCells: Set<MulCell> = emptySet(),
    val totalLineTargets: Set<MulCell> = emptySet(),
)
