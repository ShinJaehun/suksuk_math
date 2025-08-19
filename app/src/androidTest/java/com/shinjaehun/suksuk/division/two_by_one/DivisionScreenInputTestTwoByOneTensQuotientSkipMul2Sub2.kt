package com.shinjaehun.suksuk.division.two_by_one

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.division.divisionCase
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestTwoByOneTensQuotientSkipMul2Sub2 {
    @get:Rule
    val composeTestRule = createComposeRule()

    // TensQuotient_SkipBorrow_1DigitMul
    @Test
    fun testPattern_87_div_8() =
        composeTestRule.divisionCase(87, 8, listOf("1", "8", "0", "7", "0"))

    @Test
    fun testPattern_80_div_8() =
        composeTestRule.divisionCase(80, 8, listOf("1", "8", "0", "0", "0"))

}