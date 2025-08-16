package com.shinjaehun.suksuk.domain.multiplication.sequence.creator

import com.shinjaehun.suksuk.domain.multiplication.info.MulStateInfo
import com.shinjaehun.suksuk.domain.multiplication.model.MulPhase
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseSequence
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseStep
import javax.inject.Inject

class ThreeByTwoMulPhaseSequenceCreator @Inject constructor() : MulPhaseSequenceCreator {

    override fun create(info: MulStateInfo): MulPhaseSequence {
        val steps = mutableListOf<MulPhaseStep>()

        steps += MulPhaseStep(phase = MulPhase.Complete)

        return MulPhaseSequence(
            steps = steps
        )
    }
}