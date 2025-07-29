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
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_SkipBorrow_1DigitMul, 71, 6, listOf("1", "6", "1", "1", "1", "6", "5"))

    @Test
    fun testPattern_93_div_8() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_SkipBorrow_1DigitMul, 93, 8, listOf("1", "8", "1", "3", "1", "8", "5"))

    @Test
    fun testPattern_90_div_7() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_SkipBorrow_1DigitMul, 90, 8, listOf("1", "8", "1", "0", "1", "8", "2"))


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