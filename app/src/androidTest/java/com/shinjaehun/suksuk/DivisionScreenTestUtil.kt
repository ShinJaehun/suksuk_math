package com.shinjaehun.suksuk

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import com.shinjaehun.suksuk.domain.PatternDetector
import com.shinjaehun.suksuk.domain.PhaseEvaluator
import com.shinjaehun.suksuk.presentation.division.DivisionPattern
import com.shinjaehun.suksuk.presentation.division.DivisionPatternUiLayoutRegistry
import com.shinjaehun.suksuk.presentation.division.DivisionScreen
import com.shinjaehun.suksuk.presentation.division.DivisionViewModel
import com.shinjaehun.suksuk.presentation.division.FeedbackMessageProvider

//fun ComposeContentTestRule.twoDigitDivByOneDigitCase(
//    pattern: DivisionPattern,
//    dividend: Int,
//    divisor: Int,
//    inputs: List<String>,
//) {
//    val viewModel = DivisionViewModel(autoStart = false)
//    this.setContent {
//        DivisionScreen(viewModel = viewModel)
//    }
//    this.runOnIdle {
//        viewModel.startNewProblem(dividend, divisor)
//    }
//    for (i in inputs.indices) {
//        this.onNodeWithTag("numpad-${inputs[i]}").performClick()
//        this.onNodeWithTag("numpad-enter").performClick()
//        if (i < inputs.lastIndex)
//            this.onNodeWithTag("feedback").assertDoesNotExist()
//    }
//    this.onNodeWithTag("feedback").assertIsDisplayed()
//    this.onNodeWithTag("feedback").assertTextContains("정답입니다!")
//}

fun ComposeContentTestRule.twoDigitDivByOneDigitCase(
    pattern: DivisionPattern,
    dividend: Int,
    divisor: Int,
    inputs: List<String>,
) {
    val savedStateHandle = SavedStateHandle(mapOf("autoStart" to false))
    // 2. 의존성 직접 생성 (혹은 모킹)
    val phaseEvaluator = PhaseEvaluator()
    val patternDetector = PatternDetector
    val uiLayoutRegistry = DivisionPatternUiLayoutRegistry
    val feedbackProvider = FeedbackMessageProvider()

    // 3. ViewModel 직접 생성
    val viewModel = DivisionViewModel(
        savedStateHandle,
        phaseEvaluator,
        patternDetector,
        uiLayoutRegistry,
        feedbackProvider
    )

    this.setContent {
        DivisionScreen(viewModel = viewModel)
    }
    this.runOnIdle {
        viewModel.startNewProblem(dividend, divisor)
    }
    var i = 0
    while (i < inputs.size) {
        val input = inputs[i]
        if (input.length == 1) {
            // 한 자리 입력 (numpad-N → enter)
            this.onNodeWithTag("numpad-$input").performClick()
            this.onNodeWithTag("numpad-enter").performClick()
            i++
        } else if (input.length == 2) {
            // 두 자리 입력 (numpad-N → numpad-M → enter)
            this.onNodeWithTag("numpad-${input[0]}").performClick()
            this.onNodeWithTag("numpad-${input[1]}").performClick()
            this.onNodeWithTag("numpad-enter").performClick()
            i++
        } else {
            error("지원하지 않는 입력: $input")
        }

        if (i < inputs.lastIndex)
            this.onNodeWithTag("feedback").assertDoesNotExist()
    }
    this.onNodeWithTag("feedback").assertIsDisplayed()
    this.onNodeWithTag("feedback").assertTextContains("정답입니다!")
}

// 두 자리 입력을 따로 처리하지 않는구나...

