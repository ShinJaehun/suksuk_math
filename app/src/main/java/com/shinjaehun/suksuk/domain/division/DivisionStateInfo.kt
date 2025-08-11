package com.shinjaehun.suksuk.domain.division

data class DivisionStateInfo(
    val dividend: Int,
    val divisor: Int,

    val quotient: Int,
    val quotientTens: Int,
    val quotientOnes: Int,

    val dividendHundreds: Int,
    val dividendTens: Int,
    val dividendOnes: Int,

    val divisorTens: Int,
    val divisorOnes: Int,

    val needsTensQuotient: Boolean,

    // multiply (alias: multiply1 / multiply2)
    val multiplyQuotientTens: Int, // divisor * quotientTens
    val multiplyQuotientOnes: Int, // divisor * quotientOnes

    // subtract1
    val bringDownInSubtract1: Int,
    val subtract1TensOnly: Int,
    val subtract1Result: Int,

    // carry / borrow flags
    val needsCarryInMultiply1: Boolean,
    val needsCarryInMultiply2: Boolean,

    val needs2DigitsInSubtract1: Boolean,
    val needs3DigitsInMultiply2: Boolean,

    val needsBorrowFromDividendHundredsInSubtract1: Boolean,
    val needsBorrowFromSubtract1TensInSubtract2: Boolean,
    val needsBorrowFromSubtract1HundredsInSubtract2: Boolean,
    val needsSkipBorrowFromSubtract1HundredsInSubtract2: Boolean,

    // skip / zero / remainder
    val needsSkipMultiply2AndSubtract2: Boolean,
    val needsEmptySubtract1Tens: Boolean,
    val remainder: Int,
    val needs2DigitRem: Boolean,

    val needsSubtract2TensStep: Boolean,
    val tbs2: Boolean,
    val hbs2: Boolean,
    val skipHbs2: Boolean,
    val doubleBorrow: Boolean
)
