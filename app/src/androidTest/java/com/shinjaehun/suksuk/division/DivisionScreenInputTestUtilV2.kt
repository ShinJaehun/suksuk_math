package com.shinjaehun.suksuk.division

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import com.shinjaehun.suksuk.domain.division.factory.DivisionDomainStateV2Factory
import com.shinjaehun.suksuk.domain.division.layout.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.division.detector.PatternDetectorV2
import com.shinjaehun.suksuk.domain.division.evaluator.PhaseEvaluatorV2
import com.shinjaehun.suksuk.domain.division.layout.sequence.ThreeByTwoPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.layout.sequence.TwoByOnePhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.layout.sequence.TwoByTwoPhaseSequenceCreator
import com.shinjaehun.suksuk.presentation.division.DivisionScreenV2
import com.shinjaehun.suksuk.presentation.division.DivisionViewModelV2

fun ComposeContentTestRule.divisionCaseV2(
    dividend: Int,
    divisor: Int,
    inputs: List<String>
) {
    val savedStateHandle = SavedStateHandle(mapOf("autoStart" to false))


    val twoByOneCreator = TwoByOnePhaseSequenceCreator()
    val twoByTwoCreator = TwoByTwoPhaseSequenceCreator()
    val threeByTwoCreator = ThreeByTwoPhaseSequenceCreator()
    val phaseSequenceProvider = DivisionPhaseSequenceProvider(twoByOneCreator, twoByTwoCreator, threeByTwoCreator)

    val phaseEvaluator = PhaseEvaluatorV2()
    val patternDetector = PatternDetectorV2

    val factory = DivisionDomainStateV2Factory(phaseSequenceProvider, patternDetector)

    val viewModel = DivisionViewModelV2(
        savedStateHandle = savedStateHandle,
        phaseEvaluator = phaseEvaluator,
        domainStateFactory = factory
    )

    this.runOnIdle {
        viewModel.startNewProblem(dividend, divisor)
    }

    this.setContent {
        // 실제로 사용하는 DivisionScreen V2 버전 연결
        DivisionScreenV2(viewModel = viewModel)
    }


    var i = 0
    while (i < inputs.size) {
        val input = inputs[i]
        if (input.length == 1) {
            this.onNodeWithTag("numpad-$input").performClick()
            this.onNodeWithTag("numpad-enter").performClick()
            i++
        } else if (input.length == 2) {
            this.onNodeWithTag("numpad-${input[0]}").performClick()
            this.onNodeWithTag("numpad-${input[1]}").performClick()
            this.onNodeWithTag("numpad-enter").performClick()
            i++
        } else {
            error("지원하지 않는 입력: $input")
        }
        // 정답 feedback 노출 전까지는 오답 피드백 존재하면 안 됨
        if (i < inputs.lastIndex)
            this.onNodeWithTag("feedback").assertDoesNotExist()
    }
    // 마지막 입력 후 "정답입니다!" 피드백 노출되어야 함
    this.onNodeWithTag("feedback").assertIsDisplayed()
    this.onNodeWithTag("feedback").assertTextContains("정답입니다!")
}
