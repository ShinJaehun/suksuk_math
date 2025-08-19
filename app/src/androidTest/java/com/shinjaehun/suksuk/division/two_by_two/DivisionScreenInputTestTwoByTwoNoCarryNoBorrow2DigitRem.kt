package com.shinjaehun.suksuk.division.two_by_two

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.division.divisionCase
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestTwoByTwoNoCarryNoBorrow2DigitRem {
    @get:Rule
    val composeTestRule = createComposeRule()
    @Test
    fun testPattern_57_div_22() =
        composeTestRule.divisionCase(
            57, 22,
            listOf("2", "4", "4", "3", "1")
        )

    @Test
    fun testPattern_79_div_34() =
        composeTestRule.divisionCase(
            79, 34,
            listOf("2", "8", "6", "1", "1")
        )
}