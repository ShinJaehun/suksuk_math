package com.shinjaehun.suksuk

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.shinjaehun.suksuk.presentation.division.DivisionScreen
import com.shinjaehun.suksuk.presentation.division.DivisionViewModel
import org.junit.Rule
import org.junit.Test

class DivisionScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testUICases() {
        val viewModel = DivisionViewModel()
        composeTestRule.setContent {
            DivisionScreen(viewModel = viewModel)
        }

        // 문제를 세팅 (예: 93 ÷ 8)
        composeTestRule.runOnIdle {
            viewModel.startNewProblem(93, 8)
        }

        val scenario = listOf(
            "1", "8", "1", "3", "1", "8", "0", "5"
        )

        for (i in scenario.indices) {
            val input = scenario[i]

            // 1. 해당 숫자 버튼 클릭
            composeTestRule.onNodeWithTag("numpad-$input").performClick()
            // 2. ↵(엔터) 버튼 클릭
            composeTestRule.onNodeWithTag("numpad-enter").performClick()

            // 3. (Optional) 피드백 체크 (정답은 보통 마지막 입력 후)
            if (i < scenario.lastIndex) {
                composeTestRule.onNodeWithTag("feedback").assertDoesNotExist()
            }
        }

        // 마지막 입력 후 "정답" 메시지 보임
        composeTestRule.onNodeWithTag("feedback").assertIsDisplayed()
        composeTestRule.onNodeWithTag("feedback").assertTextContains("정답입니다!")
    }


    @Test fun testPatternD_93_div_8() =
        twoDigitDivByOneDigitCase("Pattern D: 93 ÷ 8", 93, 8, listOf("1", "8", "1", "3", "1", "8", "0", "5"))

    @Test fun testPatternC_50_div_3() =
        twoDigitDivByOneDigitCase("Pattern C: 50 ÷ 3", 50, 3, listOf("1", "3", "2", "0", "6", "1", "8", "1", "2"))

    @Test fun testPatternA_72_div_6() =
        twoDigitDivByOneDigitCase("Pattern A: 72 ÷ 6", 72, 6, listOf("1", "6", "1", "2", "2", "1", "2", "0"))

    @Test fun testPatternA_85_div_7() =
        twoDigitDivByOneDigitCase("Pattern A: 85 ÷ 7", 85, 7, listOf("1", "7", "1", "5", "2", "1", "4", "1"))

    @Test fun testPatternE_53_div_6() =
        twoDigitDivByOneDigitCase("Pattern E: 53 ÷ 6", 53, 6, listOf("8", "4", "8", "4", "5"))

    @Test fun testPatternB_45_div_4() =
        twoDigitDivByOneDigitCase("Pattern B: 45 ÷ 4", 45, 4, listOf("1", "4", "0", "5", "1", "4", "1"))

    @Test fun testPatternA_86_div_7() =
        twoDigitDivByOneDigitCase("Pattern A: 86 ÷ 7", 86, 7, listOf("1", "7", "1", "6", "2", "1", "4", "2"))

    @Test fun testPatternB_84_div_4() =
        twoDigitDivByOneDigitCase("Pattern B: 84 ÷ 4", 84, 4, listOf("2", "8", "0", "4", "1", "4", "0"))

    @Test fun testPatternE_62_div_7() =
        twoDigitDivByOneDigitCase("Pattern E: 62 ÷ 7", 62, 7, listOf("8", "5", "6", "5", "6"))

    @Test fun testPatternA_92_div_7() =
        twoDigitDivByOneDigitCase("Pattern A: 92 ÷ 7", 92, 7, listOf("1", "7", "2", "2", "3", "2", "1", "1"))

    @Test fun testPatternA_96_div_4() =
        twoDigitDivByOneDigitCase("Pattern A: 96 ÷ 4", 96, 4, listOf("2", "8", "1", "6", "4", "1", "6", "0"))

    @Test fun testPatternF_12_div_3() =
        twoDigitDivByOneDigitCase("Pattern F: 12 ÷ 3", 12, 3, listOf("4", "1", "2", "0"))

    @Test fun testPatternF_24_div_7() =
        twoDigitDivByOneDigitCase("Pattern F: 24 ÷ 7", 24, 7, listOf("3", "2", "1", "3"))

    @Test fun testPatternA_46_div_3() =
        twoDigitDivByOneDigitCase("Pattern A: 46 ÷ 3", 46, 3, listOf("1", "3", "1", "6", "5", "1", "5", "1"))

    @Test fun testPatternD_71_div_6() =
        twoDigitDivByOneDigitCase("Pattern D: 71 ÷ 6", 71, 6, listOf("1", "6", "1", "1", "1", "6", "0", "5"))

    @Test fun testPatternD_90_div_8() =
        twoDigitDivByOneDigitCase("Pattern D: 90 ÷ 8", 90, 8, listOf("1", "8", "1", "0", "1", "8", "0", "2"))

    @Test fun testPatternF_68_div_9() =
        twoDigitDivByOneDigitCase("Pattern F: 68 ÷ 9", 68, 9, listOf("7", "6", "3", "5"))

    @Test fun testPatternF_54_div_9() =
        twoDigitDivByOneDigitCase("Pattern F: 54 ÷ 9", 54, 9, listOf("6", "5", "4", "0"))

    @Test fun testPatternF_81_div_9() =
        twoDigitDivByOneDigitCase("Pattern F: 81 ÷ 9", 81, 9, listOf("9", "8", "1", "0"))

    @Test fun testPatternF_49_div_5() =
        twoDigitDivByOneDigitCase("Pattern F: 49 ÷ 5", 49, 5, listOf("9", "4", "5", "4"))

    @Test fun testPatternA_74_div_6() =
        twoDigitDivByOneDigitCase("Pattern A: 74 ÷ 6", 74, 6, listOf("1", "6", "1", "4", "2", "1", "2", "2"))

    @Test fun testPatternB_57_div_5() =
        twoDigitDivByOneDigitCase("Pattern B: 57 ÷ 5", 57, 5, listOf("1", "5", "0", "7", "1", "5", "2"))

    @Test fun testPatternF_39_div_4() =
        twoDigitDivByOneDigitCase("Pattern F: 39 ÷ 4", 39, 4, listOf("9", "3", "6", "3"))

    private fun twoDigitDivByOneDigitCase(name: String, dividend: Int, divisor: Int, inputs: List<String>) {
        val viewModel = DivisionViewModel()
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