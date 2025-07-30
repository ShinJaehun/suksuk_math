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

class DivisionScreenInputTensQuotientBorrow2DigitMulTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    // TensQuotient_Borrow_2DigitMul
    @Test
    fun testPattern_50_div_3() =
        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_Borrow_2DigitMul, 50, 3, listOf("1", "3", "2", "0", "6", "1", "8", "1", "2"))

    @Test
    fun testPattern_70_div_4() =
        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_Borrow_2DigitMul, 70, 4, listOf("1", "4", "3", "0", "7", "2", "8", "2", "2"))

    @Test
    fun testPattern_90_div_7() =
        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_Borrow_2DigitMul, 90, 7, listOf("1", "7", "2", "0", "2", "1", "4", "1", "6"))

}