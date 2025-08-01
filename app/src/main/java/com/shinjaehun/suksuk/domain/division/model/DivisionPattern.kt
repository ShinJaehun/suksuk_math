package com.shinjaehun.suksuk.domain.division.model

enum class DivisionPattern {
    TensQuotient_NoBorrow_2DigitMul,
    TensQuotient_NoBorrow_1DigitMul,
    TensQuotient_Borrow_2DigitMul,
    TensQuotient_SkipBorrow_1DigitMul,
    //    TensQuotient_Borrow_1DigitMul, // 이거 존재하지 않을 꺼야...
    OnesQuotient_Borrow_2DigitMul,
    OnesQuotient_NoBorrow_2DigitMul,
}
