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
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_Borrow_2DigitMul, 50, 3, listOf("1", "3", "2", "0", "6", "1", "8", "1", "2"))

    @Test
    fun testPattern_70_div_4() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_Borrow_2DigitMul, 70, 4, listOf("1", "4", "3", "0", "7", "2", "8", "2", "2"))

    @Test
    fun testPattern_90_div_7() =
        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_Borrow_2DigitMul, 90, 7, listOf("1", "7", "2", "0", "2", "1", "4", "1", "6"))


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