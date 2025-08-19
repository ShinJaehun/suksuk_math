package com.shinjaehun.suksuk.domain.division.sequence.creator

import com.shinjaehun.suksuk.domain.division.info.DivisionStateInfo
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseSequence
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseStep
import com.shinjaehun.suksuk.domain.division.model.DivisionCell
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import com.shinjaehun.suksuk.domain.pattern.DivisionPatternV2
import javax.inject.Inject

class TwoByTwoDivPhaseSequenceCreator @Inject constructor() : DivisionPhaseSequenceCreator {
    override fun create(info: DivisionStateInfo): DivisionPhaseSequence {

        val isCarryRequiredInMultiply = (info.quotientOnes * info.divisorOnes) >= 10

        val steps = mutableListOf<DivisionPhaseStep>()

        // [1] 몫 입력 단계
        steps += DivisionPhaseStep(
            phase = DivisionPhaseV2.InputQuotient,
            editableCells = listOf(DivisionCell.QuotientOnes),
            highlightCells = listOf(
                DivisionCell.DividendTens, DivisionCell.DividendOnes,
                DivisionCell.DivisorTens, DivisionCell.DivisorOnes
            )
        )

        // [2] 곱셈(일의 자리, Carry 여부 반영)
        if (isCarryRequiredInMultiply) {
            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputMultiply1,
                editableCells = listOf(DivisionCell.CarryDivisorTensM1, DivisionCell.Multiply1Ones),
                highlightCells = listOf(DivisionCell.DivisorOnes, DivisionCell.QuotientOnes),
                needsCarry = true
            )
        } else {
            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputMultiply1,
                editableCells = listOf(DivisionCell.Multiply1Ones),
                highlightCells = listOf(DivisionCell.DivisorOnes, DivisionCell.QuotientOnes)
            )
        }

        // [3] 곱셈(십의 자리)
        steps += DivisionPhaseStep(
            phase = DivisionPhaseV2.InputMultiply1,
            editableCells = listOf(DivisionCell.Multiply1Tens),
            highlightCells = buildList {
                add(DivisionCell.DivisorTens)
                add(DivisionCell.QuotientOnes)
                if (isCarryRequiredInMultiply) add(DivisionCell.CarryDivisorTensM1)
            }
        )

        // [4] 받아내림 단계 (필요한 경우만)
        if (info.needsTensBorrowInS1) {
            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputBorrow,
                editableCells = listOf(DivisionCell.BorrowDividendTens),
                highlightCells = listOf(
                    DivisionCell.DividendTens,
                    DivisionCell.DividendOnes,
                    DivisionCell.Multiply1Ones
                ),
                needsBorrow = true,
                strikeThroughCells = listOf(DivisionCell.DividendTens),
                subtractLineTargets = setOf(DivisionCell.BorrowDividendTens)
            )
        }

        // [5] 뺄셈 단계
        steps += DivisionPhaseStep(
            phase = DivisionPhaseV2.InputSubtract1,
            editableCells = listOf(DivisionCell.Subtract1Ones),
            highlightCells = buildList {
                if (info.needsTensBorrowInS1) add(DivisionCell.Borrowed10DividendOnes)
                add(DivisionCell.DividendOnes)
                add(DivisionCell.Multiply1Ones)
            },
            presetValues = if (info.needsTensBorrowInS1) mapOf(DivisionCell.Borrowed10DividendOnes to "10") else emptyMap(),
            strikeThroughCells = if (info.needsTensBorrowInS1) listOf(DivisionCell.DividendTens) else emptyList(),
            subtractLineTargets = setOf(DivisionCell.Subtract1Ones)
        )

        if(info.has2DigitsRemainder) {
            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputSubtract1,
                editableCells = listOf(DivisionCell.Subtract1Tens),
                highlightCells = buildList {
                    add(DivisionCell.Multiply1Tens)
                    if (info.needsTensBorrowInS1) add(DivisionCell.BorrowDividendTens)
                    else add(DivisionCell.DividendTens)
                },
                subtractLineTargets = setOf(DivisionCell.Subtract1Tens)
            )
        }

        // [6] 완료 단계
        steps += DivisionPhaseStep(phase = DivisionPhaseV2.Complete)

        return DivisionPhaseSequence(
            steps = steps,
            pattern = DivisionPatternV2.TwoByTwo
        )
    }

}