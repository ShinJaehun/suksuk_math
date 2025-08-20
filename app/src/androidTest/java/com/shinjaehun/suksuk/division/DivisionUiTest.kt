package com.shinjaehun.suksuk.division

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
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
import com.shinjaehun.suksuk.presentation.division.DivisionScreen
import com.shinjaehun.suksuk.presentation.division.DivisionViewModel
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DivisionUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: DivisionViewModel

    @Before
    fun setup() {
        val savedStateHandle = SavedStateHandle(mapOf("autoStart" to false))
        val factory = TestFactoryBuilders.unifiedFactoryForDivision()

        viewModel = DivisionViewModel(
            savedStateHandle = savedStateHandle,
            phaseEvaluator = DivisionPhaseEvaluator(),
            domainStateFactory = factory,
            feedbackProvider = DummyFeedbackProvider
        )
    }

    // ---- 공통 헬퍼 ----
    private fun setDivision(dividend: Int, divisor: Int) {
        viewModel.startNewProblem(dividend, divisor)
        composeTestRule.setContent {
            CompositionLocalProvider(
                LocalAudioPlayer provides NoopAudioPlayer,
                LocalHapticFeedback provides NoopHaptic
            ) {
                DivisionScreen(
                    viewModel = viewModel,
                    onNextProblem = {},
                    onExit = {}
                )
            }
        }
        // LaunchedEffect가 startNewProblem을 부르도록 잠깐 대기
        composeTestRule.waitForIdle()
    }

    private fun tapAndEnter(input: String) {
        composeTestRule.onNodeWithTag("numpad-$input").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
    }

    @Test
    fun test_TwoByOne_Multiply1TensPhase_ShowsSingleQuestionMark() {
        setDivision(85, 7)

        tapAndEnter("1")

        composeTestRule.onNodeWithTag("Multiply1Tens-cell").assertTextEquals("?")
        composeTestRule.onNodeWithTag("Multiply1Ones-cell").assertIsNotDisplayed()
    }

    @Test
    fun test_TwoByTwo_Multiply1TensPhase_ShowsSingleQuestionMark() {
        setDivision(68, 34)

        tapAndEnter("2")

        composeTestRule.onNodeWithTag("Multiply1Ones-cell").assertTextEquals("?")
        composeTestRule.onNodeWithTag("Multiply1Tens-cell").assertIsNotDisplayed()
    }

    @Test
    fun test_TwoByTwo_Subtract1OnesPhase_ShowsSingleQuestionMark() {
        setDivision(49, 24)

        tapAndEnter("2")
        tapAndEnter("8")
        tapAndEnter("4")

        composeTestRule.onNodeWithTag("Subtract1Ones-cell").assertTextEquals("?")
        composeTestRule.onNodeWithTag("Subtract1Tens-cell").assertIsNotDisplayed()
    }

    @Test
    fun test_TwoByOne_DividendTensCellCrossedOut() {
        setDivision(62, 7)

        tapAndEnter("8")
        // 두 자리 입력은 연속 탭 후 엔터
        composeTestRule.onNodeWithTag("numpad-5").performClick()
        composeTestRule.onNodeWithTag("numpad-6").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("DividendTens-crossed").assertExists()
    }

    @Test
    fun test_TwoByTwo_Borrowed10ShownOnDividendTens() {
        setDivision(81, 23)

        tapAndEnter("3") // quotient
        tapAndEnter("9") // mul1 ones
        tapAndEnter("6") // mul1 tens
        tapAndEnter("7") // borrow

        composeTestRule.onNodeWithTag("borrowed10-dividend-cell").assertTextEquals("10")
    }

    @Test
    fun test_TwoByTwo_SubtractionLineAppearsOnlyOnSubtract1Phase() {
        setDivision(68, 34)

        composeTestRule.onNodeWithTag("subtraction1-line").assertDoesNotExist()

        // 직접 submitInput도 가능
        composeTestRule.runOnIdle { viewModel.submitInput("2") }
        composeTestRule.waitForIdle()
        composeTestRule.runOnIdle { viewModel.submitInput("8") }
        composeTestRule.waitForIdle()
        composeTestRule.runOnIdle { viewModel.submitInput("6") }
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("subtraction1-line").assertExists()
    }

    @Test
    fun test_TwoByOne_Multiply2TensAndMultiply2OnesPhase_WrongInput_ShowsQuestionMarks() {
        setDivision(90, 7)

        listOf("1","7","2","0","2").forEach { tapAndEnter(it) }

        // 오답 13 입력(두 자리 → 연속 탭 후 엔터)
        composeTestRule.onNodeWithTag("numpad-1").performClick()
        composeTestRule.onNodeWithTag("numpad-3").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("Multiply2Tens-cell").assertTextEquals("?")
        composeTestRule.onNodeWithTag("Multiply2Ones-cell").assertTextEquals("?")
//        composeTestRule.onNodeWithTag("feedback").assertTextContains("다시 시도해 보세요")
    }
}