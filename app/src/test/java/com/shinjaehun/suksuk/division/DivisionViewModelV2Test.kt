package com.shinjaehun.suksuk.division

import androidx.lifecycle.SavedStateHandle
import com.shinjaehun.suksuk.domain.division.factory.DivisionDomainStateV2Factory
import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2
import com.shinjaehun.suksuk.domain.division.layout.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.division.detector.PatternDetectorV2
import com.shinjaehun.suksuk.domain.division.evaluator.PhaseEvaluatorV2
import com.shinjaehun.suksuk.presentation.division.DivisionViewModelV2
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DivisionViewModelV2Test {

    private lateinit var viewModel: DivisionViewModelV2

    @Before
    fun setup() {

        val savedStateHandle = SavedStateHandle(mapOf("autoStart" to false))
        val phaseSequenceProvider = DivisionPhaseSequenceProvider()
        val phaseEvaluator = PhaseEvaluatorV2()
        val patternDetector = PatternDetectorV2

        val factory = DivisionDomainStateV2Factory(phaseSequenceProvider, patternDetector)

        viewModel = DivisionViewModelV2(
            savedStateHandle = savedStateHandle,
            phaseEvaluator = phaseEvaluator,
            domainStateFactory = factory
        )
    }

    @Test
    fun detectPatternTest() {
        val cases = listOf(
            // === TwoByOne 패턴 세부 분기 ===
            Triple(46, 3, DivisionPatternV2.TwoByOne),      // TwoByOne_TensQuotient_NoBorrow_2DigitMul
            Triple(45, 4, DivisionPatternV2.TwoByOne),      // TwoByOne_TensQuotient_NoBorrow_1DigitMul
            Triple(50, 3, DivisionPatternV2.TwoByOne),      // TwoByOne_TensQuotient_Borrow_2DigitMul
            Triple(70, 6, DivisionPatternV2.TwoByOne),      // TwoByOne_TensQuotient_SkipBorrow_1DigitMul
            Triple(53, 6, DivisionPatternV2.TwoByOne),      // TwoByOne_OnesQuotient_Borrow_2DigitMul
            Triple(12, 3, DivisionPatternV2.TwoByOne),      // TwoByOne_OnesQuotient_NoBorrow_2DigitMul

            // === TwoByTwo 패턴 세부 분기 ===
            Triple(68, 34, DivisionPatternV2.TwoByTwo),     // TwoByTwo_NoCarry_NoBorrow_1DigitRem
            Triple(57, 22, DivisionPatternV2.TwoByTwo),     // TwoByTwo_NoCarry_NoBorrow_2DigitRem
            Triple(50, 22, DivisionPatternV2.TwoByTwo),     // TwoByTwo_NoCarry_Borrow_1DigitRem
            Triple(50, 13, DivisionPatternV2.TwoByTwo),     // TwoByTwo_NoCarry_Borrow_2DigitRem
            Triple(75, 25, DivisionPatternV2.TwoByTwo),     // TwoByTwo_Carry_NoBorrow_1DigitRem
            Triple(95, 28, DivisionPatternV2.TwoByTwo),     // TwoByTwo_Carry_NoBorrow_2DigitRem
            Triple(81, 12, DivisionPatternV2.TwoByTwo),     // TwoByTwo_Carry_Borrow_1DigitRem
            Triple(80, 17, DivisionPatternV2.TwoByTwo),     // TwoByTwo_Carry_Borrow_2DigitRem
            // 향후 ThreeByTwo도 추가 가능
        )

        for ((dividend, divisor, expectedPattern) in cases) {
            viewModel.startNewProblem(dividend, divisor)
            val actualPattern = viewModel.getCurrentPattern()
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
            // === TwoByOne 세부 패턴 ===
            // TwoByOne_TensQuotient_NoBorrow_2DigitMul
            Triple("TwoByOne_TensQuotient_NoBorrow_2DigitMul: 46 ÷ 3", 46 to 3, listOf("1", "3", "1", "6", "5", "15", "1")),
            // TwoByOne_TensQuotient_NoBorrow_1DigitMul
            Triple("TwoByOne_TensQuotient_NoBorrow_1DigitMul: 45 ÷ 4", 45 to 4, listOf("1", "4", "0", "5", "1", "4", "1")),
            // TwoByOne_TensQuotient_Borrow_2DigitMul
            Triple("TwoByOne_TensQuotient_Borrow_2DigitMul: 50 ÷ 3", 50 to 3, listOf("1", "3", "2", "0", "6", "18", "1", "2")),
            // TwoByOne_TensQuotient_SkipBorrow_1DigitMul
            Triple("TwoByOne_TensQuotient_SkipBorrow_1DigitMul: 93 ÷ 8", 93 to 8, listOf("1", "8", "1", "3", "1", "8", "5")),
            // TwoByOne_TensQuotient_SkipMul2Sub2 <------------------------------------
            Triple("TwoByOne_TensQuotient_SkipMul2Sub2: 80 ÷ 8", 80 to 8, listOf("1", "8", "0", "0", "0")),
            // TwoByOne_OnesQuotient_Borrow_2DigitMul
            Triple("TwoByOne_OnesQuotient_Borrow_2DigitMul: 62 ÷ 7", 62 to 7, listOf("8", "56", "5", "6")),
            // TwoByOne_OnesQuotient_NoBorrow_2DigitMul
            Triple("TwoByOne_OnesQuotient_NoBorrow_2DigitMul: 81 ÷ 9", 81 to 9, listOf("9", "81", "0")),

            // === TwoByTwo 세부 패턴 ===
            // TwoByTwo_NoCarry_NoBorrow_1DigitRem
            Triple("TwoByTwo_NoCarry_NoBorrow_1DigitRem: 68 ÷ 34", 68 to 34, listOf("2", "8", "6", "0")),
            // TwoByTwo_NoCarry_NoBorrow_2DigitRem
            Triple("TwoByTwo_NoCarry_NoBorrow_2DigitRem: 57 ÷ 22", 57 to 22, listOf("2", "4", "4", "3", "1")),
            // TwoByTwo_NoCarry_Borrow_1DigitRem
            Triple("TwoByTwo_NoCarry_Borrow_1DigitRem: 50 ÷ 22", 50 to 22, listOf("2", "4", "4", "4", "6")),
            // TwoByTwo_NoCarry_Borrow_2DigitRem
            Triple("TwoByTwo_NoCarry_Borrow_2DigitRem: 50 ÷ 13", 50 to 13, listOf("3", "9", "3", "4", "1", "1")),
            // TwoByTwo_Carry_NoBorrow_1DigitRem
            Triple("TwoByTwo_Carry_NoBorrow_1DigitRem: 96 ÷ 12", 96 to 12, listOf("8", "16", "9", "0")),
            // TwoByTwo_Carry_NoBorrow_2DigitRem
            Triple("TwoByTwo_Carry_NoBorrow_2DigitRem: 95 ÷ 28", 95 to 28, listOf("3", "24", "8", "1", "1")),
            // TwoByTwo_Carry_Borrow_1DigitRem
            Triple("TwoByTwo_Carry_Borrow_1DigitRem: 81 ÷ 12", 81 to 12, listOf("6", "12", "7", "7", "9")),
            // TwoByTwo_Carry_Borrow_2DigitRem
            Triple("TwoByTwo_Carry_Borrow_2DigitRem: 80 ÷ 17", 80 to 17, listOf("4", "28", "6", "7", "2", "1"))
        )
        for ((name, pair, inputs) in cases) {
            val (dividend, divisor) = pair
            val expectedPattern = when {
                dividend.toString().length == 2 && divisor.toString().length == 1 -> DivisionPatternV2.TwoByOne
                dividend.toString().length == 2 && divisor.toString().length == 2 -> DivisionPatternV2.TwoByTwo
                dividend.toString().length == 3 && divisor.toString().length == 2 -> DivisionPatternV2.ThreeByTwo
                else -> error("지원하지 않는 패턴")
            }

            // 주석으로만 세부 패턴 구분 (name 변수 참고)
            viewModel.startNewProblem(dividend, divisor)
            val actualPattern = viewModel.getCurrentPattern()
            assertEquals("$name: 대분류 패턴 불일치!", expectedPattern, actualPattern)


            // 입력값을 순차적으로 제출하며 단계별 정답 확인
            for ((i, input) in inputs.withIndex()) {
                viewModel.submitInput(input)
                val state = viewModel.uiState.value

                // 마지막 입력이면 feedback이 있어야 한다!
                if (i == inputs.lastIndex) {
                    assertEquals("$name: 마지막 입력 후 feedback 불일치", "정답입니다!", state.feedback)
                } else {
                    assertTrue("$name: ${i + 1}번째 입력 오답! (${input})", state.feedback == null)
                }
            }

            val state = viewModel.uiState.value
            assertTrue("$name: Complete phase 미도달", state.isCompleted)
        }
    }
}
