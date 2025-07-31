package com.shinjaehun.suksuk

import com.shinjaehun.suksuk.domain.PhaseEvaluator
import com.shinjaehun.suksuk.presentation.division.DivisionPhase
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class PhaseEvaluatorTest {

    private val evaluator = PhaseEvaluator()

    @Test
    fun isCorrect_TensQuotientNoBorrow2DigitMul() {
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientTens, "1", 46, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Tens, "3", 46, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Tens, "1", 46, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputBringDownFromDividendOnes, "6", 46, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "5", 46, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply2Total, "15", 46, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract2Result, "1", 46, 3))
    }

    @Test
    fun isCorrect_TensQuotientBorrow2DigitMul() {
        // 예: 50 ÷ 3 (몫 1, 곱 3, 뺄셈 2, 가져내리기 0, 일의자리몫 6, 곱 18, borrow(1), 뺄셈 2)
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientTens, "1", 50, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Tens, "3", 50, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Tens, "2", 50, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputBringDownFromDividendOnes, "0", 50, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "6", 50, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply2Total, "18", 50, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputBorrowFromSubtract1Tens, "1", 50, 3)) // borrow phase
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract2Result, "2", 50, 3))
    }

    @Test
    fun isCorrect_TensQuotientNoBorrow1DigitMul() {
        // 예: 45 ÷ 4 (몫 1, 곱 4, 뺄셈 0, 가져내리기 5, 일의자리몫 1, 곱 4, 뺄셈 1)
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientTens, "1", 45, 4))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Tens, "4", 45, 4))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Tens, "0", 45, 4))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputBringDownFromDividendOnes, "5", 45, 4))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "1", 45, 4))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply2Ones, "4", 45, 4))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract2Result, "1", 45, 4))
    }

    @Test
    fun isCorrect_TensQuotientSkipBorrow1DigitMul() {
        // 예: 71 ÷ 6 (몫 1, 곱 6, 뺄셈 1, 가져내리기 1, 일의자리몫 1, 곱 6, 뺄셈 5)
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientTens, "1", 71, 6))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Tens, "6", 71, 6))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Tens, "1", 71, 6))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputBringDownFromDividendOnes, "1", 71, 6))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "1", 71, 6))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply2Ones, "6", 71, 6))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract2Result, "5", 71, 6))
    }

    @Test
    fun isCorrect_OnesQuotientNoBorrow2DigitMul() {
        // 예: 24 ÷ 7 (몫 3, 곱 21, 뺄셈 3)
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotient, "3", 24, 7))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Total, "21", 24, 7))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Result, "3", 24, 7))
    }

    @Test
    fun isCorrect_OnesQuotientBorrow2DigitMul() {
        // 예: 62 ÷ 7 (몫 8, 곱 56, borrow(5), 뺄셈 6)
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotient, "8", 62, 7))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Total, "56", 62, 7))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputBorrowFromDividendTens, "5", 62, 7))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Result, "6", 62, 7))
    }

    @Test
    fun isCorrect_returns_false_for_wrong_answers() {
        assertFalse(evaluator.isCorrect(DivisionPhase.InputQuotientTens, "5", 46, 3))
        assertFalse(evaluator.isCorrect(DivisionPhase.InputMultiply1Total, "99", 46, 3))
    }
}