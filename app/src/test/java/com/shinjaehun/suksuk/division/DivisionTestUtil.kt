package com.shinjaehun.suksuk.division

import com.shinjaehun.suksuk.domain.OpType
import com.shinjaehun.suksuk.domain.Problem
import com.shinjaehun.suksuk.domain.division.info.DivisionStateInfoBuilder
import com.shinjaehun.suksuk.domain.model.DivisionDomainState
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.presentation.division.model.DivisionUiState
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseSequence
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseStep
import com.shinjaehun.suksuk.domain.division.sequence.creator.ThreeByTwoDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.sequence.creator.TwoByOneDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.sequence.creator.TwoByTwoDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.pattern.DivisionPattern
import com.shinjaehun.suksuk.domain.pattern.detectPattern
import com.shinjaehun.suksuk.presentation.division.model.mapDivisionUiState

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
): DivisionDomainState {
    val problem = Problem(OpType.Division, dividend, divisor)
    val opPattern = detectPattern(problem)
    val pattern = opPattern as DivisionPattern

    val info = DivisionStateInfoBuilder.from(dividend, divisor)
    val sequence = sequenceProvider.make(pattern, info)

    return DivisionDomainState(
        phaseSequence = sequence,
        currentStepIndex = step,
        inputs = inputs,
        info = info,
//        pattern = pattern
    )
}

fun makeUiStateForTest(
    dividend: Int,
    divisor: Int,
    step: Int,
    inputs: List<String> = emptyList()
): DivisionUiState {
    val domain = makeDomainStateForTest(dividend, divisor, step, inputs)
    return mapDivisionUiState(domain, currentInput = "")
}

fun createDomainState(
    dividend: Int,
    divisor: Int,
    steps: List<DivisionPhaseStep>,
    currentStepIndex: Int,
    inputs: List<String> = emptyList(),
    pattern: DivisionPattern
): DivisionDomainState {
    val info = DivisionStateInfoBuilder.from(dividend, divisor)
    return DivisionDomainState(
        phaseSequence = DivisionPhaseSequence(steps, pattern),
        currentStepIndex = currentStepIndex,
        inputs = inputs,
        info = info,
//        pattern = pattern
    )
}

