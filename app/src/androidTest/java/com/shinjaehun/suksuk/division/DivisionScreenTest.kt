package com.shinjaehun.suksuk.division

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import com.shinjaehun.suksuk.domain.division.detector.PatternDetector
import com.shinjaehun.suksuk.domain.division.evaluator.PhaseEvaluator
import com.shinjaehun.suksuk.domain.division.factory.DivisionDomainStateFactory
import com.shinjaehun.suksuk.domain.division.layout.DivisionPatternUiLayoutRegistry
import com.shinjaehun.suksuk.domain.division.model.DivisionPhase
import com.shinjaehun.suksuk.presentation.division.DivisionScreen
import com.shinjaehun.suksuk.presentation.division.DivisionViewModel
import com.shinjaehun.suksuk.presentation.division.FeedbackMessageProvider
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DivisionScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: DivisionViewModel

    @Before
    fun setup() {
        val savedStateHandle = SavedStateHandle(mapOf("autoStart" to false))

        val phaseEvaluator = PhaseEvaluator()
        val patternDetector = PatternDetector
        val uiLayoutRegistry = DivisionPatternUiLayoutRegistry
        val feedbackProvider = FeedbackMessageProvider()

        val domainStateFactory = DivisionDomainStateFactory(
            uiLayoutRegistry,
            patternDetector
        )

        viewModel = DivisionViewModel(
            savedStateHandle,
            phaseEvaluator,
            domainStateFactory,
            feedbackProvider
        )
    }

    @Test
    fun test_TwoByOne_UIFlow() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }

        composeTestRule.runOnIdle {
            viewModel.startNewProblem(93, 8)
        }

        val scenario = listOf(
            "1", "8", "1", "3", "1", "8", "5"
        )

        for (i in scenario.indices) {
            val input = scenario[i]

            composeTestRule.onNodeWithTag("numpad-$input").performClick()
            composeTestRule.onNodeWithTag("numpad-enter").performClick()

            if (i < scenario.lastIndex) {
                composeTestRule.onNodeWithTag("feedback").assertDoesNotExist()
            }
        }

        composeTestRule.onNodeWithTag("feedback").assertIsDisplayed()
        composeTestRule.onNodeWithTag("feedback").assertTextContains("정답입니다!")
    }

    @Test
    fun test_TwoByTwo_UIFlow() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(57, 22) }
        val scenario = listOf("2", "4", "4", "3", "1")
        for (input in scenario.dropLast(1)) {
            composeTestRule.onNodeWithTag("numpad-$input").performClick()
            composeTestRule.onNodeWithTag("numpad-enter").performClick()
            composeTestRule.onNodeWithTag("feedback").assertDoesNotExist()
        }
        composeTestRule.onNodeWithTag("numpad-${scenario.last()}").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("feedback").assertIsDisplayed()
        composeTestRule.onNodeWithTag("feedback").assertTextContains("정답입니다!")
    }


    @Test
    fun test_TwoByOne_Multiply1TensPhase_ShowsSingleQuestionMark() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(85, 7) }

        composeTestRule.onNodeWithTag("numpad-1").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("Multiply1Tens-cell").assertTextEquals("?")
        composeTestRule.onNodeWithTag("Multiply1Ones-cell").assertDoesNotExist()
    }

    @Test
    fun test_TwoByTwo_Multiply1TensPhase_ShowsSingleQuestionMark() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(68, 34) }

        composeTestRule.onNodeWithTag("numpad-2").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("Multiply1Ones-cell").assertTextEquals("?")
        composeTestRule.onNodeWithTag("Multiply1Tens-cell").assertDoesNotExist()
    }


    @Test
    fun test_TwoByOne_Multiply1TensAndMultiply1OnesPhase_ShowsTwoQuestionMarks() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(62, 7) }

        composeTestRule.onNodeWithTag("numpad-8").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("Multiply1Tens-cell").assertTextEquals("?")
        composeTestRule.onNodeWithTag("Multiply1Ones-cell").assertTextEquals("?")
    }

    @Test
    fun test_TwoByTwo_Multiply1OnesWithCarry_ShowsTwoQuestionMarks() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(75, 25) }
        composeTestRule.onNodeWithTag("numpad-3").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("Multiply1Ones-cell").assertTextEquals("?")
        composeTestRule.onNodeWithTag("CarryDivisorTens-cell").assertTextEquals("?")
    }

    @Test
    fun test_TwoByOne_Multiply2OnesPhase_ShowsSingleQuestionMark() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(93, 8) }

        composeTestRule.onNodeWithTag("numpad-1").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-8").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-1").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-3").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-1").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("Multiply2Ones-cell").assertTextEquals("?")
        composeTestRule.onNodeWithTag("Multiply2Tens-cell").assertDoesNotExist()
    }

    @Test
    fun test_TwoByTwo_Subtract1OnesPhase_ShowsSingleQuestionMark() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(49, 24) }

        composeTestRule.onNodeWithTag("numpad-2").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-8").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-4").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("Subtract1Ones-cell").assertTextEquals("?")
        composeTestRule.onNodeWithTag("Subtract1Tens-cell").assertDoesNotExist()
    }

    @Test
    fun test_TwoByOne_Multiply2TensAndMultiply2OnesPhase_ShowsTwoQuestionMarks() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(90, 7) }

        composeTestRule.onNodeWithTag("numpad-1").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("numpad-7").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("numpad-2").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("numpad-0").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("numpad-2").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("Multiply2Tens-cell").assertTextEquals("?")
        composeTestRule.onNodeWithTag("Multiply2Ones-cell").assertTextEquals("?")

    }

    @Test
    fun test_TwoByOne_Multiply2TensAndMultiply2OnesPhase_WrongInput_ShowsQuestionMarks() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(90, 7) }

        val scenario = listOf("1", "7", "2", "0", "2")
        for (input in scenario) {
            composeTestRule.onNodeWithTag("numpad-$input").performClick()
            composeTestRule.onNodeWithTag("numpad-enter").performClick()
        }

        composeTestRule.onNodeWithTag("numpad-1").performClick()
        composeTestRule.onNodeWithTag("numpad-3").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("Multiply2Tens-cell").assertTextEquals("?")
        composeTestRule.onNodeWithTag("Multiply2Ones-cell").assertTextEquals("?")

        composeTestRule.onNodeWithTag("feedback").assertTextContains("다시 시도해 보세요")
    }

    @Test
    fun test_TwoByTwo_Subtract1TensPhase_e_WrongInput_ShowsQuestionMarks() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(95, 28) }

        composeTestRule.onNodeWithTag("numpad-3").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("numpad-2").performClick()
        composeTestRule.onNodeWithTag("numpad-4").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("numpad-8").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("numpad-1").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()


        composeTestRule.onNodeWithTag("numpad-5").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("Subtract1Tens-cell").assertTextEquals("?")
        composeTestRule.onNodeWithTag("feedback").assertTextContains("다시 시도해 보세요")
    }


    @Test
    fun test_TwoByOne_DividendTensCellCrossedOut() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(62, 7) }

        composeTestRule.onNodeWithTag("numpad-8").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-5").performClick()
        composeTestRule.onNodeWithTag("numpad-6").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("DividendTens-crossed").assertExists()
    }

    @Test
    fun test_TwoByTwo_DividendTensCellCrossedOut() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(81, 23) }
        composeTestRule.onNodeWithTag("numpad-3").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-9").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-6").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-7").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("DividendTens-crossed").assertExists()
    }


    @Test
    fun test_TwoByOne_Borrowed10ShownOnDividendTens() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(62, 7) }

        composeTestRule.onNodeWithTag("numpad-8").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-5").performClick()
        composeTestRule.onNodeWithTag("numpad-6").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-5").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("borrowed10-dividend-cell").assertTextEquals("10")
    }

    @Test
    fun test_TwoByTwo_Borrowed10ShownOnDividendTens() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(81, 23) }
        // (몫, 곱셈1, 곱셈10, borrow)
        composeTestRule.onNodeWithTag("numpad-3").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-9").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-6").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-7").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        // DividendTens 위에 '10'
        composeTestRule.onNodeWithTag("borrowed10-dividend-cell").assertTextEquals("10")
    }


    @Test
    fun test_TwoByOne_Subtract1TensCellCrossedOut() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(50, 3) }

        composeTestRule.onNodeWithTag("numpad-1").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-3").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-2").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-0").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-6").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-1").performClick()
        composeTestRule.onNodeWithTag("numpad-8").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("Subtract1Tens-crossed").assertExists()
    }

    @Test
    fun test_TwoByOne_Borrowed10ShownOnSubtract1Ones() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(50, 3) }

        // 받아내림 입력 전까지 시나리오 입력
