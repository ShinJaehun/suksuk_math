package com.shinjaehun.suksuk.presentation.division

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DivisionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DivisionUiState())
    val uiState: StateFlow<DivisionUiState> = _uiState

    private var inputBuffer: String = ""

    fun onDigitInput(num: Int) {
        val state = _uiState.value
        if (state.stage == 5) {
            inputBuffer = if (inputBuffer.length < 2) {
                inputBuffer + num.toString()
            } else {
                inputBuffer.substring(1) + num.toString()
            }
            val ten = if (inputBuffer.length > 0) inputBuffer[0].toString() else ""
            val one = if (inputBuffer.length > 1) inputBuffer[1].toString() else ""
            _uiState.value = state.copy(
                multiply2Ten = state.multiply2Ten.copy(value = ten, editable = true),
                multiply2One = state.multiply2One.copy(value = one, editable = true)
            )
        } else {
            inputBuffer = num.toString()
            updateEditableCell(inputBuffer)
        }
    }

    fun onClear() {
        inputBuffer = ""
        updateEditableCell("")
    }

    fun onEnter() {
        val state = _uiState.value
        when (state.stage) {
            // 0: 십의자리 몫
            0 -> {
                val correct = (state.dividend / 10) / state.divisor
                if (inputBuffer == correct.toString()) {
                    _uiState.value = state.copy(
                        quotientCells = state.quotientCells.mapIndexed { i, cell ->
                            if (i == 0) cell.copy(value = inputBuffer, editable = false, correct = true) else cell
                        },
                        multiply1Cell = InputCell(editable = true),
                        stage = 1,
                        feedback = null
                    )
                    inputBuffer = ""
                } else {
                    _uiState.value = state.copy(
                        quotientCells = state.quotientCells.mapIndexed { i, cell ->
                            if (i == 0) cell.copy(value = "", editable = true, correct = false) else cell
                        },
                        feedback = "❌ 십의자리 몫이 달라요"
                    )
                    inputBuffer = ""
                }
            }
            // 1: 1차 곱셈
            1 -> {
                val mul = state.divisor * (state.quotientCells[0].value.toIntOrNull() ?: 0)
                if (inputBuffer == mul.toString()) {
                    _uiState.value = state.copy(
                        multiply1Cell = InputCell(mul.toString(), editable = false, correct = true),
                        subtract1Cell = InputCell(editable = true),
                        stage = 2,
                        feedback = null
                    )
                    inputBuffer = ""
                } else {
                    _uiState.value = state.copy(
                        multiply1Cell = InputCell("", editable = true, correct = false),
                        feedback = "❌ 곱셈 결과가 달라요"
                    )
                    inputBuffer = ""
                }
            }
            // 2: 1차 뺄셈
            2 -> {
                val ten = state.dividend / 10
                val mul = state.multiply1Cell.value.toIntOrNull() ?: 0
                val minus = ten - mul
                if (inputBuffer == minus.toString()) {
                    _uiState.value = state.copy(
                        subtract1Cell = InputCell(minus.toString(), editable = false, correct = true),
                        bringDownCell = InputCell(editable = true),
                        stage = 3,
                        feedback = null
                    )
                    inputBuffer = ""
                } else {
                    _uiState.value = state.copy(
                        subtract1Cell = InputCell("", editable = true, correct = false),
                        feedback = "❌ 뺄셈 결과가 달라요"
                    )
                    inputBuffer = ""
                }
            }
            // 3: 아래로 내리기(항상 한자리)
            3 -> {
                val one = state.dividend % 10
                if (inputBuffer == one.toString()) {
                    _uiState.value = state.copy(
                        bringDownCell = InputCell(one.toString(), editable = false, correct = true),
                        quotientCells = state.quotientCells.mapIndexed { i, cell ->
                            if (i == 1) cell.copy(editable = true) else cell
                        },
                        stage = 4,
                        feedback = null
                    )
                    inputBuffer = ""
                } else {
                    _uiState.value = state.copy(
                        bringDownCell = InputCell("", editable = true, correct = false),
                        feedback = "❌ 아래로 내리는 숫자가 달라요"
                    )
                    inputBuffer = ""
                }
            }
            // 4: 일의자리 몫
            4 -> {
                val prevMinus = state.subtract1Cell.value.toIntOrNull() ?: 0
                val one = state.dividend % 10
                val newDividend = prevMinus * 10 + one
                val q2 = newDividend / state.divisor
                if (inputBuffer == q2.toString()) {
                    _uiState.value = state.copy(
                        quotientCells = state.quotientCells.mapIndexed { i, cell ->
                            if (i == 1) cell.copy(value = inputBuffer, editable = false, correct = true) else cell
                        },
                        multiply2Ten = InputCell(editable = true),
                        multiply2One = InputCell(editable = true),
                        stage = 5,
                        feedback = null
                    )
                    inputBuffer = ""
                } else {
                    _uiState.value = state.copy(
                        quotientCells = state.quotientCells.mapIndexed { i, cell ->
                            if (i == 1) cell.copy(value = "", editable = true, correct = false) else cell
                        },
                        feedback = "❌ 일의자리 몫이 달라요"
                    )
                    inputBuffer = ""
                }
            }
            // 5: 2차 곱셈(21, 두 자리)
            5 -> {
                val prevMinus = state.subtract1Cell.value.toIntOrNull() ?: 0
                val one = state.dividend % 10
                val newDividend = prevMinus * 10 + one
                val q2 = state.quotientCells[1].value.toIntOrNull() ?: 0
                val mul = state.divisor * q2
                val ten = mul / 10
                val oneDigit = mul % 10
                if (inputBuffer.length == 2 &&
                    inputBuffer[0].digitToInt() == ten && inputBuffer[1].digitToInt() == oneDigit) {
                    _uiState.value = state.copy(
                        multiply2Ten = InputCell(ten.toString(), editable = false, correct = true),
                        multiply2One = InputCell(oneDigit.toString(), editable = false, correct = true),
                        remainderCell = InputCell(editable = true),
                        stage = 6,
                        feedback = null
                    )
                    inputBuffer = ""
                } else {
                    _uiState.value = state.copy(
                        multiply2Ten = InputCell(editable = true),
                        multiply2One = InputCell(editable = true),
                        feedback = "❌ 곱셈 결과가 달라요"
                    )
                    inputBuffer = ""
                }
            }
            // 6: 나머지(항상 한자리)
            6 -> {
                val prevMinus = state.subtract1Cell.value.toIntOrNull() ?: 0
                val one = state.dividend % 10
                val newDividend = prevMinus * 10 + one
                val q2 = state.quotientCells[1].value.toIntOrNull() ?: 0
                val mul = state.divisor * q2
                val remain = newDividend - mul
                if (inputBuffer == remain.toString()) {
                    _uiState.value = state.copy(
                        remainderCell = InputCell(remain.toString(), editable = false, correct = true),
                        feedback = "🎉 완성! 나머지: $remain"
                    )
                    inputBuffer = ""
                } else {
                    _uiState.value = state.copy(
                        remainderCell = InputCell("", editable = true, correct = false),
                        feedback = "❌ 나머지가 달라요"
                    )
                    inputBuffer = ""
                }
            }
        }
    }

    private fun updateEditableCell(value: String) {
        val state = _uiState.value
        when (state.stage) {
            0 -> _uiState.value = state.copy(
                quotientCells = state.quotientCells.mapIndexed { i, cell ->
                    if (i == 0) cell.copy(value = value, editable = true) else cell
                }
            )
            1 -> _uiState.value = state.copy(
                multiply1Cell = state.multiply1Cell.copy(value = value, editable = true)
            )
            2 -> _uiState.value = state.copy(
                subtract1Cell = state.subtract1Cell.copy(value = value, editable = true)
            )
            3 -> _uiState.value = state.copy(
                bringDownCell = state.bringDownCell.copy(value = value, editable = true)
            )
            4 -> _uiState.value = state.copy(
                quotientCells = state.quotientCells.mapIndexed { i, cell ->
                    if (i == 1) cell.copy(value = value, editable = true) else cell
                }
            )
            5 -> { // 두자리
                val ten = if (value.length > 0) value[0].toString() else ""
                val one = if (value.length > 1) value[1].toString() else ""
                _uiState.value = state.copy(
                    multiply2Ten = state.multiply2Ten.copy(value = ten, editable = true),
                    multiply2One = state.multiply2One.copy(value = one, editable = true)
                )
            }
            6 -> _uiState.value = state.copy(
                remainderCell = state.remainderCell.copy(value = value, editable = true)
            )
        }
    }
}