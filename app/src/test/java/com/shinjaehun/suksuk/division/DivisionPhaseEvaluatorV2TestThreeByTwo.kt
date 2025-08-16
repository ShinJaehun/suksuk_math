package com.shinjaehun.suksuk.division

import com.shinjaehun.suksuk.domain.division.info.DivisionStateInfoBuilder
import com.shinjaehun.suksuk.domain.division.info.DivisionStateInfo
import com.shinjaehun.suksuk.domain.division.evaluator.DivisionPhaseEvaluatorV2
import com.shinjaehun.suksuk.domain.division.model.DivisionCell
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class DivisionPhaseEvaluatorV2TestThreeByTwo {

    private val evaluator = DivisionPhaseEvaluatorV2()
    private val infoBuilder = DivisionStateInfoBuilder

    data class PhaseInputTestCase(
        val phase: DivisionPhaseV2,
        val cell: DivisionCell,
        val input: String,
        val info: DivisionStateInfo,
        val stepIndex: Int,
        val previousInputs: List<String>,
        val shouldBeCorrect: Boolean
    )

    private val info432x12 = infoBuilder.from(432, 12)
    private val info864x24 = infoBuilder.from(864, 24)

    private val cases = listOf(
        // 432 ÷ 12 = 36 (기본, Carry/Borrow 없음)
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient,  DivisionCell.QuotientTens,      "3", info432x12, 0, listOf(), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, DivisionCell.Multiply1Tens,     "6", info432x12, 1, listOf("3"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, DivisionCell.Multiply1Hundreds, "3", info432x12, 2, listOf("3","6"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputBorrow,    DivisionCell.BorrowDividendHundreds, "3", info432x12, 3, listOf("3", "6", "3"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract1, DivisionCell.Subtract1Tens,     "7", info432x12, 4, listOf("3","6","3","3"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract1, DivisionCell.Subtract1Hundreds, "0", info432x12, 5, listOf("3","6","3","3","7"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputBringDown, DivisionCell.Subtract1Ones,     "2", info432x12, 6, listOf("3","6","3","3","7","0"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient,  DivisionCell.QuotientOnes,      "6", info432x12, 7, listOf("3","6","3","3","7","0","2"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply2, DivisionCell.Multiply2Ones,     "2", info432x12, 8, listOf("3","6","3","3","7","0","2","6"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply2, DivisionCell.Multiply2Tens,     "7", info432x12, 9, listOf("3","6","3","3","7","0","2","6","2"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract2, DivisionCell.Subtract2Ones,     "0", info432x12, 10, listOf("3","6","3","3","7","0","2","6","2","6"), true),
        //  3by2: mul1/mul2 carry, subtract1/2 borrow
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient,  DivisionCell.QuotientTens,      "3",  info864x24, 0, listOf(), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, DivisionCell.CarryDivisorTensM1, "1", info864x24, 1, listOf("3"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, DivisionCell.Multiply1Tens,     "2",  info864x24, 2, listOf("3","1"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, DivisionCell.Multiply1Hundreds, "7",  info864x24, 3, listOf("3","1","2"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract1, DivisionCell.Subtract1Tens,     "4",  info864x24, 4, listOf("3","1","2","7"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract1, DivisionCell.Subtract1Hundreds, "1",  info864x24, 5, listOf("3","1","2","7","4"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputBringDown, DivisionCell.Subtract1Ones,     "4",  info864x24, 6, listOf("3","1","2","7","4","1"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient,  DivisionCell.QuotientOnes,      "6",  info864x24, 7, listOf("3","1","2","7","4","1","4"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply2, DivisionCell.CarryDivisorTensM2, "2", info864x24, 8, listOf("3","1","2","7","4","1","4","6"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply2, DivisionCell.Multiply2Ones,     "4", info864x24, 9, listOf("3","1","2","7","4","1","4","6","2"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply2, DivisionCell.Multiply2Hundreds,     "1",  info864x24, 10, listOf("3","1","2","7","4","1","4","6","2","4"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply2, DivisionCell.Multiply2Tens,     "4",  info864x24, 10, listOf("3","1","2","7","4","1","4","6","2","4","1"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract2, DivisionCell.Subtract2Ones,     "0",  info864x24, 11, listOf("3","1","2","7","4","1","4","6","2","4","1","4"), true),
    )

    @Test
    fun test_PhaseEvaluatorV2_ThreeByTwo_batchCases() {
        cases.forEach { c ->
            val result = evaluator.isCorrect(
                phase = c.phase,
                cell = c.cell,
                input = c.input,
                info = c.info,
            )
            assertEquals(
                "phase=${c.phase}, cell=${c.cell}, input=${c.input}, info=${c.info}, step=${c.stepIndex}",
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
        assertEquals("6", uiState.cells[DivisionCell.Multiply1Tens]?.value)
    }

    @Test
    fun multiply2TensAndOnesCell_shows_question_when_editing_and_no_input() {
        val uiState = makeUiStateForTest(
            dividend = 432, divisor = 12,
//            step = 8, // Multiply2Tens 단계
            step = 9, // Multiply2Tens 단계 <- PrepareMul2 단계 추가

            inputs = listOf("3", "6", "3", "3", "7", "2", "6", "12", "7")
        )
        assertEquals("?", uiState.cells[DivisionCell.Multiply2Tens]?.value)
    }

    @Test
    fun isCorrect_returns_true_for_correct_input() {
        val phase = DivisionPhaseV2.InputQuotient
        val cell = DivisionCell.QuotientTens
        val dividend = 432
        val divisor = 12
        val userInput = "3"
        val info = infoBuilder.from(dividend, divisor)

        val result = evaluator.isCorrect(
            phase, cell, userInput, info
        )

        assertTrue(result)
    }

    @Test
    fun isCorrect_returns_false_for_wrong_input() {
        val phase = DivisionPhaseV2.InputQuotient
        val cell = DivisionCell.QuotientTens
        val dividend = 432
        val divisor = 12
        val userInput = "2"
        val info = infoBuilder.from(dividend, divisor)

        val result = evaluator.isCorrect(
            phase, cell, userInput, info
        )

        assertFalse(result)
    }
}
