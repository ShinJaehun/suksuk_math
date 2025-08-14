package com.shinjaehun.suksuk.domain.division.info

object DivisionStateInfoBuilder {

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

        val bringDownInSubtract1 = dividendOnes
        val subtract1TensOnly = (dividend / 10) - multiplyQuotientTens
        val subtract1Result = subtract1TensOnly * 10 + bringDownInSubtract1

        val isCarryRequiredInMultiplyQuotientTens = (quotientTens * divisorOnes) >= 10
        val isCarryRequiredInMultiplyQuotientOnes = (quotientOnes * divisorOnes) >= 10

        val has2DigitsInSubtract1 = subtract1TensOnly >= 10
//        val is3DigitsMultiplyQuotientOnes = multiplyQuotientOnes >= 100 // 미사용

//        val isThreeByTwo = (dividend >= 100) && (divisor  >= 10) // 미사용

        // subtract1
        val s1TOHundreds = subtract1TensOnly / 10
        val s1TOTens = subtract1TensOnly % 10
        val s1TOOnes = dividendOnes

        // multiplyQuotientOnes
        val mQOHundreds = (multiplyQuotientOnes / 100) % 10
        val mQOTens = (multiplyQuotientOnes / 10) % 10
        val mQOOnes = multiplyQuotientOnes % 10

        val shouldBypassMultiply2AndSubtract2 = quotientOnes == 0

        val needsTensBorrowInS1 = dividendOnes < mQOOnes
        val skipTensBorrowInS1 = needsTensBorrowInS1 && (dividendTens == 1)
        val needsHundredsBorrowInS1 = if(hasTensQuotient) dividendTens < multiplyQuotientTens % 10
                    else dividendTens - (if (needsTensBorrowInS1) 1 else 0) < mQOTens
        val skipHundredsBorrowInS1 = needsHundredsBorrowInS1 && (dividendHundreds == 1)
        val needsDoubleBorrowInS1 = needsTensBorrowInS1 && ((dividendTens - 1) < mQOTens)

        val performedTensBorrowInS2 = needsTensBorrowInS1 && subtract1TensOnly != 1
        val skipTensBorrowInS2WhenTensIsOne = needsTensBorrowInS1 && (subtract1TensOnly == 1)

        val needsTensBorrowInS2 = s1TOOnes < mQOOnes

        val needsHundredsBorrowInS2 = s1TOTens - (if (needsTensBorrowInS2) 1 else 0) < mQOTens
        val skipHundredsBorrowInS2 = needsHundredsBorrowInS2 && (s1TOHundreds == 1)
        val needsDoubleBorrowInS2 = needsTensBorrowInS2 && ((s1TOTens - 1) < mQOTens)

        val shouldLeaveSubtract1TensEmpty =
            (dividendTens - (multiplyQuotientTens % 10)) == 0 && !has2DigitsInSubtract1

        val remainder = dividend - divisor * quotient
        val has2DigitsRemainder =
            if (shouldBypassMultiply2AndSubtract2) subtract1Result >= 10 else remainder >= 10

        val shouldPerformSubtractTensStep = !shouldBypassMultiply2AndSubtract2 && has2DigitsRemainder

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

            needsTensBorrowInS1 = needsTensBorrowInS1,
            skipTensBorrowInS1 = skipTensBorrowInS1,
            needsHundredsBorrowInS1 = needsHundredsBorrowInS1,
            skipHundredsBorrowInS1 = skipHundredsBorrowInS1,
            needsDoubleBorrowInS1 = needsDoubleBorrowInS1,

            needsTensBorrowInS2 = needsTensBorrowInS2,
            performedTensBorrowInS2 = performedTensBorrowInS2,
            skipTensBorrowInS2WhenTensIsOne = skipTensBorrowInS2WhenTensIsOne,
            needsHundredsBorrowInS2 = needsHundredsBorrowInS2,
            skipHundredsBorrowInS2 = skipHundredsBorrowInS2,
            needsDoubleBorrowInS2 = needsDoubleBorrowInS2,

            shouldBypassM2AndS2 = shouldBypassMultiply2AndSubtract2,
            shouldLeaveSubtract1TensEmpty = shouldLeaveSubtract1TensEmpty,

            remainder = remainder,
            has2DigitsRemainder = has2DigitsRemainder,

            shouldPerformSubtractTensStep = shouldPerformSubtractTensStep,

        )
    }
}