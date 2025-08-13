package com.shinjaehun.suksuk.division

import com.shinjaehun.suksuk.domain.division.DivisionStateInfoBuilder
import com.shinjaehun.suksuk.domain.division.model.DivisionDomainStateV2
import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2
import com.shinjaehun.suksuk.domain.division.layout.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.division.model.DivisionUiStateV2
import com.shinjaehun.suksuk.domain.division.detector.PatternDetectorV2
import com.shinjaehun.suksuk.domain.division.layout.PhaseSequence
import com.shinjaehun.suksuk.domain.division.layout.PhaseStep
import com.shinjaehun.suksuk.domain.division.layout.sequence.ThreeByTwoPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.layout.sequence.TwoByOnePhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.layout.sequence.TwoByTwoPhaseSequenceCreator
import com.shinjaehun.suksuk.presentation.division.mapToUiStateV2

fun makeDomainStateForTest(
    dividend: Int,
    divisor: Int,
    step: Int,
    inputs: List<String> = emptyList(),
    sequenceProvider: DivisionPhaseSequenceProvider = DivisionPhaseSequenceProvider(
        twoByOneCreator = TwoByOnePhaseSequenceCreator(),
        twoByTwoCreator = TwoByTwoPhaseSequenceCreator(),
        threeByTwoCreator = ThreeByTwoPhaseSequenceCreator(),
    )
): DivisionDomainStateV2 {
    val pattern = PatternDetectorV2.detectPattern(dividend, divisor)
    val info = DivisionStateInfoBuilder.from(dividend, divisor)
    val sequence = sequenceProvider.make(pattern, info)

    return DivisionDomainStateV2(
        phaseSequence = sequence,
        currentStepIndex = step,
        inputs = inputs,
        info = info
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

fun createDomainState(
    dividend: Int,
    divisor: Int,
    steps: List<PhaseStep>,
    currentStepIndex: Int,
    inputs: List<String> = emptyList(),
    pattern: DivisionPatternV2 = PatternDetectorV2.detectPattern(dividend, divisor)
): DivisionDomainStateV2 {
    val info = DivisionStateInfoBuilder.from(dividend, divisor)
    return DivisionDomainStateV2(
        phaseSequence = PhaseSequence(pattern, steps),
        currentStepIndex = currentStepIndex,
        inputs = inputs,
        info = info
    )
}