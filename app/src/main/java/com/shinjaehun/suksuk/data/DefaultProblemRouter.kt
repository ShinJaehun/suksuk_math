package com.shinjaehun.suksuk.data

import com.shinjaehun.suksuk.domain.OpType
import com.shinjaehun.suksuk.domain.ProblemSource
import com.shinjaehun.suksuk.domain.SessionMode
import com.shinjaehun.suksuk.domain.pattern.OperationPattern
import kotlin.random.Random

class DefaultProblemRouter(
    private val randomProvider: () -> Random = { Random(System.currentTimeMillis()) }
) {
    fun openSession(
        op: OpType,
        mode: SessionMode,
        pattern: OperationPattern? = null, // Practice 모드에서 필요
        overrideOperands: Pair<Int, Int>? = null
    ): ProblemSource {

        overrideOperands?.let { (a, b) ->
            return FixedOnceSource(op, a, b)
        }

        return when (mode) {
            SessionMode.Practice -> {
                val pat = requireNotNull(pattern) { "Practice mode requires pattern (e.g., TwoByTwo)" }
                LocalRandomSource(op = op, pattern = pat, rng = randomProvider()) // 패턴 고정
            }
            SessionMode.Challenge -> {
                ChallengeSource(rng = randomProvider())        // 패턴까지 랜덤
            }
//            SessionMode.Class -> LocalRandomSource(op, pattern ?: "TwoByTwo") // 임시. 나중에 WebSocketSource
            SessionMode.DebugFixed -> {
                FixedOnceSource(op, 81, 12)
            }
        }
    }
}