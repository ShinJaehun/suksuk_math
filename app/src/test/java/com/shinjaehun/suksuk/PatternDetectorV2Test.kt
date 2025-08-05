package com.shinjaehun.suksuk

import com.shinjaehun.suksuk.domain.division.DivisionPatternV2
import com.shinjaehun.suksuk.domain.division.PatternDetectorV2
import junit.framework.TestCase.assertEquals
import org.junit.Test

class PatternDetectorV2Test {

    @Test
    fun detectPatternV2_returns_correct_pattern_for_various_cases() {
        val cases = listOf(
            Triple(68, 34, DivisionPatternV2.TwoByTwo),
            Triple(88, 12, DivisionPatternV2.TwoByTwo),
            Triple(34, 5, DivisionPatternV2.TwoByOne),
            Triple(76, 9, DivisionPatternV2.TwoByOne),
            Triple(384, 15, DivisionPatternV2.ThreeByTwo),
            Triple(135, 17, DivisionPatternV2.ThreeByTwo)
        )
        assertPatternV2Cases(cases)
    }

    private fun assertPatternV2Cases(cases: List<Triple<Int, Int, DivisionPatternV2>>) {
        for ((dividend, divisor, expected) in cases) {
            val actual = PatternDetectorV2.detectPattern(dividend, divisor)
            assertEquals("입력: $dividend ÷ $divisor", expected, actual)
        }
    }
}
