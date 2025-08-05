package com.shinjaehun.suksuk.legacy

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
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1OnesWithBringDownDividendOnes, "6", 46, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "5", 46, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply2TensAndMultiply2Ones, "15", 46, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract2Ones, "1", 46, 3))
    }

    @Test
    fun isCorrect_TwoByOne_TensQuotientBorrow2DigitMul() {
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientTens, "1", 50, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Tens, "3", 50, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Tens, "2", 50, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1OnesWithBringDownDividendOnes, "0", 50, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "6", 50, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply2TensAndMultiply2Ones, "18", 50, 3))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputBorrowFromSubtract1Tens, "1", 50, 3)) // borrow phase
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract2Ones, "2", 50, 3))
    }

    @Test
    fun isCorrect_TwoByOne_TensQuotientNoBorrow1DigitMul() {
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientTens, "1", 45, 4))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Tens, "4", 45, 4))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Tens, "0", 45, 4))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1OnesWithBringDownDividendOnes, "5", 45, 4))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "1", 45, 4))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply2Ones, "4", 45, 4))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract2Ones, "1", 45, 4))
    }

    @Test
    fun isCorrect_TwoByOne_TensQuotientSkipBorrow1DigitMul() {
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientTens, "1", 71, 6))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Tens, "6", 71, 6))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Tens, "1", 71, 6))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1OnesWithBringDownDividendOnes, "1", 71, 6))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "1", 71, 6))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply2Ones, "6", 71, 6))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract2Ones, "5", 71, 6))
    }

    @Test
    fun isCorrect_TwoByOne_OnesQuotientNoBorrow2DigitMul() {
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotient, "3", 24, 7))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1TensAndMultiply1Ones, "21", 24, 7))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Ones, "3", 24, 7))
    }

    @Test
    fun isCorrect_TwoByOne_OnesQuotientBorrow2DigitMul() {
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotient, "8", 62, 7))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1TensAndMultiply1Ones, "56", 62, 7))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputBorrowFromDividendTens, "5", 62, 7))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Ones, "6", 62, 7))
    }

    @Test
    fun isCorrect_TwoByTwo_NoCarryNoBorrow1DigitRem() {
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "2", 68, 34))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Ones, "8", 68, 34))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Tens, "6", 68, 34))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Ones, "0", 68, 34))
    }


    @Test
    fun isCorrect_TwoByTwo_NoCarryNoBorrow2DigitRem() {
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "2", 57, 22))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Ones, "4", 57, 22))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Tens, "4", 57, 22))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Ones, "3", 57, 22))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Tens, "1", 57, 22))
    }

    @Test
    fun isCorrect_TwoByTwo_NoCarryBorrow1DigitRem() {
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "2", 50, 22))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Ones, "4", 50, 22))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Tens, "4", 50, 22))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputBorrowFromDividendTens, "4", 50, 22))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Ones, "6", 50, 22))
    }

    @Test
    fun isCorrect_TwoByTwo_NoCarryBorrow2DigitRem() {
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "3", 50, 13))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Ones, "9", 50, 13))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Tens, "3", 50, 13))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputBorrowFromDividendTens, "4", 50,13))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Ones, "1", 50, 13))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Tens, "1", 50, 13))
    }

    @Test
    fun isCorrect_TwoByTwo_CarryNoBorrow1DigitRem() {
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "8", 96, 12))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1OnesWithCarry, "16", 96, 12))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Tens, "9", 96, 12))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Ones, "0", 96, 12))
    }

    @Test
    fun isCorrect_TwoByTwo_CarryNoBorrow2DigitRem() {
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "3", 95, 28))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1OnesWithCarry, "24", 95, 28))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Tens, "8", 95, 28))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Ones, "1", 95, 28))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Tens, "1", 95, 28))
    }

    @Test
    fun isCorrect_TwoByTwo_CarryBorrow1DigitRem() {
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "6", 81, 12))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1OnesWithCarry, "12", 81, 12))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Tens, "7", 81, 12))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputBorrowFromDividendTens, "7", 81, 12))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Ones, "9", 81, 12))
    }

    @Test
    fun isCorrect_TwoByTwo_CarryBorrow2DigitRem() {
        assertTrue(evaluator.isCorrect(DivisionPhase.InputQuotientOnes, "4", 80, 17))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1OnesWithCarry, "28", 80, 17))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputMultiply1Tens, "6", 80, 17))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputBorrowFromDividendTens, "7", 80, 17))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Ones, "2", 80, 17))
        assertTrue(evaluator.isCorrect(DivisionPhase.InputSubtract1Tens, "1", 80, 17))
    }

    @Test
    fun isCorrect_returns_false_for_wrong_answers() {
        assertFalse(evaluator.isCorrect(DivisionPhase.InputQuotientTens, "5", 46, 3))
        assertFalse(evaluator.isCorrect(DivisionPhase.InputMultiply1TensAndMultiply1Ones, "99", 46, 3))
    }
}