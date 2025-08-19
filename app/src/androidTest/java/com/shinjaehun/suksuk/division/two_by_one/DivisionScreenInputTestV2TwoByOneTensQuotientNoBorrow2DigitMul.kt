package com.shinjaehun.suksuk.division.two_by_one

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.division.divisionCaseV2
import org.junit.Rule
import org.junit.Test


class DivisionScreenInputTestV2TwoByOneTensQuotientNoBorrow2DigitMul {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testPattern_72_div_6() =
        composeTestRule.divisionCaseV2( 72, 6, listOf("1", "6", "1", "2", "2", "12", "0"))

    @Test
    fun testPattern_74_div_6() =
        composeTestRule.divisionCaseV2(74, 6, listOf("1", "6", "1", "4", "2", "12", "2"))

    @Test
    fun testPattern_85_div_7() =
        composeTestRule.divisionCaseV2(85, 7, listOf("1", "7", "1", "5", "2", "14", "1"))

    @Test
    fun testPattern_86_div_7() =
        composeTestRule.divisionCaseV2(86, 7, listOf("1", "7", "1", "6", "2", "14", "2"))

    @Test
    fun testPattern_92_div_7() =
        composeTestRule.divisionCaseV2(92, 7, listOf("1", "7", "2", "2", "3", "21", "1"))

    @Test
    fun testPattern_96_div_4() =
        composeTestRule.divisionCaseV2(96, 4, listOf("2", "8", "1", "6", "4", "16", "0"))

    @Test
    fun testPattern_46_div_3() =
        composeTestRule.divisionCaseV2(46, 3, listOf("1", "3", "1", "6", "5", "15", "1"))

}
