package com.shinjaehun.suksuk.division

import com.shinjaehun.suksuk.domain.division.evaluator.PhaseEvaluatorV2
import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class PhaseEvaluatorV2Test_ThreeByTwo {

    private val evaluator = PhaseEvaluatorV2()

    data class PhaseInputTestCase(
        val phase: DivisionPhaseV2,
        val cell: CellName,
        val input: String,
        val dividend: Int,
        val divisor: Int,
        val stepIndex: Int,
        val previousInputs: List<String>,
        val shouldBeCorrect: Boolean
    )

    // 432 ÷ 12 = 36 (기본, Carry/Borrow 없음)
    val cases = listOf(
        // [1] 몫 십의자리 입력 (3)
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient, CellName.QuotientTens, "3", 432, 12, 0, listOf(), true),
        // [2] 1차 곱셈 (3 × 2 = 6) - Tens
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, CellName.Multiply1Tens, "6", 432, 12, 1, listOf("3"), true),
        // [3] 1차 곱셈 (3 × 1 = 3) - Hundreds
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, CellName.Multiply1Hundreds, "3", 432, 12, 2, listOf("3", "6"), true),
        // [4] 1차 뺄셈 (43 - 36 = 7) - Tens
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract1, CellName.Subtract1Tens, "7", 432, 12, 3, listOf("3", "6", "3"), true),
        // [5] 1차 뺄셈 (4 - 3 = 1) - Hundreds
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract1, CellName.Subtract1Hundreds, "1", 432, 12, 4, listOf("3", "6", "3", "7"), true),
        // [6] Bring down Ones (2)
        PhaseInputTestCase(DivisionPhaseV2.InputBringDown, CellName.Subtract1Ones, "2", 432, 12, 5, listOf("3", "6", "3", "7", "1"), true),
        // [7] 몫 일의자리 입력 (6)
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient, CellName.QuotientOnes, "6", 432, 12, 6, listOf("3", "6", "3", "7", "1", "2"), true),
        // [8] 2차 곱셈 (6 × 2 = 12) - Ones
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, CellName.Multiply2Ones, "2", 432, 12, 7, listOf("3", "6", "3", "7", "1", "2", "6"), true),
        // [9] 2차 곱셈 (6 × 1 = 6) - Tens
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, CellName.Multiply2Tens, "6", 432, 12, 8, listOf("3", "6", "3", "7", "1", "2", "6", "2"), true),
        // [10] 2차 뺄셈 (12 - 12 = 0) - Ones
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract1, CellName.Subtract2Ones, "0", 432, 12, 9, listOf("3", "6", "3", "7", "1", "2", "6", "2", "6"), true),
    )

    @Test
    fun test_PhaseEvaluatorV2_ThreeByTwo_batchCases() {
        cases.forEach { c ->
            val result = evaluator.isCorrect(
                phase = c.phase,
                cell = c.cell,
                input = c.input,
                dividend = c.dividend,
                divisor = c.divisor,
                stepIndex = c.stepIndex,
                previousInputs = c.previousInputs
            )
            assertEquals(
                "phase=${c.phase}, cell=${c.cell}, input=${c.input}, dividend=${c.dividend}, divisor=${c.divisor}, step=${c.stepIndex}, prevInputs=${c.previousInputs}",
                c.shouldBeCorrect, result
            )
        }
    }

    @Test
    fun multiply1TensCell_shows_correct_value_after_input() {
        val uiState = makeUiStateForTest(
            dividend = 432, divisor = 12,
            step = 1,  // Multiply1Tens 단계
            inputs = listOf("3", "6")
        )
        assertEquals("6", uiState.cells[CellName.Multiply1Tens]?.value)
    }

    @Test
    fun multiply2TensAndOnesCell_shows_question_when_editing_and_no_input() {
        val uiState = makeUiStateForTest(
            dividend = 432, divisor = 12,
            step = 8, // Multiply2Tens 단계
            inputs = listOf("3", "6", "3", "7", "1", "2", "6", "2")
        )
        assertEquals("?", uiState.cells[CellName.Multiply2Tens]?.value)
    }

    @Test
    fun isCorrect_returns_true_for_correct_input() {
        val phase = DivisionPhaseV2.InputQuotient
        val cell = CellName.QuotientTens
        val dividend = 432
        val divisor = 12
        val userInput = "3"
        val stepIndex = 0
        val previousInputs = emptyList<String>()

        val result = evaluator.isCorrect(
            phase, cell, userInput, dividend, divisor, stepIndex, previousInputs
        )

        assertTrue(result)
    }

    @Test
    fun isCorrect_returns_false_for_wrong_input() {
        val phase = DivisionPhaseV2.InputQuotient
        val cell = CellName.QuotientTens
        val dividend = 432
        val divisor = 12
        val userInput = "2"
        val stepIndex = 0
        val previousInputs = emptyList<String>()

        val result = evaluator.isCorrect(
            phase, cell, userInput, dividend, divisor, stepIndex, previousInputs
        )

        assertFalse(result)
    }
}
