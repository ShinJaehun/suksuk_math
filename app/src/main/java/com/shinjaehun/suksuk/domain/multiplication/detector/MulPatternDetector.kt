package com.shinjaehun.suksuk.domain.multiplication.detector

import com.shinjaehun.suksuk.domain.multiplication.model.MulPattern

object MulPatternDetector {
    fun detectPattern(multiplicand: Int, multiplier: Int): MulPattern {
        val aDigits = multiplicand.toString().length
        val bDigits = multiplier.toString().length

        return when {
            aDigits == 2 && bDigits == 2 -> MulPattern.TwoByTwo   // 최대 99 × 99
            aDigits == 3 && bDigits == 2 -> MulPattern.ThreeByTwo // 최대 999 × 99
            else -> error("지원하지 않는 곱셈 패턴: $multiplicand × $multiplier")
        }
    }
}