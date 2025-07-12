package com.shinjaehun.suksuk.presentation.division

data class DivisionUiState(
    val dividend: Int = 42,
    val divisor: Int = 6,
    val userInput: String = "",
    val stage: Int = 1,
    val correctAnswer: Int = 7,
    val isFinished: Boolean = false,
    val feedback: String? = null
)
