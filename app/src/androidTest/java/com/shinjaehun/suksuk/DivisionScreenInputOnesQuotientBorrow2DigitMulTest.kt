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

class DivisionScreenInputOnesQuotientBorrow2DigitMulTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    // OnesQuotient_Borrow_2DigitMul
//    @Test
//    fun testPattern_53_div_6() =
//        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_Borrow_2DigitMul, 53, 6, listOf("8", "4", "8", "4", "5"))
//
//    @Test
//    fun testPattern_62_div_7() =
//        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_Borrow_2DigitMul, 62, 7, listOf("8", "5", "6", "5", "6"))

    @Test
    fun testPattern_53_div_6() =
        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_Borrow_2DigitMul, 53, 6, listOf("8", "48", "4", "5"))

    @Test
    fun testPattern_62_div_7() =
        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_Borrow_2DigitMul, 62, 7, listOf("8", "56", "5", "6"))
}