package com.shinjaehun.suksuk

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import com.shinjaehun.suksuk.domain.PatternDetector
import com.shinjaehun.suksuk.domain.PhaseEvaluator
import com.shinjaehun.suksuk.presentation.division.DivisionPatternUiLayoutRegistry
import com.shinjaehun.suksuk.presentation.division.DivisionPhase
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
        // 테스트용 SavedStateHandle (autoStart를 false로 세팅)
        val savedStateHandle = SavedStateHandle(mapOf("autoStart" to false))

        // AppModule에서 제공하는 객체는 그냥 new 가능
        val phaseEvaluator = PhaseEvaluator()
        val patternDetector = PatternDetector
        val uiLayoutRegistry = DivisionPatternUiLayoutRegistry
        val feedbackProvider = FeedbackMessageProvider()

        // 직접 생성!
        viewModel = DivisionViewModel(
            savedStateHandle,
            phaseEvaluator,
            patternDetector,
            uiLayoutRegistry,
            feedbackProvider
        )
    }

    @Test
    fun testUICases() {
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
    fun testMultiply1TensPhase_ShowsSingleQuestionMark() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(85, 7) }

        // quotientTens 입력 (1)
        composeTestRule.onNodeWithTag("numpad-1").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        // Multiply1Tens phase 진입, 물음표는 해당 셀에만 떠야 함
        composeTestRule.onNodeWithTag("Multiply1Tens-cell").assertTextEquals("?")
        // 만약 Multiply1Ones-cell에도 placeholder가 있다면 안 보이는 게 정상
//        composeTestRule.onNodeWithTag("Multiply1Ones-cell").assertTextEquals("") // 혹은 .assertDoesNotExist() (실제 UI 구현에 따라)
        composeTestRule.onNodeWithTag("Multiply1Ones-cell").assertDoesNotExist()
    }

    @Test
    fun testMultiply1TotalPhase_ShowsTwoQuestionMarks() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(62, 7) }

        // 몫(8) 입력
        composeTestRule.onNodeWithTag("numpad-8").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        // Multiply1Total phase 진입, 물음표가 두 셀에 각각 떠야 함
        composeTestRule.onNodeWithTag("Multiply1Tens-cell").assertTextEquals("?")
        composeTestRule.onNodeWithTag("Multiply1Ones-cell").assertTextEquals("?")
    }

    @Test
    fun testMultiply2OnesPhase_ShowsSingleQuestionMark() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(93, 8) } // 예시: 두 번째 곱셈이 한 자리인 경우

        // (적절한 시나리오 입력)
        composeTestRule.onNodeWithTag("numpad-1").performClick() // quotientTens
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-8").performClick() // multiply1
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-1").performClick() // subtract1Tens
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-3").performClick() // bring down
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-1").performClick() // quotientOnes
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        // Multiply2Ones phase 진입, 한 셀에만 물음표
        composeTestRule.onNodeWithTag("Multiply2Ones-cell").assertTextEquals("?")
        // Multiply2Tens은 비어야 함 (혹은 존재하지 않음)
        composeTestRule.onNodeWithTag("Multiply2Tens-cell").assertDoesNotExist()
        // 또는 .assertDoesNotExist() (구현에 따라)
    }

    @Test
    fun testMultiply2TotalPhase_ShowsTwoQuestionMarks() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(90, 7) }

        // [1] quotientTens 입력 ("1")
        composeTestRule.onNodeWithTag("numpad-1").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        // [2] multiply1Tens 입력 ("7")
        composeTestRule.onNodeWithTag("numpad-7").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        // [3] subtract1Tens 입력 ("2")
        composeTestRule.onNodeWithTag("numpad-2").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        // [4] bring down 입력 ("0")
        composeTestRule.onNodeWithTag("numpad-0").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        // [5] quotientOnes 입력 ("2")
        composeTestRule.onNodeWithTag("numpad-2").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        // [6] Multiply2Total phase 진입 → 두 셀에 ? 표시
        composeTestRule.onNodeWithTag("Multiply2Tens-cell").assertTextEquals("?")
        composeTestRule.onNodeWithTag("Multiply2Ones-cell").assertTextEquals("?")

    }

    @Test
    fun testMultiply2TotalPhase_WrongInput_ShowsQuestionMarks() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(90, 7) }

        // (중간 과정은 생략, Multiply2Total phase로 진입한 상태)
        val scenario = listOf("1", "7", "2", "0", "2")
        for (input in scenario) {
            composeTestRule.onNodeWithTag("numpad-$input").performClick()
            composeTestRule.onNodeWithTag("numpad-enter").performClick()
        }

        // 잘못된 입력 ("1" → "3" → 엔터, 정답은 "14")
        composeTestRule.onNodeWithTag("numpad-1").performClick()
        composeTestRule.onNodeWithTag("numpad-3").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        // 오답이므로 셀이 계속 "?"인지 체크
        composeTestRule.onNodeWithTag("Multiply2Tens-cell").assertTextEquals("?")
        composeTestRule.onNodeWithTag("Multiply2Ones-cell").assertTextEquals("?")

        // feedback 메시지가 있다면, 아래처럼도 체크 가능
        composeTestRule.onNodeWithTag("feedback").assertTextContains("다시 시도해 보세요")
    }

    @Test
    fun testDividendTensCellCrossedOut() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(62, 7) }

        // 몫, 곱셈, 뺄셈까지 입력 (borrow 상황 진입)
        composeTestRule.onNodeWithTag("numpad-8").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        composeTestRule.onNodeWithTag("numpad-5").performClick()
        composeTestRule.onNodeWithTag("numpad-6").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
