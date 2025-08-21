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
class ChallengeHostViewModel @Inject constructor(
    private val saved: SavedStateHandle,
    private val problemFactory: ProblemSessionFactory,
    private val domainFactory: DomainStateFactory,
    private val mulEval: MulPhaseEvaluator,
    private val divEval: DivisionPhaseEvaluator,
    private val feedback: FeedbackProvider
) : ViewModel() {

    companion object { private const val SNAP = "challenge_snapshot" }

    private val _ui = MutableStateFlow(ChallengeUi())
    val ui: StateFlow<ChallengeUi> = _ui.asStateFlow()

    val feedbackEvents: SharedFlow<FeedbackEvent> get() = feedback.events

    // 현재 도메인 (둘 중 하나만 활성)
    private var mul: MulDomainState? = null
    private var div: DivisionDomainState? = null

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
    private fun startProblem(p: Problem) {
        val cur = _ui.value.problem
        if (cur == p) return

        when (p.type) {
            OpType.Multiplication -> {
                mul = domainFactory.create(p) as MulDomainState
                div = null
                _ui.update {
                    it.copy(
                        problem = p, op = p.type,
                        currentInput = "", isCompleted = false, showStamp = false,
                        mulUi = mapMultiplicationUiState(mul!!, ""),
                        divUi = null
                    )
                }
            }
            OpType.Division -> {
                div = domainFactory.create(p) as DivisionDomainState
                mul = null
                _ui.update {
                    it.copy(
                        problem = p, op = p.type,
                        currentInput = "", isCompleted = false, showStamp = false,
                        mulUi = null,
                        divUi = mapDivisionUiState(div!!, "")
                    )
                }
            }
        }
        persist()
    }

    // ====== 입력 ======
    fun onDigit(d: Int) {
        val st = _ui.value; val p = st.problem ?: return
        val (need, newCur) = when (p.type) {
            OpType.Multiplication -> {
                val step = mul!!.phaseSequence.steps[mul!!.currentStepIndex]
                val need = step.editableCells.size.coerceAtLeast(1)
                need to (st.currentInput + d).takeLast(need)
            }
            OpType.Division -> {
                val step = div!!.phaseSequence.steps[div!!.currentStepIndex]
                val need = step.editableCells.size.coerceAtLeast(1)
                need to (st.currentInput + d).takeLast(need)
            }
        }
        _ui.update { it.copy(currentInput = newCur) }
        // 보드 UI 갱신
        buildBoardUiAndUpdate()
        persist()
    }

    fun onClear() {
        _ui.update { it.copy(currentInput = "") }
        buildBoardUiAndUpdate()
        persist()
    }

    fun onEnter() {
        val st = _ui.value; val p = st.problem ?: return
        val input = st.currentInput.ifEmpty { return }

        // 입력을 스텝 요구 길이에 맞춰 분할
        fun chunkFor(need: Int): List<String> =
            if (need > 1) input.padStart(need, '?').chunked(1) else listOf(input)

        when (p.type) {
            OpType.Multiplication -> {
                val step = mul!!.phaseSequence.steps[mul!!.currentStepIndex]
                val need = step.editableCells.size
                val chunk = chunkFor(need)
                if (chunk.size < need || chunk.any { it == "?" }) {
                    _ui.update { it.copy(currentInput = "") }
                    buildBoardUiAndUpdate(); persist()
                    return
                }
                val eval = mulEval.evaluate(mul!!, chunk)
                if (!eval.isCorrect) {
                    feedback.wrong(FeedbackMessages.randomWrong())
                    _ui.update { it.copy(wrong = it.wrong + 1, currentStreak = 0, currentInput = "") }
                    buildBoardUiAndUpdate(); persist()
                    return
                }
                feedback.correct(FeedbackMessages.randomCorrect())
                _ui.update {
                    it.copy(correct = it.correct + 1,
                        currentStreak = it.currentStreak + 1,
                        bestStreak = maxOf(it.bestStreak, it.currentStreak + 1))
                }
                mul = mul!!.copy(
                    inputs = mul!!.inputs + chunk,
                    currentStepIndex = eval.nextStepIndex ?: mul!!.currentStepIndex
                )
                if (eval.isFinished) {
                    feedback.phaseCompleted()
                    _ui.update { it.copy(
                        solved = it.solved + 1,
                        isCompleted = true,
                        showStamp = true,
                        solvedMul = it.solvedMul + 1
                    ) }
                }
                _ui.update { it.copy(currentInput = "") }
                buildBoardUiAndUpdate(); persist()
            }

            OpType.Division -> {
                val step = div!!.phaseSequence.steps[div!!.currentStepIndex]
                val need = step.editableCells.size
                val chunk = chunkFor(need)
                if (chunk.size < need || chunk.any { it == "?" }) {
                    _ui.update { it.copy(currentInput = "") }
                    buildBoardUiAndUpdate(); persist()
                    return
                }
                val eval = divEval.evaluate(div!!, chunk)
                if (!eval.isCorrect) {
                    feedback.wrong(FeedbackMessages.randomWrong())
                    _ui.update { it.copy(wrong = it.wrong + 1, currentStreak = 0, currentInput = "") }
                    buildBoardUiAndUpdate(); persist()
                    return
                }
                feedback.correct(FeedbackMessages.randomCorrect())
                _ui.update {
                    it.copy(correct = it.correct + 1,
                        currentStreak = it.currentStreak + 1,
                        bestStreak = maxOf(it.bestStreak, it.currentStreak + 1))
                }
                div = div!!.copy(
                    inputs = div!!.inputs + chunk,
                    currentStepIndex = eval.nextStepIndex ?: div!!.currentStepIndex
                )
                if (eval.isFinished) {
                    feedback.phaseCompleted()
                    _ui.update { it.copy(
                        solved = it.solved + 1,
                        isCompleted = true,
                        showStamp = true,
                        solvedDiv = it.solvedDiv + 1
                    ) }
                }
                _ui.update { it.copy(currentInput = "") }
                buildBoardUiAndUpdate(); persist()
            }
        }
    }

    fun onNextProblem() {
        _ui.update { it.copy(showStamp = false, isCompleted = false) }
        viewModelScope.launch { source?.requestNext() }
        persist()
    }

    // ====== 저장 ======
    private fun persist() {
        val st = _ui.value
        val (stepIdx, inputs) = when (st.op) {
            OpType.Multiplication -> mul?.currentStepIndex to (mul?.inputs ?: emptyList())
            OpType.Division       -> div?.currentStepIndex to (div?.inputs ?: emptyList())
            else -> 0 to emptyList()
        }
        saved[SNAP] = ChallengeSnapshot(
            current = st.problem,
            stepIndex = stepIdx ?: 0,
            confirmedInputs = inputs,
            currentInput = st.currentInput,
            solved = st.solved, correct = st.correct, wrong = st.wrong,
            currentStreak = st.currentStreak, bestStreak = st.bestStreak,
            solvedMul = st.solvedMul,
            solvedDiv = st.solvedDiv,
            showStamp = st.showStamp
        )
    }

    // ====== 복원 ======
    private fun tryRestore(): Boolean {
        val snap = saved.get<ChallengeSnapshot>(SNAP) ?: return false
        val p = snap.current ?: return false

        // 도메인 리플레이 복원
        when (p.type) {
            OpType.Multiplication -> {
                val base = domainFactory.create(p) as MulDomainState
                mul = replayMul(base, snap.confirmedInputs)
                div = null
                _ui.value = ChallengeUi(
                    problem = p, op = p.type,
                    mulUi = mapMultiplicationUiState(mul!!, snap.currentInput),
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
                val base = domainFactory.create(p) as DivisionDomainState
                div = replayDiv(base, snap.confirmedInputs)
                mul = null
                _ui.value = ChallengeUi(
                    problem = p, op = p.type,
                    mulUi = null,
                    divUi = mapDivisionUiState(div!!, snap.currentInput),
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
        val st = _ui.value
        val p = st.problem ?: return
        when (p.type) {
            OpType.Multiplication -> {
                // ✅ 네가 이미 쓰고 있는 변환기 재활용
                val ui = mapMultiplicationUiState(mul!!, st.currentInput)
                _ui.update { it.copy(mulUi = ui, divUi = null) }
            }
            OpType.Division -> {
                val ui = mapDivisionUiState(div!!, st.currentInput)
                _ui.update { it.copy(divUi = ui, mulUi = null) }
            }
        }
    }

    // ====== 리플레이: 저장된 확정 입력으로 도메인 재구성 ======
    private fun replayMul(base: MulDomainState, confirmed: List<String>): MulDomainState {
        var ds = base
        var cursor = 0
        val steps = ds.phaseSequence.steps

        while (cursor < confirmed.size && ds.currentStepIndex <= steps.lastIndex) {
            val step = steps[ds.currentStepIndex]
            val need = step.editableCells.size.coerceAtLeast(1)
            if (cursor + need > confirmed.size) break
            val chunk = confirmed.subList(cursor, cursor + need)
            val eval = mulEval.evaluate(ds, chunk)
            if (!eval.isCorrect) break
            ds = ds.copy(
                inputs = ds.inputs + chunk,
                currentStepIndex = eval.nextStepIndex ?: ds.currentStepIndex
            )
            cursor += need
            if (eval.isFinished) break
        }
        return ds
    }

    private fun replayDiv(base: DivisionDomainState, confirmed: List<String>): DivisionDomainState {
        var ds = base
        var cursor = 0
        val steps = ds.phaseSequence.steps

        while (cursor < confirmed.size && ds.currentStepIndex <= steps.lastIndex) {
            val step = steps[ds.currentStepIndex]
            val need = step.editableCells.size.coerceAtLeast(1)
            if (cursor + need > confirmed.size) break
            val chunk = confirmed.subList(cursor, cursor + need)
            val eval = divEval.evaluate(ds, chunk)
            if (!eval.isCorrect) break
            ds = ds.copy(
                inputs = ds.inputs + chunk,
                currentStepIndex = eval.nextStepIndex ?: ds.currentStepIndex
            )
            cursor += need
            if (eval.isFinished) break
        }
        return ds
    }
}