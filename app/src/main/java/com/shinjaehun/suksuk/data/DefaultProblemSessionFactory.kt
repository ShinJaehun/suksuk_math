package com.shinjaehun.suksuk.data

import com.shinjaehun.suksuk.domain.OpType
import com.shinjaehun.suksuk.domain.ProblemSessionFactory
import com.shinjaehun.suksuk.domain.ProblemSource
import com.shinjaehun.suksuk.domain.SessionMode
import com.shinjaehun.suksuk.domain.pattern.OperationPattern
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultProblemSessionFactory @Inject constructor() : ProblemSessionFactory {
    override fun openSession(
        op: OpType,
        mode: SessionMode,
        pattern: OperationPattern?,
        overrideOperands: Pair<Int, Int>?
    ): ProblemSource {
        overrideOperands?.let { (a, b) -> return FixedOnceSource(op, a, b) }

        return when (mode) {
            SessionMode.Practice -> {
                val pat = requireNotNull(pattern) { "Practice mode requires pattern" }
                LocalRandomSource(op = op, pattern = pat)
            }
            SessionMode.Challenge -> ChallengeSource()
            SessionMode.DebugFixed -> FixedOnceSource(op, 81, 12)
        }
    }
}