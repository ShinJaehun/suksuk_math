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
    private val phaseEvaluator: PhaseEvaluatorV2, // ← 추가!
): ViewModel() {

    private val _uiState = MutableStateFlow(DivisionUiStateV2())
    val uiState: StateFlow<DivisionUiStateV2> = _uiState.asStateFlow()

    private val _currentInput = MutableStateFlow("")
    val currentInput: StateFlow<String> = _currentInput.asStateFlow()

    private lateinit var domainState: DivisionDomainStateV2

    init {
        startNewProblem(68, 34)
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


        if (domainState.feedback != null) {
            _currentInput.value = ""
            domainState = domainState.copy(feedback = null)
        }

        _currentInput.value += digit
        emitUiState()
    }

    fun onEnter() {
        println("🟡 [onEnter] currentInput=${_currentInput.value}' | currentStep=${domainState.currentStepIndex}")
        submitInput(_currentInput.value)
    }

    fun submitInput(input: String) {
        println("🧪 [submitInput] called at step=${domainState.currentStepIndex} phase=${domainState.phaseSequence.steps.getOrNull(domainState.currentStepIndex)?.phase}")
        println("🟣 currentStepIndex=${domainState.currentStepIndex}, totalSteps=${domainState.phaseSequence.steps.size}")

        if (domainState.currentStepIndex >= domainState.phaseSequence.steps.size) {
            println("⚠️ 이미 마지막 단계를 지나쳤습니다.")
            return
        }

        val step = domainState.phaseSequence.steps[domainState.currentStepIndex]

        // ✅ 완료 단계는 입력 없이 통과 (정답 검증 X)
        if (step.phase == DivisionPhaseV2.Complete) {
            domainState = domainState.copy(
                currentStepIndex = domainState.currentStepIndex + 1,
                feedback = null
            )
            emitUiState()
            return
        }


        val inputCount = step.editableCells.size

        // 💡 입력 분해: editableCell이 1개이면 마지막 숫자만 사용
        val actualInput: List<String> = if (inputCount == 1) {
            listOf(input.lastOrNull()?.toString() ?: "")
        } else {
            input.chunked(1)
        }

        if (actualInput.size < inputCount) {
            domainState = domainState.copy(feedback = "입력을 더 해주세요")
            emitUiState()
            return
        }


        val isCorrect = step.editableCells.withIndex().all { (idx, cellName) ->
            val userInput = input.getOrNull(idx)?.toString() ?: ""
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
            domainState = domainState.copy(feedback = "오답입니다! 다시 시도하세요")
            emitUiState()
            return
        }

        val updatedInputs = domainState.inputs + actualInput
        val nextStep = domainState.currentStepIndex + 1

        domainState = domainState.copy(
            inputs = updatedInputs,
            currentStepIndex = nextStep,
            feedback = null
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
        val latestChar = _currentInput.value.takeLast(1)

        _uiState.value = mapToUiStateV2(domainState, latestChar)
    }
}
