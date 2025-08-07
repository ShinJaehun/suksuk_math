package com.shinjaehun.suksuk.division.two_by_one

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.division.divisionCaseV2
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestV2TwoByOneTensQuotientNoBorrow1DigitMul {
    @get:Rule
    val composeTestRule = createComposeRule()

    // TensQuotient_NoBorrow_1DigitMul
    @Test
    fun testPattern_45_div_4() =
        composeTestRule.divisionCaseV2( 45, 4, listOf("1", "4", "0", "5", "1", "4", "1"))

    @Test
    fun testPattern_57_div_5() =
        composeTestRule.divisionCaseV2(57, 5, listOf("1", "5", "0", "7", "1", "5", "2"))

    @Test
    fun testPattern_84_div_4() =
        composeTestRule.divisionCaseV2(84, 4, listOf("2", "8", "0", "4", "1", "4", "0"))

}