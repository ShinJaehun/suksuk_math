package com.shinjaehun.suksuk.division.two_by_one

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.division.divisionCaseV2
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestV2TwoByOneTensQuotientSkipMul2Sub2 {
    @get:Rule
    val composeTestRule = createComposeRule()

    // TensQuotient_SkipBorrow_1DigitMul
    @Test
    fun testPattern_87_div_8() =
        composeTestRule.divisionCaseV2(87, 8, listOf("1", "8", "0", "7", "0"))

    @Test
    fun testPattern_80_div_8() =
        composeTestRule.divisionCaseV2(80, 8, listOf("1", "8", "0", "0", "0"))

}