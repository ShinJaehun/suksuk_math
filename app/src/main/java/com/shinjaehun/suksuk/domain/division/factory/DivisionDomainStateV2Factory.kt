package com.shinjaehun.suksuk.domain.division.factory

import com.shinjaehun.suksuk.domain.division.DivisionStateInfoBuilder
import com.shinjaehun.suksuk.domain.division.detector.PatternDetectorV2
import com.shinjaehun.suksuk.domain.division.model.DivisionDomainStateV2
import com.shinjaehun.suksuk.domain.division.layout.DivisionPhaseSequenceProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DivisionDomainStateV2Factory @Inject constructor(
    private val detector: PatternDetectorV2,
    private val phaseSequenceProvider: DivisionPhaseSequenceProvider,
) {
    fun create(dividend: Int, divisor: Int): DivisionDomainStateV2 {
        val pattern = detector.detectPattern(dividend, divisor)
//        val pattern = PatternDetectorV2.detectPattern(dividend, divisor) // 사실 이렇게 써도 됩니다...
        val info = DivisionStateInfoBuilder.from(dividend, divisor)
        val sequence = phaseSequenceProvider.make(pattern, info)

        return DivisionDomainStateV2(
            phaseSequence = sequence,
            currentStepIndex = 0,
            inputs = emptyList(),
            info = info
        )
    }
}
