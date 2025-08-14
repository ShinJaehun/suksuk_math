package com.shinjaehun.suksuk.domain.multiplication.factory

import com.shinjaehun.suksuk.domain.multiplication.detector.MulPatternDetector
import com.shinjaehun.suksuk.domain.multiplication.info.MulStateInfoBuilder
import com.shinjaehun.suksuk.domain.multiplication.model.MulDomainState
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseSequenceProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MulDomainStateFactory @Inject constructor(
    private val patternDetector: MulPatternDetector,
    private val phaseSequenceProvider: MulPhaseSequenceProvider
) {
    fun create(multiplicand: Int, multiplier: Int): MulDomainState {
        val pattern = patternDetector.detectPattern(multiplicand, multiplier)
        val info = MulStateInfoBuilder.from(multiplicand, multiplier)
        val sequence = phaseSequenceProvider.make(pattern, info)

        return MulDomainState(
            phaseSequence = sequence,
            currentStepIndex = 0,
            inputs = emptyList(),
            info = info,
            pattern = pattern
        )
    }
}