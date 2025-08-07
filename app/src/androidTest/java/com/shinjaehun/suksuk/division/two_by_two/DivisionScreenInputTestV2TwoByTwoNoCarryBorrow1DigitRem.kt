package com.shinjaehun.suksuk.division.two_by_two

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.division.divisionCaseV2
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestV2TwoByTwoNoCarryBorrow1DigitRem {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testPattern_50_div_22() =
        composeTestRule.divisionCaseV2(50, 22, listOf("2", "4", "4", "4", "6"))

    @Test
    fun testPattern_30_div_11() =
        composeTestRule.divisionCaseV2(30, 11, listOf("2", "2", "2", "2", "8"))


    @Test
    fun testPattern_70_div_31() =
        composeTestRule.divisionCaseV2(70, 31, listOf("2", "2", "6", "6", "8"))
}