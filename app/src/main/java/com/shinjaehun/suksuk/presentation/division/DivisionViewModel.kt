package com.shinjaehun.suksuk.presentation.division

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shinjaehun.suksuk.domain.DomainStateFactory
import com.shinjaehun.suksuk.domain.OpType
import com.shinjaehun.suksuk.domain.Problem
import com.shinjaehun.suksuk.domain.model.DivisionDomainState
import com.shinjaehun.suksuk.domain.division.evaluator.DivisionPhaseEvaluator
import com.shinjaehun.suksuk.presentation.division.model.DivisionUiState
import com.shinjaehun.suksuk.presentation.division.model.mapDivisionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DivisionViewModel @Inject constructor(
//    savedStateHandle: SavedStateHandle = SavedStateHandle(),
    private val savedStateHandle: SavedStateHandle,
    private val phaseEvaluator: DivisionPhaseEvaluator,
    private val domainStateFactory: DomainStateFactory,
): ViewModel() {
//    private val autoStart: Boolean = savedStateHandle["autoStart"] ?: true

    private val _uiState = MutableStateFlow(DivisionUiState())
    val uiState: StateFlow<DivisionUiState> = _uiState.asStateFlow()

    private val _currentInput = MutableStateFlow("")
//    val currentInput: StateFlow<String> = _currentInput.asStateFlow()

    private lateinit var domainState: DivisionDomainState

//    init {
//        if(autoStart){
//            startNewProblem(68, 34) //TwoByTwo_NoCarry_NoBorrow_1DigitRem
//            startNewProblem(57, 22) //TwoByTwo_NoCarry_NoBorrow_2DigitRem
//            startNewProblem(50, 22) //TwoByTwo_NoCarry_Borrow_1DigitRem
//            startNewProblem(50, 13) //TwoByTwo_NoCarry_Borrow_2DigitRem
//            startNewProblem(96, 12) //TwoByTwo_Carry_NoBorrow_1DigitRem
//            startNewProblem(95, 28) //TwoByTwo_Carry_NoBorrow_2DigitRem
//            startNewProblem(81, 12) //TwoByTwo_Carry_Borrow_1DigitRem
//            startNewProblem(70, 18) //TwoByTwo_Carry_Borrow_2DigitRem
//            startNewProblem(72, 6) // TwoByOne_TensQuotient_NoBorrow_2DigitMul
//            startNewProblem(46, 3) // TwoByOne_TensQuotient_NoBorrow_2DigitMul
//            startNewProblem(50, 3) // TwoByOne_TensQuotient_Borrow_2DigitMul
//            startNewProblem(70, 6) // TwoByOne_TensQuotient_SkipBorrow_1DigitMul
//            startNewProblem(71, 6) // TwoByOne_TensQuotient_SkipBorrow_1DigitMul (11-6 skip borrow 괜찮은거지?)
//            startNewProblem(89, 8) // TwoByOne_TensQuotient_SkipBorrow_1DigitMul (empty subtract1tens)
//            startNewProblem(87, 8) // TwoByOne_TensQuotient_SkipMul2Sub2 <------------ new
//            startNewProblem(62, 7) // TwoByOne_OnesQuotient_Borrow_2DigitMul
//            startNewProblem(81, 9) // TwoByOne_OnesQuotient_NoBorrow_2DigitMul

//            startNewProblem(131, 11) // ThreeByTwo_TensQuotient_NCM1_NBS1_NCM2_NBS2_2DR
//            startNewProblem(682, 31) // ThreeByTwo_TensQuotient_NCM1_NBM1_NCM2_NBM2_1DR

//            startNewProblem(604, 11) // ThreeByTwo_TensQuotient_NCM1_BM1_NCM2_NBM2_2DR
//            startNewProblem(517, 47) // ThreeByTwo_TensQuotient_NCM1_BS1_NCM2_NBS2_1DR

//            startNewProblem(180, 14) // ThreeByTwo_TensQuotient_NCM1_NBS1_NCM2_BS2_2DR
//            startNewProblem(280, 25) // ThreeByTwo_TensQuotient_NCM1_NBS1_NCM2_BS2_1DR

//            startNewProblem(710, 21) // ThreeByTwo_TensQuotient_NCM1_BM1_NCM2_BM2_2DR
//            startNewProblem(911, 43) // ThreeByTwo_TensQuotient_NCM1_BM1_NCM2_BM2_1DR

//            startNewProblem(446, 14) // ThreeByTwo_TensQuotient_CM1_NBS1_NCM2_NBS2_2DR
//            startNewProblem(568, 27) // ThreeByTwo_TensQuotient_CM1_NBS1_NCM2_NBS2_1DR

//            startNewProblem(619, 29) // ThreeByTwo_TensQuotient_CM1_BS1_NCM2_NBS2_2DR
//            startNewProblem(819, 39) // ThreeByTwo_TensQuotient_CM1_BS1_NCM2_NBS2_1DR

//            startNewProblem(654, 14) // ThreeByTwo_TensQuotient_CM1_BS1_CM2_NBS2_2DR
//            startNewProblem(632, 14) // ThreeByTwo_TensQuotient_CM1_BS1_CM2_NBS2_1DR

//            startNewProblem(624, 14) // ThreeByTwo_TensQuotient_CM1_BS1_CM2_BS2_1DR
//            startNewProblem(610, 13) // ThreeByTwo_TensQuotient_CM1_BS1_CM2_BS2_2DR

//            startNewProblem(700, 13) // ThreeByTwo_TensQuotient_CM1_BS1_NCM2_BS2_2DR
//            startNewProblem(800, 13) // ThreeByTwo_TensQuotient_CM1_BS1_NCM2_BS2_1DR
//            startNewProblem(680, 12) // ThreeByTwo_TensQuotient_CM1_NBS1_CM2_BS2_1DR
//            startNewProblem(670, 12) // ThreeByTwo_TensQuotient_CM1_NBS1_CM2_BS2_2DR
//            startNewProblem(660, 12) // ThreeByTwo_TensQuotient_CM1_NBS1_CM2_NBS2_1DR

//            startNewProblem(610, 13) // ThreeByTwo_TensQuotient_2DS1_2DM2
//            startNewProblem(632, 14) //
//            startNewProblem(700, 13) //
//            startNewProblem(619, 29) //
//            startNewProblem(670, 12) //
//            startNewProblem(595, 13) //
//            startNewProblem(460, 14) //
//            startNewProblem(475, 15) //
//            startNewProblem(220, 13) //
//            startNewProblem(202, 12) //
//            startNewProblem(710, 21) //
//            startNewProblem(517, 47) //
//            startNewProblem(190, 13) //
//            startNewProblem(190,12) //
//            startNewProblem(280,25) //
//            startNewProblem(682,31) //
//            startNewProblem(610,15) //
//            startNewProblem(236,13) // ThreeByTwo_TensQuotient_3DS1_3DM2
//            startNewProblem(419,21) //
//            startNewProblem(230,12) //
//            startNewProblem(210,11) //
//            startNewProblem(710,60) //
//            startNewProblem(219,11) //
//            startNewProblem(198,22) //ThreeByTwo_OnesQuotient
//            startNewProblem(230,27) //
//            startNewProblem(205,23) //
//            startNewProblem(342,49) //
//            startNewProblem(234,25) //
//            startNewProblem(315,42) //
//            startNewProblem(107,12) //
//            startNewProblem(104,12) //
//            startNewProblem(150,30) //
//            startNewProblem(109,11) //
//            startNewProblem(101,51) //
//            startNewProblem(229,23) //
//            startNewProblem(214,23) //
//        }
//    }

    fun startNewProblem(dividend: Int, divisor: Int) {
        val problem = Problem(OpType.Division, dividend, divisor)
        val ds = domainStateFactory.create(problem)
        require(ds is DivisionDomainState) { "Expected DivisionDomainState, got ${ds::class.simpleName}" }
        domainState = ds
        _currentInput.value = ""
        emitUiState()
    }

    fun onDigitInput(digit: Int) {
//        println("🟡 [onDigitInput] 입력: $digit, 기존 currentInput='${_currentInput.value}'")

        val step = domainState.phaseSequence.steps[domainState.currentStepIndex]
        val maxLength = step.editableCells.size.coerceAtLeast(1)

        _currentInput.value = (_currentInput.value + digit).takeLast(maxLength)
        emitUiState()

//        println("🟢 [onDigitInput] currentInput(after)=${_currentInput.value}")
    }

    fun onEnter() {
//        println("🟡 [onEnter] currentInput=${_currentInput.value}' | currentStep=${domainState.currentStepIndex}")
        if (_currentInput.value.isEmpty()) return
        submitInput(_currentInput.value)
        _currentInput.value = ""
    }

    fun submitInput(input: String) {
//        println("🧪 [submitInput] called at step=${domainState.currentStepIndex} phase=${domainState.phaseSequence.steps.getOrNull(domainState.currentStepIndex)?.phase}")
//        println("🟣 currentStepIndex=${domainState.currentStepIndex}, totalSteps=${domainState.phaseSequence.steps.size}")

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

    fun onClear() {
//        println("🟡 [onClear] 기존 currentInput=${_currentInput.value}'")
        _currentInput.value = ""
        emitUiState()
    }

    private fun emitUiState() {
        if (!::domainState.isInitialized) {   // 안전가드(초기 렌더링)
            _uiState.value = DivisionUiState()
            return
        }
        _uiState.value = mapDivisionUiState(domainState, _currentInput.value)
    }
}