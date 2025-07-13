package com.shinjaehun.suksuk.presentation.division

// 각 단계 데이터를 담는 모델
sealed class Stage(val label: String) {
    data class InputQuotient(val digitIndex: Int): Stage("quotient")
    data class Multiply(val result: Int, val digitIndex: Int): Stage("multiply")
    data class Subtract(val result: Int, val digitIndex: Int): Stage("subtract")
    data class Remainder(val value: Int): Stage("remainder")
}