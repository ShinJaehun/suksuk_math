package com.shinjaehun.suksuk.common.viewmodel

import androidx.lifecycle.ViewModel
import com.shinjaehun.suksuk.common.eval.EvalResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<UI, DOMAIN: Any, STEP, CELL> : ViewModel() {

    protected lateinit var domainState: DOMAIN

    protected abstract fun createDomain(vararg args: Int): DOMAIN
    protected abstract fun steps(domain: DOMAIN): List<STEP>
    protected abstract fun currentStepIndex(domain: DOMAIN): Int
    protected abstract fun editableCells(step: STEP): List<CELL>
    protected abstract fun evaluate(domain: DOMAIN, inputsForThisStep: List<String>): EvalResult
    protected abstract fun advance(domain: DOMAIN, addedInputs: List<String>, nextStepIndex: Int): DOMAIN
    protected abstract fun mapToUi(domain: DOMAIN, currentInput: String): UI

    private val _uiState = MutableStateFlow<UI?>(null)
    val uiState: StateFlow<UI?> = _uiState.asStateFlow()

    private val _currentInput = MutableStateFlow("")
//    val currentInput: StateFlow<String> = _currentInput.asStateFlow()

    fun startNewProblem(vararg args: Int) {
        domainState = createDomain(*args)
        _currentInput.value = ""
        emitUiState()
    }

    fun onDigitInput(digit: String) {
        val stepList = steps(domainState)
        val currentStepIndex = currentStepIndex(domainState).coerceIn(0, stepList.lastIndex)
        val currentStep = stepList[currentStepIndex]
        val need = editableCells(currentStep).size.coerceAtLeast(1)
        _currentInput.value = (_currentInput.value + digit).takeLast(need)
        emitUiState()
    }

    fun onEnter() {
        if (_currentInput.value.isEmpty()) return
        submitInput(_currentInput.value)
        _currentInput.value = ""
    }

    fun onClear() {
        _currentInput.value = ""
        emitUiState()
    }

    fun submitInput(input: String) {
        val stepList = steps(domainState)
        val currentStepIndex = currentStepIndex(domainState).coerceIn(0, stepList.lastIndex)
        val currentStep = stepList[currentStepIndex]
        val editableCount = editableCells(currentStep).size

        val inputsForThisStep: List<String> =
            if (editableCount > 1) input.padStart(editableCount, '?').chunked(1)
            else listOf(input)

        if (inputsForThisStep.size < editableCount || inputsForThisStep.any { it == "?" }) {
            _currentInput.value = ""
            emitUiState()
            return
        }

        val eval = evaluate(domainState, inputsForThisStep)

        if (!eval.isCorrect) {
            _currentInput.value = ""
            emitUiState()
            return
        }

        val nextIndex = eval.nextStepIndex ?: currentStepIndex
        domainState = advance(domainState, inputsForThisStep, nextIndex)

        _currentInput.value = ""
        emitUiState()
    }

    private fun emitUiState() {
        _uiState.value = if (::domainState.isInitialized) mapToUi(domainState, _currentInput.value) else null
    }
}