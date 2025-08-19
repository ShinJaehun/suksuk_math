package com.shinjaehun.suksuk.presentation.multiplication

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shinjaehun.suksuk.domain.DomainStateFactory
import com.shinjaehun.suksuk.domain.OpType
import com.shinjaehun.suksuk.domain.Problem
import com.shinjaehun.suksuk.domain.multiplication.evaluator.MulPhaseEvaluator
import com.shinjaehun.suksuk.domain.model.MulDomainState
import com.shinjaehun.suksuk.presentation.multiplication.model.MulUiState
import com.shinjaehun.suksuk.presentation.multiplication.model.mapMultiplicationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MultiplicationViewModel @Inject constructor(
//    savedStateHandle: SavedStateHandle = SavedStateHandle(),
    private val savedStateHandle: SavedStateHandle,
    private val phaseEvaluator: MulPhaseEvaluator,
    private val domainStateFactory: DomainStateFactory,
): ViewModel() {
//    private val autoStart: Boolean = savedStateHandle["autoStart"] ?: true

    private val _uiState = MutableStateFlow(MulUiState())
    val uiState: StateFlow<MulUiState> = _uiState.asStateFlow()

    private val _currentInput = MutableStateFlow("")
//    val currentInput: StateFlow<String> = _currentInput.asStateFlow()

    private lateinit var domainState: MulDomainState

//    init {
//        if(autoStart) {
//            startNewProblem(12, 34)
//        }
//    }

    fun startNewProblem(multiplicand: Int, multiplier: Int) {
        val ds = domainStateFactory.create(Problem(OpType.Multiplication, multiplicand, multiplier))
        require(ds is MulDomainState) { "Expected MulDomainState, got ${ds::class.simpleName}" }
        domainState = ds
        _currentInput.value = ""
        emitUiState()
    }

    fun onDigitInput(digit: Int){
        val step = domainState.phaseSequence.steps[domainState.currentStepIndex]
        val maxLength = step.editableCells.size.coerceAtLeast(1)
        _currentInput.value = (_currentInput.value + digit).takeLast(maxLength)
        emitUiState()
    }

    fun onEnter(){
        if(_currentInput.value.isEmpty()) return
        submitInput(_currentInput.value)
        _currentInput.value = ""
    }

    fun submitInput(input: String){
        val step = domainState.phaseSequence.steps[domainState.currentStepIndex]
        val editableCount = step.editableCells.size

        val inputsForThisStep: List<String> =
            if (editableCount > 1) input.padStart(editableCount, '?').chunked(1)
            else listOf(input)

        if (inputsForThisStep.size < editableCount || inputsForThisStep.any { it == "?" }) {
            _currentInput.value = ""
            emitUiState()
            return
        }
        val eval = phaseEvaluator.evaluate(domainState, inputsForThisStep)

        if (!eval.isCorrect) {
            _currentInput.value = ""
            emitUiState()
            return
        }

        domainState = domainState.copy(
            inputs = domainState.inputs + inputsForThisStep,
            currentStepIndex = eval.nextStepIndex ?: domainState.currentStepIndex
        )

        _currentInput.value = ""
        emitUiState()
    }

    fun onClear(){
        _currentInput.value = ""
        emitUiState()
    }

    private fun emitUiState() {
        if (!::domainState.isInitialized) {
            _uiState.value = MulUiState()
            return
        }
        _uiState.value = mapMultiplicationUiState(domainState, _currentInput.value)
    }
}