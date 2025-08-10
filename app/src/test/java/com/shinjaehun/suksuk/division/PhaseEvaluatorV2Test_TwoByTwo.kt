package com.shinjaehun.suksuk.division

import com.shinjaehun.suksuk.division.PhaseEvaluatorV2Test_ThreeByTwo.PhaseInputTestCase
import com.shinjaehun.suksuk.domain.division.DivisionInfoBuilder
import com.shinjaehun.suksuk.domain.division.DivisionStateInfo
import com.shinjaehun.suksuk.domain.division.model.CrossOutType
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import com.shinjaehun.suksuk.domain.division.evaluator.PhaseEvaluatorV2
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
        val info: DivisionStateInfo,
        val stepIndex: Int,
        val previousInputs: List<String>,
        val shouldBeCorrect: Boolean
    )

    val info68x34 = DivisionInfoBuilder.from(68, 34)
    val info96x12 = DivisionInfoBuilder.from(96, 12)

    val cases = listOf(
        // 68 ÷ 34
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient, CellName.QuotientOnes, "2", info68x34, 0, listOf(), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, CellName.Multiply1Ones, "8", info68x34, 1, listOf("2"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, CellName.Multiply1Tens, "6", info68x34, 2, listOf("2", "8"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract1, CellName.Subtract1Ones, "0", info68x34, 3, listOf("2", "8", "6"), true),
        // 오답 케이스
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient, CellName.QuotientOnes, "3", info68x34, 0, listOf(), false),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, CellName.Multiply1Ones, "7", info68x34, 1, listOf("2"), false),
        // 96 ÷ 12, 몫 8, 곱셈 Carry 발생 (16)
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient, CellName.QuotientOnes, "8", info96x12, 0, listOf(), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, CellName.CarryDivisorTensMul1, "1", info96x12, 1, listOf("8"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, CellName.Multiply1Ones, "6", info96x12, 2, listOf("8", "1"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, CellName.Multiply1Tens, "9", info96x12, 3, listOf("8", "1", "6"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract1, CellName.Subtract1Ones, "0", info96x12, 4, listOf("8", "1", "6", "9"), true),
    )

    @Test
    fun test_PhaseEvaluatorV2_batchCases() {
        val evaluator = PhaseEvaluatorV2()

        cases.forEach { c ->
            val result = evaluator.isCorrect(
                phase = c.phase,
                cell = c.cell,
                input = c.input,
                info = c.info,
                stepIndex = c.stepIndex,
                previousInputs = c.previousInputs
            )
            assertEquals(
                "phase=${c.phase}, cell=${c.cell}, input=${c.input}, step=${c.stepIndex}, prevInputs=${c.previousInputs}",
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

        val info = DivisionInfoBuilder.from(dividend, divisor)

        val result = evaluator.isCorrect(
            phase, cell, userInput, info, stepIndex, previousInputs
        )

        assertTrue(result)
    }
}