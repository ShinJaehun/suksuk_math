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

class DivisionScreenInputTensQuotientNoBorrow1DigitMulTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    // TensQuotient_NoBorrow_1DigitMul
    @Test
    fun testPattern_45_div_4() =
        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_1DigitMul, 45, 4, listOf("1", "4", "0", "5", "1", "4", "1"))

    @Test
    fun testPattern_57_div_5() =
        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_1DigitMul, 57, 5, listOf("1", "5", "0", "7", "1", "5", "2"))

    @Test
    fun testPattern_84_div_4() =
        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_1DigitMul, 84, 4, listOf("2", "8", "0", "4", "1", "4", "0"))

}