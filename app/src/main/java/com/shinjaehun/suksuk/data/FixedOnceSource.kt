package com.shinjaehun.suksuk.data

import com.shinjaehun.suksuk.domain.OpType
import com.shinjaehun.suksuk.domain.Problem
import com.shinjaehun.suksuk.domain.ProblemSource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FixedOnceSource(
    private val op: OpType,
    private val a: Int,
    private val b: Int
) : ProblemSource {
    private val _problems = MutableSharedFlow<Problem>(replay = 1)
    override val problems = _problems.asSharedFlow()

    private var emitted = false

    override suspend fun requestNext() {
        if (!emitted) {
            _problems.emit(Problem(op, a, b))
            emitted = true
        } else {
            // 한 번만 공급. 반복 요청 시 그대로 유지(필요하면 예외/재공급로직 추가)
            _problems.emit(Problem(op, a, b))
        }
    }

    override fun stop() { /* no-op */ }
}