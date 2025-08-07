package com.shinjaehun.suksuk.domain.division.legacy.model

data class DivisionUiState(
    val divisorTens: InputCell = InputCell(),
    val divisorOnes: InputCell = InputCell(),
    val dividendTens: InputCell = InputCell(),
    val dividendOnes: InputCell = InputCell(),
    val quotientTens: InputCell = InputCell(),
    val quotientOnes: InputCell = InputCell(),
    val multiply1Tens: InputCell = InputCell(),
    val multiply1Ones: InputCell = InputCell(),
    val subtract1Tens: InputCell = InputCell(),
    val subtract1Ones: InputCell = InputCell(),
    val multiply2Tens: InputCell = InputCell(),
    val multiply2Ones: InputCell = InputCell(),
    val subtract2Tens: InputCell = InputCell(),
    val subtract2Ones: InputCell = InputCell(),
    val borrowDividendTens: InputCell = InputCell(),
    val borrowSubtract1Tens: InputCell = InputCell(),
    val borrowed10DividendOnes: InputCell = InputCell(),
    val borrowed10Subtract1Ones: InputCell = InputCell(),

    val carryDivisorTens: InputCell = InputCell(),

    val stage: Int = 0,
    val feedback: String? = null,
    val subtractLines: SubtractLines = SubtractLines()
)

data class SubtractLines(
    val showSubtract1: Boolean = false,
    val showSubtract2: Boolean = false
)
