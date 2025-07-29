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
//            startNewProblem(46, 3) // TensQuotient_NoBorrow_2DigitMul
//            startNewProblem(85, 7) // TensQuotient_NoBorrow_2DigitMul
//            startNewProblem(84, 4) // TensQuotient_NoBorrow_1DigitMul
//            startNewProblem(45, 4) // TensQuotient_NoBorrow_1DigitMul
//            startNewProblem(50, 3) // TensQuotient_Borrow_2DigitMul
//            startNewProblem(90, 7) // TensQuotient_Borrow_2DigitMul
//            startNewProblem(70, 6) // TensQuotient_SkipBorrow_1DigitMul
//            startNewProblem(93, 8) // TensQuotient_SkipBorrow_1DigitMul
            startNewProblem(62, 7) // OnesQuotient_Borrow
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
//        println("📥 submitInput('$input') at phase=${phase}")

        // for debugging : 전체 UI 상태
//        val previewUiState = mapPhasesToCells(state, input)
//        println("🖼️ 이전 UI 상태:")
//        println(previewUiState.toDebugString(phase))

        // for debugging : 입력 상태
//        logPhaseContext(state, input)

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
//        println("🔁 emit new state: phase=${_uiState.value.currentPhaseIndex}, inputs=${_uiState.value.inputs}")

        currentInput = ""
//        println("→ 이동 후 Phase: ${state.phases.getOrNull(state.currentPhaseIndex + 1)}")


    }
}

//fun logPhaseContext(
//    state: DivisionPhasesState,
//    currentInput: String
//) {
//    val currentPhase = state.phases.getOrNull(state.currentPhaseIndex)
//    val currentIndex = state.currentPhaseIndex
//    val allInputs = state.inputs.joinToString()
//
//    val cellNames = guessCellNamesFromPhase(currentPhase)
//
//    println("🧩 입력상황 ▶️")
//    println("   ▶️ PhaseIndex: $currentIndex")
//    println("   ▶️ Phase     : $currentPhase")
//    println("   ▶️ CellNames : ${cellNames.joinToString()}")
//    println("   ▶️ CurrentInput: '$currentInput'")
//    println("   ▶️ All Inputs : [$allInputs]")
//    println("---------------------------------")
//}
//
//fun guessCellNamesFromPhase(phase: DivisionPhase?): List<CellName> = when (phase) {
//    DivisionPhase.InputQuotientTens -> listOf(CellName.QuotientTens)
//    DivisionPhase.InputQuotientOnes, DivisionPhase.InputQuotient -> listOf(CellName.QuotientOnes)
//
//    DivisionPhase.InputMultiply1Tens, DivisionPhase.InputMultiply1 -> listOf(CellName.Multiply1Tens)
//    DivisionPhase.InputMultiply1Ones -> listOf(CellName.Multiply1Ones)
//    DivisionPhase.InputMultiply1Total -> listOf(CellName.Multiply1Tens, CellName.Multiply1Ones)
//
//    DivisionPhase.InputSubtract1Tens -> listOf(CellName.Subtract1Tens)
//    DivisionPhase.InputSubtract1Ones, DivisionPhase.InputSubtract1Result -> listOf(CellName.Subtract1Ones)
//
//    DivisionPhase.InputMultiply2Tens -> listOf(CellName.Multiply2Tens)
//    DivisionPhase.InputMultiply2Ones -> listOf(CellName.Multiply2Ones)
//    DivisionPhase.InputMultiply2Total -> listOf(CellName.Multiply2Tens, CellName.Multiply2Ones)
//
//    DivisionPhase.InputSubtract2Result -> listOf(CellName.Subtract2Ones)
//
//    DivisionPhase.InputBorrowFromDividendTens -> listOf(CellName.BorrowDividendTens)
//    DivisionPhase.InputBorrowFromSubtract1Tens -> listOf(CellName.BorrowSubtract1Tens)
//
//    DivisionPhase.InputBringDownFromDividendOnes -> listOf(CellName.Subtract1Ones)
//
//    else -> emptyList()
//}
//
//fun DivisionUiState.toDebugString(phase: DivisionPhase?): String {
//    return buildString {
//        appendLine("▶️ Stage: $stage")
//        appendLine("▶️ Phase: ${phase}")
//        appendLine("▶️ Divisor: ${divisor.value}, highlight=${divisor.highlight}, editable=${divisor.editable}")
//        appendLine("▶️ Dividend: ${dividendTens.value}${dividendOnes.value}")
//        appendLine("    - DividendTens: highlight=${dividendTens.highlight}, crossOut=${dividendTens.crossOutColor}")
//        appendLine("    - DividendOnes: highlight=${dividendOnes.highlight}")
//        appendLine("    - Borrowed10DividendOnes: ${borrowed10DividendOnes.value}")
//
//        appendLine("▶️ Quotient: ${quotientTens.value}${quotientOnes.value}")
//        appendLine("    - QuotientTens: editable=${quotientTens.editable}, highlight=${quotientTens.highlight}")
//        appendLine("    - QuotientOnes: editable=${quotientOnes.editable}, highlight=${quotientOnes.highlight}")
//
//        appendLine("▶️ Multiply1: ${multiply1Tens.value}${multiply1Ones.value}")
//        appendLine("    - Multiply1Tens: editable=${multiply1Tens.editable}, highlight=${multiply1Tens.highlight}")
//        appendLine("    - Multiply1Ones: editable=${multiply1Ones.editable}, highlight=${multiply1Ones.highlight}")
//
//        appendLine("▶️ Subtract1: ${subtract1Tens.value}${subtract1Ones.value}")
//        appendLine("    - Subtract1Tens: editable=${subtract1Tens.editable}, crossOut=${subtract1Tens.crossOutColor}")
//        appendLine("    - Subtract1Ones: editable=${subtract1Ones.editable}")
//        appendLine("    - Borrowed10Subtract1Ones: ${borrowed10Subtract1Ones.value}")
//
//        appendLine("▶️ Multiply2: ${multiply2Tens.value}${multiply2Ones.value}")
//        appendLine("    - Multiply2Tens: editable=${multiply2Tens.editable}")
//        appendLine("    - Multiply2Ones: editable=${multiply2Ones.editable}")
//
//        appendLine("▶️ Subtract2: ${subtract2Ones.value}, editable=${subtract2Ones.editable}")
//
//        appendLine("▶️ Borrowing:")
//        appendLine("    - From DividendTens: ${borrowDividendTens.value}, editable=${borrowDividendTens.editable}")
//        appendLine("    - From Subtract1Tens: ${borrowSubtract1Tens.value}, editable=${borrowSubtract1Tens.editable}")
//
//        appendLine("▶️ SubtractLines: $subtractLines")
//        appendLine("▶️ Feedback: $feedback")
//    }
//}
