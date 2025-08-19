package com.shinjaehun.suksuk.division.two_by_two

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.division.divisionCase
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestTwoByTwoCarryNoBorrow2DigitRem {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testPattern_79_div_27() =
        composeTestRule.divisionCase(
            79, 27,
            listOf("2", "14", "5", "5", "2")
        )

    @Test
    fun testPattern_98_div_37() =
        composeTestRule.divisionCase(
            98, 37,
            listOf("2", "14", "7", "4", "2")
        )

    @Test
    fun testPattern_88_div_15() =
        composeTestRule.divisionCase(
            88, 15,
            listOf("5", "25", "7", "3", "1")
        )
}