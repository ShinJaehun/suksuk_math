package com.shinjaehun.suksuk

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.shinjaehun.suksuk.domain.division.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.division.PhaseEvaluatorV2
import com.shinjaehun.suksuk.presentation.division.DivisionScreen
import com.shinjaehun.suksuk.presentation.division.DivisionViewModelV2
import com.shinjaehun.suksuk.presentation.division.FeedbackMessageProviderV2

fun ComposeContentTestRule.divisionCaseV2(
    dividend: Int,
    divisor: Int,
    inputs: List<String>
) {
    val phaseSequenceProvider = DivisionPhaseSequenceProvider()
    val phaseEvaluator = PhaseEvaluatorV2()
    val feedbackMessageProvider = FeedbackMessageProviderV2()

    val viewModel = DivisionViewModelV2(
        phaseSequenceProvider = phaseSequenceProvider,
        phaseEvaluator = phaseEvaluator,
        feedbackProvider = feedbackMessageProvider
    )

    this.setContent {
        // 실제로 사용하는 DivisionScreen V2 버전 연결
        DivisionScreen(viewModel = viewModel)
    }

    this.runOnIdle {
        viewModel.startNewProblem(dividend, divisor)
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
