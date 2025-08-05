package com.shinjaehun.suksuk.legacy

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.shinjaehun.suksuk.domain.division.detector.PatternDetector
import com.shinjaehun.suksuk.domain.division.evaluator.PhaseEvaluator
import com.shinjaehun.suksuk.domain.division.factory.DivisionDomainStateFactory
import com.shinjaehun.suksuk.domain.division.layout.DivisionPatternUiLayoutRegistry
import com.shinjaehun.suksuk.domain.division.model.DivisionPattern
import com.shinjaehun.suksuk.domain.division.model.DivisionPhase
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
    fun detectPatternTest() {
        val cases = listOf(
            Triple(46, 3, DivisionPattern.TwoByOne_TensQuotient_NoBorrow_2DigitMul),
            Triple(45, 4, DivisionPattern.TwoByOne_TensQuotient_NoBorrow_1DigitMul),
            Triple(50, 3, DivisionPattern.TwoByOne_TensQuotient_Borrow_2DigitMul),
            Triple(70, 6, DivisionPattern.TwoByOne_TensQuotient_SkipBorrow_1DigitMul),
            Triple(53, 6, DivisionPattern.TwoByOne_OnesQuotient_Borrow_2DigitMul),
            Triple(12, 3, DivisionPattern.TwoByOne_OnesQuotient_NoBorrow_2DigitMul),
            Triple(68, 34, DivisionPattern.TwoByTwo_NoCarry_NoBorrow_1DigitRem),
            Triple(57, 22, DivisionPattern.TwoByTwo_NoCarry_NoBorrow_2DigitRem),
            Triple(50, 22, DivisionPattern.TwoByTwo_NoCarry_Borrow_1DigitRem),
            Triple(50, 13, DivisionPattern.TwoByTwo_NoCarry_Borrow_2DigitRem),
            Triple(75, 25, DivisionPattern.TwoByTwo_Carry_NoBorrow_1DigitRem),
            Triple(95, 28, DivisionPattern.TwoByTwo_Carry_NoBorrow_2DigitRem),
            Triple(81, 12, DivisionPattern.TwoByTwo_Carry_Borrow_1DigitRem),
            Triple(80, 17, DivisionPattern.TwoByTwo_Carry_Borrow_2DigitRem),

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
    fun userInputTest() = runTest {
        val cases = listOf(
            Triple("TwoByOne_TensQuotient_NoBorrow_2DigitMul: 46 ÷ 3", 46 to 3, listOf("1", "3", "1", "6", "5", "15", "1")),
            Triple("TwoByOne_TensQuotient_NoBorrow_1DigitMul: 45 ÷ 4", 45 to 4, listOf("1", "4", "0", "5", "1", "4", "1")),
            Triple("TwoByOne_TensQuotient_Borrow_2DigitMul: 50 ÷ 3", 50 to 3, listOf("1", "3", "2", "0", "6", "18", "1", "2")),
            Triple("TwoByOne_TensQuotient_SkipBorrow_1DigitMul: 93 ÷ 8", 93 to 8, listOf("1", "8", "1", "3", "1", "8", "5")),
            Triple("TwoByOne_OnesQuotient_Borrow_2DigitMul: 62 ÷ 7", 62 to 7, listOf("8", "56", "5", "6")),
            Triple("TwoByOne_OnesQuotient_NoBorrow_2DigitMul: 81 ÷ 9", 81 to 9, listOf("9", "81", "0")),
            Triple("TwoByTwo_NoCarry_NoBorrow_1DigitRem: 68 ÷ 34", 68 to 34, listOf("2", "8", "6", "0")),
            Triple("TwoByTwo_NoCarry_NoBorrow_2DigitRem: 57 ÷ 22", 57 to 22, listOf("2", "4", "4", "3", "1")),
            Triple("TwoByTwo_NoCarry_Borrow_1DigitRem: 50 ÷ 22", 50 to 22, listOf("2", "4", "4", "4", "6")),
            Triple("TwoByTwo_NoCarry_Borrow_2DigitRem: 50 ÷ 13", 50 to 13, listOf("3", "9", "3", "4", "1", "1")),
            Triple("TwoByTwo_Carry_NoBorrow_1DigitRem: 96 ÷ 12", 96 to 12, listOf("8", "16", "9", "0")),
            Triple("TwoByTwo_Carry_NoBorrow_2DigitRem: 95 ÷ 28", 95 to 28, listOf("3", "24", "8", "1", "1")),
            Triple("TwoByTwo_Carry_Borrow_1DigitRem: 81 ÷ 12", 81 to 12, listOf("6", "12", "7", "7", "9")),
            Triple("TwoByTwo_Carry_Borrow_2DigitRem: 80 ÷ 17", 80 to 17, listOf("4", "28", "6", "7", "2", "1"))
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