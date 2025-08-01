package com.shinjaehun.suksuk.domain.division.model

enum class DivisionPattern {
    TwoByOne_TensQuotient_NoBorrow_2DigitMul,
    TwoByOne_TensQuotient_NoBorrow_1DigitMul,
    TwoByOne_TensQuotient_Borrow_2DigitMul,
    TwoByOne_TensQuotient_SkipBorrow_1DigitMul,
    //    TwoByOne_TensQuotient_Borrow_1DigitMul, // 이거 존재하지 않을 꺼야...
    TwoByOne_OnesQuotient_Borrow_2DigitMul,
    TwoByOne_OnesQuotient_NoBorrow_2DigitMul,
}