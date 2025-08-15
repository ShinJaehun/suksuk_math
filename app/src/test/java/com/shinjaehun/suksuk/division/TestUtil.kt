package com.shinjaehun.suksuk.division

import com.shinjaehun.suksuk.domain.division.info.DivisionStateInfoBuilder
import com.shinjaehun.suksuk.domain.division.model.DivisionDomainStateV2
import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.presentation.division.model.DivisionUiStateV2
import com.shinjaehun.suksuk.domain.division.detector.DivisionPatternDetectorV2
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseSequence
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseStep
import com.shinjaehun.suksuk.domain.division.sequence.creator.ThreeByTwoDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.sequence.creator.TwoByOneDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.sequence.creator.TwoByTwoDivPhaseSequenceCreator
import com.shinjaehun.suksuk.presentation.division.model.mapDivisionUiStateV2

fun makeDomainStateForTest(
    dividend: Int,
    divisor: Int,
    step: Int,
    inputs: List<String> = emptyList(),
    sequenceProvider: DivisionPhaseSequenceProvider = DivisionPhaseSequenceProvider(
        twoByOneCreator = TwoByOneDivPhaseSequenceCreator(),
        twoByTwoCreator = TwoByTwoDivPhaseSequenceCreator(),
        threeByTwoCreator = ThreeByTwoDivPhaseSequenceCreator(),
    )
): DivisionDomainStateV2 {
    val pattern = DivisionPatternDetectorV2.detectPattern(dividend, divisor)
    val info = DivisionStateInfoBuilder.from(dividend, divisor)
    val sequence = sequenceProvider.make(pattern, info)

    return DivisionDomainStateV2(
        phaseSequence = sequence,
        currentStepIndex = step,
        inputs = inputs,
        info = info,
        pattern = pattern
    )
}

fun makeUiStateForTest(
    dividend: Int,
    divisor: Int,
    step: Int,
    inputs: List<String> = emptyList()
): DivisionUiStateV2 {
    val domain = makeDomainStateForTest(dividend, divisor, step, inputs)
    return mapDivisionUiStateV2(domain, currentInput = "")
}

fun createDomainState(
    dividend: Int,
    divisor: Int,
    steps: List<DivisionPhaseStep>,
    currentStepIndex: Int,
    inputs: List<String> = emptyList(),
    pattern: DivisionPatternV2 = DivisionPatternDetectorV2.detectPattern(dividend, divisor)
): DivisionDomainStateV2 {
    val info = DivisionStateInfoBuilder.from(dividend, divisor)
    return DivisionDomainStateV2(
        phaseSequence = DivisionPhaseSequence(steps),
        currentStepIndex = currentStepIndex,
        inputs = inputs,
        info = info,
        pattern = pattern
    )
}
