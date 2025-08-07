package com.shinjaehun.suksuk.division.two_by_two

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.division.divisionCaseV2
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestV2TwoByTwoCarryBorrow1DigitRem {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testPattern_81_div_12() =
        composeTestRule.divisionCaseV2(
            dividend = 81,
            divisor = 12,
            inputs = listOf("6", "12", "7", "7", "9")
            // // TwoByTwo_Carry_Borrow_1DigitRem
        )

    @Test
    fun testPattern_83_div_13() =
        composeTestRule.divisionCaseV2(
            dividend = 83,
            divisor = 13,
            inputs = listOf("6", "18", "7", "7", "5")
            // // TwoByTwo_Carry_Borrow_1DigitRem
        )
}
