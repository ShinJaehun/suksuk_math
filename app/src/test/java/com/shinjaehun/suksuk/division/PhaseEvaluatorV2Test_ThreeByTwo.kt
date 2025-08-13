package com.shinjaehun.suksuk.division

import com.shinjaehun.suksuk.domain.division.DivisionInfoBuilder
import com.shinjaehun.suksuk.domain.division.DivisionStateInfo
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
        val info: DivisionStateInfo,
        val stepIndex: Int,
        val previousInputs: List<String>,
        val shouldBeCorrect: Boolean
    )


    val info432x12 = DivisionInfoBuilder.from(432, 12)
    val info864x24 = DivisionInfoBuilder.from(864, 24)
    val info360x27 = DivisionInfoBuilder.from(360, 27)

    val cases = listOf(
        // 432 ÷ 12 = 36 (기본, Carry/Borrow 없음)
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient,  CellName.QuotientTens,      "3", info432x12, 0, listOf(), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, CellName.Multiply1Tens,     "6", info432x12, 1, listOf("3"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, CellName.Multiply1Hundreds, "3", info432x12, 2, listOf("3","6"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputBorrow,    CellName.BorrowDividendHundreds, "3", info432x12, 3, listOf("3", "6", "3"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract1, CellName.Subtract1Tens,     "7", info432x12, 4, listOf("3","6","3","3"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract1, CellName.Subtract1Hundreds, "0", info432x12, 5, listOf("3","6","3","3","7"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputBringDown, CellName.Subtract1Ones,     "2", info432x12, 6, listOf("3","6","3","3","7","0"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient,  CellName.QuotientOnes,      "6", info432x12, 7, listOf("3","6","3","3","7","0","2"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply2, CellName.Multiply2Ones,     "2", info432x12, 8, listOf("3","6","3","3","7","0","2","6"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply2, CellName.Multiply2Tens,     "7", info432x12, 9, listOf("3","6","3","3","7","0","2","6","2"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract2, CellName.Subtract2Ones,     "0", info432x12, 10, listOf("3","6","3","3","7","0","2","6","2","6"), true),
        //  3by2: mul1/mul2 carry, subtract1/2 borrow
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient,  CellName.QuotientTens,      "3",  info864x24, 0, listOf(), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, CellName.CarryDivisorTensM1, "1", info864x24, 1, listOf("3"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, CellName.Multiply1Tens,     "2",  info864x24, 2, listOf("3","1"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply1, CellName.Multiply1Hundreds, "7",  info864x24, 3, listOf("3","1","2"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract1, CellName.Subtract1Tens,     "4",  info864x24, 4, listOf("3","1","2","7"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract1, CellName.Subtract1Hundreds, "1",  info864x24, 5, listOf("3","1","2","7","4"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputBringDown, CellName.Subtract1Ones,     "4",  info864x24, 6, listOf("3","1","2","7","4","1"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputQuotient,  CellName.QuotientOnes,      "6",  info864x24, 7, listOf("3","1","2","7","4","1","4"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply2, CellName.CarryDivisorTensM2, "2", info864x24, 8, listOf("3","1","2","7","4","1","4","6"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply2, CellName.Multiply2Ones,     "4", info864x24, 9, listOf("3","1","2","7","4","1","4","6","2"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply2, CellName.Multiply2Hundreds,     "1",  info864x24, 10, listOf("3","1","2","7","4","1","4","6","2","4"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputMultiply2, CellName.Multiply2Tens,     "4",  info864x24, 10, listOf("3","1","2","7","4","1","4","6","2","4","1"), true),
        PhaseInputTestCase(DivisionPhaseV2.InputSubtract2, CellName.Subtract2Ones,     "0",  info864x24, 11, listOf("3","1","2","7","4","1","4","6","2","4","1","4"), true),
        // 360 ÷ 27 = 13 … 9 (subtract1 & subtract2 borrow)
//        PhaseInputTestCase(DivisionPhaseV2.InputQuotient,  CellName.QuotientTens, "1", info360x27, 0, listOf(), true),
//        PhaseInputTestCase(DivisionPhaseV2.InputBorrow,    CellName.BorrowDividendHundreds, "2", info360x27, 1, listOf("1"), true),
//        PhaseInputTestCase(DivisionPhaseV2.InputSubtract1, CellName.Subtract1Tens, "9", info360x27, 2, listOf("1","2"), true),
//        PhaseInputTestCase(DivisionPhaseV2.InputBringDown, CellName.Subtract1Ones, "0", info360x27, 3, listOf("1","2","9"), true),
//        PhaseInputTestCase(DivisionPhaseV2.InputQuotient,  CellName.QuotientOnes, "3", info360x27, 4, listOf("1","2","9","0"), true),
//        PhaseInputTestCase(DivisionPhaseV2.InputMultiply2, CellName.CarryDivisorTensMul2, "2", info360x27, 5, listOf("1","2","9","0","3"), true),
//        PhaseInputTestCase(DivisionPhaseV2.InputMultiply2, CellName.Multiply2Ones, "1", info360x27, 6, listOf("1","2","9","0","3","2"), true),
//        PhaseInputTestCase(DivisionPhaseV2.InputMultiply2, CellName.Multiply2Tens, "8", info360x27, 7, listOf("1","2","9","0","3","2","1"), true),
//        PhaseInputTestCase(DivisionPhaseV2.InputBorrow,    CellName.BorrowSubtract1Tens, "8", info360x27, 5, listOf("1","2","9","0","3","2","1","8"), true),
//        PhaseInputTestCase(DivisionPhaseV2.InputSubtract2, CellName.Subtract2Ones, "9", info360x27, 8, listOf("1","2","9","0","3","2","1","8","8"), true),
    )

    @Test
    fun test_PhaseEvaluatorV2_ThreeByTwo_batchCases() {
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
                "phase=${c.phase}, cell=${c.cell}, input=${c.input}, info=${c.info}, step=${c.stepIndex}, prevInputs=${c.previousInputs}",
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
        val info = DivisionInfoBuilder.from(dividend, divisor)

        val result = evaluator.isCorrect(
            phase, cell, userInput, info, stepIndex, previousInputs
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
        val info = DivisionInfoBuilder.from(dividend, divisor)

        val result = evaluator.isCorrect(
            phase, cell, userInput, info, stepIndex, previousInputs
        )

        assertFalse(result)
    }
}
