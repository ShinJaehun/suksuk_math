package com.shinjaehun.suksuk.domain.division

object PatternDetectorV2 {

    fun detectPattern(dividend: Int, divisor: Int): DivisionPatternV2 {
        val dividendDigits = dividend.toString().length
        val divisorDigits = divisor.toString().length

        return when {
            dividendDigits == 2 && divisorDigits == 1 -> DivisionPatternV2.TwoByOne
            dividendDigits == 2 && divisorDigits == 2 -> DivisionPatternV2.TwoByTwo
            dividendDigits == 3 && divisorDigits == 2 -> DivisionPatternV2.ThreeByTwo
            else -> error("지원하지 않는 패턴: $dividend ÷ $divisor")
        }
    }
}