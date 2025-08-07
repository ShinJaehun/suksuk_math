package com.shinjaehun.suksuk.domain.division.layout.sequence

import com.shinjaehun.suksuk.domain.division.layout.PhaseSequence

interface PhaseSequenceCreator {
    fun create(dividend: Int, divisor: Int): PhaseSequence
}