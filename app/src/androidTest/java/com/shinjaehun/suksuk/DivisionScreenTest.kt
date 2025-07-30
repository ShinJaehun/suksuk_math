package com.shinjaehun.suksuk

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.shinjaehun.suksuk.presentation.division.DivisionPattern
import com.shinjaehun.suksuk.presentation.division.DivisionPhase
import com.shinjaehun.suksuk.presentation.division.DivisionScreen
import com.shinjaehun.suksuk.presentation.division.DivisionViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test


class DivisionScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testUICases() {
        val viewModel = DivisionViewModel()
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }

        // 문제를 세팅 (예: 93 ÷ 8)
        composeTestRule.runOnIdle {
            viewModel.startNewProblem(93, 8)
        }

        val scenario = listOf(
            "1", "8", "1", "3", "1", "8", "5"
        )

        for (i in scenario.indices) {
            val input = scenario[i]

            // 1. 해당 숫자 버튼 클릭
            composeTestRule.onNodeWithTag("numpad-$input").performClick()
            // 2. ↵(엔터) 버튼 클릭
            composeTestRule.onNodeWithTag("numpad-enter").performClick()

            // 3. (Optional) 피드백 체크 (정답은 보통 마지막 입력 후)
            if (i < scenario.lastIndex) {
                composeTestRule.onNodeWithTag("feedback").assertDoesNotExist()
            }
        }

        // 마지막 입력 후 "정답" 메시지 보임
        composeTestRule.onNodeWithTag("feedback").assertIsDisplayed()
        composeTestRule.onNodeWithTag("feedback").assertTextContains("정답입니다!")
    }

    @Test
    fun testBorrowInputShows10AboveOnes_UITest() {
        val viewModel = DivisionViewModel()
        composeTestRule.setContent { DivisionScreen(viewModel = viewModel) }
        composeTestRule.runOnIdle { viewModel.startNewProblem(50, 3) }

        // 받아내림 입력 전까지 시나리오 입력
        val scenario = listOf("1", "3", "2", "0", "6", "1", "8", "1")
        for (i in scenario.indices) {
            composeTestRule.onNodeWithTag("numpad-${scenario[i]}").performClick()
            composeTestRule.onNodeWithTag("numpad-enter").performClick()
        }

        // 받아내림 입력 후 '10'이 나타나는지 체크
        composeTestRule.onNodeWithTag("borrowed-sub1-cell").assertTextEquals("10")
        // 혹은 '10'을 포함하는지 체크
        // composeTestRule.onNodeWithTag("borrow-cell").assertTextContains("10")
    }


    @Test
    fun testSubtractionLineAppearsOnSubtractPhase() {
        val viewModel = DivisionViewModel().apply {
            startNewProblem(85, 7) // Pattern A: 1차 뺄셈 있음
        }

        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }

        // Phase: InputSubtract1Tens까지 진행
        viewModel.submitInput("1") // quotientTens
        viewModel.submitInput("7") // multiply1
        viewModel.submitInput("1") // subtract1Tens 진입 시점

        composeTestRule.waitForIdle()

        // horizontal_line이 나타나야 함
        composeTestRule
            .onNodeWithTag("subtraction-line")
            .assertExists()
    }

    @Test
    fun testSubtractionLineDoesNotAppearBeforeSubtractPhase() {
        val viewModel = DivisionViewModel().apply {
            startNewProblem(85, 7)
        }

        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }

        // 아직 quotient 입력 중
        viewModel.submitInput("1") // quotientTens

        composeTestRule.waitForIdle()

        // 이때는 horizontal_line이 없어야 함
        composeTestRule
            .onNodeWithTag("subtraction-line")
            .assertDoesNotExist()
    }

    @Test
    fun testPattern_TensQuotient_NoBorrow_2DigitMultiply_Multiply2Total() {
        val viewModel = DivisionViewModel()
        viewModel.startNewProblem(90, 7)

        val inputs = listOf(
            "1",    // InputQuotientTens
            "7",    // InputMultiply1 (1 × 7)
            "2",    // InputSubtract1Tens (9 - 7 = 2)
            "0",    // InputBringDownFromDividendOnes → now 20
            "2",    // InputQuotientOnes
            "14",   // ✅ InputMultiply2Total → 핵심
            "1",
            "6"     // InputSubtract2Result (20 - 14 = 6)
        )

        inputs.forEach { viewModel.submitInput(it) }

        // 최종 상태 검사
        val finalState = viewModel.uiState.value
        assertEquals(DivisionPhase.Complete, finalState.phases.getOrNull(finalState.currentPhaseIndex))
    }

}