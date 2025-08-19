package com.shinjaehun.suksuk.division

import com.shinjaehun.suksuk.domain.OpType
import com.shinjaehun.suksuk.domain.Problem
import com.shinjaehun.suksuk.domain.pattern.DivisionPatternV2
import com.shinjaehun.suksuk.domain.pattern.detectPattern
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import org.junit.Test

class DivisionPatternDetectorV2Test {

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
            val problem = Problem(OpType.Division, dividend, divisor)
            val opPattern = detectPattern(problem)

            val actual = when (opPattern) {
                is DivisionPatternV2 -> opPattern
                else -> fail("Expected DivisionPatternV2 but got $opPattern for $dividend ÷ $divisor")
            }
            assertEquals("입력: $dividend ÷ $divisor", expected, actual)
        }
    }
}
