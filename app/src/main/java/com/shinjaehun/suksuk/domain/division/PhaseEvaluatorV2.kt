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

        return when (phase) {
            DivisionPhaseV2.InputQuotient -> when (cell) {
                CellName.QuotientOnes -> quotient
                else -> null
            }
            DivisionPhaseV2.InputMultiply -> when (cell) {
                CellName.CarryDivisorTens -> (quotient * divisorOnes) / 10
                CellName.Multiply1Ones    -> (quotient * divisorOnes) % 10
//                CellName.Multiply1Tens    -> (quotient * divisorTens)
                CellName.Multiply1Tens    -> if(quotient >= 10 ){
                    quotient * divisorTens
                } else {
                    quotient * divisor / 10
                }
                else -> null
            }
            DivisionPhaseV2.InputBorrow -> when (cell) {
                CellName.BorrowDividendTens -> dividendTens - 1
                else -> null
            }
            DivisionPhaseV2.InputSubtract -> when (cell) {
                CellName.Subtract1Tens -> (dividend - (quotient * divisor)) / 10
                CellName.Subtract1Ones -> (dividend - (quotient * divisor)) % 10
                else -> null
            }

            DivisionPhaseV2.InputBringDown -> null // 구현 필요시 여기에 추가
            DivisionPhaseV2.Complete -> null // 입력 없음
        }
    }
}