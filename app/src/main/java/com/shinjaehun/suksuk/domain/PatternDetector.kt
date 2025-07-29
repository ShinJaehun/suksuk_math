package com.shinjaehun.suksuk.domain

import com.shinjaehun.suksuk.presentation.division.DivisionPattern

object PatternDetector {
    fun detectPattern(dividend: Int, divisor: Int): DivisionPattern {
        val dividendTens = dividend / 10
        val dividendOnes = dividend % 10
        val quotient = dividend / divisor

        if (dividendTens < divisor) {
            if (dividendOnes < divisor * quotient % 10) {
                return DivisionPattern.OnesQuotient_Borrow_2DigitMul
            } else {
                return DivisionPattern.OnesQuotient_NoBorrow_2DigitMul
            }
        }

        val quotientTens = quotient / 10
        val multiply1 = quotientTens * divisor
        val subtract1Tens = dividendTens - multiply1
        val subtract1 = subtract1Tens * 10 + dividendOnes

//        println("ones: $ones")
//        println("firstProduct: ${firstProduct}")

        val quotientOnes = quotient % 10
        val multiply2 = quotientOnes * divisor
        val subtract2 = subtract1 - multiply2

//        val topDigit = subtract1 % 10
//        val bottomDigit = multiply2 % 10
//        val hasBorrow = topDigit < bottomDigit
        val subtract1Ones = subtract1 % 10
        val multiply2Ones = multiply2 % 10
        val hasBorrow = subtract1Ones < multiply2Ones
        val isSecondMultiplyTwoDigits = multiply2 >= 10

        val skipBorrow = hasBorrow && (subtract1Tens == 1)

        return when {
//            subtract1Tens == 0 && !isSecondMultiplyTwoDigits -> DivisionPattern.TensQuotient_Subtract1TnesZero_1DigitMul
            skipBorrow && !isSecondMultiplyTwoDigits -> DivisionPattern.TensQuotient_SkipBorrow_1DigitMul

            // C: 받아내림 있음, 두자리 곱셈
            hasBorrow && isSecondMultiplyTwoDigits -> DivisionPattern.TensQuotient_Borrow_2DigitMul
            // D: 받아내림 있음, 일의자리 곱셈만
            hasBorrow && !isSecondMultiplyTwoDigits -> DivisionPattern.TensQuotient_Borrow_1DigitMul
            // A: 받아내림 없음, 두자리 곱셈
            !hasBorrow && isSecondMultiplyTwoDigits -> DivisionPattern.TensQuotient_NoBorrow_2DigitMul
            // B: 받아내림 없음, 일의자리 곱셈만
            !hasBorrow && !isSecondMultiplyTwoDigits -> DivisionPattern.TensQuotient_NoBorrow_1DigitMul
            else -> DivisionPattern.TensQuotient_NoBorrow_2DigitMul // fallback (안 맞는 케이스는 A 처리)
        }
    }
}