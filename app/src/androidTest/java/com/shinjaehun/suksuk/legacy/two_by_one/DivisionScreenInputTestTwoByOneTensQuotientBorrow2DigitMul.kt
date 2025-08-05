package com.shinjaehun.suksuk.legacy.two_by_one

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.legacy.divisionCase
import com.shinjaehun.suksuk.domain.division.model.DivisionPattern
import org.junit.Rule
import org.junit.Test

class DivisionScreenInputTestTwoByOneTensQuotientBorrow2DigitMul {
    @get:Rule
    val composeTestRule = createComposeRule()

    // TensQuotient_Borrow_2DigitMul
//    @Test
//    fun testPattern_50_div_3() =
//        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_Borrow_2DigitMul, 50, 3, listOf("1", "3", "2", "0", "6", "1", "8", "1", "2"))
//
//    @Test
//    fun testPattern_70_div_4() =
//        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_Borrow_2DigitMul, 70, 4, listOf("1", "4", "3", "0", "7", "2", "8", "2", "2"))
//
//    @Test
//    fun testPattern_90_div_7() =
//        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_Borrow_2DigitMul, 90, 7, listOf("1", "7", "2", "0", "2", "1", "4", "1", "6"))

    @Test
    fun testPattern_50_div_3() =
        composeTestRule.divisionCase(DivisionPattern.TwoByOne_TensQuotient_Borrow_2DigitMul, 50, 3, listOf("1", "3", "2", "0", "6", "18", "1", "2"))

    @Test
    fun testPattern_70_div_4() =
        composeTestRule.divisionCase(DivisionPattern.TwoByOne_TensQuotient_Borrow_2DigitMul, 70, 4, listOf("1", "4", "3", "0", "7", "28", "2", "2"))

    @Test
    fun testPattern_90_div_7() =
        composeTestRule.divisionCase(DivisionPattern.TwoByOne_TensQuotient_Borrow_2DigitMul, 90, 7, listOf("1", "7", "2", "0", "2", "14", "1", "6"))
}