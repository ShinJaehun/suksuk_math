package com.shinjaehun.suksuk

import com.shinjaehun.suksuk.domain.division.detector.PatternDetector
import com.shinjaehun.suksuk.domain.division.model.DivisionPattern
import junit.framework.TestCase.assertEquals
import org.junit.Test

class PatternDetectorTest {

    @Test
    fun detectPattern_returns_correct_pattern_for_each_TwoByOne_case() {
        val cases = listOf(
            Triple(46, 3, DivisionPattern.TwoByOne_TensQuotient_NoBorrow_2DigitMul),
            Triple(72, 6, DivisionPattern.TwoByOne_TensQuotient_NoBorrow_2DigitMul),
            Triple(74, 6, DivisionPattern.TwoByOne_TensQuotient_NoBorrow_2DigitMul),
            Triple(85, 7, DivisionPattern.TwoByOne_TensQuotient_NoBorrow_2DigitMul),
            Triple(86, 7, DivisionPattern.TwoByOne_TensQuotient_NoBorrow_2DigitMul),
            Triple(92, 7, DivisionPattern.TwoByOne_TensQuotient_NoBorrow_2DigitMul),
            Triple(96, 4, DivisionPattern.TwoByOne_TensQuotient_NoBorrow_2DigitMul),

            Triple(45, 4, DivisionPattern.TwoByOne_TensQuotient_NoBorrow_1DigitMul),
            Triple(57, 5, DivisionPattern.TwoByOne_TensQuotient_NoBorrow_1DigitMul),
            Triple(84, 4, DivisionPattern.TwoByOne_TensQuotient_NoBorrow_1DigitMul),

            Triple(50, 3, DivisionPattern.TwoByOne_TensQuotient_Borrow_2DigitMul),
            Triple(90, 7, DivisionPattern.TwoByOne_TensQuotient_Borrow_2DigitMul),
            Triple(70, 4, DivisionPattern.TwoByOne_TensQuotient_Borrow_2DigitMul),


            Triple(70, 6, DivisionPattern.TwoByOne_TensQuotient_SkipBorrow_1DigitMul),
            Triple(71, 6, DivisionPattern.TwoByOne_TensQuotient_SkipBorrow_1DigitMul),
            Triple(90, 8, DivisionPattern.TwoByOne_TensQuotient_SkipBorrow_1DigitMul),
            Triple(93, 8, DivisionPattern.TwoByOne_TensQuotient_SkipBorrow_1DigitMul),

            Triple(53, 6, DivisionPattern.TwoByOne_OnesQuotient_Borrow_2DigitMul),
            Triple(62, 7, DivisionPattern.TwoByOne_OnesQuotient_Borrow_2DigitMul),

            Triple(12, 3, DivisionPattern.TwoByOne_OnesQuotient_NoBorrow_2DigitMul),
            Triple(24, 7, DivisionPattern.TwoByOne_OnesQuotient_NoBorrow_2DigitMul),
            Triple(39, 4, DivisionPattern.TwoByOne_OnesQuotient_NoBorrow_2DigitMul),
            Triple(49, 5, DivisionPattern.TwoByOne_OnesQuotient_NoBorrow_2DigitMul),
            Triple(54, 9, DivisionPattern.TwoByOne_OnesQuotient_NoBorrow_2DigitMul),
            Triple(68, 9, DivisionPattern.TwoByOne_OnesQuotient_NoBorrow_2DigitMul),
            Triple(81, 9, DivisionPattern.TwoByOne_OnesQuotient_NoBorrow_2DigitMul)
        )
        assertPatternCases(cases)
    }

    @Test
    fun detectPattern_returns_correct_pattern_for_each_TwoByTwo_case() {
        val cases = listOf(
            Triple(68, 34, DivisionPattern.TwoByTwo_NoCarry_NoBorrow_1DigitRem),
            Triple(48, 24, DivisionPattern.TwoByTwo_NoCarry_NoBorrow_1DigitRem),
            Triple(49, 24, DivisionPattern.TwoByTwo_NoCarry_NoBorrow_1DigitRem),

            Triple(50, 22, DivisionPattern.TwoByTwo_NoCarry_Borrow_1DigitRem),
            Triple(30, 11, DivisionPattern.TwoByTwo_NoCarry_Borrow_1DigitRem),
            Triple(70, 31, DivisionPattern.TwoByTwo_NoCarry_Borrow_1DigitRem),

            Triple(75, 25, DivisionPattern.TwoByTwo_Carry_NoBorrow_1DigitRem),
            Triple(96, 12, DivisionPattern.TwoByTwo_Carry_NoBorrow_1DigitRem),
            Triple(72, 18, DivisionPattern.TwoByTwo_Carry_NoBorrow_1DigitRem),

//            Triple(80, 28, DivisionPattern.TwoByTwo_Carry_Borrow),
            Triple(81, 12, DivisionPattern.TwoByTwo_Carry_Borrow_1DigitRem),
            Triple(83, 13, DivisionPattern.TwoByTwo_Carry_Borrow_1DigitRem)
        )
        assertPatternCases(cases)
    }

    private fun assertPatternCases(cases: List<Triple<Int, Int, DivisionPattern>>) {
        for ((dividend, divisor, expected) in cases) {
            val actual = PatternDetector.detectPattern(dividend, divisor)
            assertEquals("입력: $dividend ÷ $divisor", expected, actual)
        }
    }
}