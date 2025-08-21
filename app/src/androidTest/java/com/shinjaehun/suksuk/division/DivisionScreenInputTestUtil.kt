package com.shinjaehun.suksuk.division

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import com.shinjaehun.suksuk.DummyFeedbackProvider
import com.shinjaehun.suksuk.NoopAudioPlayer
import com.shinjaehun.suksuk.NoopHaptic
import com.shinjaehun.suksuk.TestFactoryBuilders
import com.shinjaehun.suksuk.domain.division.evaluator.DivisionPhaseEvaluator
import com.shinjaehun.suksuk.presentation.common.effects.LocalAudioPlayer
import com.shinjaehun.suksuk.presentation.common.feedback.FeedbackProviderImpl
import com.shinjaehun.suksuk.presentation.division.DivisionScreen
import com.shinjaehun.suksuk.presentation.division.DivisionViewModel

fun ComposeContentTestRule.divisionCase(
    dividend: Int,
    divisor: Int,
    inputs: List<String>
) {
    val savedStateHandle = SavedStateHandle(mapOf("autoStart" to false))
    val factory = TestFactoryBuilders.unifiedFactoryForDivision()
    val feedbackProvider = FeedbackProviderImpl()

    val viewModel = DivisionViewModel(
        savedStateHandle = savedStateHandle,
        phaseEvaluator = DivisionPhaseEvaluator(),
        domainStateFactory = factory,
        feedbackProvider = feedbackProvider
    )

    viewModel.startNewProblem(dividend, divisor)
    setContent {
        CompositionLocalProvider(
            LocalAudioPlayer provides NoopAudioPlayer,
            LocalHapticFeedback provides NoopHaptic
        ) {
            DivisionScreen(
                viewModel,
                onNextProblem = {},
                onExit = {},
            )
        }
    }

    inputs.forEachIndexed { idx, input ->
        when (input.length) {
            1 -> {
                onNodeWithTag("numpad-$input").performClick()
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

        // 중간엔 완료 오버레이가 아직 없어야 함
        if (idx < inputs.lastIndex) {
            onAllNodes(
                hasContentDescription("참 잘했어요"),
                useUnmergedTree = true
            ).assertCountEquals(0)
        }

    }
    // 마지막 입력 후: 완료 오버레이(이미지)가 붙을 때까지 기다림
    waitUntil(timeoutMillis = 3_000) {
        onAllNodes(
            hasContentDescription("참 잘했어요"),
            useUnmergedTree = true
        ).fetchSemanticsNodes().isNotEmpty()
    }

    // 실제로 보이는지 단언
    onNode(
        hasContentDescription("참 잘했어요"),
        useUnmergedTree = true
    ).assertIsDisplayed()
}
