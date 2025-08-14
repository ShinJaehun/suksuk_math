package com.shinjaehun.suksuk.domain.division.sequence.creator

import com.shinjaehun.suksuk.domain.division.info.DivisionStateInfo
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseSequence

interface DivisionPhaseSequenceCreator {
    fun create(info: DivisionStateInfo): DivisionPhaseSequence
}