package com.shinjaehun.suksuk.division.legacy

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import com.shinjaehun.suksuk.domain.division.legacy.detector.PatternDetector
import com.shinjaehun.suksuk.domain.division.legacy.evaluator.PhaseEvaluator
import com.shinjaehun.suksuk.domain.division.legacy.factory.DivisionDomainStateFactory
import com.shinjaehun.suksuk.domain.division.legacy.layout.DivisionPatternUiLayoutRegistry
import com.shinjaehun.suksuk.domain.division.legacy.model.DivisionPattern
import com.shinjaehun.suksuk.presentation.division.legacy.DivisionScreen
import com.shinjaehun.suksuk.presentation.division.legacy.DivisionViewModel
import com.shinjaehun.suksuk.presentation.division.legacy.FeedbackMessageProvider

fun ComposeContentTestRule.divisionCase(
    pattern: DivisionPattern,
    dividend: Int,
    divisor: Int,
    inputs: List<String>,
) {
    val savedStateHandle = SavedStateHandle(mapOf("autoStart" to false))

    val phaseEvaluator = PhaseEvaluator()
    val patternDetector = PatternDetector
    val uiLayoutRegistry = DivisionPatternUiLayoutRegistry
    val feedbackProvider = FeedbackMessageProvider()

    val domainStateFactory = DivisionDomainStateFactory(
        uiLayoutRegistry,
        patternDetector
    )

    val viewModel = DivisionViewModel(
        savedStateHandle,
        phaseEvaluator,
        domainStateFactory,
        feedbackProvider
    )

    this.setContent {
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

        if (i < inputs.lastIndex)
            this.onNodeWithTag("feedback").assertDoesNotExist()
    }
    this.onNodeWithTag("feedback").assertIsDisplayed()
    this.onNodeWithTag("feedback").assertTextContains("정답입니다!")
}