//        val scenario = listOf("1", "3", "2", "0", "6", "1", "8", "1")
//        for (i in scenario.indices) {
//            composeTestRule.onNodeWithTag("numpad-${scenario[i]}").performClick()
//            composeTestRule.onNodeWithTag("numpad-enter").performClick()
//        }

        val steps = listOf(
            "1" to true,
            "3" to true,
            "2" to true,
            "0" to true,
            "6" to true,
            "1" to false,  // 두 자리 입력 시작 ("1" → "8" → 엔터)
            "8" to true,
            "1" to true
        )
        for ((num, shouldEnter) in steps) {
            composeTestRule.onNodeWithTag("numpad-$num").performClick()
            if (shouldEnter) composeTestRule.onNodeWithTag("numpad-enter").performClick()
        }

        composeTestRule.onNodeWithTag("borrowed10-sub1-cell").assertTextEquals("10")
    }

    @Test
    fun test_TwoByOne_SubtractionLineAppearsOnlyOnSubtract1Phase() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(85, 7) }

        composeTestRule.onNodeWithTag("subtraction1-line").assertDoesNotExist()

        viewModel.submitInput("1")
        composeTestRule.waitForIdle()

        viewModel.submitInput("7")
        composeTestRule.waitForIdle()

        viewModel.submitInput("1")
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("subtraction1-line").assertExists()
    }

    @Test
    fun test_TwoByTwo_SubtractionLineAppearsOnlyOnSubtract1Phase() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(68, 34) }

        composeTestRule.onNodeWithTag("subtraction1-line").assertDoesNotExist()

        viewModel.submitInput("2")
        composeTestRule.waitForIdle()
        viewModel.submitInput("8")
        composeTestRule.waitForIdle()
        viewModel.submitInput("6")
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("subtraction1-line").assertExists()
    }


    @Test
    fun test_TwoByOne_SubtractionLineAppearsOnlyOnSubtract2Phase() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(85, 7) }

        composeTestRule.onNodeWithTag("subtraction2-line").assertDoesNotExist()

        viewModel.submitInput("1")
        composeTestRule.waitForIdle()
        viewModel.submitInput("7")
        composeTestRule.waitForIdle()
        viewModel.submitInput("1")
        composeTestRule.waitForIdle()
        viewModel.submitInput("5")
        composeTestRule.waitForIdle()
        viewModel.submitInput("2")
        composeTestRule.waitForIdle()
        viewModel.submitInput("14")
        composeTestRule.waitForIdle()
        viewModel.submitInput("1")
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("subtraction2-line").assertExists()
    }

    @Test
    fun test_UserInput_Pattern_TwoByOne_TensQuotientNoBorrow2DigitMul() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(90, 7) }

        val inputs = listOf(
            "1",
            "7",
            "2",
            "0",
            "2",
            "14",
            "1",
            "6"
        )

        inputs.forEach { viewModel.submitInput(it) }

        val finalState = viewModel.domainState.value
        assertEquals(DivisionPhase.Complete, finalState.phases.getOrNull(finalState.currentPhaseIndex))
    }

    @Test
    fun test_TwoByOne_TensQuotientBorrow2DigitMul_EachPhase_PlaceholderAndInputValue() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(90, 7) }

