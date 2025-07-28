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

class DivisionScreenInputTest {
    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun testPatternA_72_div_6() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 72, 6, listOf("1", "6", "1", "2", "2", "1", "2", "0"))

    @Test
    fun testPatternA_74_div_6() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 74, 6, listOf("1", "6", "1", "4", "2", "1", "2", "2"))

    @Test
    fun testPatternA_85_div_7() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 85, 7, listOf("1", "7", "1", "5", "2", "1", "4", "1"))

    @Test
    fun testPatternA_86_div_7() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 86, 7, listOf("1", "7", "1", "6", "2", "1", "4", "2"))

    @Test
    fun testPatternA_92_div_7() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 92, 7, listOf("1", "7", "2", "2", "3", "2", "1", "1"))

    @Test
    fun testPatternA_96_div_4() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 96, 4, listOf("2", "8", "1", "6", "4", "1", "6", "0"))

    @Test
    fun testPatternA_46_div_3() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 46, 3, listOf("1", "3", "1", "6", "5", "1", "5", "1"))

    @Test
    fun testPatternB_45_div_4() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_Borrow_2DigitMul, 45, 4, listOf("1", "4", "0", "5", "1", "4", "1"))

    @Test
    fun testPatternB_57_div_5() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_Borrow_2DigitMul, 57, 5, listOf("1", "5", "0", "7", "1", "5", "2"))

    @Test
    fun testPatternB_84_div_4() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_Borrow_2DigitMul, 84, 4, listOf("2", "8", "0", "4", "1", "4", "0"))

    @Test
    fun testPatternC_50_div_3() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_Borrow_2DigitMul, 50, 3, listOf("1", "3", "2", "0", "6", "1", "8", "1", "2"))

    @Test
    fun testPatternD_71_div_6() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_SkipBorrow_1DigitMul, 71, 6, listOf("1", "6", "1", "1", "1", "6", "5"))

    @Test
    fun testPatternD_90_div_8() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_SkipBorrow_1DigitMul, 90, 8, listOf("1", "8", "1", "0", "1", "8", "2"))

    @Test
    fun testPatternD_93_div_8() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_SkipBorrow_1DigitMul, 93, 8, listOf("1", "8", "1", "3", "1", "8", "5"))

    @Test
    fun testPatternE_53_div_6() =
        twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_Borrow, 53, 6, listOf("8", "4", "8", "4", "5"))

    @Test
    fun testPatternE_62_div_7() =
        twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_Borrow, 62, 7, listOf("8", "5", "6", "5", "6"))

    @Test
    fun testPatternF_12_div_3() =
        twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_NoBorrow, 12, 3, listOf("4", "1", "2", "0"))

    @Test
    fun testPatternF_24_div_7() =
        twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_NoBorrow, 24, 7, listOf("3", "2", "1", "3"))

    @Test
    fun testPatternF_39_div_4() =
        twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_NoBorrow, 39, 4, listOf("9", "3", "6", "3"))

    @Test
    fun testPatternF_49_div_5() =
        twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_NoBorrow, 49, 5, listOf("9", "4", "5", "4"))

    @Test
    fun testPatternF_54_div_9() =
        twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_NoBorrow, 54, 9, listOf("6", "5", "4", "0"))

    @Test
    fun testPatternF_68_div_9() =
        twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_NoBorrow, 68, 9, listOf("7", "6", "3", "5"))

    @Test
    fun testPatternF_81_div_9() =
        twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_NoBorrow, 81, 9, listOf("9", "8", "1", "0"))

    private fun twoDigitDivByOneDigitCase(
        pattern: DivisionPattern,
        dividend: Int,
        divisor: Int,
        inputs: List<String>,
    ) {
        val viewModel = DivisionViewModel(autoStart = false)
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }
        composeTestRule.runOnIdle {
            viewModel.startNewProblem(dividend, divisor)
        }
        for (i in inputs.indices) {
            composeTestRule.onNodeWithTag("numpad-${inputs[i]}").performClick()
            composeTestRule.onNodeWithTag("numpad-enter").performClick()
            if (i < inputs.lastIndex)
                composeTestRule.onNodeWithTag("feedback").assertDoesNotExist()
        }
        composeTestRule.onNodeWithTag("feedback").assertIsDisplayed()
        composeTestRule.onNodeWithTag("feedback").assertTextContains("정답입니다!")
    }


}