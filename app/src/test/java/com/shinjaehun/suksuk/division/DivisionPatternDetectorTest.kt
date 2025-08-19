package com.shinjaehun.suksuk.division

import com.shinjaehun.suksuk.domain.OpType
import com.shinjaehun.suksuk.domain.Problem
import com.shinjaehun.suksuk.domain.pattern.DivisionPattern
import com.shinjaehun.suksuk.domain.pattern.detectPattern
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import org.junit.Test

class DivisionPatternDetectorTest {

    @Test
    fun detectPattern_returns_correct_pattern_for_various_cases() {
        val cases = listOf(
            Triple(68, 34, DivisionPattern.TwoByTwo),
            Triple(88, 12, DivisionPattern.TwoByTwo),
            Triple(34, 5, DivisionPattern.TwoByOne),
            Triple(76, 9, DivisionPattern.TwoByOne),
            Triple(384, 15, DivisionPattern.ThreeByTwo),
            Triple(135, 17, DivisionPattern.ThreeByTwo)
        )
        assertPatternCases(cases)
    }

    private fun assertPatternCases(cases: List<Triple<Int, Int, DivisionPattern>>) {
        for ((dividend, divisor, expected) in cases) {
            val problem = Problem(OpType.Division, dividend, divisor)
            val opPattern = detectPattern(problem)

            val actual = when (opPattern) {
                is DivisionPattern -> opPattern
                else -> fail("Expected DivisionPattern but got $opPattern for $dividend ÷ $divisor")
            }
            assertEquals("입력: $dividend ÷ $divisor", expected, actual)
        }
    }
}
