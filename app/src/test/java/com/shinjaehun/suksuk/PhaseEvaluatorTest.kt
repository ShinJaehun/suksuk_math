package com.shinjaehun.suksuk

import com.shinjaehun.suksuk.domain.division.evaluator.PhaseEvaluator
import com.shinjaehun.suksuk.domain.division.model.DivisionPhase
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class PhaseEvaluatorTest {

    private val evaluator = PhaseEvaluator()

    @Test
    fun isCorrect_TwoByOne_TensQuotientNoBorrow2DigitMul() {
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientTens, "1", 46, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Tens, "3", 46, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Tens, "1", 46, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputBringDownFromDividendOnes, "6", 46, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "5", 46, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply2TensAndMultiply2Ones, "15", 46, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract2Ones, "1", 46, 3))
    }

    @Test
    fun isCorrect_TwoByOne_TensQuotientBorrow2DigitMul() {
        // 예: 50 ÷ 3 (몫 1, 곱 3, 뺄셈 2, 가져내리기 0, 일의자리몫 6, 곱 18, borrow(1), 뺄셈 2)
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientTens, "1", 50, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Tens, "3", 50, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Tens, "2", 50, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputBringDownFromDividendOnes, "0", 50, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "6", 50, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply2TensAndMultiply2Ones, "18", 50, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputBorrowFromSubtract1Tens, "1", 50, 3)) // borrow phase
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract2Ones, "2", 50, 3))
    }

    @Test
    fun isCorrect_TwoByOne_TensQuotientNoBorrow1DigitMul() {
        // 예: 45 ÷ 4 (몫 1, 곱 4, 뺄셈 0, 가져내리기 5, 일의자리몫 1, 곱 4, 뺄셈 1)
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientTens, "1", 45, 4))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Tens, "4", 45, 4))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Tens, "0", 45, 4))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputBringDownFromDividendOnes, "5", 45, 4))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "1", 45, 4))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply2Ones, "4", 45, 4))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract2Ones, "1", 45, 4))
    }

    @Test
    fun isCorrect_TwoByOne_TensQuotientSkipBorrow1DigitMul() {
        // 예: 71 ÷ 6 (몫 1, 곱 6, 뺄셈 1, 가져내리기 1, 일의자리몫 1, 곱 6, 뺄셈 5)
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientTens, "1", 71, 6))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Tens, "6", 71, 6))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Tens, "1", 71, 6))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputBringDownFromDividendOnes, "1", 71, 6))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "1", 71, 6))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply2Ones, "6", 71, 6))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract2Ones, "5", 71, 6))
    }

    @Test
    fun isCorrect_TwoByOne_OnesQuotientNoBorrow2DigitMul() {
        // 예: 24 ÷ 7 (몫 3, 곱 21, 뺄셈 3)
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotient, "3", 24, 7))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1TensAndMultiply1Ones, "21", 24, 7))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Result, "3", 24, 7))
    }

    @Test
    fun isCorrect_TwoByOne_OnesQuotientBorrow2DigitMul() {
        // 예: 62 ÷ 7 (몫 8, 곱 56, borrow(5), 뺄셈 6)
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotient, "8", 62, 7))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1TensAndMultiply1Ones, "56", 62, 7))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputBorrowFromDividendTens, "5", 62, 7))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Result, "6", 62, 7))
    }

    @Test
    fun isCorrect_TwoByTwo_NoCarry_NoBorrow() {
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "2", 68, 34))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Ones, "8", 68, 34))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Tens, "6", 68, 34))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Ones, "0", 68, 34))
    }

    @Test
    fun isCorrect_TwoByTwo_NoCarry_Borrow() {
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "2", 50, 22))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Ones, "4", 50, 22))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Tens, "4", 50, 22))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputBorrowFromDividendTens, "4", 50, 22))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Ones, "6", 50, 22))
    }

    @Test
    fun isCorrect_TwoByTwo_Carry_NoBorrow() {
        // 96 ÷ 12 = 8
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "8", 96, 12))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1OnesWithCarry, "16", 96, 12))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Tens, "9", 96, 12))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Ones, "0", 96, 12))
    }

        @Test
    fun isCorrect_TwoByTwo_Carry_Borrow() {
        // 81 ÷ 12 = 6, 6 × 12 = 72, 81 - 72 = 9 (Borrow 발생)
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "6", 81, 12))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1OnesWithCarry, "12", 81, 12))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Tens, "7", 81, 12)) // 예시: 실제 구조에 따라
        assertTrue(evaluator.isCorrect(DivisionPhase.InputBorrowFromDividendTens, "7", 81, 12)) // 예시: 실제 구조에 따라
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Ones, "9", 81, 12))
    }

    @Test
    fun isCorrect_returns_false_for_wrong_answers() {
        assertFalse(evaluator.isCorrect(DivisionPhase.InputQuotientTens, "5", 46, 3))
        assertFalse(evaluator.isCorrect(DivisionPhase.InputMultiply1TensAndMultiply1Ones, "99", 46, 3))
    }
}