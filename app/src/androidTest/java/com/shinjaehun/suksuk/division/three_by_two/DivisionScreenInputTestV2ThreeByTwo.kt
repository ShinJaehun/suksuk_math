package com.shinjaehun.suksuk.division.three_by_two

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.division.divisionCaseV2
import com.shinjaehun.suksuk.division.legacy.divisionCase
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestV2ThreeByTwo {
    @get:Rule
    val composeTestRule = createComposeRule()

    // === ThreeByTwo ===

    // 131 ÷ 11 = 11 ... 10
    // No Carry, No Borrow, No Carry, No Borrow, 2DR ThreeByTwo_TensQuotient_NCM1_NBS1_NCM2_NBS2_2DR
    @Test
    fun testPattern_131_div_11() =
        composeTestRule.divisionCaseV2(
            131, 11,
            listOf("1", "1", "1", "2", "1", "1", "1", "1", "0", "1")
        )

    // 682 ÷ 31 = 22 ... 0
    // No Carry, No Borrow, No Carry, No Borrow, 1DR ThreeByTwo_TensQuotient_NCM1_NBS1_NCM2_NBS2_1DR
    @Test
    fun testPattern_682_div_31() =
        composeTestRule.divisionCaseV2(
            682, 31,
            listOf("2", "2", "6", "6", "2", "2", "2", "6", "0")
        )

    // 604 ÷ 11 = 54 ... 10
    // No Carry 1, Borrow 1, No Carry 2, 2DR ThreeByTwo_TensQuotient_NCM1_BS1_NCM2_NBS2_2DR
    @Test
    fun testPattern_604_div_11() =
        composeTestRule.divisionCaseV2(
            604, 11,
            listOf("5", "5", "5", "5", "5", "4", "4", "4", "4", "0", "1")
        )

    // 517 ÷ 47 = 11 ... 0
    // No Carry 1, Borrow 1, No Carry 2, 1DR ThreeByTwo_TensQuotient_NCM1_BS1_NCM2_NBS2_1DR
    @Test
    fun testPattern_517_div_47() =
        composeTestRule.divisionCaseV2(
            517, 47,
            listOf("1", "7", "4", "4", "4", "7", "1", "7", "4", "0")
        )

    // 180 ÷ 14 = 12 ... 2
    // No Carry 1, No Borrow 1, No Carry 2, Borrow 2, ThreeByTwo_TensQuotient_NCM1_NBS1_NCM2_BS2_2DR
    @Test
    fun testPattern_180_div_14() =
        composeTestRule.divisionCaseV2(
            180, 14,
            listOf("1", "4", "1", "4", "0", "2", "8", "2", "3", "2", "1")
        )

    // 280 ÷ 25 = 11 ... 5
    // No Carry 1, No Borrow 1, No Carry 2, Borrow 2, ThreeByTwo_TensQuotient_NCM1_NBS1_NCM2_BS2_1DR
    @Test
    fun testPattern_280_div_25() =
        composeTestRule.divisionCaseV2(
            280, 25,
            listOf("1", "5", "2", "3", "0", "1", "5", "2", "2", "5")
        )

    // 710 ÷ 21 = 33 ... 17
    // No Carry 1, Borrow 1, No Carry 2, Borrow 2, 2자리 나머지(2DR) ThreeByTwo_TensQuotient_NCM1_BS1_NCM2_BS2_2DR
    @Test
    fun testPattern_710_div_21() =
        composeTestRule.divisionCaseV2(
            710, 21,
            listOf("3", "3", "6", "6", "8", "0", "3", "3", "6", "7", "7", "1")
        )

    // 911 ÷ 43 = 21 ... 8
    // No Carry 1, Borrow 1, No Carry 2, Borrow 2, 1자리 나머지(1DR) ThreeByTwo_TensQuotient_NCM1_BS1_NCM2_BS2_1DR
    @Test
    fun testPattern_911_div_43() =
        composeTestRule.divisionCaseV2(
            911, 43,
            listOf("2", "6", "8", "8", "5", "1", "1", "3", "4", "4", "8")
        )

    // 446 ÷ 14 = 31 ... 12
    // Carry1, No Borrow1, No Carry2, No Borrow2, (ThreeByTwo_TensQuotient_CM1_NBS1_NCM2_NBS2_2DR)
    @Test
    fun testPattern_446_div_14() =
        composeTestRule.divisionCaseV2(
            446, 14,
            listOf("3", "12", "4", "2", "6", "1", "4", "1", "2", "1")
        )

    // 568 ÷ 27 = 21 ... 1
    // Carry1, No Borrow1, No Carry2, No Borrow2, (ThreeByTwo_TensQuotient_CM1_NBS1_NCM2_NBS2_1DR)
    @Test
    fun testPattern_568_div_27() =
        composeTestRule.divisionCaseV2(
            568, 27,
            listOf("2", "14", "5", "2", "8", "1", "7", "2", "1")
        )

    // 619 ÷ 29 = 21 ... 10
    // Carry1, Borrow1, No Carry2, No Borrow2, (ThreeByTwo_TensQuotient_CM1_BS1_NCM2_NBS2_2DR)
    @Test
    fun testPattern_619_div_29() =
        composeTestRule.divisionCaseV2(
            619, 29,
            listOf("2", "18", "5", "5", "3", "9", "1", "9", "2", "0", "1")
        )

    // 819 ÷ 39 = 21 ... 0
    // Carry1, Borrow1, No Carry2, No Borrow2, (ThreeByTwo_TensQuotient_CM1_BS1_NCM2_NBS2_1DR)
    @Test
    fun testPattern_819_div_39() =
        composeTestRule.divisionCaseV2(
            819, 39,
            listOf("2", "18", "7", "7", "3", "9", "1", "9", "3", "0")
        )

    // 654 ÷ 14 = 46 ... 10
    // Carry1, Borrow1, Carry2, No Borrow2, (ThreeByTwo_TensQuotient_CM1_BS1_CM2_NBS2_2DR)
    @Test
    fun testPattern_654_div_14() =
        composeTestRule.divisionCaseV2(
            654, 14,
            listOf("4", "16", "5", "5", "9", "4", "6", "24", "8", "0", "1")
        )

    // 632 ÷ 14 = 45 ... 2
    // Carry1, Borrow1, Carry2, No Borrow2, (ThreeByTwo_TensQuotient_CM1_BS1_CM2_NBS2_1DR)
    @Test
    fun testPattern_632_div_14() =
        composeTestRule.divisionCaseV2(
            632, 14,
            listOf("4", "16", "5", "5", "7", "2", "5", "20", "7", "2")
        )

    // 610 ÷ 13 = 46 ... 12
    // Carry1, Borrow1, Carry2, Borrow2, (ThreeByTwo_TensQuotient_CM1_BS1_CM2_BS2_2DR)
    @Test
    fun testPattern_610_div_13() =
        composeTestRule.divisionCaseV2(
            610, 13,
            listOf("4", "12", "5", "5", "9", "0", "6", "18", "7", "8", "2", "1")
        )

    // 624 ÷ 14 = 44 ... 8
    // Carry1, Borrow1, Carry2, Borrow2, (ThreeByTwo_TensQuotient_CM1_BS1_CM2_BS2_1DR)
    @Test
    fun testPattern_624_div_14() =
        composeTestRule.divisionCaseV2(
            624, 14,
            listOf("4", "16", "5", "5", "6", "4", "4", "16", "5", "5", "8")
        )

}