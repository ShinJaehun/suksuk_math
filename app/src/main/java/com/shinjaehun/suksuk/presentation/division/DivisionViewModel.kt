package com.shinjaehun.suksuk.presentation.division

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class DivisionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DivisionUiState())
    val uiState: StateFlow<DivisionUiState> = _uiState

    fun onDigitInput(digit: Int) {
        if (_uiState.value.isFinished) return
        val current = _uiState.value
        val newInput = (current.userInput + digit).take(2)
        _uiState.update { it.copy(userInput = newInput) }

        if (newInput.length == 2 || newInput.toIntOrNull() == current.correctAnswer) {
            checkAnswer(newInput.toIntOrNull())
        }
    }

    fun onClear() {
        _uiState.update { it.copy(userInput = "", feedback = null) }
    }

    private fun checkAnswer(input: Int?) {
        if (input == _uiState.value.correctAnswer) {
            _uiState.update {
                it.copy(
                    isFinished = true,
                    feedback = "정답입니다!"
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    feedback = "다시 시도해보세요",
                    userInput = ""
                )
            }
        }
    }
}