package com.shinjaehun.suksuk.division

import androidx.lifecycle.SavedStateHandle
import com.shinjaehun.suksuk.domain.division.factory.DivisionDomainStateV2Factory
import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2
import com.shinjaehun.suksuk.domain.division.layout.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.division.detector.PatternDetectorV2
import com.shinjaehun.suksuk.domain.division.evaluator.PhaseEvaluatorV2
import com.shinjaehun.suksuk.domain.division.layout.sequence.ThreeByTwoPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.layout.sequence.TwoByOnePhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.layout.sequence.TwoByTwoPhaseSequenceCreator
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

        val twoByOneCreator = TwoByOnePhaseSequenceCreator()
        val twoByTwoCreator = TwoByTwoPhaseSequenceCreator()
        val threeByTwoCreator = ThreeByTwoPhaseSequenceCreator()

        val phaseSequenceProvider = DivisionPhaseSequenceProvider(twoByOneCreator, twoByTwoCreator, threeByTwoCreator)
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
            // === TwoByOne ===
            // TwoByOne_TensQuotient_NoBorrow_2DigitMul
            Triple("TwoByOne_TensQuotient_NoBorrow_2DigitMul: 46 ÷ 3", 46 to 3, listOf("1", "3", "1", "6", "5", "15", "1")),
            // TwoByOne_TensQuotient_NoBorrow_1DigitMul
            Triple("TwoByOne_TensQuotient_NoBorrow_1DigitMul: 45 ÷ 4", 45 to 4, listOf("1", "4", "0", "5", "1", "4", "1")),
            // TwoByOne_TensQuotient_Borrow_2DigitMul
            Triple("TwoByOne_TensQuotient_Borrow_2DigitMul: 50 ÷ 3", 50 to 3, listOf("1", "3", "2", "0", "6", "18", "1", "2")),
            // TwoByOne_TensQuotient_SkipBorrow_1DigitMul
            Triple("TwoByOne_TensQuotient_SkipBorrow_1DigitMul: 93 ÷ 8", 93 to 8, listOf("1", "8", "1", "3", "1", "8", "5")),
            // TwoByOne_TensQuotient_SkipMul2Sub2
            Triple("TwoByOne_TensQuotient_SkipMul2Sub2: 80 ÷ 8", 80 to 8, listOf("1", "8", "0", "0", "0")),
            // TwoByOne_OnesQuotient_Borrow_2DigitMul
            Triple("TwoByOne_OnesQuotient_Borrow_2DigitMul: 62 ÷ 7", 62 to 7, listOf("8", "56", "5", "6")),
            // TwoByOne_OnesQuotient_NoBorrow_2DigitMul
            Triple("TwoByOne_OnesQuotient_NoBorrow_2DigitMul: 81 ÷ 9", 81 to 9, listOf("9", "81", "0")),

            // === TwoByTwo ===
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
            Triple("TwoByTwo_Carry_Borrow_2DigitRem: 80 ÷ 17", 80 to 17, listOf("4", "28", "6", "7", "2", "1")),

            // === ThreeByTwo ===
            Triple("ThreeByTwo_TensQuotient_CM1_BS1_CM2_BS2_2DR: 610 ÷ 13", 610 to 13, listOf("4", "12", "5", "5", "9", "0", "6", "18", "7", "8", "2", "1")),
            Triple("ThreeByTwo_TensQuotient_CM1_BS1_CM2_BS2_1DR: 624 ÷ 14", 624 to 14, listOf("4", "16", "5", "5", "6", "4", "4", "16", "5", "5", "8")),

            Triple("ThreeByTwo_TensQuotient_CM1_BS1_CM2_NBS2_2DR: 732 ÷ 16", 732 to 16, listOf("4", "24", "6", "6", "9", "2", "5", "30", "8", "2", "1")),
            Triple("ThreeByTwo_TensQuotient_CM1_BS1_CM2_NBS2_2DR: 654 ÷ 14", 654 to 14, listOf("4", "16", "5", "5", "9", "4", "6", "24", "8", "0", "1")),
            Triple("ThreeByTwo_TensQuotient_CM1_BS1_CM2_NBS2_1DR: 632 ÷ 14", 632 to 14, listOf("4", "16", "5", "5", "7", "2", "5", "20", "7", "2")),

            Triple("ThreeByTwo_TensQuotient_CM1_BS1_NCM2_BS2_2DR: 700 ÷ 13", 700 to 13, listOf("5", "15", "6", "6", "5", "0", "3", "9", "3", "4", "1", "1")),
            Triple("ThreeByTwo_TensQuotient_CM1_BS1_NCM2_BS2_1DR: 800 ÷ 13", 800 to 13, listOf("6", "18", "7", "7", "2", "0", "1", "3", "1", "1", "7")),

            Triple("ThreeByTwo_TensQuotient_CM1_BS1_NCM2_NBS2_2DR: 619 ÷ 29", 619 to 29, listOf("2", "18", "5", "5", "3", "9", "1", "9", "2", "0", "1")),
            Triple("ThreeByTwo_TensQuotient_CM1_BS1_NCM2_NBS2_1DR: 819 ÷ 39", 819 to 39, listOf("2", "18", "7", "7", "3", "9", "1", "9", "3", "0")),

            Triple("ThreeByTwo_TensQuotient_CM1_NBS1_CM2_BS2_2DR: 670 ÷ 12", 670 to 12, listOf("5", "10", "6", "7", "0", "5", "10", "6", "0", "1")),
            Triple("ThreeByTwo_TensQuotient_CM1_NBS1_CM2_BS2_1DR: 680 ÷ 12", 680 to 12, listOf("5", "10", "6", "8", "0", "6", "12", "7", "7", "8")),

            Triple("ThreeByTwo_TensQuotient_CM1_NBS1_CM2_NBS2_2DR: 595 ÷ 13", 595 to 13, listOf("4", "12", "5", "7", "5", "5", "15", "6", "0", "1")),
            Triple("ThreeByTwo_TensQuotient_CM1_NBS1_CM2_NBS2_1DR: 660 ÷ 12", 660 to 12, listOf("5", "10", "6", "6", "0", "5", "10", "6", "0")),
            Triple("ThreeByTwo_TensQuotient_CM1_NBS1_CM2_NBS2_1DR: 672 ÷ 12", 672 to 12, listOf("5", "10", "6", "7", "2", "6", "12", "7", "0")),

            Triple("ThreeByTwo_TensQuotient_CM1_NBS1_NCM2_BS2_2DR: 460 ÷ 14", 460 to 14, listOf("3", "12", "4", "4", "0", "2", "8", "2", "3", "2", "1")),
            Triple("ThreeByTwo_TensQuotient_CM1_NBS1_NCM2_BS2_1DR: 320 ÷ 15", 320 to 15, listOf("2", "10", "3", "2", "0", "1", "5", "1", "1", "5")),

            Triple("ThreeByTwo_TensQuotient_CM1_NBS1_NCM2_NBS2_2DR: 475 ÷ 15", 475 to 15, listOf("3", "15", "4", "2", "5", "1", "5", "1", "0", "1")),
            Triple("ThreeByTwo_TensQuotient_CM1_NBS1_NCM2_NBS2_2DR: 446 ÷ 14", 446 to 14, listOf("3", "12", "4", "2", "6", "1", "4", "1", "2", "1")),
            Triple("ThreeByTwo_TensQuotient_CM1_NBS1_NCM2_NBS2_1DR: 568 ÷ 27", 568 to 27, listOf("2", "14", "5", "2", "8", "1", "7", "2", "1")),

            Triple("ThreeByTwo_TensQuotient_NCM1_BS1_CM2_BS2_2DR: 220 ÷ 13", 220 to 13, listOf("1", "3", "1", "1", "9", "0", "6", "18", "7", "8", "2", "1")),
            Triple("ThreeByTwo_TensQuotient_NCM1_BS1_CM2_BS2_1DR: 200 ÷ 16", 200 to 16, listOf("1", "6", "1", "1", "4", "0", "2", "12", "3", "3", "8")),

            Triple("ThreeByTwo_TensQuotient_NCM1_BS1_CM2_NBS2_2DR: 202 ÷ 12", 202 to 12, listOf("1", "2", "1", "1", "8", "2", "6", "12", "7", "0", "1")),
            Triple("ThreeByTwo_TensQuotient_NCM1_BS1_CM2_NBS2_1DR: 204 ÷ 12", 204 to 12, listOf("1", "2", "1", "1", "8", "4", "7", "14", "8", "0")),

            Triple("ThreeByTwo_TensQuotient_NCM1_BS1_NCM2_BS2_2DR: 710 ÷ 21", 710 to 21, listOf("3", "3", "6", "6", "8", "0", "3", "3", "6", "7", "7", "1")),
            Triple("ThreeByTwo_TensQuotient_NCM1_BS1_NCM2_BS2_1DR: 911 ÷ 43", 911 to 43, listOf("2", "6", "8", "8", "5", "1", "1", "3", "4", "4", "8")),

            Triple("ThreeByTwo_TensQuotient_NCM1_BS1_NCM2_NBS2_2DR: 604 ÷ 11", 604 to 11, listOf("5", "5", "5", "5", "5", "4", "4", "4", "4", "0", "1")),
            Triple("ThreeByTwo_TensQuotient_NCM1_BS1_NCM2_NBS2_1DR: 517 ÷ 47", 517 to 47, listOf("1", "7", "4", "4", "4", "7", "1", "7", "4", "0")),

            // ThreeByTwo_TensQuotient_NCM1_NBS1_CM2_BS2_2DR 불가
            Triple("ThreeByTwo_TensQuotient_NCM1_NBS1_CM2_BS2_1DR: 190 ÷ 13", 190 to 13, listOf("1", "3", "1", "6", "0", "4", "12", "5", "5", "8")),

            Triple("ThreeByTwo_TensQuotient_NCM1_NBS1_CM2_NBS2_1DR: 181 ÷ 12", 181 to 12, listOf("1", "2", "1", "6", "1", "5", "10", "6", "1")),
            Triple("ThreeByTwo_TensQuotient_NCM1_NBS1_CM2_NBS2_2DR: 190 ÷ 12", 190 to 12, listOf("1", "2", "1", "7", "0", "5", "10", "6", "0", "1")),

            Triple("ThreeByTwo_TensQuotient_NCM1_NBS1_NCM2_BS2_2DR: 180 ÷ 14", 180 to 14, listOf("1", "4", "1", "4", "0", "2", "8", "2", "3", "2", "1")),
            Triple("ThreeByTwo_TensQuotient_NCM1_NBS1_NCM2_BS2_1DR: 280 ÷ 25", 280 to 25, listOf("1", "5", "2", "3", "0", "1", "5", "2", "2", "5")),

            Triple("ThreeByTwo_TensQuotient_NCM1_NBS1_NCM2_NBS2_2DR: 131 ÷ 11", 131 to 11, listOf("1", "1", "1", "2", "1", "1", "1", "1", "0", "1")),
            Triple("ThreeByTwo_TensQuotient_NCM1_NBS1_NCM2_NBS2_1DR: 682 ÷ 31", 682 to 31, listOf("2", "2", "6", "6", "2", "2", "2", "6", "0")),

            Triple("ThreeByTwo_TensQuotient_CM1_BS1_SkipMul2Sub2 : 610 ÷ 15", 610 to 15, listOf("4", "20", "6", "1", "0", "0")),

            // 3ds1 3dm2: nhbs2 ntbs2
            Triple("ThreeByTwo_TensQuotient_3DS1_3DM2 : 236 ÷ 13", 236 to 13, listOf("1", "3", "1", "0", "1", "6", "8", "24", "10", "2")),
            // 3ds1 3dm2: hbs2 ntbs2
            Triple("ThreeByTwo_TensQuotient_3DS1_3DM2 : 419 ÷ 21", 419 to 21, listOf("1", "1", "2", "0", "2", "9", "9", "9", "18", "0", "1", "2")),
            // 3ds1 3dm2: nhbs2 tbs2
            Triple("ThreeByTwo_TensQuotient_3DS1_3DM2 : 230 ÷ 12", 230 to 12, listOf("1", "2", "1", "1", "1", "0", "9", "18", "10", "0", "2")),
            // 3ds1 3dm2: hbs2 tbs2 double borrow
            Triple("ThreeByTwo_TensQuotient_3DS1_3DM2 : 210 ÷ 11", 210 to 11, listOf("1", "1", "1", "0", "1", "0", "9", "9", "9", "0", "9", "1")),

            // Sub2 110 - 60: skip borrow from sub1hundreds
            Triple("ThreeByTwo_TensQuotient_3DS1_2DM2 : 710 ÷ 60", 710 to 60, listOf("1", "0", "6", "1", "1", "0", "1", "0", "6", "0", "0", "5")),
            Triple("ThreeByTwo_TensQuotient_3DS1_3DM2 : 219 ÷ 11", 219 to 11, listOf("1", "1", "1", "0", "1", "9", "9", "9", "9", "0", "1")),

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
