package com.shinjaehun.suksuk

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.shinjaehun.suksuk.domain.PatternDetector
import com.shinjaehun.suksuk.domain.PhaseEvaluator
import com.shinjaehun.suksuk.presentation.division.DivisionPattern
import com.shinjaehun.suksuk.presentation.division.DivisionPatternUiLayoutRegistry
import com.shinjaehun.suksuk.presentation.division.DivisionPhase
import com.shinjaehun.suksuk.presentation.division.DivisionViewModel
import com.shinjaehun.suksuk.presentation.division.FeedbackMessageProvider
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DivisionViewModelTest {

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
    fun detectPatternTest() {
        val cases = listOf(
            Triple(46, 3, DivisionPattern.TensQuotient_NoBorrow_2DigitMul),
            Triple(72, 6, DivisionPattern.TensQuotient_NoBorrow_2DigitMul),
            Triple(74, 6, DivisionPattern.TensQuotient_NoBorrow_2DigitMul),
            Triple(85, 7, DivisionPattern.TensQuotient_NoBorrow_2DigitMul),
            Triple(86, 7, DivisionPattern.TensQuotient_NoBorrow_2DigitMul),
            Triple(92, 7, DivisionPattern.TensQuotient_NoBorrow_2DigitMul),
            Triple(96, 4, DivisionPattern.TensQuotient_NoBorrow_2DigitMul),

            Triple(45, 4, DivisionPattern.TensQuotient_NoBorrow_1DigitMul),
            Triple(57, 5, DivisionPattern.TensQuotient_NoBorrow_1DigitMul),
            Triple(84, 4, DivisionPattern.TensQuotient_NoBorrow_1DigitMul),

            Triple(50, 3, DivisionPattern.TensQuotient_Borrow_2DigitMul),
            Triple(90, 7, DivisionPattern.TensQuotient_Borrow_2DigitMul),
            Triple(70, 4, DivisionPattern.TensQuotient_Borrow_2DigitMul),


            Triple(70, 6, DivisionPattern.TensQuotient_SkipBorrow_1DigitMul),
            Triple(71, 6, DivisionPattern.TensQuotient_SkipBorrow_1DigitMul),
            Triple(90, 8, DivisionPattern.TensQuotient_SkipBorrow_1DigitMul),
            Triple(93, 8, DivisionPattern.TensQuotient_SkipBorrow_1DigitMul),

            Triple(53, 6, DivisionPattern.OnesQuotient_Borrow_2DigitMul),
            Triple(62, 7, DivisionPattern.OnesQuotient_Borrow_2DigitMul),

            Triple(12, 3, DivisionPattern.OnesQuotient_NoBorrow_2DigitMul),
            Triple(24, 7, DivisionPattern.OnesQuotient_NoBorrow_2DigitMul),
            Triple(39, 4, DivisionPattern.OnesQuotient_NoBorrow_2DigitMul),
            Triple(49, 5, DivisionPattern.OnesQuotient_NoBorrow_2DigitMul),
            Triple(54, 9, DivisionPattern.OnesQuotient_NoBorrow_2DigitMul),
            Triple(68, 9, DivisionPattern.OnesQuotient_NoBorrow_2DigitMul),
            Triple(81, 9, DivisionPattern.OnesQuotient_NoBorrow_2DigitMul)
        )

        for ((dividend, divisor, expectedPattern) in cases) {

            viewModel.startNewProblem(dividend, divisor)
            val actualPattern = viewModel.domainState.value.pattern
            println("✅ $dividend ÷ $divisor → expected: $expectedPattern, actual: $actualPattern")

            assertEquals(
                "$dividend ÷ $divisor: 예상=$expectedPattern, 결과=$actualPattern",
                expectedPattern,
                actualPattern
            )
        }
    }

    @Test
    fun twoDigitDivByOneDigitTest() = runTest {
        val cases = listOf(
            // TensQuotient_NoBorrow_2DigitMul
            Triple("TensQuotient_NoBorrow_2DigitMul: 46 ÷ 3", 46 to 3, listOf("1", "3", "1", "6", "5", "15", "1")),
            Triple("TensQuotient_NoBorrow_2DigitMul: 72 ÷ 6", 72 to 6, listOf("1", "6", "1", "2", "2", "12", "0")),
            Triple("TensQuotient_NoBorrow_2DigitMul: 74 ÷ 6", 74 to 6, listOf("1", "6", "1", "4", "2", "12", "2")),
            Triple("TensQuotient_NoBorrow_2DigitMul: 85 ÷ 7", 85 to 7, listOf("1", "7", "1", "5", "2", "14", "1")),
            Triple("TensQuotient_NoBorrow_2DigitMul: 86 ÷ 7", 86 to 7, listOf("1", "7", "1", "6", "2", "14", "2")),
            Triple("TensQuotient_NoBorrow_2DigitMul: 92 ÷ 7", 92 to 7, listOf("1", "7", "2", "2", "3", "21", "1")),
            Triple("TensQuotient_NoBorrow_2DigitMul: 96 ÷ 4", 96 to 4, listOf("2", "8", "1", "6", "4", "16", "0")),


            // TensQuotient_NoBorrow_1DigitMul
            Triple("TensQuotient_NoBorrow_1DigitMul: 45 ÷ 4", 45 to 4, listOf("1", "4", "0", "5", "1", "4", "1")),
            Triple("TensQuotient_NoBorrow_1DigitMul: 57 ÷ 5", 57 to 5, listOf("1", "5", "0", "7", "1", "5", "2")),
            Triple("TensQuotient_NoBorrow_1DigitMul: 84 ÷ 4", 84 to 4, listOf("2", "8", "0", "4", "1", "4", "0")),

            // TensQuotient_Borrow_2DigitMul
            Triple("TensQuotient_Borrow_2DigitMul: 50 ÷ 3", 50 to 3, listOf("1", "3", "2", "0", "6", "18", "1", "2")),

            // TensQuotient_SkipBorrow_1DigitMul
            Triple("TensQuotient_SkipBorrow_1DigitMul: 71 ÷ 6", 71 to 6, listOf("1", "6", "1", "1", "1", "6", "5")),
            Triple("TensQuotient_SkipBorrow_1DigitMul: 90 ÷ 8", 90 to 8, listOf("1", "8", "1", "0", "1", "8", "2")),
            Triple("TensQuotient_SkipBorrow_1DigitMul: 93 ÷ 8", 93 to 8, listOf("1", "8", "1", "3", "1", "8", "5")),

            // OnesQuotient_Borrow
            Triple("OnesQuotient_Borrow_2DigitMul: 53 ÷ 6", 53 to 6, listOf("8", "48", "4", "5")),
            Triple("OnesQuotient_Borrow_2DigitMul: 62 ÷ 7", 62 to 7, listOf("8", "56", "5", "6")),

            // OnesQuotient_NoBorrow
            Triple("OnesQuotient_NoBorrow_2DigitMul: 12 ÷ 3", 12 to 3, listOf("4", "12", "0")),
            Triple("OnesQuotient_NoBorrow_2DigitMul: 24 ÷ 7", 24 to 7, listOf("3", "21", "3")),
            Triple("OnesQuotient_NoBorrow_2DigitMul: 39 ÷ 4", 39 to 4, listOf("9", "36", "3")),
            Triple("OnesQuotient_NoBorrow_2DigitMul: 49 ÷ 5", 49 to 5, listOf("9", "45", "4")),
            Triple("OnesQuotient_NoBorrow_2DigitMul: 54 ÷ 9", 54 to 9, listOf("6", "54", "0")),
            Triple("OnesQuotient_NoBorrow_2DigitMul: 68 ÷ 9", 68 to 9, listOf("7", "63", "5")),
            Triple("OnesQuotient_NoBorrow_2DigitMul: 81 ÷ 9", 81 to 9, listOf("9", "81", "0"))
        )
        for ((name, pair, inputs) in cases) {
            val (dividend, divisor) = pair
            val expectedPatternName = name.substringBefore(":").trim()

            viewModel.domainState.test {
                try {
                    viewModel.startNewProblem(dividend, divisor)

                    awaitItem() // 초기 emit 무시
                    var state = awaitItem()

                    val actualPattern = state.pattern?.name ?: "null"
                    println("[$name] ⏱ expected=$expectedPatternName, actual=$actualPattern")
                    assertEquals(
                        "$name: 패턴 불일치! expected=$expectedPatternName, actual=$actualPattern",
                        expectedPatternName,
                        actualPattern
                    )

//                    for (i in inputs.indices) {
//                        viewModel.submitInput(inputs[i])
//                        state = awaitItem()
//                        assertEquals("$name: ${i + 1}번째 입력 오답! (${inputs[i]})", null, state.feedback)
//                    }

                    for (i in inputs.indices) {
                        viewModel.submitInput(inputs[i])
                        state = awaitItem()
                        if (state.phases.getOrNull(state.currentPhaseIndex) == DivisionPhase.Complete) {
                            assertEquals("$name: 마지막 phase에서 feedback은 정답입니다!", "정답입니다!", state.feedback)
                        } else {
                            assertEquals("$name: ${i + 1}번째 입력 오답! (${inputs[i]})", null, state.feedback)
                        }
                    }

                    val isCompletePhase = state.phases.getOrNull(state.currentPhaseIndex) == DivisionPhase.Complete
                    assertTrue("$name: Complete phase 미도달", isCompletePhase)

                } finally {
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

}