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

    val hasTensQuotient: Boolean,

    // multiply (alias: multiply1 / multiply2)
    val multiplyQuotientTens: Int, // divisor * quotientTens
    val multiplyQuotientOnes: Int, // divisor * quotientOnes

    // subtract1
    val bringDownInSubtract1: Int,
    val subtract1TensOnly: Int,
    val subtract1Result: Int,

    // carry / borrow flags
    val isCarryRequiredInMultiplyQuotientTens: Boolean,
    val isCarryRequiredInMultiplyQuotientOnes: Boolean,

    val needsTensBorrowInS1: Boolean,
    val skipTensBorrowInS1: Boolean,
    val needsHundredsBorrowInS1: Boolean,
    val skipHundredsBorrowInS1: Boolean,
    val needsDoubleBorrowInS1: Boolean,

    val needsTensBorrowInS2: Boolean,
    val performedTensBorrowInS2: Boolean,
    val needsHundredsBorrowInS2: Boolean,
    val skipHundredsBorrowInS2: Boolean,
    val needsDoubleBorrowInS2: Boolean,

    // skip / zero / remainder
    val shouldBypassM2AndS2: Boolean,
    val isEmptySubtract1Tens: Boolean,
    val remainder: Int,
    val has2DigitsRemainder: Boolean,
    val shouldPerformSubtractTensStep: Boolean,

    )
