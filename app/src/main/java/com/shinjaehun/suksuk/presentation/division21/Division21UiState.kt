package com.shinjaehun.suksuk.presentation.division21

import com.shinjaehun.suksuk.domain.model.DivisionProblem

data class Division21UiState(
    val problem: DivisionProblem,
    val userInput: String = "",
    val result: Boolean? = null
)
