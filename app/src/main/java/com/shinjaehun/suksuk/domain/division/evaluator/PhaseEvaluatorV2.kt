package com.shinjaehun.suksuk.domain.division.evaluator

import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
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
        val expected = expectedValueForCell(
            phase, cell, dividend, divisor, stepIndex, previousInputs
        )
        println("🧪 $cell: expected=$expected, input=$input")
        return expected != null && expected == inputValue
    }

    private fun expectedValueForCell(
        phase: DivisionPhaseV2,
        cell: CellName,
        dividend: Int,
        divisor: Int,
        stepIndex: Int,
        previousInputs: List<String>
    ): Int? {
        val dividendHundreds = dividend / 100
        val dividendTens = dividend / 10 % 10
        val dividendOnes = dividend % 10

        val divisorTens = divisor / 10
        val divisorOnes = divisor % 10

        val quotient = dividend / divisor

        val quotientTens = quotient / 10
        val quotientOnes = quotient % 10

        return when (phase) {
            DivisionPhaseV2.InputQuotient -> when (cell) {
                CellName.QuotientTens -> quotientTens
                CellName.QuotientOnes -> quotientOnes
                else -> null
            }
//            DivisionPhaseV2.InputMultiply1 -> when (cell) {
//                CellName.CarryDivisorTens -> when {
//                    dividend < 100 -> (quotient * divisorOnes) / 10 // 2by1, 2by2
//                    dividend >= 100 && phase == DivisionPhaseV2.InputMultiply1 -> {
//                        when (stepIndex) {
//                            0 -> (quotientTens * divisorOnes) / 10  // 3by2, 곱셈1
//                            1 -> (quotientOnes * divisorOnes) / 10  // 3by2, 곱셈2
//                            else -> null
//                        }
//                    }
//                    else -> null
//                }
//
//                CellName.Multiply1Hundreds -> if (quotient >= 10) {
//                    quotientTens * divisorTens
//                } else {
//                    quotientOnes * divisor / 10
//                }
//
////                CellName.Multiply1Tens    -> if(quotient >= 10 ){
////                    if (dividend >= 100) {
////                        quotientTens * divisorOnes
////                    } else {
////                        quotientTens * divisor
////                    }
////                } else {
////                    quotient * divisor / 10
////                }
//
//                CellName.Multiply1Tens -> {
//                    val mul = when {
//                        quotient < 10 -> quotient * divisor / 10
//                        dividend < 100 -> quotientTens * divisor
//                        else -> quotientTens * divisorOnes
//                    }
//                    if (hasCarryInMultiply1Tens(dividend, divisor, quotient)) {
//                        // carry 발생시 특수 처리(예: UI 효과, 로그 등)
//                    }
//                    mul
//                }
//
//                CellName.Multiply1Ones    -> (quotient * divisorOnes) % 10
//
//                CellName.Multiply2Tens    -> if (dividend >= 100) {
//                    quotientOnes * divisorTens
//                } else {
//                    (divisor * quotientOnes) / 10
//                }
//
//                CellName.Multiply2Ones    -> (divisor * quotientOnes) % 10
//                else -> null
//            }

            DivisionPhaseV2.InputMultiply1 -> when (cell) {
                CellName.CarryDivisorTensMul1 -> when {
                    dividend < 100 -> (quotient * divisorOnes) / 10      // 2by1, 2by2: 몫 × 제수일의자리 / 10
                    else -> (quotientTens * divisorOnes) / 10            // 3by2: 몫십의자리 × 제수일의자리 / 10
                }
                CellName.Multiply1Hundreds -> if (dividend >= 100) {
                    if (divisorOnes * quotientTens > 10){
                        quotientTens * divisorTens + (divisorOnes * quotientTens / 10)
                    } else {
                        quotientTens * divisorTens                            // 3by2: 몫십의자리 × 제수십의자리
                    }

                } else null

                CellName.Multiply1Tens -> when {
                    dividend >= 100 -> quotientTens * divisorOnes % 10
                    dividend < 100 && quotient < 10 -> quotient * divisor / 10      // 2by1: 몫 × 제수 / 10
//                    dividend < 100 && quotient >= 10 -> quotientTens * divisor      // 2by2: 몫십의자리 × 제수
                    else -> quotientTens * divisorOnes                              // 3by2: 몫십의자리 × 제수일의자리
                }
                CellName.Multiply1Ones -> when {
                    dividend < 100 && quotient < 10 -> (quotient * divisor) % 10    // 2by1: 몫 × 제수 % 10
                    dividend < 100 && quotient >= 10 -> (quotientTens * divisor) % 10   // 2by2
                    else -> (quotientTens * divisorOnes) % 10                       // 3by2
                }
                else -> null
            }

//            if (dividend >= 100) {
//                    quotientOnes * divisorTens
//                } else {
//                    (divisor * quotientOnes) / 10
//                }

            DivisionPhaseV2.InputMultiply2 -> when (cell) {
                CellName.CarryDivisorTensMul2 -> (quotientOnes * divisorOnes) / 10   // 3by2 두 번째 곱셈 carry
                CellName.Multiply2Tens -> when {
                    dividend >= 100 -> {
//                        println("$quotientOnes * $divisorTens + ($divisorOnes * $quotientOnes")
                        if(divisorOnes * quotientOnes > 10 ){
                            quotientOnes * divisorTens + (divisorOnes * quotientOnes / 10)
                        } else {
                            quotientOnes * divisorTens
                        }
                    }
                    else -> (divisor * quotientOnes) / 10                        // 3by2 (몫일의자리 × 제수십의자리)
                }
                CellName.Multiply2Ones -> {
                    println("$divisor $quotientOnes    ${(divisor * quotientOnes) % 10}")
                    (divisor * quotientOnes) % 10
                }
                else -> null
            }

            DivisionPhaseV2.InputBringDown -> when(cell) {
                CellName.Subtract1Ones -> dividendOnes
                else -> null
            }
            DivisionPhaseV2.InputBorrow -> when (cell) {
                CellName.BorrowDividendHundreds -> dividendHundreds - 1
                CellName.BorrowDividendTens -> dividendTens - 1
                CellName.BorrowSubtract1Tens -> when {
                    dividend >= 100 -> {
                        val firstSub = (dividendHundreds * 10 + dividendTens) - (quotientTens * divisor)
                        val bringDown = dividendOnes
                        val sub2Target = firstSub * 10 + bringDown

// sub2TensBeforeBorrow는 sub2Target의 십의 자리
                        val sub2TensBeforeBorrow = sub2Target / 10
                        sub2TensBeforeBorrow - 1
                    }

                    else -> {
                        dividendTens - (divisor * quotientTens) - 1
                    }
                }
                else -> null
            }

//            DivisionPhaseV2.InputSubtract1 -> when (cell) {
//                CellName.Subtract1Hundreds -> dividendHundreds - (quotientTens * divisor) / 10
//                CellName.Subtract1Tens -> if(quotient >= 10 ) {
//                    if(dividendTens < quotientTens * divisor) {
//                        10 - dividendTens - quotientTens * divisor
//                    } else {
//                        dividendTens - quotientTens * divisor
//                    }
//                } else {
//                    (dividend - (quotient * divisor)) / 10
//                }
////                CellName.Subtract1Ones -> (dividend - (quotient * divisor)) % 10
//                CellName.Subtract1Ones -> {
//                    (dividend - (quotient * divisor)) % 10
//                }
//                CellName.Subtract2Tens -> (dividend - (quotient * divisor)) / 10
//                CellName.Subtract2Ones -> (dividend - (quotient * divisor)) % 10
//                else -> null
//            }

            DivisionPhaseV2.InputSubtract1 -> when (cell) {
                CellName.Subtract1Hundreds -> if (dividend >= 100) {
                    dividendHundreds
                } else null

                CellName.Subtract1Tens -> when {
                    dividend >= 100 -> {
                        // 3by2: 세 자리 뺄셈 십의자리, 받아내림 포함
                        val left = dividendHundreds * 10 + dividendTens
                        val right = quotientTens * divisor
                        val tens = left - right
                        tens // 음수면 실제로는 몫 계산이 잘못된 케이스니, 보정 로직은 상황에 따라
                    }
                    dividend >= 10 && quotient >= 10 -> {
                        // 2by2: 두 자리 피제수/몫
                        val tens = dividendTens - (quotientTens * divisor)
                        if (tens < 0) tens + 10 else tens
                    }
                    else -> {
                        ((dividend - (quotient * divisor)) / 10)
                    }
                }

                CellName.Subtract1Ones -> (dividend - (quotient * divisor)) % 10
                else -> null
            }


            // InputSubtract2: 두 번째 뺄셈 (2by1 일부, 3by2)
            DivisionPhaseV2.InputSubtract2 -> when (cell) {
//                CellName.Subtract2Tens -> when {
//                    dividend >= 100 -> {
//                        // [핵심] 3by2의 2차 뺄셈: (1차 뺄셈 결과 * 10 + bring down) - (몫 일의자리 × 제수)
//                        val firstSub = ((dividend % 100) - (quotientTens * divisor)) // 1차 뺄셈 결과 (예: 604 → 04, 즉 4)
//                        val bringDown = dividend % 10
//                        val secondSub = (firstSub * 10 + bringDown) - (quotientOnes * divisor)
//                        secondSub / 10
//                    }
//                    else -> {
//                        // 2by1
//                        (dividend - (quotient * divisor)) / 10
//                    }
//                }
//                CellName.Subtract2Ones -> when {
//                    dividend >= 100 -> {
//                        val firstSub = ((dividend % 100) - (quotientTens * divisor))
//                        val bringDown = dividend % 10
//                        val secondSub = (firstSub * 10 + bringDown) - (quotientOnes * divisor)
//                        secondSub % 10
//                    }
//
//                    else -> {
//                        (dividend - (quotient * divisor)) % 10
//                    }
//                }
                CellName.Subtract2Tens -> (dividend - (quotient * divisor)) / 10
                CellName.Subtract2Ones -> (dividend - (quotient * divisor)) % 10
                else -> null
            }

            DivisionPhaseV2.Complete -> null // 입력 없음
        }
    }
}
