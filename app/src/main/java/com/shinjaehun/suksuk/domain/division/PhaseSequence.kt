package com.shinjaehun.suksuk.domain.division

data class PhaseSequence(
    val pattern: DivisionPatternV2,
    val steps: List<PhaseStep>
)