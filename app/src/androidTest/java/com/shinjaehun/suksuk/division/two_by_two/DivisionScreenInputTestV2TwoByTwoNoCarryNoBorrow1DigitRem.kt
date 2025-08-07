package com.shinjaehun.suksuk.division.two_by_two

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.division.divisionCaseV2
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestV2TwoByTwoNoCarryNoBorrow1DigitRem {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testPattern_68_div_34() =
        composeTestRule.divisionCaseV2(68, 34, listOf("2", "8", "6", "0"))

    @Test
    fun testPattern_48_div_24() =
        composeTestRule.divisionCaseV2(48, 24, listOf("2", "8", "4", "0"))


    @Test
    fun testPattern_49_div_24() =
        composeTestRule.divisionCaseV2(49, 24, listOf("2", "8", "4", "1"))
}