//        composeTestRule.onNodeWithTag("numpad-5").performClick()
//        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        // [CHECK] dividendTens 셀이 취소선이 적용된 상태인지 테스트
        // 취소선 셀에 별도 testTag가 있다면 assertExists로 체크
        composeTestRule.onNodeWithTag("DividendTens-crossed").assertExists()
    }

    @Test
    fun testBorrowed10ShownOnDividendTens() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(62, 7) }

        // [1] 몫 입력: 8
        composeTestRule.onNodeWithTag("numpad-8").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        // [2] 곱셈 입력: 5, 6 (56)
        composeTestRule.onNodeWithTag("numpad-5").performClick()
        composeTestRule.onNodeWithTag("numpad-6").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()
        // [3] 뺄셈 입력: 6
        composeTestRule.onNodeWithTag("numpad-5").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        // [4] 이제 받아내림 단계, dividendTens에 '10'이 표시되어야 함
        composeTestRule.onNodeWithTag("borrowed10-dividend-cell").assertTextEquals("10")
    }

    @Test
    fun testSubtract1TensCellCrossedOut() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(50, 3) }

        // 1. 몫 입력 (QuotientTens)
        composeTestRule.onNodeWithTag("numpad-1").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        // 2. 1 * 3 입력 (Multiply1Tens)
        composeTestRule.onNodeWithTag("numpad-3").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        // 3. 첫번째 뺄셈 (Subtract1Tens) → 5 - 3 = 2
        composeTestRule.onNodeWithTag("numpad-2").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        // 4. Bring down (DividendOnes, usually '0')
        composeTestRule.onNodeWithTag("numpad-0").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        // 5. 두 번째 몫 (QuotientOnes, 6)
        composeTestRule.onNodeWithTag("numpad-6").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        // 6. 두 번째 곱셈 (Multiply2Total, 18)
        composeTestRule.onNodeWithTag("numpad-1").performClick()
        composeTestRule.onNodeWithTag("numpad-8").performClick()
        composeTestRule.onNodeWithTag("numpad-enter").performClick()

        // 7. 두 번째 뺄셈 전: 취소선이 없어야 한다면, 아래 코드 추가 (optional)
        // composeTestRule.onNodeWithTag("DividendTens-crossed").assertDoesNotExist()

        // [CHECK] 이제 취소선이 생기는지
        composeTestRule.onNodeWithTag("Subtract1Tens-crossed").assertExists()
    }

    @Test
    fun testBorrowed10ShownOnSubtract1Ones() {
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
            "1" to true,   // 입력 → 엔터
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

        // 받아내림 입력 후 '10'이 나타나는지 체크
        composeTestRule.onNodeWithTag("borrowed10-sub1-cell").assertTextEquals("10")
        // 혹은 '10'을 포함하는지 체크
        // composeTestRule.onNodeWithTag("borrow-cell").assertTextContains("10")
    }

