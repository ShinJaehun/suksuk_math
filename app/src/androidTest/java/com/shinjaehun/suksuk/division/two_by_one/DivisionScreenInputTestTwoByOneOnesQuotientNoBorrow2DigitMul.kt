package com.shinjaehun.suksuk.division.two_by_one

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.division.divisionCase
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestTwoByOneOnesQuotientNoBorrow2DigitMul {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testPattern_12_div_3() =
        composeTestRule.divisionCase(12, 3, listOf("4", "12", "0"))

    @Test
    fun testPattern_24_div_7() =
        composeTestRule.divisionCase(24, 7, listOf("3", "21", "3"))

    @Test
    fun testPattern_39_div_4() =
        composeTestRule.divisionCase(39, 4, listOf("9", "36", "3"))

    @Test
    fun testPattern_49_div_5() =
        composeTestRule.divisionCase(49, 5, listOf("9", "45", "4"))

    @Test
    fun testPattern_54_div_9() =
        composeTestRule.divisionCase(54, 9, listOf("6", "54", "0"))

    @Test
    fun testPattern_68_div_9() =
        composeTestRule.divisionCase( 68, 9, listOf("7", "63", "5"))

    @Test
    fun testPattern_81_div_9() =
        composeTestRule.divisionCase(81, 9, listOf("9", "81", "0"))

}