package com.shinjaehun.suksuk.domain.division.model

enum class DivisionCellName {
    None,

    DivisorTens,
    DivisorOnes,
    DividendHundreds,

    DividendTens,
    DividendOnes,

    QuotientTens,
    QuotientOnes,

    Multiply1Hundreds,
    Multiply1Tens,
    Multiply1Ones,

    Subtract1Hundreds,
    Subtract1Tens,
    Subtract1Ones,

    Multiply2Hundreds,
    Multiply2Tens,
    Multiply2Ones,
    Subtract2Tens,
    Subtract2Ones,

    BorrowDividendHundreds,
    BorrowDividendTens,
    Borrowed10DividendTens,
    Borrowed10DividendOnes,

    BorrowSubtract1Hundreds,
    BorrowSubtract1Tens,
    Borrowed10Subtract1Tens,
    Borrowed10Subtract1Ones,

    CarryDivisorTensM1,
    CarryDivisorTensM2,
}
