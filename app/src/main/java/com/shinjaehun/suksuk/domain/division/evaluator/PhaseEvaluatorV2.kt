package com.shinjaehun.suksuk.domain.division.evaluator

import com.shinjaehun.suksuk.domain.division.DivisionStateInfo
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import com.shinjaehun.suksuk.domain.division.model.CellName

class PhaseEvaluatorV2 {

    fun isCorrect(
        phase: DivisionPhaseV2,
        cell: CellName,
        input: String,
        info: DivisionStateInfo,
        stepIndex: Int,
        previousInputs: List<String>
    ): Boolean {
        val inputValue = input.toIntOrNull() ?: return false
        val expected = expectedValueForCell(
            phase, cell, info, stepIndex, previousInputs
        )
        println("🧪 $cell: expected=$expected, input=$input")
        return expected != null && expected == inputValue
    }

    private fun expectedValueForCell(
        phase: DivisionPhaseV2,
        cell: CellName,
        i: DivisionStateInfo,
        stepIndex: Int,
        previousInputs: List<String>
    ): Int? {

        return when (phase) {
            DivisionPhaseV2.InputQuotient -> when (cell) {
                CellName.QuotientTens -> i.quotientTens
                CellName.QuotientOnes -> i.quotientOnes
                else -> null
            }

            DivisionPhaseV2.InputMultiply1 -> when (cell) {
                CellName.CarryDivisorTensMul1 ->
                    if (i.dividend >= 100) (i.quotientTens * i.divisorOnes) / 10
                    else (i.quotient * i.divisorOnes) / 10

                // multiply1quotientTens 이용하면 더 간단히 정리할 수 있지 않을까?
                CellName.Multiply1Hundreds ->
                    if (i.dividend >= 100) {
                        if (i.needsCarryInMultiply1)
                            i.quotientTens * i.divisorTens + (i.quotientTens * i.divisorOnes / 10)
                        else
                            i.quotientTens * i.divisorTens
                    } else null

                CellName.Multiply1Tens -> when {
                    i.dividend >= 100 -> (i.quotientTens * i.divisorOnes) % 10           // 3by2
                    !i.needsTensQuotient -> (i.quotient * i.divisor) / 10                // 2by1
                    else -> i.quotientTens * i.divisorOnes                                // 2by2 표시 규칙 유지
                }

                CellName.Multiply1Ones -> when {
                    i.dividend >= 100 -> (i.quotientTens * i.divisorOnes) % 10           // 3by2
                    i.needsTensQuotient -> (i.quotientTens * i.divisor) % 10             // 2by2
                    else -> (i.quotient * i.divisor) % 10                                 // 2by1
                }

                else -> null
            }

            DivisionPhaseV2.InputBringDown -> when (cell) {
                CellName.Subtract1Ones -> i.bringDownInSubtract1
                else -> null
            }

            DivisionPhaseV2.InputBorrow -> when (cell) {
                CellName.BorrowDividendHundreds ->
                    if (i.dividend >= 100) i.dividendHundreds - 1 else null
                CellName.BorrowDividendTens -> i.dividendTens - 1
                CellName.BorrowSubtract1Tens -> {
                    if (i.needsBorrowFromSubtract1TensInSubtract2 &&
                        i.needsBorrowFromSubtract1HundredsInSubtract2) {
                        9
                    } else {
                        val sub2TensBeforeBorrow = (i.subtract1Result / 10) % 10
                        sub2TensBeforeBorrow - 1
                    }
                }
                CellName.BorrowSubtract1Hundreds -> (i.subtract1Result / 100) - 1
                else -> null
            }

            DivisionPhaseV2.InputSubtract1 -> when (cell) {
                CellName.Subtract1Hundreds ->
//                    if (i.dividend >= 100) i.dividendHundreds - (i.quotientTens * i.divisor) / 10
                    if(i.needsTensQuotient){
                        i.subtract1Result / 100
                    } else null
                CellName.Subtract1Tens -> when {
                    i.dividend >= 100 -> {
                        val tens = i.dividendHundreds * 10 + i.dividendTens - i.multiplyQuotientTens
                        tens % 10
                    }
                    i.dividend >= 10 && i.quotient >= 10 -> {
                        val tens = i.dividendTens - (i.quotientTens * i.divisor)
                        if (tens < 0) tens + 10 else tens
                    }
                    else -> (i.dividend - (i.quotient * i.divisor)) / 10
                }

                CellName.Subtract1Ones -> (i.dividend - (i.quotient * i.divisor)) % 10
                else -> null
            }

            DivisionPhaseV2.InputMultiply2 -> when (cell) {
                CellName.CarryDivisorTensMul2 -> (i.quotientOnes * i.divisorOnes) / 10

                CellName.Multiply2Hundreds ->
                    if(i.dividend >= 100) i.multiplyQuotientOnes / 100
                    else null

//                CellName.Multiply2Tens -> when {
//                    i.dividend >= 100 -> {
//                        if (i.needsCarryInMultiply2)
//                            i.quotientOnes * i.divisorTens + (i.quotientOnes * i.divisorOnes / 10)
//                        else
//                            i.quotientOnes * i.divisorTens
//                        when {
//                            i.needs3DigitsInMultiply2 -> 0
//                            i.needsCarryInMultiply2 -> i.quotientOnes * i.divisorTens + (i.quotientOnes * i.divisorOnes / 10)
//                            else -> i.quotientOnes * i.divisorTens
//                        }
//                    }
//                    else -> i.multiplyQuotientOnes / 10
//                }
                CellName.Multiply2Tens -> i.multiplyQuotientOnes / 10 % 10

                CellName.Multiply2Ones -> i.multiplyQuotientOnes % 10
                else -> null
            }

            DivisionPhaseV2.InputSubtract2 -> when (cell) {
                CellName.Subtract2Tens -> i.remainder / 10
                CellName.Subtract2Ones -> i.remainder % 10
                else -> null
            }

            DivisionPhaseV2.Complete -> null
        }
    }

//    private fun expectedValueForCell(
//        phase: DivisionPhaseV2,
//        cell: CellName,
//        dividend: Int,
//        divisor: Int,
//        stepIndex: Int,
//        previousInputs: List<String>
//    ): Int? {
//        val quotient = dividend / divisor
//        val quotientTens = quotient / 10
//        val quotientOnes = quotient % 10
//
//        val dividendHundreds = dividend / 100
//        val dividendTens = dividend / 10 % 10
//        val dividendOnes = dividend % 10
//
//        val divisorTens = divisor / 10
//        val divisorOnes = divisor % 10
//
//        val needsTensQuotient = quotient >= 10
//
//        val multiplyQuotientTens = divisor * quotientTens
//        val multiplyQuotientOnes = divisor * quotientOnes
//
//        val needsBorrowFromDividendHundredsInSubtract1 =
//            (dividend >= 100) && (dividendTens < (multiplyQuotientTens % 10))
//
//        val bringDownInSubtract1 = dividend % 10
//        val subtract1TensOnly = (dividend / 10) - (quotientTens * divisor)
//
//        val subtract1Result = subtract1TensOnly * 10 + bringDownInSubtract1
//
//        val needsCarryInMultiply1 = (quotientTens * divisorOnes) >= 10
//        val needsCarryInMultiply2 = (quotientOnes * divisorOnes) >= 10
//
//        val needsBorrowFromSubtract1TensInSubtract2 =
//            (quotientOnes != 0) && ((subtract1Result % 10) < (multiplyQuotientOnes % 10))
//
//        val needsSkipMultiply2AndSubtract2 = (quotientOnes == 0)
//
//        val needsEmptySubtract1Tens = (dividendTens - (multiplyQuotientTens % 10)) == 0
//
//        val needsBorrowFromSubtract1HundredsInSubtract2 =
//            (dividend >= 100) && (((subtract1Result / 10) % 10) < ((multiplyQuotientOnes / 10) % 10)) // GUARD
//
//        val remainder = dividend - divisor * quotient
//
//        val needs2DigitRem =
//            if (needsSkipMultiply2AndSubtract2) subtract1Result >= 10
//            else remainder >= 10
//
//
//        return when (phase) {
//            DivisionPhaseV2.InputQuotient -> when (cell) {
//                CellName.QuotientTens -> quotientTens
//                CellName.QuotientOnes -> quotientOnes
//                else -> null
//            }
////            DivisionPhaseV2.InputMultiply1 -> when (cell) {
////                CellName.CarryDivisorTens -> when {
////                    dividend < 100 -> (quotient * divisorOnes) / 10 // 2by1, 2by2
////                    dividend >= 100 && phase == DivisionPhaseV2.InputMultiply1 -> {
////                        when (stepIndex) {
////                            0 -> (quotientTens * divisorOnes) / 10  // 3by2, 곱셈1
////                            1 -> (quotientOnes * divisorOnes) / 10  // 3by2, 곱셈2
////                            else -> null
////                        }
////                    }
////                    else -> null
////                }
////
////                CellName.Multiply1Hundreds -> if (quotient >= 10) {
////                    quotientTens * divisorTens
////                } else {
////                    quotientOnes * divisor / 10
////                }
////
//////                CellName.Multiply1Tens    -> if(quotient >= 10 ){
//////                    if (dividend >= 100) {
//////                        quotientTens * divisorOnes
//////                    } else {
//////                        quotientTens * divisor
//////                    }
//////                } else {
//////                    quotient * divisor / 10
//////                }
////
////                CellName.Multiply1Tens -> {
////                    val mul = when {
////                        quotient < 10 -> quotient * divisor / 10
////                        dividend < 100 -> quotientTens * divisor
////                        else -> quotientTens * divisorOnes
////                    }
////                    if (hasCarryInMultiply1Tens(dividend, divisor, quotient)) {
////                        // carry 발생시 특수 처리(예: UI 효과, 로그 등)
////                    }
////                    mul
////                }
////
////                CellName.Multiply1Ones    -> (quotient * divisorOnes) % 10
////
////                CellName.Multiply2Tens    -> if (dividend >= 100) {
////                    quotientOnes * divisorTens
////                } else {
////                    (divisor * quotientOnes) / 10
////                }
////
////                CellName.Multiply2Ones    -> (divisor * quotientOnes) % 10
////                else -> null
////            }
//
//            DivisionPhaseV2.InputMultiply1 -> when (cell) {
//                CellName.CarryDivisorTensMul1 -> when {
//                    dividend >= 100 -> (quotientTens * divisorOnes) / 10
//                    else -> (quotient * divisorOnes) / 10
//                }
//                CellName.Multiply1Hundreds -> if (dividend >= 100) {
//                    if (needsCarryInMultiply1){
//                        quotientTens * divisorTens + (divisorOnes * quotientTens / 10)
//                    } else {
//                        quotientTens * divisorTens
//                    }
//                } else null
//
//                CellName.Multiply1Tens -> when {
//                    dividend >= 100 -> quotientTens * divisorOnes % 10
////                    dividend < 100 && quotient < 10 -> quotient * divisor / 10      // 2by1: 몫 × 제수 / 10
//                    !needsTensQuotient -> quotient * divisor / 10
//                    else -> quotientTens * divisorOnes
//                }
//                CellName.Multiply1Ones -> when {
//                    dividend >= 100 -> (quotientTens * divisorOnes) % 10
//                    needsTensQuotient -> (quotientTens * divisor) % 10
//                    else -> (quotient * divisor) % 10
//                }
//                else -> null
//            }
//
//            DivisionPhaseV2.InputBringDown -> when(cell) {
//                CellName.Subtract1Ones -> dividendOnes
//                else -> null
//            }
//
//            DivisionPhaseV2.InputBorrow -> when (cell) {
//                CellName.BorrowDividendHundreds -> if (dividend >= 100) dividendHundreds - 1 else null
//                CellName.BorrowDividendTens -> dividendTens - 1
//                CellName.BorrowSubtract1Tens -> when {
//                    dividend >= 100 -> {
//                        val sub2TensBeforeBorrow = subtract1Result / 10
//                        sub2TensBeforeBorrow - 1
//                    }
//                    else -> {
//                        dividendTens - (divisor * quotientTens) - 1
//                    }
//                }
//                else -> null
//            }
//
//            DivisionPhaseV2.InputSubtract1 -> when (cell) {
//                CellName.Subtract1Hundreds -> if (dividend >= 100) {
//                    dividendHundreds - (quotientTens * divisor) / 10
//                } else null
//
//                CellName.Subtract1Tens -> when {
//                    dividend >= 100 -> {
//                        val tens = dividendHundreds * 10 + dividendTens - multiplyQuotientTens
//                        tens % 10
//                    }
//                    dividend >= 10 && quotient >= 10 -> {
//                        val tens = dividendTens - (quotientTens * divisor)
//                        if (tens < 0) tens + 10 else tens
//                    }
//                    else -> {
//                        ((dividend - (quotient * divisor)) / 10)
//                    }
//                }
//
//                CellName.Subtract1Ones -> (dividend - (quotient * divisor)) % 10
//                else -> null
//            }
//
//            DivisionPhaseV2.InputMultiply2 -> when (cell) {
//                CellName.CarryDivisorTensMul2 -> (quotientOnes * divisorOnes) / 10
//                CellName.Multiply2Tens -> when {
//                    dividend >= 100 -> {
////                        println("$quotientOnes * $divisorTens + ($divisorOnes * $quotientOnes")
//                        if(needsCarryInMultiply2){
//                            quotientOnes * divisorTens + (divisorOnes * quotientOnes / 10)
//                        } else {
//                            quotientOnes * divisorTens
//                        }
//                    }
//                    else -> (divisor * quotientOnes) / 10
//                }
//                CellName.Multiply2Ones -> {
//                    println("$divisor $quotientOnes    ${(divisor * quotientOnes) % 10}")
//                    (divisor * quotientOnes) % 10
//                }
//                else -> null
//            }
//
//            DivisionPhaseV2.InputSubtract2 -> when (cell) {
//                CellName.Subtract2Tens -> (dividend - (quotient * divisor)) / 10
//                CellName.Subtract2Ones -> (dividend - (quotient * divisor)) % 10
//                else -> null
//            }
//
//            DivisionPhaseV2.Complete -> null
//        }
//    }



}
