package com.shinjaehun.suksuk

import app.cash.turbine.test
import com.shinjaehun.suksuk.presentation.division.DivisionPattern
import com.shinjaehun.suksuk.presentation.division.DivisionPhase
import com.shinjaehun.suksuk.presentation.division.DivisionPhasesState
import com.shinjaehun.suksuk.presentation.division.DivisionViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DivisionViewModelTest {

    @Test
    fun detectPatternTest() {
        val cases = listOf(
            Triple(46, 3, DivisionPattern.TensQuotient_NoBorrow_2DigitMul),
            Triple(72, 6, DivisionPattern.TensQuotient_NoBorrow_2DigitMul),
            Triple(74, 6, DivisionPattern.TensQuotient_NoBorrow_2DigitMul),
            Triple(85, 7, DivisionPattern.TensQuotient_NoBorrow_2DigitMul),
            Triple(86, 7, DivisionPattern.TensQuotient_NoBorrow_2DigitMul),
            Triple(92, 7, DivisionPattern.TensQuotient_NoBorrow_2DigitMul),
            Triple(96, 4, DivisionPattern.TensQuotient_NoBorrow_2DigitMul),

            Triple(45, 4, DivisionPattern.TensQuotient_NoBorrow_1DigitMul),
            Triple(57, 5, DivisionPattern.TensQuotient_NoBorrow_1DigitMul),
            Triple(84, 4, DivisionPattern.TensQuotient_NoBorrow_1DigitMul),

            Triple(50, 3, DivisionPattern.TensQuotient_Borrow_2DigitMul),
            Triple(90, 7, DivisionPattern.TensQuotient_Borrow_2DigitMul),

            Triple(70, 6, DivisionPattern.TensQuotient_SkipBorrow_1DigitMul),
            Triple(71, 6, DivisionPattern.TensQuotient_SkipBorrow_1DigitMul),
            Triple(90, 8, DivisionPattern.TensQuotient_SkipBorrow_1DigitMul),
            Triple(93, 8, DivisionPattern.TensQuotient_SkipBorrow_1DigitMul),

            Triple(53, 6, DivisionPattern.OnesQuotient_Borrow),
            Triple(62, 7, DivisionPattern.OnesQuotient_Borrow),

            Triple(12, 3, DivisionPattern.OnesQuotient_NoBorrow),
            Triple(24, 7, DivisionPattern.OnesQuotient_NoBorrow),
            Triple(39, 4, DivisionPattern.OnesQuotient_NoBorrow),
            Triple(49, 5, DivisionPattern.OnesQuotient_NoBorrow),
            Triple(54, 9, DivisionPattern.OnesQuotient_NoBorrow),
            Triple(68, 9, DivisionPattern.OnesQuotient_NoBorrow),
            Triple(81, 9, DivisionPattern.OnesQuotient_NoBorrow)
        )

        for ((dividend, divisor, expectedPattern) in cases) {
            val viewModel = DivisionViewModel(autoStart = false)
            viewModel.startNewProblem(dividend, divisor)
            val actualPattern = viewModel.uiState.value.pattern
            println("✅ $dividend ÷ $divisor → expected: $expectedPattern, actual: $actualPattern")

            assertEquals(
                "$dividend ÷ $divisor: 예상=$expectedPattern, 결과=$actualPattern",
                expectedPattern,
                actualPattern
            )
        }
    }

    @Test
    fun twoDigitDivByOneDigitTest() = runTest {
        val cases = listOf(
            // TensQuotient_NoBorrow_2DigitMul
            Triple("TensQuotient_NoBorrow_2DigitMul: 46 ÷ 3", 46 to 3, listOf("1", "3", "1", "6", "5", "1", "5", "1")),
            Triple("TensQuotient_NoBorrow_2DigitMul: 72 ÷ 6", 72 to 6, listOf("1", "6", "1", "2", "2", "1", "2", "0")),
            Triple("TensQuotient_NoBorrow_2DigitMul: 74 ÷ 6", 74 to 6, listOf("1", "6", "1", "4", "2", "1", "2", "2")),
            Triple("TensQuotient_NoBorrow_2DigitMul: 85 ÷ 7", 85 to 7, listOf("1", "7", "1", "5", "2", "1", "4", "1")),
            Triple("TensQuotient_NoBorrow_2DigitMul: 86 ÷ 7", 86 to 7, listOf("1", "7", "1", "6", "2", "1", "4", "2")),
            Triple("TensQuotient_NoBorrow_2DigitMul: 92 ÷ 7", 92 to 7, listOf("1", "7", "2", "2", "3", "2", "1", "1")),
            Triple("TensQuotient_NoBorrow_2DigitMul: 96 ÷ 4", 96 to 4, listOf("2", "8", "1", "6", "4", "1", "6", "0")),

            // TensQuotient_NoBorrow_1DigitMul
            Triple("TensQuotient_NoBorrow_1DigitMul: 45 ÷ 4", 45 to 4, listOf("1", "4", "0", "5", "1", "4", "1")),
            Triple("TensQuotient_NoBorrow_1DigitMul: 57 ÷ 5", 57 to 5, listOf("1", "5", "0", "7", "1", "5", "2")),
            Triple("TensQuotient_NoBorrow_1DigitMul: 84 ÷ 4", 84 to 4, listOf("2", "8", "0", "4", "1", "4", "0")),

            // TensQuotient_Borrow_2DigitMul
            Triple("TensQuotient_Borrow_2DigitMul: 50 ÷ 3", 50 to 3, listOf("1", "3", "2", "0", "6", "1", "8", "1", "2")),

            // TensQuotient_SkipBorrow_1DigitMul
            Triple("TensQuotient_SkipBorrow_1DigitMul: 71 ÷ 6", 71 to 6, listOf("1", "6", "1", "1", "1", "6", "5")),
            Triple("TensQuotient_SkipBorrow_1DigitMul: 90 ÷ 8", 90 to 8, listOf("1", "8", "1", "0", "1", "8", "2")),
            Triple("TensQuotient_SkipBorrow_1DigitMul: 93 ÷ 8", 93 to 8, listOf("1", "8", "1", "3", "1", "8", "5")),

            // OnesQuotient_Borrow
            Triple("OnesQuotient_Borrow: 53 ÷ 6", 53 to 6, listOf("8", "4", "8", "4", "5")),
            Triple("OnesQuotient_Borrow: 62 ÷ 7", 62 to 7, listOf("8", "5", "6", "5", "6")),

            // OnesQuotient_NoBorrow
            Triple("OnesQuotient_NoBorrow: 12 ÷ 3", 12 to 3, listOf("4", "1", "2", "0")),
            Triple("OnesQuotient_NoBorrow: 24 ÷ 7", 24 to 7, listOf("3", "2", "1", "3")),
            Triple("OnesQuotient_NoBorrow: 39 ÷ 4", 39 to 4, listOf("9", "3", "6", "3")),
            Triple("OnesQuotient_NoBorrow: 49 ÷ 5", 49 to 5, listOf("9", "4", "5", "4")),
            Triple("OnesQuotient_NoBorrow: 54 ÷ 9", 54 to 9, listOf("6", "5", "4", "0")),
            Triple("OnesQuotient_NoBorrow: 68 ÷ 9", 68 to 9, listOf("7", "6", "3", "5")),
            Triple("OnesQuotient_NoBorrow: 81 ÷ 9", 81 to 9, listOf("9", "8", "1", "0"))
        )
        for ((name, pair, inputs) in cases) {
            val (dividend, divisor) = pair
            val expectedPatternName = name.substringBefore(":").trim()

            val viewModel = DivisionViewModel(autoStart = false)
            viewModel.uiState.test {
                try {
                    viewModel.startNewProblem(dividend, divisor)

                    awaitItem() // 초기 emit 무시
                    var state = awaitItem()

                    val actualPattern = state.pattern?.name ?: "null"
                    println("[$name] ⏱ expected=$expectedPatternName, actual=$actualPattern")
                    assertEquals(
                        "$name: 패턴 불일치! expected=$expectedPatternName, actual=$actualPattern",
                        expectedPatternName,
                        actualPattern
                    )

                    for (i in inputs.indices) {
                        viewModel.submitInput(inputs[i])
                        state = awaitItem()
                        assertEquals("$name: ${i + 1}번째 입력 오답! (${inputs[i]})", null, state.feedback)
                    }

                    val isCompletePhase = state.phases.getOrNull(state.currentPhaseIndex) == DivisionPhase.Complete
                    assertTrue("$name: Complete phase 미도달", isCompletePhase)

                } finally {
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

}