//        val text = composeTestRule.onNodeWithTag("quotient-tens-cell").fetchSemanticsNode().config.getOrNull(
//            SemanticsProperties.Text)
//        println("▶️ 실제 텍스트: $text")

        composeTestRule.onNodeWithTag("QuotientTens-cell").assertTextContains("?")
        composeTestRule.runOnIdle { viewModel.submitInput("1") }
        composeTestRule.onNodeWithTag("QuotientTens-cell").assertTextContains("1")

        composeTestRule.onNodeWithTag("Multiply1Tens-cell").assertTextContains("?")
        composeTestRule.runOnIdle { viewModel.submitInput("7") }
        composeTestRule.onNodeWithTag("Multiply1Tens-cell").assertTextContains("7")

        composeTestRule.onNodeWithTag("Subtract1Tens-cell").assertTextContains("?")
        composeTestRule.runOnIdle { viewModel.submitInput("2") }
        composeTestRule.onNodeWithTag("Subtract1Tens-cell").assertTextContains("2")

        composeTestRule.onNodeWithTag("Subtract1Ones-cell").assertTextContains("?")
        composeTestRule.runOnIdle { viewModel.submitInput("0") }
        composeTestRule.onNodeWithTag("Subtract1Ones-cell").assertTextContains("0")

        composeTestRule.onNodeWithTag("QuotientOnes-cell").assertTextContains("?")
        composeTestRule.runOnIdle { viewModel.submitInput("2") }
        composeTestRule.onNodeWithTag("QuotientOnes-cell").assertTextContains("2")

        composeTestRule.onNodeWithTag("Multiply2Tens-cell").assertTextContains("?")
        composeTestRule.onNodeWithTag("Multiply2Ones-cell").assertTextContains("?")
        composeTestRule.runOnIdle { viewModel.submitInput("14") }
        composeTestRule.onNodeWithTag("Multiply2Tens-cell").assertTextContains("1")
        composeTestRule.onNodeWithTag("Multiply2Ones-cell").assertTextContains("4")

        composeTestRule.onNodeWithTag("BorrowSubtract1Tens-cell").assertTextContains("?")
        composeTestRule.runOnIdle { viewModel.submitInput("1") }
        composeTestRule.onNodeWithTag("BorrowSubtract1Tens-cell").assertTextContains("1")

        composeTestRule.onNodeWithTag("Subtract2Ones-cell").assertTextContains("?")
        composeTestRule.runOnIdle { viewModel.submitInput("6") }
        composeTestRule.onNodeWithTag("Subtract2Ones-cell").assertTextContains("6")

        val finalState = viewModel.domainState.value
        assertEquals(DivisionPhase.Complete, finalState.phases.getOrNull(finalState.currentPhaseIndex))
    }

    @Test
    fun test_TwoByTwo_NoCarryNoBorrow2DigitRem_EachPhase_PlaceholderAndInputValue() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(57, 22) }

        composeTestRule.onNodeWithTag("QuotientOnes-cell").assertTextContains("?")
        composeTestRule.runOnIdle { viewModel.submitInput("2") }
        composeTestRule.onNodeWithTag("QuotientOnes-cell").assertTextContains("2")

        composeTestRule.onNodeWithTag("Multiply1Ones-cell").assertTextContains("?")
        composeTestRule.runOnIdle { viewModel.submitInput("4") }
        composeTestRule.onNodeWithTag("Multiply1Ones-cell").assertTextContains("4")

        composeTestRule.onNodeWithTag("Multiply1Tens-cell").assertTextContains("?")
        composeTestRule.runOnIdle { viewModel.submitInput("4") }
        composeTestRule.onNodeWithTag("Multiply1Tens-cell").assertTextContains("4")

        composeTestRule.onNodeWithTag("Subtract1Ones-cell").assertTextContains("?")
        composeTestRule.runOnIdle { viewModel.submitInput("3") }
        composeTestRule.onNodeWithTag("Subtract1Ones-cell").assertTextContains("3")

        composeTestRule.onNodeWithTag("Subtract1Tens-cell").assertTextContains("?")
        composeTestRule.runOnIdle { viewModel.submitInput("1") }
        composeTestRule.onNodeWithTag("Subtract1Tens-cell").assertTextContains("1")

        composeTestRule.onNodeWithTag("feedback").assertIsDisplayed()
        composeTestRule.onNodeWithTag("feedback").assertTextContains("정답입니다!")
    }


}