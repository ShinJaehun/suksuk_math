package com.shinjaehun.suksuk.domain.division.evaluator

import com.shinjaehun.suksuk.common.eval.EvalResult
import com.shinjaehun.suksuk.domain.division.info.DivisionStateInfo
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import com.shinjaehun.suksuk.domain.division.model.DivisionCell
import com.shinjaehun.suksuk.domain.model.DivisionDomainStateV2
import javax.inject.Inject

class DivisionPhaseEvaluatorV2 @Inject constructor() {

    fun isCorrect(
        phase: DivisionPhaseV2,
        cell: DivisionCell,
        input: String,
        info: DivisionStateInfo,
    ): Boolean {
        val inputValue = input.toIntOrNull() ?: return false
        val expected = expectedValueForCell(phase, cell, info)
        println("🧪 $cell: expected=$expected, input=$input")
        return expected != null && expected == inputValue
    }

    // 표준 진입점: 판정 + 전이 + 종료 포함
    fun evaluate(
        domain: DivisionDomainStateV2,
        inputsForThisStep: List<String>
    ): EvalResult {
//        val step = domain.phaseSequence.steps[domain.currentStepIndex]
//        val ok = isCorrect(step.phase, step.editableCells.firstOrNull() ?: DivisionCellName.None, input, domain.info, domain.currentStepIndex, domain.inputs)
//        if (!ok) {
//            return EvalResult(
//                isCorrect = false,
//                nextStepIndex = null,
//                isFinished = false,
//                normalizedInput = input.trim()
//            )
//        }
//
//        val next = domain.currentStepIndex + 1
//        val finished = next >= domain.phaseSequence.steps.lastIndex
//
//        return EvalResult(
//            isCorrect = true,
//            nextStepIndex = next,
//            isFinished = finished,
//            normalizedInput = input.trim()
//        )

//        val step = domain.phaseSequence.steps[domain.currentStepIndex]
//        val cells = step.editableCells
//
//        val allOk = cells.withIndex().all { (idx, cell) ->
//            val user = inputsForThisStep.getOrNull(idx) ?: return@all false
//            isCorrect(step.phase, cell, user, domain.info, domain.currentStepIndex, domain.inputs)
//        }
//        if (!allOk) return EvalResult(false, null, false, inputsForThisStep.joinToString(""))
//
//        val next = domain.currentStepIndex + 1
//        val last = domain.phaseSequence.steps.lastIndex
//        val finished = next >= last
//
//        return EvalResult(
//            isCorrect = true,
//            nextStepIndex = next,        // ✅ 항상 next로 이동 (Complete로 진입)
//            isFinished = finished,       // ✅ Complete 진입 즉시 true
//            normalizedInput = inputsForThisStep.joinToString("")
//        )

        val steps = domain.phaseSequence.steps
        val currentStepIndex   = domain.currentStepIndex
        val currentStep  = steps[currentStepIndex]

        // [1] 이번 스텝 전부 검증 (로그는 여기서만 찍힘)
        val allOk = currentStep.editableCells.withIndex().all { (i, cell) ->
            val user = inputsForThisStep.getOrNull(i) ?: return@all false
            isCorrect(currentStep.phase, cell, user, domain.info)
        }
        if (!allOk) {
            return EvalResult(
                isCorrect = false,
                nextStepIndex = null,
                isFinished = false,
            )
        }

        // [2] 전이: 비편집 스텝 자동 스킵
        var next = currentStepIndex + 1
        while (next <= steps.lastIndex && steps[next].editableCells.isEmpty()) {
            next++
        }

        // [3] Complete 진입 즉시 종료
        val completeIndex = steps.lastIndex // 마지막이 Complete라고 가정
        val finished = next >= completeIndex ||
                steps.getOrNull(next)?.phase == DivisionPhaseV2.Complete

        // [4] nextStepIndex는 항상 next로 이동(되돌리지 않음)
        val clampedNext = minOf(next, steps.lastIndex)

        return EvalResult(
            isCorrect = true,
            nextStepIndex = clampedNext,
            isFinished = finished,
        )
    }

    private fun expectedValueForCell(
        phase: DivisionPhaseV2,
        cell: DivisionCell,
        i: DivisionStateInfo,
    ): Int? {

        return when (phase) {
            DivisionPhaseV2.InputQuotient -> when (cell) {
                DivisionCell.QuotientTens -> i.quotientTens
                DivisionCell.QuotientOnes -> i.quotientOnes
                else -> null
            }

            DivisionPhaseV2.InputMultiply1 -> when (cell) {
                DivisionCell.CarryDivisorTensM1 ->
                    if (i.hasTensQuotient) {
                        (i.quotientTens * i.divisorOnes) / 10
                    }
                    else (i.quotient * i.divisorOnes) / 10

                DivisionCell.Multiply1Hundreds ->
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

                DivisionCell.Multiply1Tens -> when {
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

                DivisionCell.Multiply1Ones -> when {
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
                DivisionCell.Subtract1Ones -> i.bringDownInSubtract1
                else -> null
            }

            DivisionPhaseV2.InputBorrow -> when (cell) {
                DivisionCell.BorrowDividendHundreds ->
                    if (i.dividend >= 100) i.dividendHundreds - 1 else null
                DivisionCell.BorrowDividendTens ->
                    if(i.needsTensBorrowInS1 &&
                        i.needsHundredsBorrowInS1) {
                        9
                    } else {
                        i.dividendTens - 1
                    }
                DivisionCell.BorrowSubtract1Tens -> {
                    if (i.needsTensBorrowInS2 &&
                        i.needsHundredsBorrowInS2) {
                            9
                    } else {
                        val sub2TensBeforeBorrow = (i.subtract1Result / 10) % 10
                        sub2TensBeforeBorrow - 1
                    }
                }
                DivisionCell.BorrowSubtract1Hundreds -> (i.subtract1Result / 100) - 1
                else -> null
            }

            DivisionPhaseV2.InputSubtract1 -> when (cell) {
                DivisionCell.Subtract1Hundreds ->
                    if (i.hasTensQuotient) {
                        i.subtract1Result / 100
                    } else null

                DivisionCell.Subtract1Tens ->
                    if (i.hasTensQuotient){
                        i.subtract1TensOnly % 10
                    } else {
                        i.remainder / 10
                    }

                DivisionCell.Subtract1Ones -> i.remainder % 10
                else -> null
            }

            DivisionPhaseV2.PrepareNextOp -> null

            DivisionPhaseV2.InputMultiply2 -> when (cell) {
                DivisionCell.CarryDivisorTensM2 -> (i.quotientOnes * i.divisorOnes) / 10

                DivisionCell.Multiply2Hundreds ->
                    if(i.dividend >= 100) i.multiplyQuotientOnes / 100
                    else null

                DivisionCell.Multiply2Tens -> i.multiplyQuotientOnes / 10 % 10

                DivisionCell.Multiply2Ones -> i.multiplyQuotientOnes % 10
                else -> null
            }

            DivisionPhaseV2.InputSubtract2 -> when (cell) {
                DivisionCell.Subtract2Tens -> i.remainder / 10
                DivisionCell.Subtract2Ones -> i.remainder % 10
                else -> null
            }

            DivisionPhaseV2.Complete -> null
        }
    }
}