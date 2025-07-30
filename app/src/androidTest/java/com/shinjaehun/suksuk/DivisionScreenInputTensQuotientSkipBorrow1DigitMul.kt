package com.shinjaehun.suksuk

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.shinjaehun.suksuk.presentation.division.DivisionPattern
import com.shinjaehun.suksuk.presentation.division.DivisionScreen
import com.shinjaehun.suksuk.presentation.division.DivisionViewModel
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTensQuotientSkipBorrow1DigitMul {
    @get:Rule
    val composeTestRule = createComposeRule()

    // TensQuotient_SkipBorrow_1DigitMul
    @Test
    fun testPattern_71_div_6() =
        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_SkipBorrow_1DigitMul, 71, 6, listOf("1", "6", "1", "1", "1", "6", "5"))

    @Test
    fun testPattern_93_div_8() =
        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_SkipBorrow_1DigitMul, 93, 8, listOf("1", "8", "1", "3", "1", "8", "5"))

    @Test
    fun testPattern_90_div_7() =
        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_SkipBorrow_1DigitMul, 90, 8, listOf("1", "8", "1", "0", "1", "8", "2"))
}