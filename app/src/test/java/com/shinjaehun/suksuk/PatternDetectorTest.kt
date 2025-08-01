package com.shinjaehun.suksuk

import com.shinjaehun.suksuk.domain.division.detector.PatternDetector
import com.shinjaehun.suksuk.domain.division.model.DivisionPattern
import junit.framework.TestCase.assertEquals
import org.junit.Test

class PatternDetectorTest {

    @Test
    fun detectPattern_returns_correct_pattern_for_each_2by1_case() {
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

        for ((dividend, divisor, expected) in cases) {
            val actual = PatternDetector.detectPattern(dividend, divisor)
            assertEquals("입력: $dividend ÷ $divisor", expected, actual)
        }
    }
}