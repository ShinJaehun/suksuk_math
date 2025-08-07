package com.shinjaehun.suksuk.presentation.division.legacy

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shinjaehun.suksuk.domain.division.legacy.evaluator.PhaseEvaluator
import com.shinjaehun.suksuk.domain.division.legacy.factory.DivisionDomainStateFactory
import com.shinjaehun.suksuk.domain.division.legacy.model.DivisionDomainState
import com.shinjaehun.suksuk.domain.division.legacy.model.DivisionPhase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DivisionViewModel @Inject constructor (
    private val savedStateHandle: SavedStateHandle,
    private val phaseEvaluator: PhaseEvaluator,
    private val domainStateFactory: DivisionDomainStateFactory,
    private val feedbackProvider: FeedbackMessageProvider
) : ViewModel() {
    private val autoStart: Boolean = savedStateHandle["autoStart"] ?: true
    private val _domainState = MutableStateFlow(DivisionDomainState(0, 0))
    val domainState: StateFlow<DivisionDomainState> = _domainState

    var currentInput by mutableStateOf("")
        private set

    init {
        if (autoStart) {
//            startNewProblem(46, 3) // TensQuotient_NoBorrow_2DigitMul
//            startNewProblem(85, 7) // TensQuotient_NoBorrow_2DigitMul
//            startNewProblem(50, 3) // TensQuotient_Borrow_2DigitMul
//            startNewProblem(90, 7) // TensQuotient_Borrow_2DigitMul

//            startNewProblem(84, 4) // TensQuotient_NoBorrow_1DigitMul
//            startNewProblem(45, 4) // TensQuotient_NoBorrow_1DigitMul
//            startNewProblem(70, 6) // TensQuotient_SkipBorrow_1DigitMul
//            startNewProblem(93, 8) // TensQuotient_SkipBorrow_1DigitMul
//            startNewProblem(90, 8) // TensQuotient_SkipBorrow_1DigitMul
//            startNewProblem(62, 7) // OnesQuotient_Borrow
//            startNewProblem(39, 4) // OnesQuotient_NoBorrow
//            startNewProblem(10, 9) // 현재 이러한 경우는 고려하고 있지 않음...
//            startNewProblem(48, 19) // 병시나 나머지 두 자리...ㅠㅠ
//            startNewProblem(68, 34) // TwoByTwo_NoCarry_NoBorrow_1DigitRem
//            startNewProblem(50, 22) // TwoByTwo_NoCarry_Borrow_1DigitRem
//            startNewProblem(57, 22) // TwoByTwo_NoCarry_Borrow_1DigitRem
//            startNewProblem(75, 25) // TwoByTwo_Carry_NoBorrow_1DigitRem
//            startNewProblem(81, 12) // TwoByTwo_Carry_Borrow_1DigitRem
//            startNewProblem(57, 22) // TwoByTwo_NoCarry_NoBorrow_2DigitRem
//            startNewProblem(50, 13) // TwoByTwo_NoCarry_Borrow_2DigitRem
        }
    }

    fun startNewProblem(dividend: Int, divisor: Int) {
        _domainState.value = domainStateFactory.create(dividend, divisor)
    }

    fun onDigitInput(digit: Int) {
//        println("onDigitInput($digit) phase=${_domainState.value.phases.getOrNull(_domainState.value.currentPhaseIndex)} currentInput(before)=$currentInput")

        val state = _domainState.value
        val phase = state.phases.getOrNull(state.currentPhaseIndex) ?: return

        val maxLength = when (phase) {
            DivisionPhase.InputMultiply1TensAndMultiply1Ones,
            DivisionPhase.InputMultiply2TensAndMultiply2Ones,
            DivisionPhase.InputMultiply1OnesWithCarry -> 2
            else -> 1
        }
        currentInput = (currentInput + digit).takeLast(maxLength)
//        println("currentInput(after)=$currentInput")

    }

    fun onClear() {
        currentInput = ""
    }

    fun onEnter() {
//        println("onEnter called! currentInput=$currentInput phase=${_domainState.value.phases.getOrNull(_domainState.value.currentPhaseIndex)}")
        if (currentInput.isEmpty()) {
//            println("onEnter: currentInput empty, return")
            return
        }

        submitInput(currentInput)
        currentInput = ""
    }

    fun submitInput(input: String) {
        val state = _domainState.value
        val phase = state.phases.getOrNull(state.currentPhaseIndex) ?: return
//        println("📥 submitInput('$input') at phase=${phase}")
//
//        // for debugging : 전체 UI 상태
//        val previewUiState = DivisionUiStateBuilder.mapToUiState(state, currentInput)
//        println("🖼️ 이전 UI 상태:")
//        println(previewUiState.toDebugString(phase))
//
//        // for debugging : 입력 상태
//        logPhaseContext(state, input)

        val nextPhaseIndex = state.currentPhaseIndex + 1
        val nextPhase = state.phases.getOrNull(nextPhaseIndex)
        val feedback = feedbackProvider.getSuccessMessage(nextPhase ?: DivisionPhase.Complete)

        if (phase == DivisionPhase.InputMultiply1TensAndMultiply1Ones ||
            phase == DivisionPhase.InputMultiply2TensAndMultiply2Ones ||
            phase == DivisionPhase.InputMultiply1OnesWithCarry ) {
            val isCorrect = phaseEvaluator.isCorrect(phase, input, state.dividend, state.divisor)
            if (!isCorrect) {
                _domainState.value = state.copy(feedback = feedbackProvider.getWrongMessage(phase))
                return
            }

            val newInputs = state.inputs + input[0].toString() + input[1].toString()
            _domainState.value = state.copy(
                inputs = newInputs,
                currentPhaseIndex = nextPhaseIndex,
                feedback = feedback,
                pattern = state.pattern,
            )
            currentInput = ""
            return
        } else {
            val isCorrect = phaseEvaluator.isCorrect(phase, input, state.dividend, state.divisor)
            if (!isCorrect) {
                _domainState.value = state.copy(feedback = feedbackProvider.getWrongMessage(phase))
                return
            }
            val newInputs = state.inputs + input
            _domainState.value = state.copy(
                inputs = newInputs,
                currentPhaseIndex = nextPhaseIndex,
                feedback = feedback,
                pattern = state.pattern,
            )
            currentInput = ""
        }
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