//    @Test
//    fun testSubtractionLineAppearsOnSubtractPhase() {
//        composeTestRule.setContent {
//            DivisionScreen(viewModel = viewModel)
//        }
//        composeTestRule.runOnIdle { viewModel.startNewProblem(85, 7) }
//
//        // Phase: InputSubtract1Tens까지 진행
//        viewModel.submitInput("1") // quotientTens
//        viewModel.submitInput("7") // multiply1
//        viewModel.submitInput("1") // subtract1Tens 진입 시점
//
//        composeTestRule.waitForIdle()
//
//        // horizontal_line이 나타나야 함
//        composeTestRule
//            .onNodeWithTag("subtraction1-line")
//            .assertExists()
//    }
//
//    @Test
//    fun testSubtractionLineDoesNotAppearBeforeSubtractPhase() {
//        composeTestRule.setContent {
//            DivisionScreen(viewModel = viewModel)
//        }
//        composeTestRule.runOnIdle { viewModel.startNewProblem(85, 7) }
//
//        // 아직 quotient 입력 중
//        viewModel.submitInput("1") // quotientTens
//
//        composeTestRule.waitForIdle()
//
//        // 이때는 horizontal_line이 없어야 함
//        composeTestRule
//            .onNodeWithTag("subtraction1-line")
//            .assertDoesNotExist()
//    }

    @Test
    fun testSubtractionLineAppearsOnlyOnSubtract1Phase() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(85, 7) }

        // [1] 아직 quotient 입력 단계, 선이 없어야 함
        composeTestRule.onNodeWithTag("subtraction1-line").assertDoesNotExist()

        // [2] quotientTens 입력 (몫 십의자리)
        viewModel.submitInput("1")
        composeTestRule.waitForIdle()

        // [3] multiply1 입력 (1×7)
        viewModel.submitInput("7")
        composeTestRule.waitForIdle()

        // [4] subtract1Tens 입력 단계 진입, 선이 보여야 함
        viewModel.submitInput("1")
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("subtraction1-line").assertExists()
    }

    @Test
    fun testSubtractionLineAppearsOnlyOnSubtract2Phase() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(85, 7) }

        composeTestRule.onNodeWithTag("subtraction2-line").assertDoesNotExist()

        // [1] quotientTens 입력 (몫 십의자리)
        viewModel.submitInput("1")
        composeTestRule.waitForIdle()
        // [2] multiply1 입력 (1×7)
        viewModel.submitInput("7")
        composeTestRule.waitForIdle()
        // [3] subtract1Tens 입력
        viewModel.submitInput("1")
        composeTestRule.waitForIdle()
        // [4] bringDown 입력
        viewModel.submitInput("5")
        composeTestRule.waitForIdle()
        // [5] quotientOnes 입력 (2)
        viewModel.submitInput("2")
        composeTestRule.waitForIdle()
        // [6] multiply2Total 입력 (14)
        viewModel.submitInput("14")
        composeTestRule.waitForIdle()
        // [7] subtract2Result 입력 직전, 선이 **아직 없어야** 함
        // [8] subtract2Result 입력 단계 진입, 선이 **이제 보여야** 함
        viewModel.submitInput("1")
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("subtraction2-line").assertExists()
    }

    @Test
    fun testUserInput_Pattern_TensQuotient_NoBorrow_2DigitMultiply_Multiply2Total() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(90, 7) }

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
        val finalState = viewModel.domainState.value
        assertEquals(DivisionPhase.Complete, finalState.phases.getOrNull(finalState.currentPhaseIndex))
    }

