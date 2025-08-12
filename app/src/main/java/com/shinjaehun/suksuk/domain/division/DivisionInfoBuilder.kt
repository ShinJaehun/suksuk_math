package com.shinjaehun.suksuk.domain.division

object DivisionInfoBuilder {
    fun from(dividend: Int, divisor: Int): DivisionStateInfo {
        val quotient = dividend / divisor
        val quotientTens = quotient / 10
        val quotientOnes = quotient % 10

        val dividendHundreds = dividend / 100
        val dividendTens = (dividend / 10) % 10
        val dividendOnes = dividend % 10

        val divisorTens = divisor / 10
        val divisorOnes = divisor % 10

        val hasTensQuotient = quotient >= 10

        val multiplyQuotientTens = divisor * quotientTens
        val multiplyQuotientOnes = divisor * quotientOnes
//
//        val needsBorrowFromDividendHundredsInSubtract1 =
//            (dividend >= 100) && (dividendTens < (multiplyQuotientTens % 10))

        val bringDownInSubtract1 = dividendOnes
        val subtract1TensOnly = (dividend / 10) - multiplyQuotientTens
        val subtract1Result = subtract1TensOnly * 10 + bringDownInSubtract1

        val isCarryRequiredInMultiplyQuotientTens = (quotientTens * divisorOnes) >= 10
        val isCarryRequiredInMultiplyQuotientOnes = (quotientOnes * divisorOnes) >= 10

        val is2DigitsInSubtract1 = subtract1TensOnly >= 10
        val is3DigitsMultiplyQuotientOnes = multiplyQuotientOnes >= 100

        val isThreeByTwo = (dividend >= 100) && (divisor  >= 10)

        // subtract1
        val s1TOHundreds = subtract1TensOnly / 10
        val s1TOTens = subtract1TensOnly % 10
        val s1TOOnes = dividendOnes

        // multiplyQuotientOnes
        val mQOHundreds = (multiplyQuotientOnes / 100) % 10
        val mQOTens = (multiplyQuotientOnes / 10) % 10
        val mQOOnes = multiplyQuotientOnes % 10

        val isBorrowFromDividendTensRequiredInSubtract1 = dividendOnes < mQOOnes

//        val needsBorrowFromSubtract1TensInSubtract2 =
//            (quotientOnes != 0) && ((subtract1Result % 10) < (multiplyQuotientOnes % 10))
        val isBorrowFromSubtract1TensRequiredInS2 = s1TOOnes < mQOOnes
        val shouldBypassMultiply2AndSubtract2 = quotientOnes == 0
//        val needsBorrowFromSubtract1HundredsInSubtract2 =
//            (dividend >= 100) &&
//                    (((subtract1Result / 10) % 10) < ((multiplyQuotientOnes / 10) % 10))
        val isBorrowFromSubtract1HundredsRequiredInS2 =
            (dividend >= 100) && ((s1TOTens - (if (isBorrowFromSubtract1TensRequiredInS2) 1 else 0)) < mQOTens)

//        val needsBorrowFromDividendHundredsInSubtract1 =
//            (dividend >= 100) && ((dividendTens - (if (needsBorrowFromDividendTensInSubtract1) 1 else 0)) < mQOTens)

        val isBorrowFromDividendHundredsRequiredInS1 =
            if (dividend >= 100 && hasTensQuotient)
                (dividendTens < (multiplyQuotientTens % 10))
            else ((dividendTens - (if (isBorrowFromDividendTensRequiredInSubtract1) 1 else 0)) < mQOTens)

        val isEmptySubtract1Tens =
            (dividendTens - (multiplyQuotientTens % 10)) == 0 && !is2DigitsInSubtract1

        val remainder = dividend - divisor * quotient
        val is2DigitRem =
            if (shouldBypassMultiply2AndSubtract2) subtract1Result >= 10 else remainder >= 10

        val shouldPerformSubtractTensStep = !shouldBypassMultiply2AndSubtract2 && is2DigitRem

//        println("InfoBuilder: q=$quotient, q1=$quotientOnes, skip2=${(quotientOnes == 0)}")
//        println("InfoBuilder(assign): skip2=$needsSkipMultiply2AndSubtract2")

        return DivisionStateInfo(
            dividend = dividend,
            divisor = divisor,
            quotient = quotient,
            quotientTens = quotientTens,
            quotientOnes = quotientOnes,

            dividendHundreds = dividendHundreds,
            dividendTens = dividendTens,
            dividendOnes = dividendOnes,

            divisorTens = divisorTens,
            divisorOnes = divisorOnes,

            hasTensQuotient = hasTensQuotient,

            multiplyQuotientTens = multiplyQuotientTens,
            multiplyQuotientOnes = multiplyQuotientOnes,

            bringDownInSubtract1 = bringDownInSubtract1,
            subtract1TensOnly = subtract1TensOnly,
            subtract1Result = subtract1Result,

            isCarryRequiredInMultiplyQuotientTens = isCarryRequiredInMultiplyQuotientTens,
            isCarryRequiredInMultiplyQuotientOnes = isCarryRequiredInMultiplyQuotientOnes,

            is2DigitsInSubtract1 = is2DigitsInSubtract1,
            is3DigitsMultiplyQuotientOnes = is3DigitsMultiplyQuotientOnes,

            isBorrowFromDividendTensRequiredInS1 = isBorrowFromDividendTensRequiredInSubtract1,
            isBorrowFromDividendHundredsRequiredInS1 = isBorrowFromDividendHundredsRequiredInS1,
            isBorrowFromSubtract1TensRequiredInS2 = isBorrowFromSubtract1TensRequiredInS2,
            isBorrowFromSubtract1HundredsRequiredInS2 = isBorrowFromSubtract1HundredsRequiredInS2,

            shouldBypassM2AndS2 = shouldBypassMultiply2AndSubtract2,
            isEmptySubtract1Tens = isEmptySubtract1Tens,

            remainder = remainder,
            is2DigitRem = is2DigitRem,

            shouldPerformSubtractTensStep = shouldPerformSubtractTensStep,

        )
    }
}