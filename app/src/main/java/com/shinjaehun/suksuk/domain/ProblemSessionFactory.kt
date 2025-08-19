package com.shinjaehun.suksuk.domain

import com.shinjaehun.suksuk.domain.pattern.OperationPattern

interface ProblemSessionFactory {
    fun openSession(
        op: OpType,
        mode: SessionMode,
        pattern: OperationPattern? = null,
        overrideOperands: Pair<Int, Int>? = null
    ): ProblemSource
}