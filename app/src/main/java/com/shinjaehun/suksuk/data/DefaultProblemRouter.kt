package com.shinjaehun.suksuk.data

import android.util.Log
import com.shinjaehun.suksuk.domain.OpType
import com.shinjaehun.suksuk.domain.ProblemSource
import com.shinjaehun.suksuk.domain.SessionMode

class DefaultProblemRouter {
    fun openSession(
        op: OpType,
        mode: SessionMode,
        pattern: String? = null,                // Practice/Division에는 반드시 필요
        overrideOperands: Pair<Int, Int>? = null
    ): ProblemSource {

        overrideOperands?.let { (a, b) ->
            return FixedOnceSource(op, a, b)
        }

        return when (mode) {
            SessionMode.Practice -> {
                require(pattern != null) { "Practice mode requires pattern (e.g., TwoByTwo)" }
                LocalRandomSource(op = op, pattern = pattern) // 패턴 고정
            }
            SessionMode.Challenge -> {
                ChallengeSource()        // 패턴까지 랜덤
            }
//            SessionMode.Class -> LocalRandomSource(op, pattern ?: "TwoByTwo") // 임시. 나중에 WebSocketSource
            SessionMode.DebugFixed -> {
                FixedOnceSource(op, 81, 12)
            }
        }
    }
}