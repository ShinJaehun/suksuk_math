package com.shinjaehun.suksuk.presentation.challenge

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinjaehun.suksuk.domain.DomainStateFactory
import com.shinjaehun.suksuk.domain.OpType
import com.shinjaehun.suksuk.domain.Problem
import com.shinjaehun.suksuk.domain.ProblemSessionFactory
import com.shinjaehun.suksuk.domain.ProblemSource
import com.shinjaehun.suksuk.domain.SessionMode
import com.shinjaehun.suksuk.domain.division.evaluator.DivisionPhaseEvaluator
import com.shinjaehun.suksuk.domain.model.DivisionDomainState
import com.shinjaehun.suksuk.domain.model.MulDomainState
import com.shinjaehun.suksuk.domain.multiplication.evaluator.MulPhaseEvaluator
import com.shinjaehun.suksuk.presentation.common.feedback.FeedbackEvent
import com.shinjaehun.suksuk.presentation.common.feedback.FeedbackMessages
import com.shinjaehun.suksuk.presentation.common.feedback.FeedbackProvider
import com.shinjaehun.suksuk.presentation.division.model.mapDivisionUiState
import com.shinjaehun.suksuk.presentation.multiplication.model.mapMultiplicationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val saved: SavedStateHandle,
    private val problemFactory: ProblemSessionFactory,
    private val domainFactory: DomainStateFactory,
    private val mulEval: MulPhaseEvaluator,
    private val divEval: DivisionPhaseEvaluator,
    private val feedback: FeedbackProvider
) : ViewModel() {

    companion object { private const val SNAP = "challenge_snapshot" }

    private val _challengeUi = MutableStateFlow(ChallengeUi())
    val challengeUi: StateFlow<ChallengeUi> = _challengeUi.asStateFlow()

    val feedbackEvents: SharedFlow<FeedbackEvent> get() = feedback.events

    // 현재 도메인 (둘 중 하나만 활성)
    private var mulDomainState: MulDomainState? = null
    private var divDomainState: DivisionDomainState? = null

    // 문제 소스(Host가 독점)
    private var source: ProblemSource? = null
    private var initialized = false

    // ====== 초기화: 복원 우선, 실패 시 세션 오픈 + 첫 문제 요청 ======
    fun initIfNeeded() {
        if (initialized) return
        if (tryRestore()) { initialized = true; return }

        source = problemFactory.openSession(
            op = OpType.Division, // 내부에서 랜덤 믹스 핸들한다면 아무거나
            mode = SessionMode.Challenge,
            pattern = null,
            overrideOperands = null
        )

        viewModelScope.launch {
            source!!.problems.collect { p -> startProblem(p) }
        }
        viewModelScope.launch { source!!.requestNext() }

        initialized = true
    }

    // ====== 문제 시작 (동일 문제면 no-op) ======
    private fun startProblem(problem: Problem) {
        val currentProblem = _challengeUi.value.problem
        if (currentProblem == problem) return

        when (problem.type) {
            OpType.Multiplication -> {
                mulDomainState = domainFactory.create(problem) as MulDomainState
                divDomainState = null
                _challengeUi.update {
                    it.copy(
                        problem = problem, op = problem.type,
                        currentInput = "", isCompleted = false, showStamp = false,
                        mulUi = mapMultiplicationUiState(mulDomainState!!, ""),
                        divUi = null
                    )
                }
            }
            OpType.Division -> {
                divDomainState = domainFactory.create(problem) as DivisionDomainState
                mulDomainState = null
                _challengeUi.update {
                    it.copy(
                        problem = problem, op = problem.type,
                        currentInput = "", isCompleted = false, showStamp = false,
                        mulUi = null,
                        divUi = mapDivisionUiState(divDomainState!!, "")
                    )
                }
            }
        }
        persist()
    }

    // ====== 입력 ======
    fun onDigit(d: Int) {
        val currentUi = _challengeUi.value
        val currentProblem = currentUi.problem ?: return
        val (requiredInputCount, updatedInput) = when (currentProblem.type) {
            OpType.Multiplication -> {
                val currentStep = mulDomainState!!.phaseSequence.steps[mulDomainState!!.currentStepIndex]
                val required = currentStep.editableCells.size.coerceAtLeast(1)
                required to (currentUi.currentInput + d).takeLast(required)
            }
            OpType.Division -> {
                val currentStep = divDomainState!!.phaseSequence.steps[divDomainState!!.currentStepIndex]
                val required = currentStep.editableCells.size.coerceAtLeast(1)
                required to (currentUi.currentInput + d).takeLast(required)
            }
        }
        _challengeUi.update { it.copy(currentInput = updatedInput) }
        // 보드 UI 갱신
        buildBoardUiAndUpdate()
        persist()
    }

    fun onClear() {
        _challengeUi.update { it.copy(currentInput = "") }
        buildBoardUiAndUpdate()
        persist()
    }

    fun onEnter() {
        val currentUi = _challengeUi.value
        val currentProblem = currentUi.problem ?: return
        val input = currentUi.currentInput.ifEmpty { return }

        // 입력을 스텝 요구 길이에 맞춰 분할
        fun toInputChunk(need: Int): List<String> =
            if (need > 1) input.padStart(need, '?').chunked(1) else listOf(input)

        when (currentProblem.type) {
            OpType.Multiplication -> {
                val currentStep = mulDomainState!!.phaseSequence.steps[mulDomainState!!.currentStepIndex]
                val requiredInputCount = currentStep.editableCells.size
                val inputChunk = toInputChunk(requiredInputCount)
                if (inputChunk.size < requiredInputCount || inputChunk.any { it == "?" }) {
                    _challengeUi.update { it.copy(currentInput = "") }
                    buildBoardUiAndUpdate(); persist()
                    return
                }
                val evalResult = mulEval.evaluate(mulDomainState!!, inputChunk)
                if (!evalResult.isCorrect) {
                    feedback.wrong(FeedbackMessages.randomWrong())
                    _challengeUi.update { it.copy(wrong = it.wrong + 1, currentStreak = 0, currentInput = "") }
                    buildBoardUiAndUpdate(); persist()
                    return
                }
                feedback.correct(FeedbackMessages.randomCorrect())
                _challengeUi.update {
                    it.copy(correct = it.correct + 1,
                        currentStreak = it.currentStreak + 1,
                        bestStreak = maxOf(it.bestStreak, it.currentStreak + 1))
                }
                mulDomainState = mulDomainState!!.copy(
                    inputs = mulDomainState!!.inputs + inputChunk,
                    currentStepIndex = evalResult.nextStepIndex ?: mulDomainState!!.currentStepIndex
                )
                if (evalResult.isFinished) {
                    feedback.phaseCompleted()
                    _challengeUi.update { it.copy(
                        solved = it.solved + 1,
                        isCompleted = true,
                        showStamp = true,
                        solvedMul = it.solvedMul + 1
                    ) }
                }
                _challengeUi.update { it.copy(currentInput = "") }
                buildBoardUiAndUpdate(); persist()
            }

            OpType.Division -> {
                val currentStep = divDomainState!!.phaseSequence.steps[divDomainState!!.currentStepIndex]
                val requiredInputCount = currentStep.editableCells.size
                val inputChunk = toInputChunk(requiredInputCount)
                if (inputChunk.size < requiredInputCount || inputChunk.any { it == "?" }) {
                    _challengeUi.update { it.copy(currentInput = "") }
                    buildBoardUiAndUpdate(); persist()
                    return
                }
                val evalResult = divEval.evaluate(divDomainState!!, inputChunk)
                if (!evalResult.isCorrect) {
                    feedback.wrong(FeedbackMessages.randomWrong())
                    _challengeUi.update { it.copy(wrong = it.wrong + 1, currentStreak = 0, currentInput = "") }
                    buildBoardUiAndUpdate(); persist()
                    return
                }
                feedback.correct(FeedbackMessages.randomCorrect())
                _challengeUi.update {
                    it.copy(correct = it.correct + 1,
                        currentStreak = it.currentStreak + 1,
                        bestStreak = maxOf(it.bestStreak, it.currentStreak + 1))
                }
                divDomainState = divDomainState!!.copy(
                    inputs = divDomainState!!.inputs + inputChunk,
                    currentStepIndex = evalResult.nextStepIndex ?: divDomainState!!.currentStepIndex
                )
                if (evalResult.isFinished) {
                    feedback.phaseCompleted()
                    _challengeUi.update { it.copy(
                        solved = it.solved + 1,
                        isCompleted = true,
                        showStamp = true,
                        solvedDiv = it.solvedDiv + 1
                    ) }
                }
                _challengeUi.update { it.copy(currentInput = "") }
                buildBoardUiAndUpdate(); persist()
            }
        }
    }

    fun onNextProblem() {
        _challengeUi.update { it.copy(showStamp = false, isCompleted = false) }
        viewModelScope.launch { source?.requestNext() }
        persist()
    }

    // ====== 저장 ======
    private fun persist() {
        val currentUi = _challengeUi.value
        val (stepIndex, inputs) = when (currentUi.op) {
            OpType.Multiplication -> mulDomainState?.currentStepIndex to (mulDomainState?.inputs ?: emptyList())
            OpType.Division       -> divDomainState?.currentStepIndex to (divDomainState?.inputs ?: emptyList())
            else -> 0 to emptyList()
        }
        saved[SNAP] = ChallengeSnapshot(
            current = currentUi.problem,
            stepIndex = stepIndex ?: 0,
            confirmedInputs = inputs,
            currentInput = currentUi.currentInput,
            solved = currentUi.solved, correct = currentUi.correct, wrong = currentUi.wrong,
            currentStreak = currentUi.currentStreak, bestStreak = currentUi.bestStreak,
            solvedMul = currentUi.solvedMul,
            solvedDiv = currentUi.solvedDiv,
            showStamp = currentUi.showStamp
        )
    }

    // ====== 복원 ======
    private fun tryRestore(): Boolean {
        val snap = saved.get<ChallengeSnapshot>(SNAP) ?: return false
        val currentProblem = snap.current ?: return false

        // 도메인 리플레이 복원
        when (currentProblem.type) {
            OpType.Multiplication -> {
                val initialDomainState = domainFactory.create(currentProblem) as MulDomainState
                mulDomainState = replayMul(initialDomainState, snap.confirmedInputs)
                divDomainState = null
                _challengeUi.value = ChallengeUi(
                    problem = currentProblem, op = currentProblem.type,
                    mulUi = mapMultiplicationUiState(mulDomainState!!, snap.currentInput),
                    divUi = null,
                    currentInput = snap.currentInput,
                    // 완료 래치는 복원 시 false로 두는 것이 보통 안전
                    isCompleted = false,
                    showStamp = snap.showStamp,
                    solvedMul = snap.solvedMul,
                    solvedDiv = snap.solvedDiv,
                    solved = snap.solved, correct = snap.correct, wrong = snap.wrong,
                    currentStreak = snap.currentStreak, bestStreak = snap.bestStreak
                )
            }
            OpType.Division -> {
                val initialDomainState = domainFactory.create(currentProblem) as DivisionDomainState
                divDomainState = replayDiv(initialDomainState, snap.confirmedInputs)
                mulDomainState = null
                _challengeUi.value = ChallengeUi(
                    problem = currentProblem, op = currentProblem.type,
                    mulUi = null,
                    divUi = mapDivisionUiState(divDomainState!!, snap.currentInput),
                    currentInput = snap.currentInput,
                    isCompleted = false,
                    showStamp = snap.showStamp,
                    solved = snap.solved, correct = snap.correct, wrong = snap.wrong,
                    currentStreak = snap.currentStreak, bestStreak = snap.bestStreak
                )
            }
        }
        return true
    }

    // ====== 보드 UI 갱신 헬퍼 ======
    private fun buildBoardUiAndUpdate() {
        val currentUi = _challengeUi.value
        val currentProblem = currentUi.problem ?: return
        when (currentProblem.type) {
            OpType.Multiplication -> {
                val ui = mapMultiplicationUiState(mulDomainState!!, currentUi.currentInput)
                _challengeUi.update { it.copy(mulUi = ui, divUi = null) }
            }
            OpType.Division -> {
                val ui = mapDivisionUiState(divDomainState!!, currentUi.currentInput)
                _challengeUi.update { it.copy(divUi = ui, mulUi = null) }
            }
        }
    }

    // ====== 리플레이: 저장된 확정 입력으로 도메인 재구성 ======
    private fun replayMul(initialDomainState: MulDomainState, confirmedInputs: List<String>): MulDomainState {
        var currentDomainState = initialDomainState
        var inputIndex = 0
        val steps = currentDomainState.phaseSequence.steps

        while (inputIndex < confirmedInputs.size && currentDomainState.currentStepIndex <= steps.lastIndex) {
            val currentStep = steps[currentDomainState.currentStepIndex]
            val requiredInputCount = currentStep.editableCells.size.coerceAtLeast(1)
            if (inputIndex + requiredInputCount > confirmedInputs.size) break
            val inputChunk = confirmedInputs.subList(inputIndex, inputIndex + requiredInputCount)
            val evalResult = mulEval.evaluate(currentDomainState, inputChunk)
            if (!evalResult.isCorrect) break
            currentDomainState = currentDomainState.copy(
                inputs = currentDomainState.inputs + inputChunk,
                currentStepIndex = evalResult.nextStepIndex ?: currentDomainState.currentStepIndex
            )
            inputIndex += requiredInputCount
            if (evalResult.isFinished) break
        }
        return currentDomainState
    }

    private fun replayDiv(initialDomainState: DivisionDomainState, confirmedInputs: List<String>): DivisionDomainState {
        var currentDomainState = initialDomainState
        var inputIndex = 0
        val steps = currentDomainState.phaseSequence.steps

        while (inputIndex < confirmedInputs.size && currentDomainState.currentStepIndex <= steps.lastIndex) {
            val currentStep = steps[currentDomainState.currentStepIndex]
            val requiredInputCount = currentStep.editableCells.size.coerceAtLeast(1)
            if (inputIndex + requiredInputCount > confirmedInputs.size) break
            val inputChunk = confirmedInputs.subList(inputIndex, inputIndex + requiredInputCount)
            val evalResult = divEval.evaluate(currentDomainState, inputChunk)
            if (!evalResult.isCorrect) break
            currentDomainState = currentDomainState.copy(
                inputs = currentDomainState.inputs + inputChunk,
                currentStepIndex = evalResult.nextStepIndex ?: currentDomainState.currentStepIndex
            )
            inputIndex += requiredInputCount
            if (evalResult.isFinished) break
        }
        return currentDomainState
    }
}