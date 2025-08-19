package com.shinjaehun.suksuk.data

import com.shinjaehun.suksuk.domain.OpType
import com.shinjaehun.suksuk.domain.Problem
import com.shinjaehun.suksuk.domain.generator.ProblemRules
import com.shinjaehun.suksuk.domain.ProblemSource
import com.shinjaehun.suksuk.domain.pattern.DivisionPattern
import com.shinjaehun.suksuk.domain.pattern.MulPattern
import com.shinjaehun.suksuk.domain.pattern.OperationPattern
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.random.Random

class LocalRandomSource(
    private val op: OpType,
    private val pattern: OperationPattern,
    private val rng: Random = Random(System.currentTimeMillis())
) : ProblemSource {

    // Replay 1: 새로운 구독자가 최신 문제를 즉시 받도록 보장
    // - 화면 회전/재생성 시 문제 상태 복원에 유리
    // - 문제 생성은 requestNext()로 트리거되므로 버퍼는 크지 않아도 됨
    private val _problems = MutableSharedFlow<Problem>(
        replay = 1,
        extraBufferCapacity = 0
    )
    override val problems = _problems.asSharedFlow()

    override suspend fun requestNext() {

        val problem: Problem = when (op) {
            OpType.Multiplication -> when (pattern) {
                MulPattern.TwoByTwo -> ProblemRules.randomTwoByTwoMul(rng)
                MulPattern.ThreeByTwo -> ProblemRules.randomThreeByTwoMul(rng)
                else -> error("unsupported multiplication pattern: $pattern")
            }
            OpType.Division -> when (pattern) {
                DivisionPattern.TwoByOne -> ProblemRules.randomTwoByOneDiv(rng)
                DivisionPattern.TwoByTwo -> ProblemRules.randomTwoByTwoDiv(rng)
                DivisionPattern.ThreeByTwo -> ProblemRules.randomThreeByTwoDiv(rng)
                else -> error("unsupported division pattern: $pattern")
            }
        }
        _problems.emit(problem)
    }

    override fun stop() {
    }
}