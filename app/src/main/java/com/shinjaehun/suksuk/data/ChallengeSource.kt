package com.shinjaehun.suksuk.data

import com.shinjaehun.suksuk.domain.Problem
import com.shinjaehun.suksuk.domain.generator.ProblemRules
import com.shinjaehun.suksuk.domain.ProblemSource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.random.Random

class ChallengeSource(
    private val rng: Random = Random(System.currentTimeMillis())
) : ProblemSource {
    private val _problems = MutableSharedFlow<Problem>(replay = 1)
    override val problems = _problems.asSharedFlow()

    override suspend fun requestNext() {
        // 패턴까지 랜덤하게 섞어 제공
        val pick = rng.nextInt(5)
        val p = when (pick) {
            0 -> ProblemRules.randomTwoByTwoMul(rng)   // 2×2 곱셈
            1 -> ProblemRules.randomThreeByTwoMul(rng) // 3×2 곱셈
            2 -> ProblemRules.randomTwoByOneDiv(rng)   // 2÷1 나눗셈
            3 -> ProblemRules.randomTwoByTwoDiv(rng)   // 2÷2 나눗셈
            else -> ProblemRules.randomThreeByTwoDiv(rng) // 3÷2 나눗셈
        }
        _problems.emit(p)
    }

    override fun stop() {}
}