package com.shinjaehun.suksuk.division

import org.junit.Assert.*
import org.junit.Test

class ThreeByTwoBringDownThreeDigitsTest {

    data class Case(val dividend: Int, val divisor: Int, val expectThreeDigitsAfterBringDown: Boolean)

    private fun analyze(dividend: Int, divisor: Int): Triple<Boolean, Boolean, Boolean> {
        require(dividend in 100..999) { "3-by-2 가정: dividend는 3자리여야 합니다." }
        require(divisor in 10..99) { "3-by-2 가정: divisor는 2자리여야 합니다." }

        val hundreds = dividend / 100
        val tens = (dividend / 10) % 10
        val ones = dividend % 10

        val ab = hundreds * 10 + tens // 앞의 두 자리
        if (ab < divisor) {
            // 몫의 십의 자리가 없으므로 실패
            return Triple(false, false, false)
        }
        val qTens = ab / divisor                 // 첫 몫(= 몫의 십의 자리)
        val mul1 = divisor * qTens               // 1차 곱셈
        val remainder1 = ab - mul1               // 1차 뺄셈 결과 (0 <= remainder1 < divisor)
        val afterBringDown = remainder1 * 10 + ones

        val hasTensQuotient = qTens >= 1
        val mul1IsTwoDigits = mul1 in 10..99
        val afterBringDownIsThreeDigits = afterBringDown >= 100

        return Triple(hasTensQuotient, mul1IsTwoDigits, afterBringDownIsThreeDigits)
    }

    @Test
    fun `3by2 - bringdown 후 3자리 되는 양성 케이스들`() {
        val positives = listOf(
            // ab mod divisor >= 10 이 되도록 고른 예시들
            Case(236, 13, true), // ab=23, qTens=1, mul1=13(두 자리), remainder=10 -> bringdown 106 (세 자리)
            Case(224, 12, true), // ab=22, qTens=1, mul1=12, remainder=10 -> bringdown 104
            Case(359, 12, true), // ab=35, qTens=2, mul1=24, remainder=11 -> bringdown 119
            Case(710, 12, true), // ab=71, qTens=5, mul1=60, remainder=11 -> bringdown 110
            Case(947, 14, true)  // ab=94, qTens=6, mul1=84, remainder=10 -> bringdown 107
        )

        positives.forEach { (dividend, divisor, expected) ->
            val (hasTens, mul2d, threeDigits) = analyze(dividend, divisor)
            assertTrue("tens quotient가 있어야 함: $dividend ÷ $divisor", hasTens)
            assertTrue("1차 곱셈 두 자리여야 함: $dividend ÷ $divisor", mul2d)
            assertEquals("bringdown 후 3자리여야 함: $dividend ÷ $divisor", expected, threeDigits)
        }
    }

    @Test
    fun `3by2 - bringdown 후 3자리 되지 않는 음성 케이스들`() {
        val negatives = listOf(
            Case(610, 13, false), // ab=61, qTens=4, mul1=52, remainder=9  -> bringdown 90 (두 자리)
            Case(987, 12, false)  // ab=98, qTens=8, mul1=96, remainder=2  -> bringdown 27 (두 자리)
        )

        negatives.forEach { (dividend, divisor, expected) ->
            val (hasTens, mul2d, threeDigits) = analyze(dividend, divisor)
            assertTrue("tens quotient가 있어야 함: $dividend ÷ $divisor", hasTens)
            assertTrue("1차 곱셈 두 자리여야 함: $dividend ÷ $divisor", mul2d)
            assertEquals("bringdown 후 3자리가 아니어야 함: $dividend ÷ $divisor", expected, threeDigits)
        }
    }
}
