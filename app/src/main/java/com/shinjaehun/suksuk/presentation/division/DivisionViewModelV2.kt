package com.shinjaehun.suksuk.presentation.division

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shinjaehun.suksuk.domain.division.DivisionDomainStateV2
import com.shinjaehun.suksuk.domain.division.DivisionPatternV2
import com.shinjaehun.suksuk.domain.division.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.division.DivisionPhaseV2
import com.shinjaehun.suksuk.domain.division.DivisionUiStateV2
import com.shinjaehun.suksuk.domain.division.PatternDetectorV2
import com.shinjaehun.suksuk.domain.division.PhaseEvaluatorV2
import com.shinjaehun.suksuk.domain.division.PhaseSequence
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DivisionViewModelV2 @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val phaseSequenceProvider: DivisionPhaseSequenceProvider,
    private val phaseEvaluator: PhaseEvaluatorV2,
    private val feedbackProvider: FeedbackMessageProviderV2
): ViewModel() {
    private val autoStart: Boolean = savedStateHandle["autoStart"] ?: true

    private val _uiState = MutableStateFlow(DivisionUiStateV2())
    val uiState: StateFlow<DivisionUiStateV2> = _uiState.asStateFlow()

    private val _currentInput = MutableStateFlow("")
    val currentInput: StateFlow<String> = _currentInput.asStateFlow()

    private lateinit var domainState: DivisionDomainStateV2
    fun getCurrentPattern(): DivisionPatternV2 = domainState.phaseSequence.pattern

    init {
        if(autoStart){
//        startNewProblem(68, 34) //TwoByTwo_NoCarry_NoBorrow_1DigitRem
//        startNewProblem(57, 22) //TwoByTwo_NoCarry_NoBorrow_2DigitRem
//        startNewProblem(50, 22) //TwoByTwo_NoCarry_Borrow_1DigitRem
//        startNewProblem(50, 13) //TwoByTwo_NoCarry_Borrow_2DigitRem
//        startNewProblem(96, 12) //TwoByTwo_Carry_NoBorrow_1DigitRem
//        startNewProblem(95, 28) //TwoByTwo_Carry_NoBorrow_2DigitRem
//        startNewProblem(81, 12) //TwoByTwo_Carry_Borrow_1DigitRem
//        startNewProblem(70, 18) //TwoByTwo_Carry_Borrow_2DigitRem
//        startNewProblem(72, 6) // TwoByOne_TensQuotient_NoBorrow_2DigitMul
            startNewProblem(46, 3) // TwoByOne_TensQuotient_NoBorrow_2DigitMul
        }
    }

    fun startNewProblem(dividend: Int, divisor: Int) {
        val pattern = PatternDetectorV2.detectPattern(dividend, divisor)

        val sequence = when (pattern) {
            DivisionPatternV2.TwoByOne -> phaseSequenceProvider.makeTwoByOnePhaseSequence(dividend, divisor)
            DivisionPatternV2.TwoByTwo -> phaseSequenceProvider.makeTwoByTwoPhaseSequence(dividend, divisor)
            DivisionPatternV2.ThreeByTwo -> TODO()
        }

        domainState = DivisionDomainStateV2(
            dividend = dividend,
            divisor = divisor,
            phaseSequence = sequence,
            currentStepIndex = 0,
            inputs = emptyList()
        )
        _currentInput.value = ""
        emitUiState()
    }

    fun onDigitInput(digit: String) {
//        println("🟡 [onDigitInput] 입력: $digit, 기존 currentInput='${_currentInput.value}'")

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

//        println("🟢 [onDigitInput] currentInput(after)=${_currentInput.value}")
    }

    fun onEnter() {
//        println("🟡 [onEnter] currentInput=${_currentInput.value}' | currentStep=${domainState.currentStepIndex}")
//        submitInput(_currentInput.value)

        if (_currentInput.value.isEmpty()) return
        submitInput(_currentInput.value)
        _currentInput.value = ""
    }

    fun submitInput(input: String) {
//        println("🧪 [submitInput] called at step=${domainState.currentStepIndex} phase=${domainState.phaseSequence.steps.getOrNull(domainState.currentStepIndex)?.phase}")
//        println("🟣 currentStepIndex=${domainState.currentStepIndex}, totalSteps=${domainState.phaseSequence.steps.size}")

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
            // 입력 부족한 경우도 UI에서 판단하게 위임할 수 있음 (예: ? 포함 셀 강조 등)
            // 그냥 없애도 될 듯!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            _currentInput.value = ""
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
            // 오답 피드백도 UI에서 판단하게 위임할 수 있음
            _currentInput.value = ""
            emitUiState()
            return
        }

//        val updatedInputs = domainState.inputs + actualInput
//        val nextStep = domainState.currentStepIndex + 1
//        val totalSteps = domainState.phaseSequence.steps.size
//
//        if (nextStep >= totalSteps) {
//            // 마지막 정답을 맞추고 완료 상태에 진입
//            domainState = domainState.copy(
//                inputs = updatedInputs,
//                currentStepIndex = nextStep,
//                feedback = feedbackProvider.getSuccessMessage(DivisionPhaseV2.Complete)
//            )
//            println("🟢 [submitInput] COMPLETE! feedback=${domainState.feedback}")
//            emitUiState()
//            return
//        } else {
//            // 아직 남은 단계가 있음
//            domainState = domainState.copy(
//                inputs = updatedInputs,
//                currentStepIndex = nextStep,
//                feedback = null
//            )
//        }

//        // 정답 입력 처리
//        val updatedInputs = domainState.inputs + actualInput
//        val nextStep = domainState.currentStepIndex + 1
//        val totalSteps = domainState.phaseSequence.steps.size
//        val isLastInput = nextStep >= totalSteps
//
//        // ✅ 핵심: feedback은 여기서만 처리
//        domainState = domainState.copy(
//            inputs = updatedInputs,
//            currentStepIndex = nextStep,
//            feedback = if (isLastInput) {
//                feedbackProvider.getSuccessMessage(DivisionPhaseV2.Complete)
//            } else null
//        )

//        // 정답이면 입력 반영 + 단계 전환
//        val updatedInputs = domainState.inputs + actualInput
//        val nextStep = domainState.currentStepIndex + 1
//
//        domainState = domainState.copy(
//            inputs = updatedInputs,
//            currentStepIndex = nextStep
//        )

        val updatedInputs = domainState.inputs + actualInput
        val nextStep = domainState.currentStepIndex + 1

        domainState = domainState.copy(
            inputs = updatedInputs,
            currentStepIndex = nextStep
        )

        _currentInput.value = ""
        emitUiState()
    }

    fun onClear() {
//        println("🟡 [onClear] 기존 currentInput=${_currentInput.value}'")
        _currentInput.value = ""
        emitUiState()
    }

//    private fun emitUiState() {
//        println("🟢 emitUiState | domainState=$domainState | currentInput='${_currentInput.value}'")
////        _uiState.value = mapToUiStateV2(domainState, _currentInput.value)
////        val isComplete = domainState.currentStepIndex == domainState.phaseSequence.steps.lastIndex
////        if (isComplete && domainState.feedback.isNullOrBlank()) {
////            domainState = domainState.copy(feedback = feedbackProvider.getSuccessMessage(DivisionPhaseV2.Complete))
////        }
//        _uiState.value = mapToUiStateV2(domainState, _currentInput.value)
//    }

    private fun emitUiState() {


//        val isComplete = domainState.currentStepIndex == domainState.phaseSequence.steps.lastIndex
//        val feedbackToUse = when {
//            !domainState.feedback.isNullOrBlank() -> domainState.feedback
//            isComplete -> feedbackProvider.getSuccessMessage(DivisionPhaseV2.Complete)
//            else -> null
//        }
//
//        _uiState.value = mapToUiStateV2(
//            domainState.copy(feedback = feedbackToUse),
//            _currentInput.value
//        )

        _uiState.value = mapToUiStateV2(domainState, _currentInput.value)
    }
}
