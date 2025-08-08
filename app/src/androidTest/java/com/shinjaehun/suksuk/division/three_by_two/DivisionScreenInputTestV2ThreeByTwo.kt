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

    // 682 ÷ 31 = 22 ... 0
    // No Carry, No Borrow, No Carry, No Borrow, 1자리 나머지(1DR)
    @Test
    fun testPattern_682_div_31() =
        composeTestRule.divisionCaseV2(
            682, 31,
            listOf("2", "2", "6", "6", "2", "2", "2", "6", "0")
        )

    // 604 ÷ 11 = 54 ... 10
    // No Carry 1, Borrow 1, No Carry 2, 2자리 나머지(2DR)
    @Test
    fun testPattern_604_div_11() =
        composeTestRule.divisionCaseV2(
            604, 11,
            listOf("5", "5", "5", "5", "5", "4", "4", "4", "4", "0", "1")
        )

    // 517 ÷ 47 = 11 ... 0
    // No Carry 1, Borrow 1, No Carry 2, 1자리 나머지(1DR)
    @Test
    fun testPattern_517_div_47() =
        composeTestRule.divisionCaseV2(
            517, 47,
            listOf("1", "7", "4", "4", "4", "7", "1", "7", "4", "0")
        )

    // 710 ÷ 21 = 33 ... 17
    // No Carry 1, Borrow 1, No Carry 2, Borrow 2, 2자리 나머지(2DR)
    @Test
    fun testPattern_710_div_21() =
        composeTestRule.divisionCaseV2(
            710, 21,
            listOf("3", "3", "6", "6", "8", "0", "3", "3", "6", "7", "7", "1")
        )

    // 911 ÷ 43 = 21 ... 8
    // No Carry 1, Borrow 1, No Carry 2, Borrow 2, 1자리 나머지(1DR)
    @Test
    fun testPattern_911_div_43() =
        composeTestRule.divisionCaseV2(
            911, 43,
            listOf("2", "6", "8", "8", "5", "1", "1", "3", "4", "4", "8")
        )




    // 632 ÷ 14 = 45 ... 2
// Carry1, Borrow1, Carry2, No Borrow2, (ThreeByTwo_TensQuotient_CM1_BS1_CM2_NBS2)
    @Test
    fun testPattern_632_div_14() =
        composeTestRule.divisionCaseV2(
            632, 14,
            listOf("4", "1", "6", "5", "7", "2", "5", "2", "0", "7", "2")
        )


}