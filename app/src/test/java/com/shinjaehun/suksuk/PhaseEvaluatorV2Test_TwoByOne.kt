package com.shinjaehun.suksuk

import com.shinjaehun.suksuk.domain.division.DivisionPhaseV2
import com.shinjaehun.suksuk.domain.division.PhaseEvaluatorV2
import com.shinjaehun.suksuk.domain.division.model.CellName
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class PhaseEvaluatorV2Test_TwoByOne {

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

    // 예시: 46 ÷ 3 (몫 15, 곱셈 15, 뺄셈 1, ...)
    val cases = listOf(
        // 1단계: 몫 십의자리 입력
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient, CellName.QuotientTens, "1", 46, 3, 0, listOf(), true),
        // 2단계: 곱셈 (3 × 1 = 3)
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply, CellName.Multiply1Tens, "3", 46, 3, 1, listOf("1"), true),
        // 3단계: 뺄셈 (4 - 3 = 1)
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract, CellName.Subtract1Tens, "1", 46, 3, 2, listOf("1", "3"), true),
        // 4단계: 곱셈(일의자리 곱하고 아래로 내리기)
        PhaseInputTestCase(DivisionPhaseV2.InputBringDown, CellName.Subtract1Ones, "6", 46, 3, 3, listOf("1", "3", "1"), true),
        // 5단계: 몫 일의자리
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient, CellName.QuotientOnes, "5", 46, 3, 4, listOf("1", "3", "1", "6"), true),
        // 6단계: 곱셈(15)
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply, CellName.Multiply2Tens, "1", 46, 3, 5, listOf("1", "3", "1", "6", "5"), true),

        PhaseInputTestCase(DivisionPhaseV2.InputMultiply, CellName.Multiply2Ones, "5", 46, 3, 6, listOf("1", "3", "1", "6", "5", "1"), true),
        // 7단계: 뺄셈(1)
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract, CellName.Subtract2Ones, "1", 46, 3, 7, listOf("1", "3", "1", "6", "5", "1", "5"), true),
    )

    @Test
    fun test_PhaseEvaluatorV2_TwoByOne_batchCases() {
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
            dividend = 46, divisor = 3,
            step = 1,  // Multiply1Tens 단계
            inputs = listOf("1", "3")
        )
        assertEquals("3", uiState.cells[CellName.Multiply1Tens]?.value)
    }

    @Test
    fun multiply2TensAndOnesCell_shows_question_when_editing_and_no_input() {
        val uiState = makeUiStateForTest(
            dividend = 46, divisor = 3,
            step = 5, // Multiply2TensAndOnes 단계
            inputs = listOf("1", "3", "1", "6", "5")
        )
        assertEquals("?", uiState.cells[CellName.Multiply2Ones]?.value)
    }

    @Test
    fun isCorrect_returns_true_for_correct_input() {
        val phase = DivisionPhaseV2.InputQuotient
        val cell = CellName.QuotientTens
        val dividend = 46
        val divisor = 3
        val userInput = "1"
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
        val dividend = 46
        val divisor = 3
        val userInput = "2"
        val stepIndex = 0
        val previousInputs = emptyList<String>()

        val result = evaluator.isCorrect(
            phase, cell, userInput, dividend, divisor, stepIndex, previousInputs
        )

        assertFalse(result)
    }
}
