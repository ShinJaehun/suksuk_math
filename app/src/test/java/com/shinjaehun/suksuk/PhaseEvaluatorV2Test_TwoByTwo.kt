package com.shinjaehun.suksuk

import com.shinjaehun.suksuk.domain.division.CrossOutType
import com.shinjaehun.suksuk.domain.division.DivisionPhaseV2
import com.shinjaehun.suksuk.domain.division.PhaseEvaluatorV2
import com.shinjaehun.suksuk.domain.division.model.CellName
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class PhaseEvaluatorV2Test_TwoByTwo {

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

    val cases = listOf(
        // 68 ÷ 34의 단계별 예시
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient, CellName.QuotientOnes, "2", 68, 34, 0, listOf(), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply, CellName.Multiply1Ones, "8", 68, 34, 1, listOf("2"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply, CellName.Multiply1Tens, "6", 68, 34, 2, listOf("2", "8"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract, CellName.Subtract1Ones, "0", 68, 34, 3, listOf("2", "8", "6"), true),
        // 오답 케이스
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient, CellName.QuotientOnes, "3", 68, 34, 0, listOf(), false),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply, CellName.Multiply1Ones, "7", 68, 34, 1, listOf("2"), false),

        // 96 ÷ 12, 몫 8, 곱셈 Carry 발생 (16)
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient, CellName.QuotientOnes, "8", 96, 12, 0, listOf(), true),
        // Carry를 먼저 입력 (1)
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply, CellName.CarryDivisorTens, "1", 96, 12, 1, listOf("8"), true),
        // Ones 입력 (6)
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply, CellName.Multiply1Ones, "6", 96, 12, 2, listOf("8", "1"), true),
        // Tens 입력 (9)
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply, CellName.Multiply1Tens, "9", 96, 12, 3, listOf("8", "1", "6"), true),
        // 뺄셈 (0)
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract, CellName.Subtract1Ones, "0", 96, 12, 4, listOf("8", "1", "6", "9"), true),
    )

    @Test
    fun test_PhaseEvaluatorV2_batchCases() {
        val evaluator = PhaseEvaluatorV2()

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
    fun multiply1OnesCell_shows_correct_value_after_input() {
        val uiState = makeUiStateForTest(
            dividend = 68, divisor = 34,
            step = 1,  // Multiply1Ones 단계
            inputs = listOf("2", "8")
        )
        assertEquals("8", uiState.cells[CellName.Multiply1Ones]?.value)
    }

    @Test
    fun uiState_shows_expected_values_at_step() {
        val uiState = makeUiStateForTest(
            dividend = 68, divisor = 34,
            step = 2, // 예시: Multiply1Tens 단계
            inputs = listOf("2", "8", "6")
        )
        assertEquals("6", uiState.cells[CellName.Multiply1Tens]?.value)
    }

    @Test
    fun multiply1TensCell_shows_question_when_editing_and_no_input() {
        val uiState = makeUiStateForTest(
            dividend = 68, divisor = 34,
            step = 2,
            inputs = listOf("2", "8") // 아직 Multiply1Tens 미입력
        )
        assertEquals("?", uiState.cells[CellName.Multiply1Tens]?.value)
    }

    @Test
    fun borrowCell_shows_borrowed_value_and_crossout() {
        val uiState = makeUiStateForTest(
            dividend = 52, divisor = 34,
            step = 3,  // Borrow 단계로 가정
            inputs = listOf("1", "4", "3", "4")
        )
        assertEquals("4", uiState.cells[CellName.BorrowDividendTens]?.value)
        assertEquals(CrossOutType.Pending, uiState.cells[CellName.DividendTens]?.crossOutType)
    }

    @Test
    fun isCorrect_returns_true_for_correct_input() {
        val phase = DivisionPhaseV2.InputQuotient
        val cell = CellName.QuotientOnes
        val dividend = 68
        val divisor = 34
        val userInput = "2"
        val stepIndex = 0
        val previousInputs = emptyList<String>()

        val result = evaluator.isCorrect(
            phase, cell, userInput, dividend, divisor, stepIndex, previousInputs
        )

        assertTrue(result)
    }
}