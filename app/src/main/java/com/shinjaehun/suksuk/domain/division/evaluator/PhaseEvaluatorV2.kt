package com.shinjaehun.suksuk.domain.division.evaluator

import com.shinjaehun.suksuk.domain.division.DivisionStateInfo
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.domain.division.model.DivisionDomainStateV2
import javax.inject.Inject

class PhaseEvaluatorV2 @Inject constructor() {

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

    // 표준 진입점: 판정 + 전이 + 종료 포함
    fun evaluate(
        domain: DivisionDomainStateV2,
        input: String
    ): EvalResultV2 {
        val step = domain.phaseSequence.steps[domain.currentStepIndex]
        val ok = isCorrect(step.phase, step.editableCells.firstOrNull() ?: CellName.None, input, domain.info, domain.currentStepIndex, domain.inputs)
        if (!ok) {
            return EvalResultV2(
                isCorrect = false,
                nextStepIndex = null,
                isFinished = false,
                normalizedInput = input.trim()
            )
        }

        val next = domain.currentStepIndex + 1
        val finished = next >= domain.phaseSequence.steps.lastIndex

        return EvalResultV2(
            isCorrect = true,
            nextStepIndex = next,
            isFinished = finished,
            normalizedInput = input.trim()
        )
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
                CellName.CarryDivisorTensM1 ->
                    if (i.hasTensQuotient) {
                        (i.quotientTens * i.divisorOnes) / 10
                    }
                    else (i.quotient * i.divisorOnes) / 10

                CellName.Multiply1Hundreds ->
                    if (i.dividend >= 100) {
                        if(i.hasTensQuotient){
                            if (i.isCarryRequiredInMultiplyQuotientTens)
                                i.quotientTens * i.divisorTens + (i.quotientTens * i.divisorOnes / 10)
                            else
                                i.quotientTens * i.divisorTens
                        } else {
                            i.multiplyQuotientOnes / 100
                        }
                    } else null

                CellName.Multiply1Tens -> when {
                    i.dividend >= 100 -> {
                        if (i.hasTensQuotient) {
                            (i.quotientTens * i.divisorOnes) % 10
                        } else {
                            i.multiplyQuotientOnes / 10 % 10
                        }
                    }
                    i.hasTensQuotient -> i.quotientTens * i.divisorOnes
                    else -> (i.quotient * i.divisor) / 10
                }

                CellName.Multiply1Ones -> when {
                    i.dividend >= 100 -> {
                        if (i.hasTensQuotient) {
                            (i.quotientTens * i.divisorOnes) % 10
                        } else {
                            i.multiplyQuotientOnes % 10
                        }
                    }
                    i.hasTensQuotient -> (i.quotientTens * i.divisor) % 10
                    else -> i.multiplyQuotientOnes % 10
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
                CellName.BorrowDividendTens ->
                    if(i.needsTensBorrowInS1 &&
                        i.needsHundredsBorrowInS1) {
                        9
                    } else {
                        i.dividendTens - 1
                    }
                CellName.BorrowSubtract1Tens -> {
                    if (i.needsTensBorrowInS2 &&
                        i.needsHundredsBorrowInS2) {
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
                    if (i.hasTensQuotient) {
                        i.subtract1Result / 100
                    } else null

                CellName.Subtract1Tens ->
                    if (i.hasTensQuotient){
                        i.subtract1TensOnly % 10
                    } else {
                        i.remainder / 10
                    }

                CellName.Subtract1Ones -> i.remainder % 10
                else -> null
            }

            DivisionPhaseV2.InputMultiply2 -> when (cell) {
                CellName.CarryDivisorTensM2 -> (i.quotientOnes * i.divisorOnes) / 10

                CellName.Multiply2Hundreds ->
                    if(i.dividend >= 100) i.multiplyQuotientOnes / 100
                    else null

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
}

data class EvalResultV2(
    val isCorrect: Boolean,
    val nextStepIndex: Int?,   // 정답일 때만 증가, 오답이면 null
    val isFinished: Boolean,   // 단일 진실(SSOT): 마지막 단계 도달 여부
    val normalizedInput: String? = null
)