//    private fun assertCell(
//        tag: String,
//        isEnabled: Boolean,
//        isHighlighted: Boolean,
//        expectedValue: String? = null
//    ) {
//        val node = composeTestRule.onNodeWithTag(tag)
//        if (isEnabled) node.assertIsEnabled() else node.assertIsNotEnabled()
//        if (isHighlighted) node.assert(hasAnyAncestor(hasTestTag("highlight-editing")))
//        if (expectedValue != null) node.assertTextEquals(expectedValue)
//    }
//
//    @Test
//    fun testEachPhase_HighlightAndFixedValue() {
//        composeTestRule.setContent { DivisionScreen(viewModel = viewModel) }
//        composeTestRule.runOnIdle { viewModel.startNewProblem(85, 7) }
//
//        // 1단계: 몫(quotientTens) 입력
//        assertCell("quotient-tens-cell", true, false)
//        assertCell("divisor-cell", false, true)
//        assertCell("dividend-tens-cell", false, true)
//
//        // 몫 입력
//        composeTestRule.runOnIdle { viewModel.submitInput("1") }
//
//        assertCell("multiply1-tens-cell", true, false)
//        assertCell("quotient-tens-cell", false, true, "1")
//        assertCell("divisor-cell", false, true)
//
//        // 곱셈 입력
//        composeTestRule.runOnIdle { viewModel.submitInput("7") }
//
//        // 뺄셈
//        assertCell("dividend-tens-cell", false, true)
//        assertCell("multiply1-tens-cell", false, true, "7")
//        assertCell("subtract1-tens-cell", true, false)
//
//        // 뺄셈 입력
//        composeTestRule.runOnIdle { viewModel.submitInput("1") }
//
//        // subtract1-tens-cell 값 고정
//        assertCell("subtract1-tens-cell", false, false, "1")
//        // 이후 단계 추가 시 동일 방식으로 확장 가능!
//    }

    @Test
    fun testEachPhase_PlaceholderAndInputValue() {
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle { viewModel.startNewProblem(90, 7) }

//        val text = composeTestRule.onNodeWithTag("quotient-tens-cell").fetchSemanticsNode().config.getOrNull(
//            SemanticsProperties.Text)
//        println("▶️ 실제 텍스트: $text")

        // [1] InputQuotientTens (몫 십의자리)
        composeTestRule.onNodeWithTag("QuotientTens-cell").assertTextContains("?")
        composeTestRule.runOnIdle { viewModel.submitInput("1") }
        composeTestRule.onNodeWithTag("QuotientTens-cell").assertTextContains("1")

        // [2] InputMultiply1 (곱셈: 1×7)
        composeTestRule.onNodeWithTag("Multiply1Tens-cell").assertTextContains("?")
        composeTestRule.runOnIdle { viewModel.submitInput("7") }
        composeTestRule.onNodeWithTag("Multiply1Tens-cell").assertTextContains("7")

        // [3] InputSubtract1Tens (뺄셈: 9-7)
        composeTestRule.onNodeWithTag("Subtract1Tens-cell").assertTextContains("?")
        composeTestRule.runOnIdle { viewModel.submitInput("2") }
        composeTestRule.onNodeWithTag("Subtract1Tens-cell").assertTextContains("2")

        // [4] InputBringDownFromDividendOnes (내려쓰기: 0)
        composeTestRule.onNodeWithTag("Subtract1Ones-cell").assertTextContains("?")
        composeTestRule.runOnIdle { viewModel.submitInput("0") }
        composeTestRule.onNodeWithTag("Subtract1Ones-cell").assertTextContains("0")

        // [5] InputQuotientOnes (몫 일의자리)
        composeTestRule.onNodeWithTag("QuotientOnes-cell").assertTextContains("?")
        composeTestRule.runOnIdle { viewModel.submitInput("2") }
        composeTestRule.onNodeWithTag("QuotientOnes-cell").assertTextContains("2")

        // [6] InputMultiply2Total (곱셈: 2×7=14)
        // 두 자리 입력이니 tens/ones에 대해 반복!
        composeTestRule.onNodeWithTag("Multiply2Tens-cell").assertTextContains("?")
        composeTestRule.onNodeWithTag("Multiply2Ones-cell").assertTextContains("?")
        composeTestRule.runOnIdle { viewModel.submitInput("14") }
        composeTestRule.onNodeWithTag("Multiply2Tens-cell").assertTextContains("1")
        composeTestRule.onNodeWithTag("Multiply2Ones-cell").assertTextContains("4")

        composeTestRule.onNodeWithTag("BorrowSubtract1Tens-cell").assertTextContains("?")
        composeTestRule.runOnIdle { viewModel.submitInput("1") }
        composeTestRule.onNodeWithTag("BorrowSubtract1Tens-cell").assertTextContains("1")

        // [7] InputSubtract2Result (마지막 뺄셈)
        composeTestRule.onNodeWithTag("Subtract2Ones-cell").assertTextContains("?")
        composeTestRule.runOnIdle { viewModel.submitInput("6") }
        composeTestRule.onNodeWithTag("Subtract2Ones-cell").assertTextContains("6")

        // 최종: Complete 상태 체크(옵션)
        val finalState = viewModel.domainState.value
        assertEquals(DivisionPhase.Complete, finalState.phases.getOrNull(finalState.currentPhaseIndex))
    }
}