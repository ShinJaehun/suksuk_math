package com.shinjaehun.suksuk.multiplication

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
import com.shinjaehun.suksuk.domain.multiplication.evaluator.MulPhaseEvaluator
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.multiplication.sequence.creator.ThreeByTwoMulPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.multiplication.sequence.creator.TwoByTwoMulPhaseSequenceCreator
import com.shinjaehun.suksuk.presentation.common.effects.LocalAudioPlayer
import com.shinjaehun.suksuk.presentation.common.feedback.FeedbackProviderImpl
import com.shinjaehun.suksuk.presentation.multiplication.MultiplicationScreen
import com.shinjaehun.suksuk.presentation.multiplication.MultiplicationViewModel

fun ComposeContentTestRule.multiplicationCase(
    multiplicand: Int,
    multiplier: Int,
    inputs: List<String>
) {
    val savedStateHandle = SavedStateHandle(mapOf("autoStart" to false))
    val factory = TestFactoryBuilders.unifiedFactoryForMultiplication()
    val feedbackProvider = FeedbackProviderImpl()

    val viewModel = MultiplicationViewModel(
        savedStateHandle = savedStateHandle,
        phaseEvaluator = MulPhaseEvaluator(),
        domainStateFactory = factory,
        feedbackProvider = feedbackProvider
    )

    viewModel.startNewProblem(multiplicand, multiplier)
    setContent {
        CompositionLocalProvider(
            LocalAudioPlayer provides NoopAudioPlayer,
            LocalHapticFeedback provides NoopHaptic
        ) {
            // 화면 컴포저블 시그니처에 맞춰 전달
            MultiplicationScreen(
                viewModel = viewModel,
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