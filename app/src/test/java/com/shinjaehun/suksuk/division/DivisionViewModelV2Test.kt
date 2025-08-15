package com.shinjaehun.suksuk.division

import com.shinjaehun.suksuk.domain.division.factory.DivisionDomainStateV2Factory
import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.division.detector.DivisionPatternDetectorV2
import com.shinjaehun.suksuk.domain.division.evaluator.DivisionPhaseEvaluatorV2
import com.shinjaehun.suksuk.domain.division.sequence.creator.ThreeByTwoDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.sequence.creator.TwoByOneDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.sequence.creator.TwoByTwoDivPhaseSequenceCreator
import com.shinjaehun.suksuk.presentation.division.DivisionViewModelV2
import com.shinjaehun.suksuk.presentation.division.model.DivisionUiStateV2
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DivisionViewModelV2Test {

    private lateinit var viewModel: DivisionViewModelV2

    @Before
    fun setup() {

        val twoByOneCreator = TwoByOneDivPhaseSequenceCreator()
        val twoByTwoCreator = TwoByTwoDivPhaseSequenceCreator()
        val threeByTwoCreator = ThreeByTwoDivPhaseSequenceCreator()

        val phaseSequenceProvider = DivisionPhaseSequenceProvider(
            twoByOneCreator,
            twoByTwoCreator,
            threeByTwoCreator,
        )
        val phaseEvaluator = DivisionPhaseEvaluatorV2()

        val factory = DivisionDomainStateV2Factory(DivisionPatternDetectorV2, phaseSequenceProvider)

        viewModel = DivisionViewModelV2(
            evaluator = phaseEvaluator,
            factory = factory
        )
    }

    private fun DivisionViewModelV2.uiOrThrow(): DivisionUiStateV2 =
        requireNotNull(uiState.value) { "uiState is null. Did you call startNewProblem()?" }

    @Test
    fun detectPatternTest() = runTest {
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

            val actualPattern = viewModel.uiOrThrow().pattern
            assertEquals(expectedPattern, actualPattern)

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

            // 정확히 떨어짐(=0) + TensQuotient 경계
            // 84 ÷ 7 = 12 R0 (1자리/2자리 곱셈 분기 둘 다 점검됨)
            Triple("TwoByOne: 84 ÷ 7", 84 to 7, listOf("1", "7", "1", "4", "2", "14", "0")),
            // 90 ÷ 9 = 10 R0 (몫 일의 자리=0 입력 경계)
            Triple("TwoByOne: 90 ÷ 9", 90 to 9, listOf("1", "9", "0", "0", "0")),

            // Dividend 끝이 0 + Borrow 분기
            // 70 ÷ 6 = 11 R4 (Subtract2에서 borrow 발생)
            Triple("TwoByOne: 70 ÷ 6", 70 to 6, listOf("1", "6", "1", "0", "1", "6", "4")),
            // Dividend < 2×Divisor 경계(몫 1자리만)
            // 10 ÷ 6 = 1 R4 (No-borrow, 1-digit mul)
            Triple("TwoByOne: 10 ÷ 6", 10 to 6, listOf("1", "6", "4")),
            // 20 ÷ 6 = 3 R2 (Borrow 여부 분기 확인)
            Triple("TwoByOne: 20 ÷ 6", 20 to 6, listOf("3", "18", "1", "2")),
            // 제수 5, 6, 7, 8, 9 경곗값
            // 99 ÷ 9 = 11 R0 (연속 9 처리/carry 없는 정확 나눗셈)
            Triple("TwoByOne: 99 ÷ 9", 99 to 9, listOf("1", "9", "0", "9", "1", "9", "0")),

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

            //Dividend == Divisor (항상 1, R0)
            //34 ÷ 34 = 1 R0 (너 리스트에 68/34는 있으나 동일수 케이스 미포함)
            Triple("TwoByTwo: 34 ÷ 34", 34 to 34, listOf("1", "4", "3", "0")),
            //Divisor=11(반복자리) 특성
            //72 ÷ 11 = 6 R6 (No-carry/borrow 확인)
            Triple("TwoByTwo: 72 ÷ 11", 72 to 11, listOf("6", "6", "6", "6", "6")),
            //77 ÷ 11 = 7 R0 (정확히 떨어짐)
            Triple("TwoByTwo: 77 ÷ 11", 77 to 11, listOf("7", "7", "7", "0")),
            //Carry만 있는 케이스의 2-digit remainder
            //94 ÷ 18 = 5 R4 (CM, NBS 조합 확인)
            Triple("TwoByTwo: 94 ÷ 18", 94 to 18, listOf("5", "40", "9", "4")),
            //Borrow만 있는 케이스의 2-digit remainder
            //53 ÷ 24 = 2 R5
            Triple("TwoByTwo: 53 ÷ 24", 53 to 24, listOf("2", "8", "4", "4", "5")),
            //몫이 3 이상에서 곱셈 두 자릿수(=20·30 경계)
            //96 ÷ 24 = 4 R0 (CM, R0)
            Triple("TwoByTwo: 96 ÷ 24", 96 to 24, listOf("4", "16", "9", "0")),
            //첫 뺄셈이 0로 바로 bring-down
            //40 ÷ 35 = 1 R5 (Subtract1=5, bring-down 로직 검증)
            Triple("TwoByTwo: 40 ÷ 35", 40 to 35, listOf("1", "5", "3", "3", "5")),

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

            // nhbs1 ntbs1
            Triple("ThreeByTwo_OneQuotient : 198 ÷ 22", 198 to 22, listOf("9", "18", "19", "0")),
            // nhbs1 tbs1
            Triple("ThreeByTwo_OneQuotient : 230 ÷ 27", 230 to 27, listOf("8", "56", "21", "2", "4", "1")),
            // hbs1 ntbs1
            Triple("ThreeByTwo_OneQuotient : 205 ÷ 23", 205 to 23, listOf("8", "24", "18", "1", "1", "2")),
            // hbs1 tbs1 double borrow
            Triple("ThreeByTwo_OneQuotient : 342 ÷ 49", 342 to 49, listOf("6", "54", "29", "2", "9", "8", "4")),
            // nhbs1 tbs1 2digit mul
            Triple("ThreeByTwo_OneQuotient : 234 ÷ 25", 234 to 25, listOf("9", "45", "22", "2", "9")),
            // hbs1 ntbs1 2digit mul
            Triple("ThreeByTwo_OneQuotient : 315 ÷ 42", 315 to 42, listOf("7", "14", "29", "1", "2", "2")),
            // skip hbs1 / ntbs1 2digit mul
            Triple("ThreeByTwo_OneQuotient : 107 ÷ 12", 107 to 12, listOf("8", "16", "9", "1", "0", "1")),
            // hbs1 tbs1 double borrow 2digit mul
            Triple("ThreeByTwo_OneQuotient : 104 ÷ 12", 104 to 12, listOf("8", "16", "9", "0", "9", "8")),
            // 2digit mul remain 0
            Triple("ThreeByTwo_OneQuotient : 150 ÷ 30", 150 to 30, listOf("5", "0", "15", "0")),
            // skip hbs1
            Triple("ThreeByTwo_OneQuotient : 109 ÷ 11", 109 to 11, listOf("9", "9", "9", "0", "1")),
            // skip hbs1
            Triple("ThreeByTwo_OneQuotient : 101 ÷ 51", 101 to 51, listOf("1", "1", "5", "0", "5")),
            // no borrow 3digit mul 2digit remain
            Triple("ThreeByTwo_OneQuotient : 229 ÷ 23", 229 to 23, listOf("9", "27", "20", "2", "2")),
            // tbs1 1digit remain
            Triple("ThreeByTwo_OneQuotient : 214 ÷ 23", 214 to 23, listOf("9", "27", "20", "0", "7")),

            // 공통 경계
            // 몫이 정확히 10 (tens=1, ones=0)
            // 110 ÷ 11 = 10 R0 (QuotientOnes=0 입력/표시 경계)
            Triple("ThreeByTwo : 110 ÷ 11", 110 to 11, listOf("1", "1", "1", "0", "0", "0")),
            //제수의 1의 자리가 0 (×10, ×20류) 다양화
            //240 ÷ 20 = 12 R0 (네 목록엔 150÷30, 710÷60 있음. 12형도 추가)
            Triple("ThreeByTwo : 240 ÷ 20", 240 to 20, listOf("1", "0", "2", "4", "0", "2", "0", "4", "0")),
            //605 ÷ 20 = 30 R5 (subtract1/2에서 borrow 패턴 확인)
            Triple("ThreeByTwo : 605 ÷ 20", 605 to 20, listOf("3", "0", "6", "0", "5", "0")),
            // 최댓값 근처(큰 나머지)
            // 999 ÷ 99 = 10 R9 (tens quotient, ones=0, R 한 자리)
            Triple("ThreeByTwo : 999 ÷ 99", 999 to 99, listOf("1", "9", "9", "0", "9", "0")),
            //Dividend == 제수×NN + 0 (완전 배수)
            //396 ÷ 33 = 12 R0 (CM1, NBS1 또는 변형 분기 점검)
            Triple("ThreeByTwo : 396 ÷ 33", 396 to 33, listOf("1", "3", "3", "6", "6", "2", "6", "6", "0")),
            // 케이스 보강
            // 402 ÷ 27 = 14 R24 (1차 뺄셈 후 3자리 상태/bring-down 처리)
            Triple("ThreeByTwo : 402 ÷ 27", 402 to 27, listOf("1", "7", "2", "3", "3", "1", "2", "4", "28", "10", "2", "4", "2")),
            // 유사 패턴 변형으로 308 ÷ 11 = 28 R0 추가 (Subtract1에서 0 포함 경계)
            Triple("ThreeByTwo : 308 ÷ 11", 308 to 11, listOf("2", "2", "2", "2", "8", "8", "8", "8", "8", "0")),
            //Double-borrow/Skip-borrow 변형 구멍 메우기
            //Sub1 double-borrow 후 Sub2 skip-borrow
            //342 ÷ 38 = 9 R0 (예: Sub1에서 hbs 두 번, Sub2는 (…)=1 조건으로 skip 확인)
            Triple("ThreeByTwo : 342 ÷ 38", 342 to 38, listOf("9", "72", "34", "0")),
            //Sub1 skip-borrow 후 Sub2 double-borrow
            //418 ÷ 41 = 10 R8 (Sub1는 1 경계로 skip, Sub2에서 tens→ones 연속 borrow)
            Triple("ThreeByTwo : 418 ÷ 41", 418 to 41, listOf("1", "1", "4", "0", "8", "0")),
            //Mul1Hundreds가 10 경계(= carry-in 정확 처리)
            //425 ÷ 25 = 17 R0 (1×25=25, 7×25=175 경계들—Mul1/2 carry 흐름 확인)
            Triple("ThreeByTwo : 425 ÷ 25", 425 to 25, listOf("1", "5", "2", "3", "7", "1", "5", "7", "35", "17", "0")),
            //Mul2가 정확히 100/90/80 경계
            //284 ÷ 28 = 10 R4 (Mul2=0/100 경계, ones=0)
            Triple("ThreeByTwo : 284 ÷ 28", 284 to 28, listOf("1", "8", "2", "0", "4", "0")),
            //368 ÷ 32 = 11 R16 (Mul2=352; carry/borrow 합성)
            Triple("ThreeByTwo : 368 ÷ 32", 368 to 32, listOf("1", "2", "3", "4", "8", "1", "2", "3", "6", "1")),

            //Remainder 다양화(0/1-digit/2-digit)
            //현재 패턴별 R0 누락 보강
            //672 ÷ 32 = 21 R0 (CM1, NBS1, NCM2, NBS2 류 교차 확인)
            Triple("ThreeByTwo : 672 ÷ 32", 672 to 32, listOf("2", "4", "6", "3", "2", "1", "2", "3", "0")),

            //1자리 나머지만 남는 케이스
            //815 ÷ 39 = 20 R35는 2자리라서, 1자리 전용으로
            Triple("ThreeByTwo : 815 ÷ 39", 815 to 39, listOf("2", "18", "7", "7", "3", "5", "0")),
            //731 ÷ 36 = 20 R11 대신 731 ÷ 35 = 20 R31 등 조정해서 R 한 자리 예시도 확보
            //2자리 나머지인데 Sub2에서 borrow 없음
            Triple("ThreeByTwo : 731 ÷ 36", 731 to 36, listOf("2", "12", "7", "1", "1", "0")),
            //568 ÷ 26 = 21 R22
            Triple("ThreeByTwo : 568 ÷ 26", 568 to 26, listOf("2", "12", "5", "4", "8", "1", "6", "2", "2", "2")),

            //스킵 단계 UI/StrikeThrough 정합성 전용
            //Sub1 skip 시 strikeThrough 미표시 + highlight 최소화
            //209 ÷ 19 = 11 R0 (skip 발생 + R0)
            Triple("ThreeByTwo : 209 ÷ 19", 209 to 19, listOf("1", "9", "1", "1", "1", "9", "1", "9", "1", "0")),
            //Sub2 skip 시 borrowed10 미표시 보증
            //840 ÷ 70 = 12 R0
            Triple("ThreeByTwo : 840 ÷ 70", 840 to 70, listOf("1", "0", "7", "4", "1", "0", "2", "0", "14", "0")),
            )

        for ((name, pair, inputs) in cases) {
            val (dividend, divisor) = pair
            val expectedPattern = when {
                dividend.toString().length == 2 && divisor.toString().length == 1 -> DivisionPatternV2.TwoByOne
                dividend.toString().length == 2 && divisor.toString().length == 2 -> DivisionPatternV2.TwoByTwo
                dividend.toString().length == 3 && divisor.toString().length == 2 -> DivisionPatternV2.ThreeByTwo
                else -> error("지원하지 않는 패턴")
            }


            viewModel.startNewProblem(dividend, divisor)
            var state = viewModel.uiOrThrow()

            val actualPattern = state.pattern   // ← 여기로 변경
            assertEquals("$name: 대분류 패턴 불일치!", expectedPattern, actualPattern)


            // 입력값을 순차적으로 제출하며 단계별 정답 확인
            for ((i, input) in inputs.withIndex()) {

                viewModel.submitInput(input)
                state = viewModel.uiOrThrow()

                // 마지막 입력이면 feedback이 있어야 한다!
                if (i == inputs.lastIndex) {
                    assertEquals("$name: 마지막 입력 후 feedback 불일치", "정답입니다!", state.feedback)
                } else {
                    assertTrue("$name: ${i + 1}번째 입력 오답! (${input})", state.feedback == null)
                }
            }

//            val state = viewModel.uiState.value
            assertTrue("$name: Complete phase 미도달", state.isCompleted)
        }
    }
}
