package com.shinjaehun.suksuk.presentation.multiplication

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shinjaehun.suksuk.domain.multiplication.evaluator.MulPhaseEvaluator
import com.shinjaehun.suksuk.domain.multiplication.factory.MulDomainStateFactory
import com.shinjaehun.suksuk.domain.multiplication.model.MulDomainState
import com.shinjaehun.suksuk.presentation.division.legacy.DivisionUiStateBuilder.Companion.mapToUiState
import com.shinjaehun.suksuk.presentation.multiplication.model.MulUiState
import com.shinjaehun.suksuk.presentation.multiplication.model.mapMultiplicationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MultiplicationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val phaseEvaluator: MulPhaseEvaluator,
    private val domainStateFactory: MulDomainStateFactory,
): ViewModel() {
    private val autoStart: Boolean = savedStateHandle["autoStart"] ?: true

    private val _uiState = MutableStateFlow(MulUiState())
    val uiState: StateFlow<MulUiState> = _uiState.asStateFlow()

    private val _currentInput = MutableStateFlow("")
    val currentInput: StateFlow<String> = _currentInput.asStateFlow()

    private lateinit var domainState: MulDomainState

    init {
        if(autoStart) {
            startNewProblem(12, 34)
        }
    }

    fun startNewProblem(multiplicand: Int, multiplier: Int) {
        domainState = domainStateFactory.create(multiplicand, multiplier)
        _currentInput.value = ""
        emitUiState()
    }

    fun onDigitInput(digit: String){
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
//        val step = domainState.phaseSequence.steps[domainState.currentStepIndex]
//        val editableCount = step.editableCells.size

//        val actualInput: List<String> = if(editableCount > 1){
//            input.padStart(editableCount, '?').chunked(1).map{it}
//        } else {
//            listOf(input)
//        }
//
//        if(actualInput.size < editableCount || actualInput.any{it == "?"}){
//            _currentInput.value = ""
//            emitUiState()
//            return
//        }
//
//        val allCorrect = step.editableCells.withIndex().all { (idx, cellName) ->
//            val userInput = actualInput.getOrNull(idx) ?: ""
//            phaseEvaluator.isCorrect(
//                phase = step.phase,
//                cell = cellName,
//                input = userInput,
//                info = domainState.info,
//                stepIndex = domainState.currentStepIndex,
//                previousInputs = domainState.inputs
//            )
//        }
//
//        if(!allCorrect){
//            _currentInput.value = ""
//            emitUiState()
//            return
//        }
//
//        val evalInputForTransition = actualInput.firstOrNull() ?: ""
//        val eval = phaseEvaluator.evaluate(domainState, evalInputForTransition)
//
//        val updatedInputs = domainState.inputs + actualInput
//        val nextStepIndex = eval.nextStepIndex ?: domainState.currentStepIndex

//        domainState = domainState.copy(
//            inputs = updatedInputs,
//            currentStepIndex = nextStepIndex
//        )

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

//        val steps = domainState.phaseSequence.steps
//
////        println("👉 cur=${domainState.currentStepIndex}, phase=${steps[domainState.currentStepIndex].phase}")
////        steps.forEachIndexed { i, s ->
////            println("[$i] ${s.phase} editable=${s.editableCells}")
////        }
//
//        val cur   = domainState.currentStepIndex
//        val step  = steps[cur]
//
//        val editableCount = step.editableCells.size
//
//        // [A] 편집 셀이 0개인 스텝에서 사용자가 실수로 Enter를 눌러도
//        //     evaluator에게 빈 입력을 넘겨 전이/자동 스킵/Complete 판정을 수행하도록 위임
//        if (editableCount == 0) {
//            val eval = phaseEvaluator.evaluate(domainState, emptyList())
//            if (eval.isCorrect) {
//                domainState = domainState.copy(
//                    // inputs는 증가 없음
//                    currentStepIndex = eval.nextStepIndex ?: cur
//                )
//            }
//            _currentInput.value = ""
//            emitUiState()
//            return
//        }
//
//        // [B] 현재 스텝이 요구하는 자리수만큼 입력을 준비 (공백은 미리 제거)
//        val trimmed = input.trim()
//        val inputsForThisStep: List<String> =
//            if (editableCount > 1) trimmed.padStart(editableCount, '?').chunked(1)
//            else listOf(trimmed)
//
//        // [C] 자리수 부족/플레이스홀더 존재 시 조용히 리턴
//        if (inputsForThisStep.size < editableCount || inputsForThisStep.any { it == "?" }) {
//            _currentInput.value = ""
//            emitUiState()
//            return
//        }
//
//        // [D] 판정 + 전이 + Complete 진입 처리(피드백은 UiState 쪽에서 Complete면 노출)
//        val eval = phaseEvaluator.evaluate(domainState, inputsForThisStep)
//
//        if (!eval.isCorrect) {
//            _currentInput.value = ""
//            emitUiState()
//            return
//        }
//
//        domainState = domainState.copy(
//            inputs = domainState.inputs + inputsForThisStep,
//            currentStepIndex = eval.nextStepIndex ?: cur
//        )
//
//        _currentInput.value = ""
//        emitUiState()

    }

    fun onClear(){
        _currentInput.value = ""
        emitUiState()
    }

    private fun emitUiState() {
//        if(::domainState.isInitialized) {
//            _uiState.value = mapMultiplicationUiState(domainState, _currentInput.value)
//        } else {
//            _uiState.value = MulUiState()
//        }
        _uiState.value = mapMultiplicationUiState(domainState, _currentInput.value)
    }
}