package com.shinjaehun.suksuk.domain.division

import com.shinjaehun.suksuk.domain.division.model.CellName
import javax.inject.Inject

class DivisionPhaseSequenceProvider @Inject constructor() {
//    fun getTwoByTwo(): PhaseSequence = twoByTwoPhaseSequence
    fun getForPattern(pattern: DivisionPatternV2): PhaseSequence = when (pattern) {
//        DivisionPatternV2.TwoByTwo -> twoByTwoPhaseSequence
        // ... 더 추가
        else -> error("아직 미구현!")
    }

    fun makeTwoByTwoPhaseSequence(dividend: Int, divisor: Int): PhaseSequence {
        val quotient = dividend / divisor
        val multiply1 = quotient * divisor
        val multiply1DivisorOnes = quotient * (divisor % 10)

        val needsCarry = multiply1DivisorOnes >= 10
        val needsBorrow = (dividend % 10) < (multiply1 % 10)

        val remainder = dividend - multiply1
        val needsTensSubtraction = remainder >= 10

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
        steps += if (needsCarry) {
            PhaseStep(
                phase = DivisionPhaseV2.InputMultiply,
                editableCells = listOf(CellName.CarryDivisorTens, CellName.Multiply1Ones),
                highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientOnes),
                needsCarry = true
            )
        } else {
            PhaseStep(
                phase = DivisionPhaseV2.InputMultiply,
                editableCells = listOf(CellName.Multiply1Ones),
                highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientOnes)
            )
        }

        // [3] 곱셈(십의 자리)
        steps += PhaseStep(
            phase = DivisionPhaseV2.InputMultiply,
            editableCells = listOf(CellName.Multiply1Tens),
            highlightCells = listOf(CellName.DivisorTens, CellName.QuotientOnes).let {
                if (needsCarry) it + CellName.CarryDivisorTens else it
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
            phase = DivisionPhaseV2.InputSubtract,
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

        if(needsTensSubtraction) {
            steps += PhaseStep(
                phase = DivisionPhaseV2.InputSubtract,
                editableCells = listOf(CellName.Subtract1Tens),
                highlightCells = listOf(CellName.Multiply1Tens).let {
                    if(needsBorrow) it + CellName.BorrowDividendTens else it + CellName.DividendTens
                },
                subtractLineTargets = setOf(CellName.Subtract1Tens)
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
