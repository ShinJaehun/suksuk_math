package com.shinjaehun.suksuk.domain.multiplication.evaluator

import com.shinjaehun.suksuk.common.eval.EvalResult
import com.shinjaehun.suksuk.domain.multiplication.info.MulStateInfo
import com.shinjaehun.suksuk.domain.multiplication.model.MulCellName
import com.shinjaehun.suksuk.domain.multiplication.model.MulDomainState
import com.shinjaehun.suksuk.domain.multiplication.model.MulPhase
import javax.inject.Inject

class MulPhaseEvaluator @Inject constructor() {
    fun isCorrect(
        phase: MulPhase,
        cell: MulCellName,
        input: String,
        info: MulStateInfo,
        stepIndex: Int,
        previousInputs: List<String>
    ): Boolean {
        val inputValue = input.trim().toIntOrNull() ?: return false
        val expected = expectedValueForCell(phase, cell, info, stepIndex, previousInputs)
        println("🧪 [MUL] $cell: expected=$expected, input=$inputValue")
        return expected != null && expected == inputValue
    }

    fun evaluate(
        domain: MulDomainState,
        inputsForThisStep: List<String>
    ): EvalResult {
//        val step = domain.phaseSequence.steps[domain.currentStepIndex]
//        val targetCell = step.editableCells.firstOrNull() ?: MulCellName.None
//
//        val ok = isCorrect(
//            phase = step.phase,
//            cell = targetCell,
//            input = input,
//            info = domain.info,
//            stepIndex = domain.currentStepIndex,
//            previousInputs = domain.inputs
//        )
//
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
        val cur   = domain.currentStepIndex
        val step  = steps[cur]

        // [1] 스텝 전체 검증
        val allOk = step.editableCells.withIndex().all { (i, cell) ->
            val user = inputsForThisStep.getOrNull(i) ?: return@all false
            isCorrect(step.phase, cell, user, domain.info, cur, domain.inputs)
        }
        if (!allOk) {
            return EvalResult(
                isCorrect = false,
                nextStepIndex = null,
                isFinished = false
            )
        }

        // [2] 비편집 스텝 자동 스킵
        var next = cur + 1
        while (next <= steps.lastIndex && steps[next].editableCells.isEmpty()) {
            next++
        }

        // [3] Complete 진입 즉시 종료
        val finished = next >= steps.lastIndex ||
                steps.getOrNull(next)?.phase == MulPhase.Complete

        // [4] 항상 next로 이동
        val clampedNext = minOf(next, steps.lastIndex)

        return EvalResult(
            isCorrect = true,
            nextStepIndex = clampedNext,
            isFinished = finished
        )
    }

    private fun expectedValueForCell(
        phase: MulPhase,
        cell: MulCellName,
        i: MulStateInfo,
        stepIndex: Int,
        previousInputs: List<String>
    ): Int? = when (phase) {

        MulPhase.InputMultiply1 -> when (cell) {
            MulCellName.CarryP1Tens     -> i.carryP1Tens
            MulCellName.P1Ones          -> i.product1Ones
            MulCellName.P1Tens          -> i.product1Tens
            MulCellName.P1Hundreds      -> i.product1Hundreds
            MulCellName.P1Thousands     -> i.product1Thousands
            else -> null
        }

        MulPhase.InputMultiply2 -> when (cell) {
            MulCellName.CarryP2Tens     -> i.carryP2Tens
            MulCellName.P2Tens          -> i.product2Tens
            MulCellName.P2Hundreds      -> i.product2Hundreds
            MulCellName.P2Thousands     -> i.product2Thousands
            MulCellName.P2TenThousands  -> i.product2TenThousands
            else -> null
        }

        MulPhase.InputSum -> when (cell) {
            MulCellName.SumOnes             -> i.sumOnes
            MulCellName.CarrySumHundreds    -> i.carrySumHundreds
            MulCellName.SumTens             -> i.sumTens
            MulCellName.CarrySumThousands   -> i.carrySumThousands
            MulCellName.SumHundreds         -> i.sumHundreds
            MulCellName.CarrySumTenThousands-> i.carrySumTenThousands
            MulCellName.SumThousands        -> i.sumThousands
            MulCellName.SumTenThousands     -> i.sumTenThousands
            else -> null
        }

        MulPhase.Complete -> null
    }
}