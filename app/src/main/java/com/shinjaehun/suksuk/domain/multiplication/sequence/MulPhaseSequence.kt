package com.shinjaehun.suksuk.domain.multiplication.sequence

import com.shinjaehun.suksuk.domain.pattern.MulPattern

data class MulPhaseSequence (
    val steps: List<MulPhaseStep>,
    val pattern: MulPattern
)