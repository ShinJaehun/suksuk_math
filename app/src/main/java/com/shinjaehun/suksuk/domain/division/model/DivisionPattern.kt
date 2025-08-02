package com.shinjaehun.suksuk.domain.division.model

enum class DivisionPattern {
    TwoByOne_TensQuotient_NoBorrow_2DigitMul,
    TwoByOne_TensQuotient_NoBorrow_1DigitMul,
    TwoByOne_TensQuotient_Borrow_2DigitMul,
    TwoByOne_TensQuotient_SkipBorrow_1DigitMul,
    TwoByOne_OnesQuotient_Borrow_2DigitMul,
    TwoByOne_OnesQuotient_NoBorrow_2DigitMul,
    TwoByTwo_NoCarry_NoBorrow_1DigitRem,
    TwoByTwo_NoCarry_NoBorrow_2DigitRem,
    TwoByTwo_NoCarry_Borrow_1DigitRem,
    TwoByTwo_NoCarry_Borrow_2DigitRem,
    TwoByTwo_Carry_NoBorrow_1DigitRem,
    TwoByTwo_Carry_NoBorrow_2DigitRem,
    TwoByTwo_Carry_Borrow_1DigitRem,
    TwoByTwo_Carry_Borrow_2DigitRem,
}