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

        val needsTensQuotient = quotient >= 10

        val multiplyQuotientTens = divisor * quotientTens
        val multiplyQuotientOnes = divisor * quotientOnes

        val needsBorrowFromDividendHundredsInSubtract1 =
            (dividend >= 100) && (dividendTens < (multiplyQuotientTens % 10))

        val bringDownInSubtract1 = dividendOnes
        val subtract1TensOnly = (dividend / 10) - multiplyQuotientTens
        val subtract1Result = subtract1TensOnly * 10 + bringDownInSubtract1

        val needsCarryInMultiply1 = (quotientTens * divisorOnes) >= 10
        val needsCarryInMultiply2 = (quotientOnes * divisorOnes) >= 10

        val needsBorrowFromSubtract1TensInSubtract2 =
            (quotientOnes != 0) && ((subtract1Result % 10) < (multiplyQuotientOnes % 10))

        val needsSkipMultiply2AndSubtract2 = quotientOnes == 0
        val needsEmptySubtract1Tens = (dividendTens - (multiplyQuotientTens % 10)) == 0

        val needsBorrowFromSubtract1HundredsInSubtract2 =
            (dividend >= 100) &&
                    (((subtract1Result / 10) % 10) < ((multiplyQuotientOnes / 10) % 10))

        val remainder = dividend - divisor * quotient
        val needs2DigitRem =
            if (needsSkipMultiply2AndSubtract2) subtract1Result >= 10 else remainder >= 10

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

            needsTensQuotient = needsTensQuotient,

            multiplyQuotientTens = multiplyQuotientTens,
            multiplyQuotientOnes = multiplyQuotientOnes,

            bringDownInSubtract1 = bringDownInSubtract1,
            subtract1TensOnly = subtract1TensOnly,
            subtract1Result = subtract1Result,

            needsCarryInMultiply1 = needsCarryInMultiply1,
            needsCarryInMultiply2 = needsCarryInMultiply2,

            needsBorrowFromDividendHundredsInSubtract1 = needsBorrowFromDividendHundredsInSubtract1,
            needsBorrowFromSubtract1TensInSubtract2 = needsBorrowFromSubtract1TensInSubtract2,
            needsBorrowFromSubtract1HundredsInSubtract2 = needsBorrowFromSubtract1HundredsInSubtract2,

            needsSkipMultiply2AndSubtract2 = needsSkipMultiply2AndSubtract2,  // ← 문제의 필드
            needsEmptySubtract1Tens = needsEmptySubtract1Tens,

            remainder = remainder,
            needs2DigitRem = needs2DigitRem
        )
    }
}