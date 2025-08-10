package com.shinjaehun.suksuk.domain.division.layout.sequence

import com.shinjaehun.suksuk.domain.division.DivisionStateInfo
import com.shinjaehun.suksuk.domain.division.layout.PhaseSequence

interface PhaseSequenceCreator {
    fun create(info: DivisionStateInfo): PhaseSequence
}