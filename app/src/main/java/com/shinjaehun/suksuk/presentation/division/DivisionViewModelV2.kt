package com.shinjaehun.suksuk.presentation.division

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.shinjaehun.suksuk.domain.division.DivisionDomainStateV2
import com.shinjaehun.suksuk.domain.division.DivisionPatternV2
import com.shinjaehun.suksuk.domain.division.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.division.DivisionPhaseV2
import com.shinjaehun.suksuk.domain.division.DivisionUiStateV2
import com.shinjaehun.suksuk.domain.division.PhaseEvaluatorV2
import com.shinjaehun.suksuk.domain.division.PhaseSequence
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DivisionViewModelV2 @Inject constructor(
    private val phaseSequenceProvider: DivisionPhaseSequenceProvider,
    private val phaseEvaluator: PhaseEvaluatorV2,
    private val feedbackProvider: FeedbackMessageProviderV2
): ViewModel() {

    private val _uiState = MutableStateFlow(DivisionUiStateV2())
    val uiState: StateFlow<DivisionUiStateV2> = _uiState.asStateFlow()

    private val _currentInput = MutableStateFlow("")
    val currentInput: StateFlow<String> = _currentInput.asStateFlow()

    private lateinit var domainState: DivisionDomainStateV2
    fun getCurrentPattern(): DivisionPatternV2 = domainState.phaseSequence.pattern

    init {
//        startNewProblem(68, 34) // TwoByTwo_NoCarry_NoBorrow_1DigitRem
//        startNewProblem(57, 22) //TwoByTwo_NoCarry_NoBorrow_2DigitRem
//        startNewProblem(50, 22) //TwoByTwo_NoCarry_Borrow_1DigitRem
//        startNewProblem(50, 13) //TwoByTwo_NoCarry_Borrow_2DigitRem
//        startNewProblem(96, 12) //TwoByTwo_Carry_NoBorrow_1DigitRem
//        startNewProblem(95, 28) //TwoByTwo_Carry_NoBorrow_2DigitRem
//        startNewProblem(81, 12) //TwoByTwo_Carry_Borrow_1DigitRem
        startNewProblem(70, 18) //TwoByTwo_Carry_Borrow_2DigitRem

    }

    fun startNewProblem(dividend: Int, divisor: Int) {
        domainState = DivisionDomainStateV2(
            dividend = dividend,
            divisor = divisor,
            phaseSequence = phaseSequenceProvider.makeTwoByTwoPhaseSequence(dividend, divisor),
            currentStepIndex = 0,
            inputs = emptyList()
        )
        _currentInput.value = ""
        emitUiState()
    }

    fun onDigitInput(digit: String) {
        println("🟡 [onDigitInput] 입력: $digit, 기존 currentInput='${_currentInput.value}'")

        val step = domainState.phaseSequence.steps[domainState.currentStepIndex]
        val maxLength = step.editableCells.size.coerceAtLeast(1)

//        // 입력 길이가 이미 가득 찼으면 "새 입력"으로 덮어쓰기!
//        _currentInput.value = if (_currentInput.value.length >= maxLength) {
//            digit
//        } else {
//            _currentInput.value + digit
//        }

        _currentInput.value = (_currentInput.value + digit).takeLast(maxLength)
        emitUiState()

        println("🟢 [onDigitInput] currentInput(after)=${_currentInput.value}")
    }

    fun onEnter() {
        println("🟡 [onEnter] currentInput=${_currentInput.value}' | currentStep=${domainState.currentStepIndex}")
//        submitInput(_currentInput.value)

        if (_currentInput.value.isEmpty()) return
        submitInput(_currentInput.value)
        _currentInput.value = ""
    }

    fun submitInput(input: String) {
        println("🧪 [submitInput] called at step=${domainState.currentStepIndex} phase=${domainState.phaseSequence.steps.getOrNull(domainState.currentStepIndex)?.phase}")
        println("🟣 currentStepIndex=${domainState.currentStepIndex}, totalSteps=${domainState.phaseSequence.steps.size}")

        val step = domainState.phaseSequence.steps[domainState.currentStepIndex]
        val editableCount = step.editableCells.size
        val actualInput: List<String> = if (editableCount > 1) {
            // 입력값 개수 < editableCells 개수이면 앞에 "?" 등으로 채워넣을 수도 있음
            // but, 항상 maxLength로 입력 받으니 그냥 split!
            input.padStart(editableCount, '?').chunked(1).map { it } // "?"는 임의로, 보통 빈칸이면 처리 가능
        } else {
            listOf(input)
        }

        if (actualInput.size < editableCount || actualInput.any { it == "?" }) {
            domainState = domainState.copy(feedback = "입력을 더 해주세요")
            emitUiState()
            return
        }

        val isCorrect = step.editableCells.withIndex().all { (idx, cellName) ->
            val userInput = actualInput.getOrNull(idx) ?: ""
            phaseEvaluator.isCorrect(
                phase = step.phase,
                cell = cellName,
                input = userInput,
                dividend = domainState.dividend,
                divisor = domainState.divisor,
                stepIndex = domainState.currentStepIndex,
                previousInputs = domainState.inputs
            )
        }

        if (!isCorrect) {
            domainState = domainState.copy(feedback = feedbackProvider.getWrongMessage(step.phase))
            emitUiState()
            return
        }

        // 입력값 분해하여 각각의 셀에 저장
        val updatedInputs = domainState.inputs + actualInput
        val nextStep = domainState.currentStepIndex + 1

        val isLastStep = nextStep >= domainState.phaseSequence.steps.size
        val lastPhase = domainState.phaseSequence.steps.lastOrNull()?.phase

        domainState = domainState.copy(
            inputs = updatedInputs,
            currentStepIndex = nextStep,
            feedback =  if (isLastStep) feedbackProvider.getSuccessMessage(lastPhase ?: DivisionPhaseV2.Complete) else null
        )
        _currentInput.value = ""
        emitUiState()
    }

    fun onClear() {
        println("🟡 [onClear] 기존 currentInput=${_currentInput.value}'")
        _currentInput.value = ""
        domainState = domainState.copy(feedback = null)
        emitUiState()
    }

    private fun emitUiState() {
        println("🟢 emitUiState | domainState=$domainState | currentInput='${_currentInput.value}'")
//        _uiState.value = mapToUiStateV2(domainState, _currentInput.value)
        val isComplete = domainState.currentStepIndex == domainState.phaseSequence.steps.lastIndex
        if (isComplete && domainState.feedback.isNullOrBlank()) {
            domainState = domainState.copy(feedback = feedbackProvider.getSuccessMessage(DivisionPhaseV2.Complete))
        }
        _uiState.value = mapToUiStateV2(domainState, _currentInput.value)
    }
}
