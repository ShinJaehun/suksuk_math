package com.shinjaehun.suksuk.domain.division

import com.shinjaehun.suksuk.domain.division.model.CellName

class PhaseEvaluatorV2 {
    fun isCorrect(
        phase: DivisionPhaseV2,
        cell: CellName,
        input: String,
        dividend: Int,
        divisor: Int,
        stepIndex: Int,
        previousInputs: List<String>
    ): Boolean {
        val inputValue = input.toIntOrNull() ?: return false

        // 예: 68 ÷ 34
        val dividendTens = dividend / 10      // 6
        val dividendOnes = dividend % 10      // 8
        val divisorTens = divisor / 10        // 3
        val divisorOnes = divisor % 10        // 4

        val quotient = dividend / divisor     // 2

        return when (phase) {
            DivisionPhaseV2.InputQuotient -> {
                // quotient 입력
                inputValue == quotient
            }
            DivisionPhaseV2.InputMultiply -> {
                // 곱셈 단계에서는 stepIndex로 어떤 곱셈인지 판단 필요
                when (cell) {
                    CellName.CarryDivisorTens -> {
                        // (2 × 4) = 8 → Carry는 0
                        (quotient * divisorOnes) / 10 == inputValue
                    }
                    CellName.Multiply1Ones -> {
                        // (2 × 4) = 8 → Ones는 8
                        (quotient * divisorOnes) % 10 == inputValue
                    }
                    CellName.Multiply1Tens -> {
                        // (2 × 3) = 6
                        (quotient * divisorTens) == inputValue
                    }
                    else -> false
                }
            }
            DivisionPhaseV2.InputBorrow -> {
                // 받아내림 입력: 보통 십의 자리에서 1 감소
                inputValue == (dividendTens - 1)
            }
            DivisionPhaseV2.InputSubtract -> {
                // 뺄셈 단계에서, Ones 자리(받아내림 포함)
                // ex) 8(받아내림 10) - 8(곱셈결과) = 2
                // 이전 입력: Borrow(5) → borrowed10DividendOnes("10")
                // 이전 입력: Multiply1Ones("8")
//                val borrowed10 = 10 // 보통 fixed
//                val multiply1Ones = previousInputs.getOrNull(stepIndex - 1)?.toIntOrNull() ?: 0
//                inputValue == (borrowed10 - multiply1Ones)
//                val borrowed10 = 10
//                val multiply1Ones = previousInputs.getOrNull(stepIndex - 1)?.toIntOrNull() ?: 0
//                val expected = borrowed10 - multiply1Ones
//                println("🧮 Subtract 검증: borrowed10=$borrowed10, multiply1Ones=$multiply1Ones, expected=$expected, 입력=$inputValue")
//                inputValue == expected

                inputValue == dividend - (quotient * divisor)
            }
            DivisionPhaseV2.InputBringDown -> {
                // 나중 확장(자리 내려쓰기 등)
                false
            }
            DivisionPhaseV2.Complete -> true
        }
    }
}