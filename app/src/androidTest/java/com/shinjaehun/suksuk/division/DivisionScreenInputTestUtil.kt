package com.shinjaehun.suksuk.division

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import com.shinjaehun.suksuk.TestFactoryBuilders
import com.shinjaehun.suksuk.domain.division.evaluator.DivisionPhaseEvaluator
import com.shinjaehun.suksuk.presentation.division.DivisionScreen
import com.shinjaehun.suksuk.presentation.division.DivisionViewModel

fun ComposeContentTestRule.divisionCase(
    dividend: Int,
    divisor: Int,
    inputs: List<String>
) {

    val savedStateHandle = SavedStateHandle(mapOf("autoStart" to false))
    val factory = TestFactoryBuilders.unifiedFactoryForDivision()

    val viewModel = DivisionViewModel(
        savedStateHandle = savedStateHandle,
        phaseEvaluator = DivisionPhaseEvaluator(),
        domainStateFactory = factory
    )

    setContent {
        DivisionScreen(dividend, divisor, viewModel)
    }

    var i = 0
    while (i < inputs.size) {
        val input = inputs[i]
        if (input.length == 1) {
            this.onNodeWithTag("numpad-$input").performClick()
            this.onNodeWithTag("numpad-enter").performClick()
        } else if (input.length == 2) {
            this.onNodeWithTag("numpad-${input[0]}").performClick()
            this.onNodeWithTag("numpad-${input[1]}").performClick()
            this.onNodeWithTag("numpad-enter").performClick()
        } else {
            error("지원하지 않는 입력: $input")
        }

        waitForIdle()

        // 정답 feedback 노출 전까지는 오답 피드백 존재하면 안 됨
        if (i < inputs.lastIndex)
            this.onNodeWithTag("feedback").assertDoesNotExist()
        i++

    }

    // 마지막 입력 후 "정답입니다!" 피드백 노출되어야 함
    this.onNodeWithTag("feedback").assertIsDisplayed()
    this.onNodeWithTag("feedback").assertTextContains("정답입니다!")

}
