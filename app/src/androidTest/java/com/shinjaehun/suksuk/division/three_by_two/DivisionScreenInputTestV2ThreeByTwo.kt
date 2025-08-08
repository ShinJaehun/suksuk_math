package com.shinjaehun.suksuk.division.three_by_two

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.division.divisionCaseV2
import com.shinjaehun.suksuk.division.legacy.divisionCase
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestV2ThreeByTwo {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testPattern_682_div_31() =
        composeTestRule.divisionCaseV2( 682, 31, listOf("2", "2", "6", "6", "2", "2", "2", "6", "0"))

    @Test
    fun testPattern_604_div_11() =
        composeTestRule.divisionCaseV2( 604, 11, listOf("5", "5", "5", "5", "5", "4", "4", "4", "4", "0", "1"))
}