package com.shinjaehun.suksuk.presentation.division

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class DivisionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DivisionUiState())
    val uiState: StateFlow<DivisionUiState> = _uiState

    init { resetProblem() }

    fun onDigitInput(d: Int) {
        _uiState.update { it.copy(userInput = (it.userInput + d).take(2), feedback = null) }
    }

    fun onClear() {
        _uiState.update { it.copy(userInput = "", feedback = null) }
    }

    fun onEnter() {
        viewModelScope.launch {
            val state = _uiState.value
            val answer = state.dividend / state.divisor
            val input = state.userInput.toIntOrNull()
            if (input == answer) {
                _uiState.update { it.copy(feedback = "정답! 입력: \$input, 몫: \$answer") }
            } else {
                _uiState.update { it.copy(feedback = "오답, 다시 시도하세요") }
            }
        }
    }

    fun resetProblem() {
        _uiState.value = DivisionUiState()
    }
}
