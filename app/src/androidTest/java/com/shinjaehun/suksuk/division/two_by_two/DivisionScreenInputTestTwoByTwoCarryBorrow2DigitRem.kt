package com.shinjaehun.suksuk.division.two_by_two

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.division.divisionCase
import com.shinjaehun.suksuk.domain.division.model.DivisionPattern
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestTwoByTwoCarryBorrow2DigitRem {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testPattern_80_div_28() =
        composeTestRule.divisionCase(
            DivisionPattern.TwoByTwo_Carry_Borrow_2DigitRem,
            80,
            28,
            listOf("2", "16", "5", "7", "4", "2")
        )

    @Test
    fun testPattern_81_div_29() =
        composeTestRule.divisionCase(
            DivisionPattern.TwoByTwo_Carry_Borrow_2DigitRem,
            81, 29,
            listOf("2", "18", "5", "7", "3", "2")
        )


    @Test
    fun testPattern_73_div_27() =
        composeTestRule.divisionCase(
            DivisionPattern.TwoByTwo_Carry_Borrow_2DigitRem,
            73, 27,
            listOf("2", "14", "5", "6", "9", "1")
        )
}
