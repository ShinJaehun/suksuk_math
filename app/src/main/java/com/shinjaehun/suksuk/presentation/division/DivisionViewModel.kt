package com.shinjaehun.suksuk.presentation.division

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.shinjaehun.suksuk.domain.PatternDetector
import com.shinjaehun.suksuk.domain.PhaseBuilder
import com.shinjaehun.suksuk.domain.PhaseEvaluator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DivisionViewModel(
    private val autoStart: Boolean = true
) : ViewModel() {
    private val _uiState = MutableStateFlow(DivisionPhasesState(0, 0))
    val uiState: StateFlow<DivisionPhasesState> = _uiState

    private val phaseBuilder = PhaseBuilder()
    private val evaluator = PhaseEvaluator()

    // ViewModel 내부에 현재 키패드 입력값을 관리하기 위한 상태
    var currentInput by mutableStateOf("")
        private set

    init {
        if(autoStart){
            startNewProblem(46, 3) // TensQuotient_NoBorrow_2DigitMul
//            startNewProblem(85, 7) // TensQuotient_NoBorrow_2DigitMul
//            startNewProblem(84, 4) // TensQuotient_NoBorrow_1DigitMul
//            startNewProblem(45, 4) // TensQuotient_NoBorrow_1DigitMul
//            startNewProblem(50, 3) // TensQuotient_Borrow_2DigitMul
//            startNewProblem(90, 7) // TensQuotient_Borrow_2DigitMul
//            startNewProblem(70, 6) // TensQuotient_SkipBorrow_1DigitMul
//            startNewProblem(93, 8) // TensQuotient_SkipBorrow_1DigitMul
//            startNewProblem(62, 7) // OnesQuotient_Borrow
//            startNewProblem(39, 4) // OnesQuotient_NoBorrow
//            startNewProblem(10, 9) // 현재 이러한 경우는 고려하고 있지 않음...
        }
    }

    fun startNewProblem(dividend: Int, divisor: Int) {
        val pattern = PatternDetector.detectPattern(dividend, divisor)
        val phases = phaseBuilder.buildPhasesFor(pattern)
//        println("🔍 [startNewProblem] $dividend ÷ $divisor → pattern=$pattern")

        _uiState.value = DivisionPhasesState(
            dividend,
            divisor,
            0,
            phases,
            mutableListOf(),
            null,
            pattern
        )
    }

    fun onDigitInput(digit: Int) {
        currentInput += digit.toString()
    }

    fun onClear() {
        currentInput = ""
    }


    fun onEnter() {
        val state = _uiState.value
        val phase = state.phases.getOrNull(state.currentPhaseIndex) ?: return

        if (currentInput.isEmpty()) {
            // 아무것도 입력 안했을 때 무시
            return
        }

        when (phase) {
            DivisionPhase.InputMultiply1Total, DivisionPhase.InputMultiply2Total -> {
                if (currentInput.length == 2) {
                    // **전체 2자리 입력을 한 번에 전달!**
                    submitInput(currentInput)
                    currentInput = ""
                }
                // else: 두 자리 안되면 대기 (아니면 에러)
            }
            else -> {
                // 한 칸 입력은 그대로
                submitInput(currentInput)
                currentInput = ""
            }
        }
    }

    fun submitInput(input: String) {
        val state = _uiState.value
        val phase = state.phases.getOrNull(state.currentPhaseIndex) ?: return
        println("📥 submitInput('$input') at phase=${phase}")

        val isCorrect = evaluator.isCorrect(phase, input, state.dividend, state.divisor)

        if (!isCorrect) {
            _uiState.value = state.copy(feedback = "오답입니다. 다시 시도해 보세요")
            return
        }

        val newInputs = state.inputs + input

        _uiState.value = state.copy(
            inputs = newInputs,
            currentPhaseIndex = state.currentPhaseIndex + 1,
            feedback = null,
            pattern = state.pattern,
        )
        println("🔁 emit new state: phase=${_uiState.value.currentPhaseIndex}, inputs=${_uiState.value.inputs}")

        currentInput = ""
//        println("→ 이동 후 Phase: ${state.phases.getOrNull(state.currentPhaseIndex + 1)}")

    }

}