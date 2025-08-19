package com.shinjaehun.suksuk.domain.division.sequence

import com.shinjaehun.suksuk.domain.pattern.DivisionPatternV2

data class DivisionPhaseSequence(
    val steps: List<DivisionPhaseStep>,
    val pattern: DivisionPatternV2
)