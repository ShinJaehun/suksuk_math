package com.shinjaehun.suksuk.domain.multiplication.sequence

import com.shinjaehun.suksuk.domain.multiplication.info.MulStateInfo
import com.shinjaehun.suksuk.domain.multiplication.model.MulPattern
import com.shinjaehun.suksuk.domain.multiplication.sequence.creator.ThreeByTwoMulPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.multiplication.sequence.creator.TwoByTwoMulPhaseSequenceCreator
import javax.inject.Inject

class MulPhaseSequenceProvider @Inject constructor(
    private val twoByTwo: TwoByTwoMulPhaseSequenceCreator,
    private val threeByTwo: ThreeByTwoMulPhaseSequenceCreator,
) {
    fun make(pattern: MulPattern, info: MulStateInfo): MulPhaseSequence = when (pattern) {
        MulPattern.TwoByTwo   -> twoByTwo.create(info)
        MulPattern.ThreeByTwo -> threeByTwo.create(info)
    }
}