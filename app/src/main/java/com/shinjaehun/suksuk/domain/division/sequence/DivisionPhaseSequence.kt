package com.shinjaehun.suksuk.domain.division.sequence

import com.shinjaehun.suksuk.domain.pattern.DivisionPattern

data class DivisionPhaseSequence(
    val steps: List<DivisionPhaseStep>,
    val pattern: DivisionPattern
)