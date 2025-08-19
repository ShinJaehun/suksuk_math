package com.shinjaehun.suksuk.division.two_by_two

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.division.divisionCase
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestTwoByTwoCarryNoBorrow1DigitRem {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testPattern_75_div_25() =
        composeTestRule.divisionCase(75, 25, listOf("3", "15", "7", "0"))

    @Test
    fun testPattern_96_div_12() =
        composeTestRule.divisionCase(96, 12, listOf("8", "16", "9", "0"))


    @Test
    fun testPattern_72_div_18() =
        composeTestRule.divisionCase(72, 18, listOf("4", "32", "7", "0"))
}