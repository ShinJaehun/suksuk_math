package com.shinjaehun.suksuk.multiplication

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
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
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class MultiplicationUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: MultiplicationViewModel

    @Before
    fun setup() {
        val savedStateHandle = SavedStateHandle(mapOf("autoStart" to false))
        val factory = TestFactoryBuilders.unifiedFactoryForMultiplication()

        viewModel = MultiplicationViewModel(
            savedStateHandle = savedStateHandle,
            phaseEvaluator = MulPhaseEvaluator(),
            domainStateFactory = factory
        )
    }

    // ---- 공통 헬퍼 ----
    private fun setMultiplication(multiplicand: Int, multiplier: Int) {
        composeTestRule.setContent {
            MultiplicationScreen(
                multiplicand = multiplicand,
                multiplier = multiplier,
                viewModel = viewModel
            )
        }
        composeTestRule.waitForIdle()
    }

    // ---------------------------------------
    // 1) 전체 플로우: 2×1 (예: 47 × 6)
    // ---------------------------------------
    @Test
    fun test_TwoByOne_FullFlow_ShowsSuccessFeedback() {
        setMultiplication(47, 16)

        composeTestRule.onNodeWithTag("numpad-4").performClick()
        composeTestRule.onNodeWithTag("numpad-2").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("numpad-2").performClick()
        composeTestRule.onNodeWithTag("numpad-8").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("numpad-7").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("numpad-4").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("numpad-2").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("numpad-1").performClick()
        composeTestRule.onNodeWithTag("numpad-5").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("numpad-7").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("feedback").assertIsDisplayed()
        composeTestRule.onNodeWithTag("feedback").assertTextContains("정답입니다!")

        composeTestRule.runOnIdle {
            val ui = requireNotNull(viewModel.uiState.value)
            assertTrue("Complete phase 미도달", ui.isCompleted)
        }
    }

    // ---------------------------------------
    // 2) 부분곱 1단계: 일의 자리 입력 후
    // ---------------------------------------
    @Test
    fun test_TwoByTwo_M1OnesPhase_ShowsSingleQuestionMark() {
        setMultiplication(68, 34)

        // 첫 스텝: 승수 일의 자리(=4)로 곱셈 일의 결과를 맞게 입력하는 상황
        composeTestRule.onNodeWithTag("numpad-3").performClick()
        composeTestRule.onNodeWithTag("numpad-2").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("P1Ones-cell").assertTextEquals("2")
        composeTestRule.onNodeWithTag("P1Tens-cell").assertTextEquals("?")
        composeTestRule.onNodeWithTag("P1Hundreds-cell").assertTextEquals("?")
        composeTestRule.onNodeWithTag("P2Tens-cell").assertIsNotDisplayed()
    }

    // ---------------------------------------
    // 3) Carry 표시: M1(일의 자리 곱셈)에서 받아올림이 있으면 carry 셀에 보임
    // ---------------------------------------
    @Test
    fun test_TwoByTwo_M1OnesWithCarry_ShowsCarryCell() {
        setMultiplication(75, 26)

        composeTestRule.onNodeWithTag("numpad-3").performClick()
        composeTestRule.onNodeWithTag("numpad-0").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("CarryP1Tens-cell").assertTextEquals("3")
        composeTestRule.onNodeWithTag("P1Tens-cell").assertTextEquals("?")
    }

    // ---------------------------------------
    // 4) 2행 부분곱(=십의 자리 승수)로 넘어가기 전까지 합산선은 나오지 않음
    // ---------------------------------------
    @Test
    fun test_TwoByTwo_TotalLineAppearsOnlyAtSummationPhase() {
        setMultiplication(34, 26)

        composeTestRule.onNodeWithTag("total-line").assertDoesNotExist()

        // 1행 부분곱 처리(일의 → 받아올림 → 십의 …)
        composeTestRule.onNodeWithTag("numpad-2").performClick()
        composeTestRule.onNodeWithTag("numpad-4").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("numpad-2").performClick()
        composeTestRule.onNodeWithTag("numpad-0").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        // 아직 합산선 없음
        composeTestRule.onNodeWithTag("total-line").assertDoesNotExist()

        // 2행(=십의 자리 승수 2) 부분곱 입력이 끝나고 합산 단계로 들어가면 ‘합산선’ 등장
        composeTestRule.onNodeWithTag("numpad-8").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-6").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("total-line").assertExists()
    }

    // ---------------------------------------
    // 5) 2행 부분곱(십의 자리 곱)
    // ---------------------------------------
    @Test
    fun test_TwoByTwo_M2TensAndOnes_ShowsTwoQuestionMarks() {
        setMultiplication(90, 27)

        // 1행 입력을 한 번에 진행(예시)
        composeTestRule.onNodeWithTag("numpad-0").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-6").performClick()
        composeTestRule.onNodeWithTag("numpad-3").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        // 2행(십의 자리 승수 2)에 진입하면
        composeTestRule.onNodeWithTag("P2Tens-cell").assertTextEquals("?")
        composeTestRule.onNodeWithTag("P2Hundreds-cell").assertIsNotDisplayed()
        composeTestRule.onNodeWithTag("P2Thousands-cell").assertIsNotDisplayed()
    }

    // ---------------------------------------
    // 6) 오답 입력 시: 셀은 물음표 유지 + 피드백(있다면) 노출
    // ---------------------------------------
    @Test
    fun test_TwoByTwo_M2Phase_WrongInput_ShowsQuestionMarks_AndFeedback() {
        setMultiplication(90, 27)

        // 1행 처리
        composeTestRule.onNodeWithTag("numpad-0").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-6").performClick()
        composeTestRule.onNodeWithTag("numpad-3").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        // 2행에서 오답 입력
        composeTestRule.onNodeWithTag("numpad-1").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("P2Tens-cell").assertTextEquals("?")
    }

    // ---------------------------------------
    // 7) 보조 숫자(예: carry 텍스트)
    // ---------------------------------------
    @Test
    fun test_TwoByTwo_M1Phase_CarryTextVisible() {
        setMultiplication(67, 48)

        // 1행에서 carry가 생기도록 입력(예시 값)
        composeTestRule.onNodeWithTag("numpad-5").performClick()
        composeTestRule.onNodeWithTag("numpad-6").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("CarryP1Tens-cell").assertTextEquals("5")
    }

    @Test
    fun test_TwoByTwo_M2Phase_CarryTextVisible() {
        setMultiplication(67, 48)

        // 1행에서 carry가 생기도록 입력(예시 값)
        composeTestRule.onNodeWithTag("numpad-5").performClick()
        composeTestRule.onNodeWithTag("numpad-6").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("numpad-5").performClick()
        composeTestRule.onNodeWithTag("numpad-3").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("numpad-2").performClick()
        composeTestRule.onNodeWithTag("numpad-8").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("CarryP2Tens-cell").assertTextEquals("2")
    }

    @Test
    fun test_TwoByTwo_SumPhase_CarryTextVisible() {
        setMultiplication(67, 48)

        // 1행에서 carry가 생기도록 입력(예시 값)
        composeTestRule.onNodeWithTag("numpad-5").performClick()
        composeTestRule.onNodeWithTag("numpad-6").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("numpad-5").performClick()
        composeTestRule.onNodeWithTag("numpad-3").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("numpad-2").performClick()
        composeTestRule.onNodeWithTag("numpad-8").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("numpad-2").performClick()
        composeTestRule.onNodeWithTag("numpad-6").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("numpad-6").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("numpad-1").performClick()
        composeTestRule.onNodeWithTag("numpad-1").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        composeTestRule.onNodeWithTag("CarrySumHundreds-cell").assertTextEquals("1")
    }
}