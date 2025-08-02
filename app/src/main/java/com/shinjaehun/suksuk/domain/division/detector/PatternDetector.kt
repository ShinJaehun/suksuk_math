package com.shinjaehun.suksuk.domain.division.detector

import com.shinjaehun.suksuk.domain.division.model.DivisionPattern

object PatternDetector {
    fun detectPattern(dividend: Int, divisor: Int): DivisionPattern {
        val quotient = dividend / divisor

        if (dividend in 10..99 && divisor in 10..99) {
            // 곱셈: (몫 × 제수)
            val multiply1 = (quotient % 10) * (divisor % 10)
            val hasCarry = multiply1 >= 10
            val product = quotient * divisor
            val hasBorrow = (dividend % 10) < (product % 10)

            val remainder = dividend - product
            val isTwoDigitRem = remainder >= 10

            return when {
                !hasCarry && !hasBorrow && !isTwoDigitRem -> DivisionPattern.TwoByTwo_NoCarry_NoBorrow_1DigitRem
                !hasCarry && !hasBorrow && isTwoDigitRem  -> DivisionPattern.TwoByTwo_NoCarry_NoBorrow_2DigitRem
                !hasCarry && hasBorrow  && !isTwoDigitRem -> DivisionPattern.TwoByTwo_NoCarry_Borrow_1DigitRem
                !hasCarry && hasBorrow  && isTwoDigitRem  -> DivisionPattern.TwoByTwo_NoCarry_Borrow_2DigitRem
                hasCarry  && !hasBorrow && !isTwoDigitRem -> DivisionPattern.TwoByTwo_Carry_NoBorrow_1DigitRem
                hasCarry  && !hasBorrow && isTwoDigitRem  -> DivisionPattern.TwoByTwo_Carry_NoBorrow_2DigitRem
                hasCarry  && hasBorrow  && !isTwoDigitRem -> DivisionPattern.TwoByTwo_Carry_Borrow_1DigitRem
                hasCarry  && hasBorrow  && isTwoDigitRem  -> DivisionPattern.TwoByTwo_Carry_Borrow_2DigitRem
                else -> DivisionPattern.TwoByTwo_NoCarry_NoBorrow_1DigitRem
            }
        }

        val dividendTens = dividend / 10
        val dividendOnes = dividend % 10

        if (dividendTens < divisor) {
            if (dividendOnes < divisor * quotient % 10) {
                return DivisionPattern.TwoByOne_OnesQuotient_Borrow_2DigitMul
            } else {
                return DivisionPattern.TwoByOne_OnesQuotient_NoBorrow_2DigitMul
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
            skipBorrow && !isSecondMultiplyTwoDigits -> DivisionPattern.TwoByOne_TensQuotient_SkipBorrow_1DigitMul

            // C: 받아내림 있음, 두자리 곱셈
            hasBorrow && isSecondMultiplyTwoDigits -> DivisionPattern.TwoByOne_TensQuotient_Borrow_2DigitMul
            // D: 받아내림 있음, 일의자리 곱셈만
//            hasBorrow && !isSecondMultiplyTwoDigits -> DivisionPattern.TensQuotient_Borrow_1DigitMul
            // A: 받아내림 없음, 두자리 곱셈
            !hasBorrow && isSecondMultiplyTwoDigits -> DivisionPattern.TwoByOne_TensQuotient_NoBorrow_2DigitMul
            // B: 받아내림 없음, 일의자리 곱셈만
            !hasBorrow && !isSecondMultiplyTwoDigits -> DivisionPattern.TwoByOne_TensQuotient_NoBorrow_1DigitMul
            else -> DivisionPattern.TwoByOne_TensQuotient_NoBorrow_2DigitMul // fallback (안 맞는 케이스는 A 처리)
        }
    }
}