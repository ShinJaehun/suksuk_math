package com.shinjaehun.suksuk

import com.shinjaehun.suksuk.domain.division.DivisionDomainStateV2
import com.shinjaehun.suksuk.domain.division.DivisionPatternV2
import com.shinjaehun.suksuk.domain.division.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.division.DivisionUiStateV2
import com.shinjaehun.suksuk.domain.division.PatternDetectorV2
import com.shinjaehun.suksuk.presentation.division.mapToUiStateV2

fun makeDomainStateForTest(
    dividend: Int,
    divisor: Int,
    step: Int,
    inputs: List<String> = emptyList(),
    pattern: DivisionPatternV2 = PatternDetectorV2.detectPattern(dividend, divisor),
    sequenceProvider: DivisionPhaseSequenceProvider = DivisionPhaseSequenceProvider()
): DivisionDomainStateV2 {
    return DivisionDomainStateV2(
        dividend = dividend,
        divisor = divisor,
        phaseSequence = when (pattern) {
            DivisionPatternV2.TwoByOne -> sequenceProvider.makeTwoByOnePhaseSequence(dividend, divisor)
            DivisionPatternV2.TwoByTwo -> sequenceProvider.makeTwoByTwoPhaseSequence(dividend, divisor)
            DivisionPatternV2.ThreeByTwo -> TODO()
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