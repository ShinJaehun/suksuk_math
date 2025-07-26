package com.shinjaehun.suksuk

import app.cash.turbine.test
import com.shinjaehun.suksuk.presentation.division.DivisionPhase
import com.shinjaehun.suksuk.presentation.division.DivisionPhasesState
import com.shinjaehun.suksuk.presentation.division.DivisionViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DivisionViewModelTest {

    @Test
    fun twoDigitDivByOneDigitTest() = runTest {
        val cases = listOf(
            Triple("Pattern D: 93 ÷ 8", 93 to 8, listOf("1", "8", "1", "3", "1", "8", "0", "5")),
            Triple("Pattern C: 50 ÷ 3", 50 to 3, listOf("1", "3", "2", "0", "6", "1", "8", "1", "2")),
            Triple("Pattern A: 72 ÷ 6", 72 to 6, listOf("1", "6", "1", "2", "2", "1", "2", "0")),
            Triple("Pattern A: 85 ÷ 7", 85 to 7, listOf("1", "7", "1", "5", "2", "1", "4", "1")),
            Triple("Pattern E: 53 ÷ 6", 53 to 6, listOf("8", "4", "8", "4", "5")),
            Triple("Pattern B: 45 ÷ 4", 45 to 4, listOf("1", "4", "0", "5", "1", "4", "1")),
            Triple("Pattern A: 86 ÷ 7", 86 to 7, listOf("1", "7", "1", "6", "2", "1", "4", "2")),
            Triple("Pattern B: 84 ÷ 4", 84 to 4, listOf("2", "8", "0", "4", "1", "4", "0")),
            Triple("Pattern E: 62 ÷ 7", 62 to 7, listOf("8", "5", "6", "5", "6")),
            Triple("Pattern A: 92 ÷ 7", 92 to 7, listOf("1", "7", "2", "2", "3", "2", "1", "1")),
            Triple("Pattern A: 96 ÷ 4", 96 to 4, listOf("2", "8", "1", "6", "4", "1", "6", "0")),
            Triple("Pattern F: 12 ÷ 3", 12 to 3, listOf("4", "1", "2", "0")),
            Triple("Pattern F: 24 ÷ 7", 24 to 7, listOf("3", "2", "1", "3")),
            Triple("Pattern A: 46 ÷ 3", 46 to 3, listOf("1", "3", "1", "6", "5", "1", "5", "1")),
            Triple("Pattern D: 71 ÷ 6", 71 to 6, listOf("1", "6", "1", "1", "1", "6", "0", "5")),
            Triple("Pattern D: 90 ÷ 8", 90 to 8, listOf("1", "8", "1", "0", "1", "8", "0", "2")),
            Triple("Pattern F: 68 ÷ 9", 68 to 9, listOf("7", "6", "3", "5")),
            Triple("Pattern F: 54 ÷ 9", 54 to 9, listOf("6", "5", "4", "0")),
            Triple("Pattern F: 81 ÷ 9", 81 to 9, listOf("9", "8", "1", "0")),
            Triple("Pattern F: 49 ÷ 5", 49 to 5, listOf("9", "4", "5", "4")),
            Triple("Pattern A: 74 ÷ 6", 74 to 6, listOf("1", "6", "1", "4", "2", "1", "2", "2")),
            Triple("Pattern B: 57 ÷ 5", 57 to 5, listOf("1", "5", "0", "7", "1", "5", "2")),
            Triple("Pattern F: 39 ÷ 4", 39 to 4, listOf("9", "3", "6", "3")),
        )
        for ((name, pair, inputs) in cases) {

            val (dividend, divisor) = pair
            val viewModel = DivisionViewModel()
            viewModel.uiState.test {
                viewModel.startNewProblem(dividend, divisor)
                var state = awaitItem() // 최초 상태
                println("[$name] 패턴: ${state.pattern}")

                // 마지막 입력 전까지 awaitItem() 사용
                for (i in inputs.indices) {
//                    println("입력값: ${inputs[i]}, currentPhase: ${state.phases[state.currentPhaseIndex]}, feedback: ${state.feedback}")
                    viewModel.submitInput(inputs[i])
                    // 마지막 입력이 아닐 때만 awaitItem()
                    if (i < inputs.lastIndex) {
                        state = awaitItem()
                        assertEquals("$name: 오답!", null, state.feedback)
                    }
                }
                // 마지막 입력 후 상태는 StateFlow에서 직접 읽음
                state = viewModel.uiState.value

                // 마지막 phase에 도달했는지 체크
                val isLastPhase = state.currentPhaseIndex == state.phases.lastIndex
                assertTrue("$name: 마지막 phase 미완료", isLastPhase)

                cancelAndIgnoreRemainingEvents()
            }
        }
    }

}