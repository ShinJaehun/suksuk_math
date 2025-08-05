package com.shinjaehun.suksuk.two_by_two

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.divisionCaseV2
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestV2TwoByTwoCarryBorrow2DigitRem {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testPattern_80_div_28() =
        composeTestRule.divisionCaseV2(
            80,
            28,
            listOf("2", "16", "5", "7", "4", "2")
        )

    @Test
    fun testPattern_81_div_29() =
        composeTestRule.divisionCaseV2(
            81, 29,
            listOf("2", "18", "5", "7", "3", "2")
        )


    @Test
    fun testPattern_73_div_27() =
        composeTestRule.divisionCaseV2(
            73, 27,
            listOf("2", "14", "5", "6", "9", "1")
        )
}
