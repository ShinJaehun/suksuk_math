package com.shinjaehun.suksuk.division.two_by_one

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.division.divisionCaseV2
import com.shinjaehun.suksuk.division.legacy.divisionCase
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestV2TwoByOneOnesQuotientNoBorrow2DigitMul {
    @get:Rule
    val composeTestRule = createComposeRule()

    // OnesQuotient_NoBorrow_2DigitMul
//    @Test
//    fun testPattern_12_div_3() =
//        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_NoBorrow_2DigitMul, 12, 3, listOf("4", "1", "2", "0"))
//
//    @Test
//    fun testPattern_24_div_7() =
//        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_NoBorrow_2DigitMul, 24, 7, listOf("3", "2", "1", "3"))
//
//    @Test
//    fun testPattern_39_div_4() =
//        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_NoBorrow_2DigitMul, 39, 4, listOf("9", "3", "6", "3"))
//
//    @Test
//    fun testPattern_49_div_5() =
//        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_NoBorrow_2DigitMul, 49, 5, listOf("9", "4", "5", "4"))
//
//    @Test
//    fun testPattern_54_div_9() =
//        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_NoBorrow_2DigitMul, 54, 9, listOf("6", "5", "4", "0"))
//
//    @Test
//    fun testPattern_68_div_9() =
//        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_NoBorrow_2DigitMul, 68, 9, listOf("7", "6", "3", "5"))
//
//    @Test
//    fun testPattern_81_div_9() =
//        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_NoBorrow_2DigitMul, 81, 9, listOf("9", "8", "1", "0"))
    @Test
    fun testPattern_12_div_3() =
        composeTestRule.divisionCaseV2(12, 3, listOf("4", "12", "0"))

    @Test
    fun testPattern_24_div_7() =
        composeTestRule.divisionCaseV2(24, 7, listOf("3", "21", "3"))

    @Test
    fun testPattern_39_div_4() =
        composeTestRule.divisionCaseV2(39, 4, listOf("9", "36", "3"))

    @Test
    fun testPattern_49_div_5() =
        composeTestRule.divisionCaseV2(49, 5, listOf("9", "45", "4"))

    @Test
    fun testPattern_54_div_9() =
        composeTestRule.divisionCaseV2(54, 9, listOf("6", "54", "0"))

    @Test
    fun testPattern_68_div_9() =
        composeTestRule.divisionCaseV2( 68, 9, listOf("7", "63", "5"))

    @Test
    fun testPattern_81_div_9() =
        composeTestRule.divisionCaseV2(81, 9, listOf("9", "81", "0"))

}