//    // TensQuotient_NoBorrow_2DigitMul
//    @Test fun testPatternA_46_div_3() =
//        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 46, 3, listOf("1", "3", "1", "6", "5", "15", "1"))
//
//    @Test fun testPatternA_72_div_6() =
//        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 72, 6, listOf("1", "6", "1", "2", "2", "12", "0"))
//
//    @Test fun testPatternA_74_div_6() =
//        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 74, 6, listOf("1", "6", "1", "4", "2", "12", "2"))
//
//    @Test fun testPatternA_85_div_7() =
//        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 85, 7, listOf("1", "7", "1", "5", "2", "14", "1"))
//
//    @Test fun testPatternA_86_div_7() =
//        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 86, 7, listOf("1", "7", "1", "6", "2", "14", "2"))
//
//    @Test fun testPatternA_92_div_7() =
//        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 92, 7, listOf("1", "7", "2", "2", "3", "21", "1"))
//
//    @Test fun testPatternA_96_div_4() =
//        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_2DigitMul, 96, 4, listOf("2", "8", "1", "6", "4", "16", "0"))
//
//    // TensQuotient_NoBorrow_1DigitMul
//    @Test fun testPatternB_45_div_4() =
//        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_1DigitMul, 45, 4, listOf("1", "4", "0", "5", "1", "4", "1"))
//
//    @Test fun testPatternB_57_div_5() =
//        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_1DigitMul, 57, 5, listOf("1", "5", "0", "7", "1", "5", "2"))
//
//    @Test fun testPatternB_84_div_4() =
//        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_NoBorrow_1DigitMul, 84, 4, listOf("2", "8", "0", "4", "1", "4", "0"))
//
//    // TensQuotient_Borrow_2DigitMul
//    @Test fun testPatternC_50_div_3() =
//        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_Borrow_2DigitMul, 50, 3, listOf("1", "3", "2", "0", "6", "18", "1", "2"))
//
//    // TensQuotient_SkipBorrow_1DigitMul
//    @Test fun testPatternD_71_div_6() =
//        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_SkipBorrow_1DigitMul, 71, 6, listOf("1", "6", "1", "1", "1", "6", "5"))
//
//    @Test fun testPatternD_90_div_8() =
//        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_SkipBorrow_1DigitMul, 90, 8, listOf("1", "8", "1", "0", "1", "8", "2"))
//
//    @Test fun testPatternD_93_div_8() =
//        twoDigitDivByOneDigitCase(DivisionPattern.TensQuotient_SkipBorrow_1DigitMul, 93, 8, listOf("1", "8", "1", "3", "1", "8", "5"))
//
//    // OnesQuotient_Borrow
//    @Test fun testPatternE_53_div_6() =
//        twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_Borrow_2DigitMul, 53, 6, listOf("8", "48", "4", "5"))
//
//    @Test fun testPatternE_62_div_7() =
//        twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_Borrow_2DigitMul, 62, 7, listOf("8", "56", "5", "6"))
//
//    // OnesQuotient_NoBorrow
//    @Test fun testPatternF_12_div_3() =
//        twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_NoBorrow_2DigitMul, 12, 3, listOf("4", "12", "0"))
//
//    @Test fun testPatternF_24_div_7() =
//        twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_NoBorrow_2DigitMul, 24, 7, listOf("3", "21", "3"))
//
//    @Test fun testPatternF_39_div_4() =
//        twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_NoBorrow_2DigitMul, 39, 4, listOf("9", "36", "3"))
//
//    @Test fun testPatternF_49_div_5() =
//        twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_NoBorrow_2DigitMul, 49, 5, listOf("9", "45", "4"))
//
//    @Test fun testPatternF_54_div_9() =
//        twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_NoBorrow_2DigitMul, 54, 9, listOf("6", "54", "0"))
//
//    @Test fun testPatternF_68_div_9() =
//        twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_NoBorrow_2DigitMul, 68, 9, listOf("7", "63", "5"))
//
//    @Test fun testPatternF_81_div_9() =
//        twoDigitDivByOneDigitCase(DivisionPattern.OnesQuotient_NoBorrow_2DigitMul, 81, 9, listOf("9", "81", "0"))

