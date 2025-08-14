package com.shinjaehun.suksuk.domain.division.sequence.creator

import com.shinjaehun.suksuk.domain.division.info.DivisionStateInfo
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseSequence
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseStep
import com.shinjaehun.suksuk.domain.division.model.DivisionCellName
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import javax.inject.Inject

class TwoByTwoDivPhaseSequenceCreator @Inject constructor() : DivisionPhaseSequenceCreator {
    override fun create(info: DivisionStateInfo): DivisionPhaseSequence {

        val isCarryRequiredInMultiply = (info.quotientOnes * info.divisorOnes) >= 10

        val steps = mutableListOf<DivisionPhaseStep>()

        // [1] 몫 입력 단계
        steps += DivisionPhaseStep(
            phase = DivisionPhaseV2.InputQuotient,
            editableCells = listOf(DivisionCellName.QuotientOnes),
            highlightCells = listOf(
                DivisionCellName.DividendTens, DivisionCellName.DividendOnes,
                DivisionCellName.DivisorTens, DivisionCellName.DivisorOnes
            )
        )

        // [2] 곱셈(일의 자리, Carry 여부 반영)
        if (isCarryRequiredInMultiply) {
            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputMultiply1,
                editableCells = listOf(DivisionCellName.CarryDivisorTensM1, DivisionCellName.Multiply1Ones),
                highlightCells = listOf(DivisionCellName.DivisorOnes, DivisionCellName.QuotientOnes),
                needsCarry = true
            )
        } else {
            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputMultiply1,
                editableCells = listOf(DivisionCellName.Multiply1Ones),
                highlightCells = listOf(DivisionCellName.DivisorOnes, DivisionCellName.QuotientOnes)
            )
        }

        // [3] 곱셈(십의 자리)
        steps += DivisionPhaseStep(
            phase = DivisionPhaseV2.InputMultiply1,
            editableCells = listOf(DivisionCellName.Multiply1Tens),
            highlightCells = buildList {
                add(DivisionCellName.DivisorTens)
                add(DivisionCellName.QuotientOnes)
                if (isCarryRequiredInMultiply) add(DivisionCellName.CarryDivisorTensM1)
            }
        )

        // [4] 받아내림 단계 (필요한 경우만)
        if (info.needsTensBorrowInS1) {
            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputBorrow,
                editableCells = listOf(DivisionCellName.BorrowDividendTens),
                highlightCells = listOf(
                    DivisionCellName.DividendTens,
                    DivisionCellName.DividendOnes,
                    DivisionCellName.Multiply1Ones
                ),
                needsBorrow = true,
                strikeThroughCells = listOf(DivisionCellName.DividendTens),
                subtractLineTargets = setOf(DivisionCellName.BorrowDividendTens)
            )
        }

        // [5] 뺄셈 단계
        steps += DivisionPhaseStep(
            phase = DivisionPhaseV2.InputSubtract1,
            editableCells = listOf(DivisionCellName.Subtract1Ones),
            highlightCells = buildList {
                if (info.needsTensBorrowInS1) add(DivisionCellName.Borrowed10DividendOnes)
                add(DivisionCellName.DividendOnes)
                add(DivisionCellName.Multiply1Ones)
            },
            presetValues = if (info.needsTensBorrowInS1) mapOf(DivisionCellName.Borrowed10DividendOnes to "10") else emptyMap(),
            strikeThroughCells = if (info.needsTensBorrowInS1) listOf(DivisionCellName.DividendTens) else emptyList(),
            subtractLineTargets = setOf(DivisionCellName.Subtract1Ones)
        )

        if(info.has2DigitsRemainder) {
            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputSubtract1,
                editableCells = listOf(DivisionCellName.Subtract1Tens),
                highlightCells = buildList {
                    add(DivisionCellName.Multiply1Tens)
                    if (info.needsTensBorrowInS1) add(DivisionCellName.BorrowDividendTens)
                    else add(DivisionCellName.DividendTens)
                },
                subtractLineTargets = setOf(DivisionCellName.Subtract1Tens)
            )
        }

        // [6] 완료 단계
        steps += DivisionPhaseStep(phase = DivisionPhaseV2.Complete)

        return DivisionPhaseSequence(
//            pattern = DivisionPatternV2.TwoByTwo,
            steps = steps
        )
    }

}