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
        val expected = expectedValueForCell(
            phase, cell, dividend, divisor, stepIndex, previousInputs
        )
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
        val dividendTens = dividend / 10
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
            DivisionPhaseV2.InputMultiply -> when (cell) {
                CellName.CarryDivisorTens -> (quotient * divisorOnes) / 10
                CellName.Multiply1Ones    -> (quotient * divisorOnes) % 10
//                CellName.Multiply1Tens    -> (quotient * divisorTens)
                CellName.Multiply1Tens    -> if(quotient >= 10 ){
                    quotientTens * divisor
                } else {
                    quotient * divisor / 10
                }
                CellName.Multiply2Tens    -> (divisor * quotientOnes) / 10
                CellName.Multiply2Ones    -> (divisor * quotientOnes) % 10
                else -> null
            }
            DivisionPhaseV2.InputBringDown -> when(cell) {
                CellName.Subtract1Ones -> dividendOnes
                else -> null
            }
            DivisionPhaseV2.InputBorrow -> when (cell) {
                CellName.BorrowDividendTens -> dividendTens - 1
                else -> null
            }
            DivisionPhaseV2.InputSubtract -> when (cell) {
                CellName.Subtract1Tens -> if(quotient >= 10 ) {
                    dividendTens - quotientTens * divisor
                } else {
                    (dividend - (quotient * divisor)) / 10
                }
                CellName.Subtract1Ones -> (dividend - (quotient * divisor)) % 10
                CellName.Subtract2Ones -> (dividend - (quotient * divisor)) % 10
                else -> null
            }


            DivisionPhaseV2.Complete -> null // 입력 없음
        }
    }
}