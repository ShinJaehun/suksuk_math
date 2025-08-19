package com.shinjaehun.suksuk.division.two_by_two

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.division.divisionCase
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestTwoByTwoNoCarryBorrow2DigitRem {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testPattern_81_div_23() =
        composeTestRule.divisionCase(
            81, 23,
            listOf("3", "9", "6", "7", "2", "1")
        )

    @Test
    fun testPattern_61_div_24() =
        composeTestRule.divisionCase(
            61, 24,
            listOf("2", "8", "4", "5", "3", "1")
        )

}