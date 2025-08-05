package com.shinjaehun.suksuk.two_by_two

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.divisionCaseV2
import com.shinjaehun.suksuk.legacy.divisionCase
import com.shinjaehun.suksuk.domain.division.model.DivisionPattern
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestV2TwoByTwoCarryNoBorrow2DigitRem {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testPattern_79_div_27() =
        composeTestRule.divisionCaseV2(
            79, 27,
            listOf("2", "14", "5", "5", "2")
        )

    @Test
    fun testPattern_98_div_37() =
        composeTestRule.divisionCaseV2(
            98, 37,
            listOf("2", "14", "7", "4", "2")
        )

    @Test
    fun testPattern_88_div_15() =
        composeTestRule.divisionCaseV2(
            88, 15,
            listOf("5", "25", "7", "3", "1")
        )
}