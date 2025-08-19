package com.shinjaehun.suksuk.domain.pattern

sealed interface OperationPattern

enum class DivisionPattern: OperationPattern {
    TwoByOne,      // 2자리 ÷ 1자리
    TwoByTwo,      // 2자리 ÷ 2자리
    ThreeByTwo    // 3자리 ÷ 2자리
}

enum class MulPattern: OperationPattern {
    TwoByTwo,
    ThreeByTwo
}