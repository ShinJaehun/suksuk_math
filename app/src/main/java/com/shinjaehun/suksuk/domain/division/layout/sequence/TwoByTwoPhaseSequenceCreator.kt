package com.shinjaehun.suksuk.domain.division.layout.sequence

import com.shinjaehun.suksuk.domain.division.layout.PhaseSequence
import com.shinjaehun.suksuk.domain.division.layout.PhaseStep
import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import javax.inject.Inject

class TwoByTwoPhaseSequenceCreator @Inject constructor() : PhaseSequenceCreator {
    override fun create(dividend: Int, divisor: Int): PhaseSequence {
        val quotient = dividend / divisor
        val multiply1Total = quotient * divisor
        val multiply1DivisorOnes = quotient * (divisor % 10)

        val needsCarry = multiply1DivisorOnes >= 10
        val needsBorrow = (dividend % 10) < (multiply1Total % 10)

        val remainder = dividend - multiply1Total
        val needs2DigitRem = remainder >= 10

        val steps = mutableListOf<PhaseStep>()

        // [1] 몫 입력 단계
        steps += PhaseStep(
            phase = DivisionPhaseV2.InputQuotient,
            editableCells = listOf(CellName.QuotientOnes),
            highlightCells = listOf(
                CellName.DividendTens, CellName.DividendOnes,
                CellName.DivisorTens, CellName.DivisorOnes
            )
        )

        // [2] 곱셈(일의 자리, Carry 여부 반영)
        if (needsCarry) {
            steps += PhaseStep(
                phase = DivisionPhaseV2.InputMultiply1,
                editableCells = listOf(CellName.CarryDivisorTens, CellName.Multiply1Ones),
                highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientOnes),
                needsCarry = true
            )
        } else {
            steps += PhaseStep(
                phase = DivisionPhaseV2.InputMultiply1,
                editableCells = listOf(CellName.Multiply1Ones),
                highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientOnes)
            )
        }

        // [3] 곱셈(십의 자리)
        steps += PhaseStep(
            phase = DivisionPhaseV2.InputMultiply1,
            editableCells = listOf(CellName.Multiply1Tens),
            highlightCells = buildList {
                add(CellName.DivisorTens)
                add(CellName.QuotientOnes)
                if (needsCarry) add(CellName.CarryDivisorTens)
            }
        )

        // [4] 받아내림 단계 (필요한 경우만)
        if (needsBorrow) {
            steps += PhaseStep(
                phase = DivisionPhaseV2.InputBorrow,
                editableCells = listOf(CellName.BorrowDividendTens),
                highlightCells = listOf(CellName.DividendTens),
                needsBorrow = true,
                strikeThroughCells = listOf(CellName.DividendTens),
                subtractLineTargets = setOf(CellName.BorrowDividendTens)
            )
        }

        // [5] 뺄셈 단계
        steps += PhaseStep(
            phase = DivisionPhaseV2.InputSubtract1,
            editableCells = listOf(CellName.Subtract1Ones),
            highlightCells = buildList {
                if (needsBorrow) add(CellName.Borrowed10DividendOnes)
                add(CellName.DividendOnes)
                add(CellName.Multiply1Ones)
            },
            presetValues = if (needsBorrow) mapOf(CellName.Borrowed10DividendOnes to "10") else emptyMap(),
            strikeThroughCells = if (needsBorrow) listOf(CellName.DividendTens) else emptyList(),
            subtractLineTargets = setOf(CellName.Subtract1Ones)
        )

        if(needs2DigitRem) {
            steps += PhaseStep(
                phase = DivisionPhaseV2.InputSubtract1,
                editableCells = listOf(CellName.Subtract1Tens),
                highlightCells = buildList {
                    add(CellName.Multiply1Tens)
                    if (needsBorrow) add(CellName.BorrowDividendTens)
                    else add(CellName.DividendTens)
                },
//                subtractLineTargets = setOf(CellName.Subtract1Tens)
            )
        }

        // [6] 완료 단계
        steps += PhaseStep(phase = DivisionPhaseV2.Complete)

        return PhaseSequence(
            pattern = DivisionPatternV2.TwoByTwo,
            steps = steps
        )
    }

}