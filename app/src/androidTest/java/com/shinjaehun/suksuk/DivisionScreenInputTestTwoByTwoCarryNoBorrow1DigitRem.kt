package com.shinjaehun.suksuk

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.domain.division.model.DivisionPattern
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestTwoByTwoCarryNoBorrow1DigitRem {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testPattern_75_div_25() =
        composeTestRule.divisionCase(DivisionPattern.TwoByTwo_Carry_NoBorrow_1DigitRem, 75, 25, listOf("3", "15", "7", "0"))

    @Test
    fun testPattern_96_div_12() =
        composeTestRule.divisionCase(DivisionPattern.TwoByTwo_Carry_NoBorrow_1DigitRem, 96, 12, listOf("8", "16", "9", "0"))


    @Test
    fun testPattern_72_div_18() =
        composeTestRule.divisionCase(DivisionPattern.TwoByTwo_Carry_NoBorrow_1DigitRem, 72, 18, listOf("4", "32", "7", "0"))
}