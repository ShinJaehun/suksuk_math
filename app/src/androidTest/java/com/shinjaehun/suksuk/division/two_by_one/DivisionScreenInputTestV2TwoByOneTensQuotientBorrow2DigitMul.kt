package com.shinjaehun.suksuk.division.two_by_one

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.division.divisionCaseV2
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestV2TwoByOneTensQuotientBorrow2DigitMul {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testPattern_50_div_3() =
        composeTestRule.divisionCaseV2(50, 3, listOf("1", "3", "2", "0", "6", "18", "1", "2"))

    @Test
    fun testPattern_70_div_4() =
        composeTestRule.divisionCaseV2(70, 4, listOf("1", "4", "3", "0", "7", "28", "2", "2"))

    @Test
    fun testPattern_90_div_7() =
        composeTestRule.divisionCaseV2(90, 7, listOf("1", "7", "2", "0", "2", "14", "1", "6"))
}