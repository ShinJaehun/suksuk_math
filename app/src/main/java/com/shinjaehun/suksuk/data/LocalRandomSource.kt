package com.shinjaehun.suksuk.data

import android.util.Log
import com.shinjaehun.suksuk.domain.OpType
import com.shinjaehun.suksuk.domain.Problem
import com.shinjaehun.suksuk.domain.ProblemSource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.math.max
import kotlin.random.Random

class LocalRandomSource(
    private val op: OpType,
    private val pattern: String
) : ProblemSource {

    private val _problems = MutableSharedFlow<Problem>(
        replay = 1,
        extraBufferCapacity = 0
    )
    override val problems = _problems.asSharedFlow()

    private val rng = Random(System.currentTimeMillis())

//    override suspend fun requestNext() {
//        val p = when (op) {
//            OpType.Division -> {
//                // 예시 범위: 2~3자리 ÷ 1~2자리
//                val dividend = rng.nextInt(50, 999)
//                val divisor  = rng.nextInt(2, 50)
//                Problem(OpType.Division, dividend, divisor)
//            }
//            OpType.Multiplication -> {
//                // 예시 범위: 2×2
//                val multiplicand = rng.nextInt(10, 99)
//                val multiplier   = rng.nextInt(10, 99)
//                Problem(OpType.Multiplication, multiplicand, multiplier)
//            }
//        }
//        _problems.emit(p)
//    }

    override suspend fun requestNext() {
        Log.d("SRC", "requestNext() op=$op, pattern=$pattern")

        val problem: Problem = when (op) {
            OpType.Multiplication -> {
                when (pattern) {
                    "TwoByTwo" -> {
                        // 10..99 × 10..99
                        val a = rng.nextInt(10, 100)
                        val b = rng.nextInt(10, 100)
                        Problem(OpType.Multiplication, a, b)
                    }
                    "ThreeByTwo" -> {
                        // 100..999 × 10..99
                        val a = rng.nextInt(100, 1000)
                        val b = rng.nextInt(10, 100)
                        Problem(OpType.Multiplication, a, b)
                    }
                    else -> error("unsupported multiplication pattern: $pattern")
                }
            }

            OpType.Division -> {
                when (pattern) {
                    "TwoByOne" -> {
                        // 10..99 ÷ 2..9  (dividend > divisor 보장)
                        val divisor = rng.nextInt(2, 10)                 // 2..9
                        val from    = max(10, divisor + 1)               // ≥10
                        val dividend= rng.nextInt(from, 100)             // from..99
                        Problem(OpType.Division, dividend, divisor)
                    }
                    "TwoByTwo" -> {
                        // 10..99 ÷ 10..98 (dividend > divisor 보장, 경계 안전)
                        val divisor = rng.nextInt(10, 99)                // 10..98  ← 상한 -1
                        val from    = max(11, divisor + 1)               // 최소 11
                        val dividend= rng.nextInt(from, 100)             // from..99
                        Problem(OpType.Division, dividend, divisor)
                    }
                    "ThreeByTwo" -> {
                        // 100..999 ÷ 10..99 (dividend > divisor 보장)
                        val divisor = rng.nextInt(10, 100)               // 10..99
                        val from    = max(101, divisor + 1)              // 최소 101
                        val dividend= rng.nextInt(from, 1000)            // from..999
                        Log.d("SRC", "DIV 3x2 -> $dividend ÷ $divisor")
                        Problem(OpType.Division, dividend, divisor)
                    }
                    else -> error("unsupported division pattern: $pattern")
                }
            }
        }
        Log.d("SRC", "emit problem: $problem")
        _problems.emit(problem)
    }

    override fun stop() {
        Log.d("SRC", "stop() called")

    }
}