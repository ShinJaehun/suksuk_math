package com.shinjaehun.suksuk.domain.division.layout

import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2

data class PhaseSequence(
    val pattern: DivisionPatternV2,
    val steps: List<PhaseStep>
)