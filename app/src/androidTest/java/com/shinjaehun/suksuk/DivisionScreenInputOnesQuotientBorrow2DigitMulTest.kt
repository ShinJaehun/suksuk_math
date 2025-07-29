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
    @Test
    fun testPattern_53_div_6() =
        twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_Borrow_2DigitMul, 53, 6, listOf("8", "4", "8", "4", "5"))

    @Test
    fun testPattern_62_div_7() =
        twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_Borrow_2DigitMul, 62, 7, listOf("8", "5", "6", "5", "6"))


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