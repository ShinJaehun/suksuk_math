package com.shinjaehun.suksuk.division.three_by_two

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.division.divisionCaseV2
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestV2ThreeByTwo {
    @get:Rule
    val composeTestRule = createComposeRule()

    // === ThreeByTwo ===
// 610 ÷ 13 = 46 ... 12
// Carry1, Borrow1, Carry2, Borrow2, ThreeByTwo_TensQuotient_CM1_BS1_CM2_BS2_2DR
    @Test
    fun testPattern_610_div_13() =
        composeTestRule.divisionCaseV2(
            610, 13,
            listOf("4", "12", "5", "5", "9", "0", "6", "18", "7", "8", "2", "1")
        )

    // 624 ÷ 14 = 44 ... 8
// Carry1, Borrow1, Carry2, Borrow2, ThreeByTwo_TensQuotient_CM1_BS1_CM2_BS2_1DR
    @Test
    fun testPattern_624_div_14() =
        composeTestRule.divisionCaseV2(
            624, 14,
            listOf("4", "16", "5", "5", "6", "4", "4", "16", "5", "5", "8")
        )

    // 732 ÷ 16 = 45 ... 12
// Carry1, Borrow1, Carry2, No Borrow2, ThreeByTwo_TensQuotient_CM1_BS1_CM2_NBS2_2DR
    @Test
    fun testPattern_732_div_16() =
        composeTestRule.divisionCaseV2(
            732, 16,
            listOf("4", "24", "6", "6", "9", "2", "5", "30", "8", "2", "1")
        )

    // 654 ÷ 14 = 46 ... 10
// Carry1, Borrow1, Carry2, No Borrow2, ThreeByTwo_TensQuotient_CM1_BS1_CM2_NBS2_2DR
    @Test
    fun testPattern_654_div_14() =
        composeTestRule.divisionCaseV2(
            654, 14,
            listOf("4", "16", "5", "5", "9", "4", "6", "24", "8", "0", "1")
        )

    // 632 ÷ 14 = 45 ... 2
// Carry1, Borrow1, Carry2, No Borrow2, ThreeByTwo_TensQuotient_CM1_BS1_CM2_NBS2_1DR
    @Test
    fun testPattern_632_div_14() =
        composeTestRule.divisionCaseV2(
            632, 14,
            listOf("4", "16", "5", "5", "7", "2", "5", "20", "7", "2")
        )

    // 700 ÷ 13 = 53 ... 11
// Carry1, Borrow1, No Carry2, Borrow2, ThreeByTwo_TensQuotient_CM1_BS1_NCM2_BS2_2DR
    @Test
    fun testPattern_700_div_13() =
        composeTestRule.divisionCaseV2(
            700, 13,
            listOf("5", "15", "6", "6", "5", "0", "3", "9", "3", "4", "1", "1")
        )

    // 800 ÷ 13 = 61 ... 7
// Carry1, Borrow1, No Carry2, Borrow2, ThreeByTwo_TensQuotient_CM1_BS1_NCM2_BS2_1DR
    @Test
    fun testPattern_800_div_13() =
        composeTestRule.divisionCaseV2(
            800, 13,
            listOf("6", "18", "7", "7", "2", "0", "1", "3", "1", "1", "7")
        )

    // 619 ÷ 29 = 21 ... 10
// Carry1, Borrow1, No Carry2, No Borrow2, ThreeByTwo_TensQuotient_CM1_BS1_NCM2_NBS2_2DR
    @Test
    fun testPattern_619_div_29() =
        composeTestRule.divisionCaseV2(
            619, 29,
            listOf("2", "18", "5", "5", "3", "9", "1", "9", "2", "0", "1")
        )

    // 819 ÷ 39 = 21 ... 0
// Carry1, Borrow1, No Carry2, No Borrow2, ThreeByTwo_TensQuotient_CM1_BS1_NCM2_NBS2_1DR
    @Test
    fun testPattern_819_div_39() =
        composeTestRule.divisionCaseV2(
            819, 39,
            listOf("2", "18", "7", "7", "3", "9", "1", "9", "3", "0")
        )

    // 670 ÷ 12 = 55 ... 10
// Carry1, No Borrow1, Carry2, Borrow2, ThreeByTwo_TensQuotient_CM1_NBS1_CM2_BS2_2DR
    @Test
    fun testPattern_670_div_12() =
        composeTestRule.divisionCaseV2(
            670, 12,
            listOf("5", "10", "6", "7", "0", "5", "10", "6", "0", "1")
        )

    // 680 ÷ 12 = 56 ... 8
// Carry1, No Borrow1, Carry2, Borrow2, ThreeByTwo_TensQuotient_CM1_NBS1_CM2_BS2_1DR
    @Test
    fun testPattern_680_div_12() =
        composeTestRule.divisionCaseV2(
            680, 12,
            listOf("5", "10", "6", "8", "0", "6", "12", "7", "7", "8")
        )

    // 595 ÷ 13 = 45 ... 10
// Carry1, No Borrow1, Carry2, No Borrow2, ThreeByTwo_TensQuotient_CM1_NBS1_CM2_NBS2_2DR
    @Test
    fun testPattern_595_div_13() =
        composeTestRule.divisionCaseV2(
            595, 13,
            listOf("4", "12", "5", "7", "5", "5", "15", "6", "0", "1")
        )

    // 660 ÷ 12 = 55 ... 0
// Carry1, No Borrow1, Carry2, No Borrow2, ThreeByTwo_TensQuotient_CM1_NBS1_CM2_NBS2_1DR
    @Test
    fun testPattern_660_div_12() =
        composeTestRule.divisionCaseV2(
            660, 12,
            listOf("5", "10", "6", "6", "0", "5", "10", "6", "0")
        )

    // 672 ÷ 12 = 56 ... 0
// Carry1, No Borrow1, Carry2, No Borrow2, ThreeByTwo_TensQuotient_CM1_NBS1_CM2_NBS2_1DR
    @Test
    fun testPattern_672_div_12() =
        composeTestRule.divisionCaseV2(
            672, 12,
            listOf("5", "10", "6", "7", "2", "6", "12", "7", "0")
        )

    // 460 ÷ 14 = 32 ... 12
// Carry1, No Borrow1, No Carry2, Borrow2, ThreeByTwo_TensQuotient_CM1_NBS1_NCM2_BS2_2DR
    @Test
    fun testPattern_460_div_14() =
        composeTestRule.divisionCaseV2(
            460, 14,
            listOf("3", "12", "4", "4", "0", "2", "8", "2", "3", "2", "1")
        )

    // 320 ÷ 15 = 21 ... 5
// Carry1, No Borrow1, No Carry2, Borrow2, ThreeByTwo_TensQuotient_CM1_NBS1_NCM2_BS2_1DR
    @Test
    fun testPattern_320_div_15() =
        composeTestRule.divisionCaseV2(
            320, 15,
            listOf("2", "10", "3", "2", "0", "1", "5", "1", "1", "5")
        )

    // 475 ÷ 15 = 31 ... 10
// Carry1, No Borrow1, No Carry2, No Borrow2, ThreeByTwo_TensQuotient_CM1_NBS1_NCM2_NBS2_2DR
    @Test
    fun testPattern_475_div_15() =
        composeTestRule.divisionCaseV2(
            475, 15,
            listOf("3", "15", "4", "2", "5", "1", "5", "1", "0", "1")
        )

    // 446 ÷ 14 = 31 ... 12
// Carry1, No Borrow1, No Carry2, No Borrow2, ThreeByTwo_TensQuotient_CM1_NBS1_NCM2_NBS2_2DR
    @Test
    fun testPattern_446_div_14() =
        composeTestRule.divisionCaseV2(
            446, 14,
            listOf("3", "12", "4", "2", "6", "1", "4", "1", "2", "1")
        )

    // 568 ÷ 27 = 21 ... 1
// Carry1, No Borrow1, No Carry2, No Borrow2, ThreeByTwo_TensQuotient_CM1_NBS1_NCM2_NBS2_1DR
    @Test
    fun testPattern_568_div_27() =
        composeTestRule.divisionCaseV2(
            568, 27,
            listOf("2", "14", "5", "2", "8", "1", "7", "2", "1")
        )

    // 220 ÷ 13 = 16 ... 12
// No Carry1, Borrow1, Carry2, Borrow2, ThreeByTwo_TensQuotient_NCM1_BS1_CM2_BS2_2DR
    @Test
    fun testPattern_220_div_13() =
        composeTestRule.divisionCaseV2(
            220, 13,
            listOf("1", "3", "1", "1", "9", "0", "6", "18", "7", "8", "2", "1")
        )

    // 200 ÷ 16 = 12 ... 8
// No Carry1, Borrow1, Carry2, Borrow2, ThreeByTwo_TensQuotient_NCM1_BS1_CM2_BS2_1DR
    @Test
    fun testPattern_200_div_16() =
        composeTestRule.divisionCaseV2(
            200, 16,
            listOf("1", "6", "1", "1", "4", "0", "2", "12", "3", "3", "8")
        )

    // 202 ÷ 12 = 16 ... 10
// No Carry1, Borrow1, Carry2, No Borrow2, ThreeByTwo_TensQuotient_NCM1_BS1_CM2_NBS2_2DR
    @Test
    fun testPattern_202_div_12() =
        composeTestRule.divisionCaseV2(
            202, 12,
            listOf("1", "2", "1", "1", "8", "2", "6", "12", "7", "0", "1")
        )

    // 204 ÷ 12 = 17 ... 0
// No Carry1, Borrow1, Carry2, No Borrow2, ThreeByTwo_TensQuotient_NCM1_BS1_CM2_NBS2_1DR
    @Test
    fun testPattern_204_div_12() =
        composeTestRule.divisionCaseV2(
            204, 12,
            listOf("1", "2", "1", "1", "8", "4", "7", "14", "8", "0")
        )

    // 710 ÷ 21 = 33 ... 17
// No Carry1, Borrow1, No Carry2, Borrow2, ThreeByTwo_TensQuotient_NCM1_BS1_NCM2_BS2_2DR
    @Test
    fun testPattern_710_div_21() =
        composeTestRule.divisionCaseV2(
            710, 21,
            listOf("3", "3", "6", "6", "8", "0", "3", "3", "6", "7", "7", "1")
        )

    // 911 ÷ 43 = 21 ... 8
// No Carry1, Borrow1, No Carry2, Borrow2, ThreeByTwo_TensQuotient_NCM1_BS1_NCM2_BS2_1DR
    @Test
    fun testPattern_911_div_43() =
        composeTestRule.divisionCaseV2(
            911, 43,
            listOf("2", "6", "8", "8", "5", "1", "1", "3", "4", "4", "8")
        )

    // 604 ÷ 11 = 54 ... 10
// No Carry1, Borrow1, No Carry2, No Borrow2, ThreeByTwo_TensQuotient_NCM1_BS1_NCM2_NBS2_2DR
    @Test
    fun testPattern_604_div_11() =
        composeTestRule.divisionCaseV2(
            604, 11,
            listOf("5", "5", "5", "5", "5", "4", "4", "4", "4", "0", "1")
        )

    // 517 ÷ 47 = 11 ... 0
// No Carry1, Borrow1, No Carry2, No Borrow2, ThreeByTwo_TensQuotient_NCM1_BS1_NCM2_NBS2_1DR
    @Test
    fun testPattern_517_div_47() =
        composeTestRule.divisionCaseV2(
            517, 47,
            listOf("1", "7", "4", "4", "4", "7", "1", "7", "4", "0")
        )

// (불가 주석 유지) ThreeByTwo_TensQuotient_NCM1_NBS1_CM2_BS2_2DR 불가

    // 190 ÷ 13 = 14 ... 8
// No Carry1, No Borrow1, Carry2, Borrow2, ThreeByTwo_TensQuotient_NCM1_NBS1_CM2_BS2_1DR
    @Test
    fun testPattern_190_div_13() =
        composeTestRule.divisionCaseV2(
            190, 13,
            listOf("1", "3", "1", "6", "0", "4", "12", "5", "5", "8")
        )

    // 181 ÷ 12 = 15 ... 1
// No Carry1, No Borrow1, Carry2, No Borrow2, ThreeByTwo_TensQuotient_NCM1_NBS1_CM2_NBS2_1DR
    @Test
    fun testPattern_181_div_12() =
        composeTestRule.divisionCaseV2(
            181, 12,
            listOf("1", "2", "1", "6", "1", "5", "10", "6", "1")
        )

    // 190 ÷ 12 = 15 ... 10
// No Carry1, No Borrow1, Carry2, No Borrow2, ThreeByTwo_TensQuotient_NCM1_NBS1_CM2_NBS2_2DR
    @Test
    fun testPattern_190_div_12() =
        composeTestRule.divisionCaseV2(
            190, 12,
            listOf("1", "2", "1", "7", "0", "5", "10", "6", "0", "1")
        )

    // 180 ÷ 14 = 12 ... 12
// No Carry1, No Borrow1, No Carry2, Borrow2, ThreeByTwo_TensQuotient_NCM1_NBS1_NCM2_BS2_2DR
    @Test
    fun testPattern_180_div_14() =
        composeTestRule.divisionCaseV2(
            180, 14,
            listOf("1", "4", "1", "4", "0", "2", "8", "2", "3", "2", "1")
        )

    // 280 ÷ 25 = 11 ... 5
// No Carry1, No Borrow1, No Carry2, Borrow2, ThreeByTwo_TensQuotient_NCM1_NBS1_NCM2_BS2_1DR
    @Test
    fun testPattern_280_div_25() =
        composeTestRule.divisionCaseV2(
            280, 25,
            listOf("1", "5", "2", "3", "0", "1", "5", "2", "2", "5")
        )

    // 131 ÷ 11 = 11 ... 10
// No Carry1, No Borrow1, No Carry2, No Borrow2, ThreeByTwo_TensQuotient_NCM1_NBS1_NCM2_NBS2_2DR
    @Test
    fun testPattern_131_div_11() =
        composeTestRule.divisionCaseV2(
            131, 11,
            listOf("1", "1", "1", "2", "1", "1", "1", "1", "0", "1")
        )

    // 682 ÷ 31 = 22 ... 0
// No Carry1, No Borrow1, No Carry2, No Borrow2, ThreeByTwo_TensQuotient_NCM1_NBS1_NCM2_NBS2_1DR
    @Test
    fun testPattern_682_div_31() =
        composeTestRule.divisionCaseV2(
            682, 31,
            listOf("2", "2", "6", "6", "2", "2", "2", "6", "0")
        )

    // 610 ÷ 15 = 40 ... 10
// Carry1, Borrow1, Skip Multiply2/Subtract2, ThreeByTwo_TensQuotient_CM1_BS1_SkipMul2Sub2
    @Test
    fun testPattern_610_div_15() =
        composeTestRule.divisionCaseV2(
            610, 15,
            listOf("4", "20", "6", "1", "0", "0")
        )


    // 236 ÷ 13 = 18 ... 2
// ThreeByTwo_TensQuotient_3DS1_3DM2
    @Test
    fun testPattern_236_div_13() =
        composeTestRule.divisionCaseV2(
            236, 13,
            listOf("1", "3", "1", "0", "1", "6", "8", "24", "10", "2")
        )

    // 419 ÷ 21 = 19 ... 20
// ThreeByTwo_TensQuotient_3DS1_3DM2 (hbs2, ntbs2)
    @Test
    fun testPattern_419_div_21() =
        composeTestRule.divisionCaseV2(
            419, 21,
            listOf("1", "1", "2", "0", "2", "9", "9", "9", "18", "0", "1", "2")
        )

    // 230 ÷ 12 = 19 ... 2
// ThreeByTwo_TensQuotient_3DS1_3DM2 (nhbs2, tbs2)
    @Test
    fun testPattern_230_div_12() =
        composeTestRule.divisionCaseV2(
            230, 12,
            listOf("1", "2", "1", "1", "1", "0", "9", "18", "10", "0", "2")
        )

    // 210 ÷ 11 = 19 ... 1
// ThreeByTwo_TensQuotient_3DS1_3DM2 (hbs2 + tbs2, double borrow)
    @Test
    fun testPattern_210_div_11() =
        composeTestRule.divisionCaseV2(
            210, 11,
            listOf("1", "1", "1", "0", "1", "0", "9", "9", "9", "0", "9", "1")
        )

    // 710 ÷ 60 = 11 ... 50
// ThreeByTwo_TensQuotient_3DS1_2DM2 : Sub2 110-60, skip borrow from sub1Hundreds
    @Test
    fun testPattern_710_div_60() =
        composeTestRule.divisionCaseV2(
            710, 60,
            listOf("1", "0", "6", "1", "1", "0", "1", "0", "6", "0", "0", "5")
        )

    // 219 ÷ 11 = 19 ... 10
// ThreeByTwo_TensQuotient_3DS1_3DM2
    @Test
    fun testPattern_219_div_11() =
        composeTestRule.divisionCaseV2(
            219, 11,
            listOf("1", "1", "1", "0", "1", "9", "9", "9", "9", "0", "1")
        )

    // nhbs1 ntbs1
    @Test fun test_ThreeByTwo_OneQuotient_198_div_22() =
        composeTestRule.divisionCaseV2(198, 22, listOf("9", "18", "19", "0"))

    // nhbs1 tbs1
    @Test fun test_ThreeByTwo_OneQuotient_230_div_27() =
        composeTestRule.divisionCaseV2(230, 27, listOf("8", "56", "21", "2", "4", "1"))

    // hbs1 ntbs1
    @Test fun test_ThreeByTwo_OneQuotient_205_div_23() =
        composeTestRule.divisionCaseV2(205, 23, listOf("8", "24", "18", "1", "1", "2"))

    // hbs1 tbs1 double borrow
    @Test fun test_ThreeByTwo_OneQuotient_342_div_49() =
        composeTestRule.divisionCaseV2(342, 49, listOf("6", "54", "29", "2", "9", "8", "4"))

    // nhbs1 tbs1 2digit mul
    @Test fun test_ThreeByTwo_OneQuotient_234_div_25() =
        composeTestRule.divisionCaseV2(234, 25, listOf("9", "45", "22", "2", "9"))

    // hbs1 ntbs1 2digit mul
    @Test fun test_ThreeByTwo_OneQuotient_315_div_42() =
        composeTestRule.divisionCaseV2(315, 42, listOf("7", "14", "29", "1", "2", "2"))

    // hbs1 ntbs1 2digit mul
    @Test fun test_ThreeByTwo_OneQuotient_107_div_12() =
        composeTestRule.divisionCaseV2(107, 12, listOf("8", "16", "9", "1", "0", "1"))

    // hbs1 tbs1 double borrow 2digit mul
    @Test fun test_ThreeByTwo_OneQuotient_104_div_12() =
        composeTestRule.divisionCaseV2(104, 12, listOf("8", "16", "9", "0", "9", "8"))

    // 2digit mul remain 0
    @Test fun test_ThreeByTwo_OneQuotient_150_div_30() =
        composeTestRule.divisionCaseV2(150, 30, listOf("5", "0", "15", "0"))

    // skip hbs1
    @Test fun test_ThreeByTwo_OneQuotient_109_div_11() =
        composeTestRule.divisionCaseV2(109, 11, listOf("9", "9", "9", "0", "1"))

    // skip hbs1
    @Test fun test_ThreeByTwo_OneQuotient_101_div_51() =
        composeTestRule.divisionCaseV2(101, 51, listOf("1", "1", "5", "0", "5"))

    // no borrow 3digit mul 2digit remain
    @Test fun test_ThreeByTwo_OneQuotient_229_div_23() =
        composeTestRule.divisionCaseV2(229, 23, listOf("9", "27", "20", "2", "2"))

    // tbs1 1digit remain
    @Test fun test_ThreeByTwo_OneQuotient_214_div_23() =
        composeTestRule.divisionCaseV2(214, 23, listOf("9", "27", "20", "0", "7"))

    // ========== 공통 경계 ==========
    @Test fun test_ThreeByTwo_110_div_11() =
        composeTestRule.divisionCaseV2(110, 11, listOf("1", "1", "1", "0", "0", "0"))

    @Test fun test_ThreeByTwo_240_div_20() =
        composeTestRule.divisionCaseV2(240, 20, listOf("1", "0", "2", "4", "0", "2", "0", "4", "0"))

    @Test fun test_ThreeByTwo_605_div_20() =
        composeTestRule.divisionCaseV2(605, 20, listOf("3", "0", "6", "0", "5", "0"))

    @Test fun test_ThreeByTwo_999_div_99() =
        composeTestRule.divisionCaseV2(999, 99, listOf("1", "9", "9", "0", "9", "0"))

    @Test fun test_ThreeByTwo_396_div_33() =
        composeTestRule.divisionCaseV2(396, 33, listOf("1", "3", "3", "6", "6", "2", "6", "6", "0"))

    @Test fun test_ThreeByTwo_402_div_27() =
        composeTestRule.divisionCaseV2(402, 27, listOf("1", "7", "2", "3", "3", "1", "2", "4", "28", "10", "2", "4", "2"))

    @Test fun test_ThreeByTwo_308_div_11() =
        composeTestRule.divisionCaseV2(308, 11, listOf("2", "2", "2", "2", "8", "8", "8", "8", "8", "0"))

    // ========== Double-borrow / Skip-borrow 변형 ==========
    @Test fun test_ThreeByTwo_342_div_38() =
        composeTestRule.divisionCaseV2(342, 38, listOf("9", "72", "34", "0"))

    @Test fun test_ThreeByTwo_418_div_41() =
        composeTestRule.divisionCaseV2(418, 41, listOf("1", "1", "4", "0", "8", "0"))

    // ========== Mul 경계 ==========
    @Test fun test_ThreeByTwo_425_div_25() =
        composeTestRule.divisionCaseV2(425, 25, listOf("1", "5", "2", "3", "7", "1", "5", "7", "35", "17", "0"))

    @Test fun test_ThreeByTwo_284_div_28() =
        composeTestRule.divisionCaseV2(284, 28, listOf("1", "8", "2", "0", "4", "0"))

    @Test fun test_ThreeByTwo_368_div_32() =
        composeTestRule.divisionCaseV2(368, 32, listOf("1", "2", "3", "4", "8", "1", "2", "3", "6", "1"))

    // ========== Remainder 다양화 ==========
    @Test fun test_ThreeByTwo_672_div_32() =
        composeTestRule.divisionCaseV2(672, 32, listOf("2", "4", "6", "3", "2", "1", "2", "3", "0"))

    @Test fun test_ThreeByTwo_815_div_39() =
        composeTestRule.divisionCaseV2(815, 39, listOf("2", "18", "7", "7", "3", "5", "0"))

    @Test fun test_ThreeByTwo_731_div_36() =
        composeTestRule.divisionCaseV2(731, 36, listOf("2", "12", "7", "1", "1", "0"))

    @Test fun test_ThreeByTwo_568_div_26() =
        composeTestRule.divisionCaseV2(568, 26, listOf("2", "12", "5", "4", "8", "1", "6", "2", "2", "2"))

    // ========== 스킵 단계 UI/StrikeThrough 정합성 ==========
    @Test fun test_ThreeByTwo_209_div_19() =
        composeTestRule.divisionCaseV2(209, 19, listOf("1", "9", "1", "1", "1", "9", "1", "9", "1", "0"))

    @Test fun test_ThreeByTwo_840_div_70() =
        composeTestRule.divisionCaseV2(840, 70, listOf("1", "0", "7", "4", "1", "0", "2", "0", "14", "0"))

}