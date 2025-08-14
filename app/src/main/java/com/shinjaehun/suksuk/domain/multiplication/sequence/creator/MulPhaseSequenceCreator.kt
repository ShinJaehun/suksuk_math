package com.shinjaehun.suksuk.domain.multiplication.sequence.creator

import com.shinjaehun.suksuk.domain.multiplication.info.MulStateInfo
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseSequence

interface MulPhaseSequenceCreator {
    fun create(info: MulStateInfo): MulPhaseSequence
}