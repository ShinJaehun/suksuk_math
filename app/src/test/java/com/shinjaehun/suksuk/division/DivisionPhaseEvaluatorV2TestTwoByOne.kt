package com.shinjaehun.suksuk.division

import com.shinjaehun.suksuk.domain.division.info.DivisionStateInfoBuilder
import com.shinjaehun.suksuk.domain.division.info.DivisionStateInfo
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import com.shinjaehun.suksuk.domain.division.evaluator.DivisionPhaseEvaluatorV2
import com.shinjaehun.suksuk.domain.division.model.DivisionCellName
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class DivisionPhaseEvaluatorV2TestTwoByOne {

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

    private val info46x3 = infoBuilder.from(46, 3)
    private val info90x9 = infoBuilder.from(90, 9)
    private val info93x8 = infoBuilder.from(93, 8)


    private val cases = listOf(
        // 예시: 46 ÷ 3 (몫 15, 곱셈 15, 뺄셈 1, ...)
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient, DivisionCellName.QuotientTens, "1", info46x3, 0, listOf(), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, DivisionCellName.Multiply1Tens, "3", info46x3, 1, listOf("1"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract1, DivisionCellName.Subtract1Tens, "1", info46x3, 2, listOf("1", "3"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputBringDown, DivisionCellName.Subtract1Ones, "6", info46x3, 3, listOf("1", "3", "1"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient, DivisionCellName.QuotientOnes, "5", info46x3, 4, listOf("1", "3", "1", "6"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply2, DivisionCellName.Multiply2Tens, "1", info46x3, 5, listOf("1", "3", "1", "6", "5"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply2, DivisionCellName.Multiply2Ones, "5", info46x3, 6, listOf("1", "3", "1", "6", "5", "1"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract2, DivisionCellName.Subtract2Ones, "1", info46x3, 7, listOf("1", "3", "1", "6", "5", "1", "5"), true),
        // 2by1: quotientOnes == 0 → Multiply2/Subtract2 스킵 (90 ÷ 9 = 10)
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient,  DivisionCellName.QuotientTens,  "1", info90x9, 0, listOf(), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, DivisionCellName.Multiply1Tens, "9", info90x9, 1, listOf("1"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract1, DivisionCellName.Subtract1Tens, "0", info90x9, 2, listOf("1","9"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputBringDown, DivisionCellName.Subtract1Ones, "0", info90x9, 3, listOf("1","9","0"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient,  DivisionCellName.QuotientOnes,  "0", info90x9, 4, listOf("1","9","0","0"), true),
        // 2by1: skip-borrow (93 ÷ 8 = 11)
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient,  DivisionCellName.QuotientTens,  "1", info93x8, 0, listOf(), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, DivisionCellName.Multiply1Tens, "8", info93x8, 1, listOf("1"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract1, DivisionCellName.Subtract1Tens, "1", info93x8, 2, listOf("1","8"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputBringDown, DivisionCellName.Subtract1Ones, "3", info93x8, 3, listOf("1","8","1"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient,  DivisionCellName.QuotientOnes,  "1", info93x8, 4, listOf("1","8","1","3"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply2, DivisionCellName.Multiply2Ones,  "8", info93x8, 5, listOf("1","8","1","3","1"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract2, DivisionCellName.Subtract2Ones,  "5", info93x8, 6, listOf("1","8","1","3","1","8"), true)
    )

    @Test
    fun test_PhaseEvaluatorV2_TwoByOne_batchCases() {
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
    fun multiply1TensCell_shows_correct_value_after_input() {
        val uiState = makeUiStateForTest(
            dividend = 46, divisor = 3,
            step = 1,  // Multiply1Tens 단계
            inputs = listOf("1", "3")
        )
        assertEquals("3", uiState.cells[DivisionCellName.Multiply1Tens]?.value)
    }

    @Test
    fun multiply2TensAndOnesCell_shows_question_when_editing_and_no_input() {
        val uiState = makeUiStateForTest(
            dividend = 46, divisor = 3,
            step = 5, // Multiply2TensAndOnes 단계
            inputs = listOf("1", "3", "1", "6", "5")
        )
        assertEquals("?", uiState.cells[DivisionCellName.Multiply2Ones]?.value)
    }

    @Test
    fun isCorrect_returns_true_for_correct_input() {
        val phase = DivisionPhaseV2.InputQuotient
        val cell = DivisionCellName.QuotientTens
        val dividend = 46
        val divisor = 3
        val userInput = "1"

        val info = infoBuilder.from(dividend, divisor)

        val result = evaluator.isCorrect(
            phase, cell, userInput, info
        )

        assertTrue(result)
    }

    @Test
    fun isCorrect_returns_false_for_wrong_input() {
        val phase = DivisionPhaseV2.InputQuotient
        val cell = DivisionCellName.QuotientTens
        val dividend = 46
        val divisor = 3
        val userInput = "2"

        val info = infoBuilder.from(dividend, divisor)

        val result = evaluator.isCorrect(
            phase, cell, userInput, info
        )

        assertFalse(result)
    }
}
