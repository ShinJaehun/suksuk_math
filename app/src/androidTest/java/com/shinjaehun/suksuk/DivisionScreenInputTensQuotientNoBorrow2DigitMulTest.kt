package com.shinjaehun.suksuk

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.domain.division.model.DivisionPattern
import org.junit.Rule
import org.junit.Test


class DivisionScreenInputTensQuotientNoBorrow2DigitMulTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    // TensQuotient_NoBorrow_2DigitMul
//    @Test
//    fun testPattern_72_div_6() =
//        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 72, 6, listOf("1", "6", "1", "2", "2", "1", "2", "0"))
//
//    @Test
//    fun testPattern_74_div_6() =
//        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 74, 6, listOf("1", "6", "1", "4", "2", "1", "2", "2"))
//
//    @Test
//    fun testPattern_85_div_7() =
//        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 85, 7, listOf("1", "7", "1", "5", "2", "1", "4", "1"))
//
//    @Test
//    fun testPattern_86_div_7() =
//        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 86, 7, listOf("1", "7", "1", "6", "2", "1", "4", "2"))
//
//    @Test
//    fun testPattern_92_div_7() =
//        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 92, 7, listOf("1", "7", "2", "2", "3", "2", "1", "1"))
//
//    @Test
//    fun testPattern_96_div_4() =
//        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 96, 4, listOf("2", "8", "1", "6", "4", "1", "6", "0"))
//
//    @Test
//    fun testPattern_46_div_3() =
//        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 46, 3, listOf("1", "3", "1", "6", "5", "1", "5", "1"))
    @Test
    fun testPattern_72_div_6() =
        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 72, 6, listOf("1", "6", "1", "2", "2", "12", "0"))

    @Test
    fun testPattern_74_div_6() =
        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 74, 6, listOf("1", "6", "1", "4", "2", "12", "2"))

    @Test
    fun testPattern_85_div_7() =
        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 85, 7, listOf("1", "7", "1", "5", "2", "14", "1"))

    @Test
    fun testPattern_86_div_7() =
        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 86, 7, listOf("1", "7", "1", "6", "2", "14", "2"))

    @Test
    fun testPattern_92_div_7() =
        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 92, 7, listOf("1", "7", "2", "2", "3", "21", "1"))

    @Test
    fun testPattern_96_div_4() =
        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 96, 4, listOf("2", "8", "1", "6", "4", "16", "0"))

    @Test
    fun testPattern_46_div_3() =
        composeTestRule.twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 46, 3, listOf("1", "3", "1", "6", "5", "15", "1"))

}
