package com.shinjaehun.suksuk.presentation.multiplication

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shinjaehun.suksuk.domain.DomainStateFactory
import com.shinjaehun.suksuk.domain.OpType
import com.shinjaehun.suksuk.domain.Problem
import com.shinjaehun.suksuk.domain.multiplication.evaluator.MulPhaseEvaluator
import com.shinjaehun.suksuk.domain.model.MulDomainState
import com.shinjaehun.suksuk.domain.multiplication.MulPracticeSnapshot
import com.shinjaehun.suksuk.presentation.common.feedback.FeedbackEvent
import com.shinjaehun.suksuk.presentation.common.feedback.FeedbackMessages
import com.shinjaehun.suksuk.presentation.common.feedback.FeedbackProvider
import com.shinjaehun.suksuk.presentation.multiplication.model.MulUiState
import com.shinjaehun.suksuk.presentation.multiplication.model.mapMultiplicationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MultiplicationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val phaseEvaluator: MulPhaseEvaluator,
    private val domainStateFactory: DomainStateFactory,
    private val feedbackProvider: FeedbackProvider
): ViewModel() {

    private val _uiState = MutableStateFlow(MulUiState())
    val uiState: StateFlow<MulUiState> = _uiState.asStateFlow()

    private val _currentInput = MutableStateFlow("")
//    val currentInput: StateFlow<String> = _currentInput.asStateFlow()

    private lateinit var domainState: MulDomainState

    val feedbackEvents: SharedFlow<FeedbackEvent> get() = feedbackProvider.events

    private var restoredOnce = false // 복원 완료 래치
    private var lastProblem: Problem? = null

    private companion object {
        const val SNAPSHOT_KEY = "mul_practice_snapshot"
    }

    /** ---------- 복원 진입점 ---------- */
    fun startNewProblem(problem: Problem, force: Boolean = false) {
        require(problem.type == OpType.Multiplication) { "MulViewModel expects Mul problem, got ${problem.type}" }

        // 같은 문제면 절대 리셋하지 않음
        if (!force && problem == lastProblem && ::domainState.isInitialized) return

        // 아직 복원 전이고, 스냅샷이 이 문제라면 복원 우선
        if (!force && !restoredOnce) {
            val snap = savedStateHandle.get<MulPracticeSnapshot>(SNAPSHOT_KEY)
            if (snap != null && snap.problem == problem) {
                restoreFrom(snap)
                restoredOnce = true
                lastProblem = problem
                return
            }
        }

        lastProblem = problem

        val newDomainState = domainStateFactory.create(problem)
        require(newDomainState is MulDomainState)
        domainState = newDomainState
        _currentInput.value = ""
        emitUiState()
        persistSnapshot() // 최초 진입 시점에도 저장
    }

    fun startNewProblem(multiplicand: Int, multiplier: Int) {
        startNewProblem(Problem(OpType.Multiplication, multiplicand, multiplier))
    }

    fun onDigitInput(digit: Int){
        val currentStep = domainState.phaseSequence.steps[domainState.currentStepIndex]
        val maxLength = currentStep.editableCells.size.coerceAtLeast(1)
        _currentInput.value = (_currentInput.value + digit).takeLast(maxLength)
        emitUiState()
        persistSnapshot()
    }

    fun onEnter(){
        if(_currentInput.value.isEmpty()) return
        submitInput(_currentInput.value)
        _currentInput.value = ""
        persistSnapshot()
    }

    fun submitInput(input: String){
        val currentStep = domainState.phaseSequence.steps[domainState.currentStepIndex]
        val editableCount = currentStep.editableCells.size

        val inputsForThisStep: List<String> =
            if (editableCount > 1) input.padStart(editableCount, '?').chunked(1)
            else listOf(input)

        if (inputsForThisStep.size < editableCount || inputsForThisStep.any { it == "?" }) {
            _currentInput.value = ""
            emitUiState()
            persistSnapshot()
            return
        }
        val evalResult = phaseEvaluator.evaluate(domainState, inputsForThisStep)

        if (!evalResult.isCorrect) {
            feedbackProvider.wrong(FeedbackMessages.randomWrong())
            _currentInput.value = ""
            emitUiState()
            persistSnapshot()
            return
        }

        feedbackProvider.correct(FeedbackMessages.randomCorrect())

        domainState = domainState.copy(
            inputs = domainState.inputs + inputsForThisStep,
            currentStepIndex = evalResult.nextStepIndex ?: domainState.currentStepIndex
        )

        if (evalResult.isFinished) {
            feedbackProvider.phaseCompleted()
        }

        _currentInput.value = ""
        emitUiState()
        persistSnapshot()
    }

    fun onClear(){
        _currentInput.value = ""
        emitUiState()
        persistSnapshot()
    }

    private fun emitUiState() {
        if (!::domainState.isInitialized) {
            _uiState.value = MulUiState()
            return
        }
        _uiState.value = mapMultiplicationUiState(domainState, _currentInput.value)
    }

    /** ---------- 스냅샷 저장 ---------- */
    private fun persistSnapshot() {
        if (!::domainState.isInitialized) return

        val currentProblem = lastProblem
            ?: return  // 문제 정보가 없으면 저장하지 않음(안전장치)

        savedStateHandle[SNAPSHOT_KEY] = MulPracticeSnapshot(
            problem = currentProblem,
            stepIndex = domainState.currentStepIndex,
            confirmedInputs = domainState.inputs,
            currentInput = _currentInput.value
        )
    }

    /** ---------- 스냅샷 복원 ---------- */
    private fun restoreFrom(snap: MulPracticeSnapshot) {
        // 옵션1: 항상 리플레이 복원
        val restoredState = restoreByReplaying(snap)
        domainState = restoredState
        _currentInput.value = snap.currentInput
        emitUiState()
        persistSnapshot()
    }

    private fun restoreByReplaying(snap: MulPracticeSnapshot): MulDomainState {
        val initialState = domainStateFactory.create(snap.problem) as MulDomainState
        var currentState = initialState
        var inputIndex = 0

        while (inputIndex < snap.confirmedInputs.size) {
            val currentStep = currentState.phaseSequence.steps[currentState.currentStepIndex]
            val requiredInputCount = currentStep.editableCells.size.coerceAtLeast(1)
            if (inputIndex + requiredInputCount > snap.confirmedInputs.size) break

            val inputChunk = snap.confirmedInputs.subList(inputIndex, inputIndex + requiredInputCount)
            val evalResult = phaseEvaluator.evaluate(currentState, inputChunk)

            currentState = currentState.copy(
                inputs = currentState.inputs + inputChunk,
                currentStepIndex = evalResult.nextStepIndex ?: currentState.currentStepIndex
            )
            inputIndex += requiredInputCount
        }
        return currentState
    }

    /**
     * Entry에서 바로 호출해서, 문제를 기다리지 않고 곧장 복원.
     * 복원 성공하면 true, 없으면 false.
     */
    fun tryRestoreAtEntry(): Boolean {
        val snap = savedStateHandle.get<MulPracticeSnapshot>(SNAPSHOT_KEY) ?: return false
        // 이미 복원한 적 있으면 재복원 금지
        if (restoredOnce) return true
        restoreFrom(snap)
        restoredOnce = true                     // 래치 ON
        lastProblem = snap.problem
        return true
    }
}