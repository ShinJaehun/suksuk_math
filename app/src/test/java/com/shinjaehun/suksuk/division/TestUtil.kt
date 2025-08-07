package com.shinjaehun.suksuk.division

import com.shinjaehun.suksuk.domain.division.model.DivisionDomainStateV2
import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2
import com.shinjaehun.suksuk.domain.division.layout.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.division.model.DivisionUiStateV2
import com.shinjaehun.suksuk.domain.division.detector.PatternDetectorV2
import com.shinjaehun.suksuk.domain.division.layout.sequence.ThreeByTwoPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.layout.sequence.TwoByOnePhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.layout.sequence.TwoByTwoPhaseSequenceCreator
import com.shinjaehun.suksuk.presentation.division.mapToUiStateV2

fun makeDomainStateForTest(
    dividend: Int,
    divisor: Int,
    step: Int,
    inputs: List<String> = emptyList(),
    pattern: DivisionPatternV2 = PatternDetectorV2.detectPattern(dividend, divisor),
    sequenceProvider: DivisionPhaseSequenceProvider = DivisionPhaseSequenceProvider(
        twoByOneCreator = TwoByOnePhaseSequenceCreator(),
        twoByTwoCreator = TwoByTwoPhaseSequenceCreator(),
        threeByTwoCreator = ThreeByTwoPhaseSequenceCreator()
    )
): DivisionDomainStateV2 {
    return DivisionDomainStateV2(
        dividend = dividend,
        divisor = divisor,
        phaseSequence = when (pattern) {
            DivisionPatternV2.TwoByOne -> sequenceProvider.makeTwoByOnePhaseSequence(dividend, divisor)
            DivisionPatternV2.TwoByTwo -> sequenceProvider.makeTwoByTwoPhaseSequence(dividend, divisor)
            DivisionPatternV2.ThreeByTwo -> sequenceProvider.makeThreeByTwoPhaseSequence(dividend, divisor)
        },
        currentStepIndex = step,
        inputs = inputs
    )
}

fun makeUiStateForTest(
    dividend: Int,
    divisor: Int,
    step: Int,
    inputs: List<String> = emptyList()
): DivisionUiStateV2 {
    val domain = makeDomainStateForTest(dividend, divisor, step, inputs)
    return mapToUiStateV2(domain, currentInput = "")
}