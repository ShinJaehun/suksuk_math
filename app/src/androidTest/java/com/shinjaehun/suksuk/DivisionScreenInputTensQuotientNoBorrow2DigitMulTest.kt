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


class DivisionScreenInputTensQuotientNoBorrow2DigitMulTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    // TensQuotient_NoBorrow_2DigitMul
    @Test
    fun testPattern_72_div_6() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 72, 6, listOf("1", "6", "1", "2", "2", "1", "2", "0"))

    @Test
    fun testPattern_74_div_6() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 74, 6, listOf("1", "6", "1", "4", "2", "1", "2", "2"))

    @Test
    fun testPattern_85_div_7() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 85, 7, listOf("1", "7", "1", "5", "2", "1", "4", "1"))

    @Test
    fun testPattern_86_div_7() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 86, 7, listOf("1", "7", "1", "6", "2", "1", "4", "2"))

    @Test
    fun testPattern_92_div_7() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 92, 7, listOf("1", "7", "2", "2", "3", "2", "1", "1"))

    @Test
    fun testPattern_96_div_4() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 96, 4, listOf("2", "8", "1", "6", "4", "1", "6", "0"))

    @Test
    fun testPattern_46_div_3() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 46, 3, listOf("1", "3", "1", "6", "5", "1", "5", "1"))

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
