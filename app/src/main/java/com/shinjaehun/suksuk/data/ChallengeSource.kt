package com.shinjaehun.suksuk.data

import com.shinjaehun.suksuk.domain.OpType
import com.shinjaehun.suksuk.domain.Problem
import com.shinjaehun.suksuk.domain.ProblemSource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.random.Random

class ChallengeSource : ProblemSource {
    private val _problems = MutableSharedFlow<Problem>(replay = 1)
    override val problems = _problems.asSharedFlow()
    private val rng = Random(System.currentTimeMillis())

    override suspend fun requestNext() {
        val pick = rng.nextInt(5)
        val p = when (pick) {
            0 -> Problem(OpType.Multiplication, rng.nextInt(10, 99), rng.nextInt(10, 99)) // 2x2
            1 -> Problem(OpType.Multiplication, rng.nextInt(100, 999), rng.nextInt(10, 99)) // 3x2
            2 -> Problem(OpType.Division, rng.nextInt(10, 99), rng.nextInt(2, 9)) // 2÷1
            3 -> Problem(OpType.Division, rng.nextInt(10, 99), rng.nextInt(10, 99)) // 2÷2
            else -> Problem(OpType.Division, rng.nextInt(100, 999), rng.nextInt(10, 99)) // 3÷2
        }
        _problems.emit(p)
    }

    override fun stop() {}
}