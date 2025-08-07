package com.shinjaehun.suksuk.domain.division.factory

import com.shinjaehun.suksuk.domain.division.model.DivisionDomainStateV2
import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2
import com.shinjaehun.suksuk.domain.division.layout.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.division.detector.PatternDetectorV2
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DivisionDomainStateV2Factory @Inject constructor(
    private val phaseSequenceProvider: DivisionPhaseSequenceProvider,
    private val patternDetector: PatternDetectorV2
) {
    fun create(dividend: Int, divisor: Int): DivisionDomainStateV2 {
        val pattern = patternDetector.detectPattern(dividend, divisor)
        val sequence = when (pattern) {
            DivisionPatternV2.TwoByOne -> phaseSequenceProvider.makeTwoByOnePhaseSequence(dividend, divisor)
            DivisionPatternV2.TwoByTwo -> phaseSequenceProvider.makeTwoByTwoPhaseSequence(dividend, divisor)
            DivisionPatternV2.ThreeByTwo -> phaseSequenceProvider.makeThreeByTwoPhaseSequence(dividend, divisor)
        }

        return DivisionDomainStateV2(
            dividend = dividend,
            divisor = divisor,
            phaseSequence = sequence,
            currentStepIndex = 0,
            inputs = emptyList()
        )
    }
}
