package com.shinjaehun.suksuk.multiplication

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import com.shinjaehun.suksuk.TestFactoryBuilders
import com.shinjaehun.suksuk.domain.multiplication.evaluator.MulPhaseEvaluator
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.multiplication.sequence.creator.ThreeByTwoMulPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.multiplication.sequence.creator.TwoByTwoMulPhaseSequenceCreator
import com.shinjaehun.suksuk.presentation.multiplication.MultiplicationScreen
import com.shinjaehun.suksuk.presentation.multiplication.MultiplicationViewModel

fun ComposeContentTestRule.multiplicationCase(
    multiplicand: Int,
    multiplier: Int,
    inputs: List<String>
) {
    val savedStateHandle = SavedStateHandle(mapOf("autoStart" to false))

    val factory = TestFactoryBuilders.unifiedFactoryForMultiplication()

    val viewModel = MultiplicationViewModel(
        savedStateHandle = savedStateHandle,
        phaseEvaluator = MulPhaseEvaluator(),
        domainStateFactory = factory
    )

    setContent {
        // 화면 컴포저블 시그니처에 맞춰 전달 (multiplicand/multiplier 주입)
        MultiplicationScreen(multiplicand = multiplicand, multiplier = multiplier, viewModel = viewModel)
    }

    var i = 0
    while (i < inputs.size) {
        val input = inputs[i]
        when (input.length) {
            1 -> {
                onNodeWithTag("numpad-${input}").performClick()
                onNodeWithTag("numpad-enter").performClick()
            }
            2 -> {
                onNodeWithTag("numpad-${input[0]}").performClick()
                onNodeWithTag("numpad-${input[1]}").performClick()
                onNodeWithTag("numpad-enter").performClick()
            }
            else -> error("지원하지 않는 입력: $input")
        }

        waitForIdle()

        // 마지막 스텝 전까지는 오답 피드백이 없어야 함
        if (i < inputs.lastIndex) {
            onNodeWithTag("feedback").assertDoesNotExist()
        }
        i++
    }

    // 마지막 입력 후 정답 피드백 확인
    onNodeWithTag("feedback").assertIsDisplayed()
    onNodeWithTag("feedback").assertTextContains("정답입니다!")
}