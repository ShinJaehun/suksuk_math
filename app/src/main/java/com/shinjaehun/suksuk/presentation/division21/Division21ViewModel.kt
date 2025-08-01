package com.shinjaehun.suksuk.presentation.division21

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

// legacy code

//@HiltViewModel
//class Division21ViewModel @Inject constructor(
//    private val generateProblem: GenerateDivisionProblemUseCase
//): ViewModel() {
//
//    private val _uiState = MutableStateFlow(
//        Division21UiState(problem = generateProblem())
//    )
//    val uiState: StateFlow<Division21UiState> = _uiState
//
//    fun onInputChange(input: String) {
//        _uiState.value = _uiState.value.copy(userInput = input)
//    }
//
//    fun checkAnswer(){
//        val correct = _uiState.value.problem.quotient.toString() == _uiState.value.userInput
//        _uiState.value = _uiState.value.copy(result = correct)
//    }
//}