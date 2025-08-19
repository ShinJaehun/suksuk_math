package com.shinjaehun.suksuk.domain.generator

import com.shinjaehun.suksuk.domain.OpType
import com.shinjaehun.suksuk.domain.Problem
import kotlin.random.Random

/**
 * 문제 생성 규칙(숫자 범위/제약)을 도메인에 모아둔 유틸.
 * - data(Source)에서는 이 규칙만 호출하고, 규칙 자체는 domain에서 관리한다.
 */
object ProblemRules {

    // ===== Multiplication =====

    /** 2×2: 10..99 × 10..99 */
    fun randomTwoByTwoMul(rng: Random): Problem {
        val a = rng.nextInt(10, 100)
        val b = rng.nextInt(10, 100)
        return Problem(OpType.Multiplication, a, b)
    }

    /** 3×2: 100..999 × 10..99 */
    fun randomThreeByTwoMul(rng: Random): Problem {
        val a = rng.nextInt(100, 1000)
        val b = rng.nextInt(10, 100)
        return Problem(OpType.Multiplication, a, b)
    }

    // ===== Division =====

    /** 2÷1: 10..99 ÷ 2..9  (dividend > divisor 보장) */
    fun randomTwoByOneDiv(rng: Random): Problem {
        val divisor  = rng.nextInt(2, 10)             // 2..9
        val from     = maxOf(10, divisor + 1)           // ≥ 10
        val dividend = rng.nextInt(from, 100)         // from..99
        return Problem(OpType.Division, dividend, divisor)
    }

    /** 2÷2: 11..99 ÷ 10..98  (dividend > divisor 보장) */
    fun randomTwoByTwoDiv(rng: Random): Problem {
        val divisor  = rng.nextInt(10, 99)            // 10..98
        val from     = maxOf(11, divisor + 1)           // ≥ 11
        val dividend = rng.nextInt(from, 100)         // from..99
        return Problem(OpType.Division, dividend, divisor)
    }

    /** 3÷2: 101..999 ÷ 10..99 (dividend > divisor 보장) */
    fun randomThreeByTwoDiv(rng: Random): Problem {
        val divisor  = rng.nextInt(10, 100)           // 10..99
        val from     = maxOf(101, divisor + 1)          // ≥ 101
        val dividend = rng.nextInt(from, 1000)        // from..999
        return Problem(OpType.Division, dividend, divisor)
    }
}