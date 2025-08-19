package com.shinjaehun.suksuk.division.two_by_one

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.division.divisionCaseV2
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestV2TwoByOneOnesQuotientBorrow2DigitMul {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testPattern_53_div_6() =
        composeTestRule.divisionCaseV2( 53, 6, listOf("8", "48", "4", "5"))

    @Test
    fun testPattern_62_div_7() =
        composeTestRule.divisionCaseV2( 62, 7, listOf("8", "56", "5", "6"))
}