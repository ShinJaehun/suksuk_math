package com.shinjaehun.suksuk.presentation.division

import kotlin.random.Random

//data class DivisionUiState(
//    val dividend: Int = Random.nextInt(10, 100),
//    val divisor: Int = Random.nextInt(2, 10),
//    val quotient: Int = dividend / divisor,
//    val currentStage: Int = 1,
//    val userInput: String = "",
//    val feedback: String? = null,
//    val steps: List<Stage> = emptyList()
//)


data class DivisionUiState(
    val dividend: Int = Random.nextInt(10, 100),
    val divisor: Int = Random.nextInt(2, 10),
    val quotient: Int = dividend / divisor,
    val userInput: String = "",
    val feedback: String? = null
)