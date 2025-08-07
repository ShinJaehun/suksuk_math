package com.shinjaehun.suksuk.division.two_by_two

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.division.divisionCaseV2
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestV2TwoByTwoNoCarryNoBorrow2DigitRem {
    @get:Rule
    val composeTestRule = createComposeRule()
    @Test
    fun testPattern_57_div_22() =
        composeTestRule.divisionCaseV2(
            57, 22,
            listOf("2", "4", "4", "3", "1")
        )

    @Test
    fun testPattern_79_div_34() =
        composeTestRule.divisionCaseV2(
            79, 34,
            listOf("2", "8", "6", "1", "1")
        )
}