package com.shinjaehun.suksuk.division.two_by_two

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.division.divisionCase
import com.shinjaehun.suksuk.domain.division.model.DivisionPattern
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestTwoByTwoCarryBorrow1DigitRem {
    @get:Rule
    val composeTestRule = createComposeRule()

//    @Test
//    fun testPattern_80_div_28() =
//        composeTestRule.divisionCase(DivisionPattern.TwoByTwo_Carry_Borrow, 80, 28, listOf("2", "16", "5", "7", "4", "2"))

    @Test
    fun testPattern_81_div_12() =
        composeTestRule.divisionCase(DivisionPattern.TwoByTwo_Carry_Borrow_1DigitRem, 81, 12, listOf("6", "12", "7", "7", "9"))

    @Test
    fun testPattern_83_div_13() =
        composeTestRule.divisionCase(DivisionPattern.TwoByTwo_Carry_Borrow_1DigitRem, 83, 13, listOf("6", "18", "7", "7","5"))
}