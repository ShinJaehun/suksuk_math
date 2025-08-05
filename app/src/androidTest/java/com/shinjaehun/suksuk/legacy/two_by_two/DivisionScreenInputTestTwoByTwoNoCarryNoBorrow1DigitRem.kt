package com.shinjaehun.suksuk.legacy.two_by_two

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.legacy.divisionCase
import com.shinjaehun.suksuk.domain.division.model.DivisionPattern
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestTwoByTwoNoCarryNoBorrow1DigitRem {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testPattern_68_div_34() =
        composeTestRule.divisionCase(DivisionPattern.TwoByTwo_NoCarry_NoBorrow_1DigitRem, 68, 34, listOf("2", "8", "6", "0"))

    @Test
    fun testPattern_48_div_24() =
        composeTestRule.divisionCase(DivisionPattern.TwoByTwo_NoCarry_NoBorrow_1DigitRem, 48, 24, listOf("2", "8", "4", "0"))


    @Test
    fun testPattern_49_div_24() =
        composeTestRule.divisionCase(DivisionPattern.TwoByTwo_NoCarry_NoBorrow_1DigitRem, 49, 24, listOf("2", "8", "4", "1"))
}