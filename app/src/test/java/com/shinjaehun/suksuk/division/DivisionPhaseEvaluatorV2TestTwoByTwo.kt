package com.shinjaehun.suksuk.division

import com.shinjaehun.suksuk.domain.division.info.DivisionStateInfoBuilder
import com.shinjaehun.suksuk.domain.division.info.DivisionStateInfo
import com.shinjaehun.suksuk.presentation.division.model.CrossOutType
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import com.shinjaehun.suksuk.domain.division.evaluator.DivisionPhaseEvaluatorV2
import com.shinjaehun.suksuk.domain.division.model.DivisionCellName
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class DivisionPhaseEvaluatorV2TestTwoByTwo {

    private val evaluator = DivisionPhaseEvaluatorV2()
    private val infoBuilder = DivisionStateInfoBuilder

    data class PhaseInputTestCase(
        val phase: DivisionPhaseV2,
        val cell: DivisionCellName,
        val input: String,
        val info: DivisionStateInfo,
        val stepIndex: Int,
        val previousInputs: List<String>,
        val shouldBeCorrect: Boolean
    )

    private val info68x34 = infoBuilder.from(68, 34)
    private val info96x12 = infoBuilder.from(96, 12)

    private val cases = listOf(
        // 68 ÷ 34
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient, DivisionCellName.QuotientOnes, "2", info68x34, 0, listOf(), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, DivisionCellName.Multiply1Ones, "8", info68x34, 1, listOf("2"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, DivisionCellName.Multiply1Tens, "6", info68x34, 2, listOf("2", "8"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract1, DivisionCellName.Subtract1Ones, "0", info68x34, 3, listOf("2", "8", "6"), true),
        // 오답 케이스
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient, DivisionCellName.QuotientOnes, "3", info68x34, 0, listOf(), false),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, DivisionCellName.Multiply1Ones, "7", info68x34, 1, listOf("2"), false),
        // 96 ÷ 12, 몫 8, 곱셈 Carry 발생 (16)
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient, DivisionCellName.QuotientOnes, "8", info96x12, 0, listOf(), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, DivisionCellName.CarryDivisorTensM1, "1", info96x12, 1, listOf("8"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, DivisionCellName.Multiply1Ones, "6", info96x12, 2, listOf("8", "1"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, DivisionCellName.Multiply1Tens, "9", info96x12, 3, listOf("8", "1", "6"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract1, DivisionCellName.Subtract1Ones, "0", info96x12, 4, listOf("8", "1", "6", "9"), true),
    )

    @Test
    fun test_PhaseEvaluatorV2_batchCases() {
        val evaluator = DivisionPhaseEvaluatorV2()

        cases.forEach { c ->
            val result = evaluator.isCorrect(
                phase = c.phase,
                cell = c.cell,
                input = c.input,
                info = c.info,
            )
            assertEquals(
                "phase=${c.phase}, cell=${c.cell}, input=${c.input}",
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
        assertEquals("8", uiState.cells[DivisionCellName.Multiply1Ones]?.value)
    }

    @Test
    fun uiState_shows_expected_values_at_step() {
        val uiState = makeUiStateForTest(
            dividend = 68, divisor = 34,
            step = 2, // 예시: Multiply1Tens 단계
            inputs = listOf("2", "8", "6")
        )
        assertEquals("6", uiState.cells[DivisionCellName.Multiply1Tens]?.value)
    }

    @Test
    fun multiply1TensCell_shows_question_when_editing_and_no_input() {
        val uiState = makeUiStateForTest(
            dividend = 68, divisor = 34,
            step = 2,
            inputs = listOf("2", "8") // 아직 Multiply1Tens 미입력
        )
        assertEquals("?", uiState.cells[DivisionCellName.Multiply1Tens]?.value)
    }

    @Test
    fun borrowCell_shows_borrowed_value_and_crossout() {
        val uiState = makeUiStateForTest(
            dividend = 52, divisor = 34,
            step = 3,  // Borrow 단계로 가정
            inputs = listOf("1", "4", "3", "4")
        )
        assertEquals("4", uiState.cells[DivisionCellName.BorrowDividendTens]?.value)
        assertEquals(CrossOutType.Pending, uiState.cells[DivisionCellName.DividendTens]?.crossOutType)
    }

    @Test
    fun isCorrect_returns_true_for_correct_input() {
        val phase = DivisionPhaseV2.InputQuotient
        val cell = DivisionCellName.QuotientOnes
        val dividend = 68
        val divisor = 34
        val userInput = "2"

        val info = infoBuilder.from(dividend, divisor)

        val result = evaluator.isCorrect(
            phase, cell, userInput, info
        )

        assertTrue(result)
    }
}