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

        if(needs2DigitRem) {
            steps += PhaseStep(
                phase = DivisionPhaseV2.InputSubtract,
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

    fun makeTwoByOnePhaseSequence(dividend: Int, divisor: Int): PhaseSequence {
        val quotient = dividend / divisor


        val needsTensQuotient = quotient >= 10
        val multiplyQuotientTens = divisor * (quotient / 10)
        val multiplyQuotientOnes = divisor * (quotient % 10)

        val needsBorrowSubtract1 = (dividend % 10) < (multiplyQuotientOnes % 10)

        val subtract1TensQuotient = ((dividend / 10) - multiplyQuotientTens) * 10 + (dividend % 10)

        val needs2DigitMul = multiplyQuotientOnes >= 10
        val needsBorrowSubtract2 = (subtract1TensQuotient % 10) < (multiplyQuotientOnes % 10)
        val needsSkipBorrowSubtract2 = subtract1TensQuotient == 1 && needsBorrowSubtract2

        val subtract1OnesQuotient = dividend - multiplyQuotientOnes

        val bringDown = dividend % 10

//        val needsBorrow = (dividend % 10) < (multiply1 % 10)
//
//        val remainder = dividend - multiply1
//        val needsTensSubtraction = remainder >= 10

        val steps = mutableListOf<PhaseStep>()

        if (needsTensQuotient) {
            steps += PhaseStep(
                phase = DivisionPhaseV2.InputQuotient,
                editableCells = listOf(CellName.QuotientTens),
                highlightCells = listOf(CellName.DividendTens, CellName.DivisorOnes)
            )

            steps += PhaseStep(
                phase = DivisionPhaseV2.InputMultiply,
                editableCells = listOf(CellName.Multiply1Tens),
                highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientTens),
            )

            steps += PhaseStep(
                phase = DivisionPhaseV2.InputSubtract,
                editableCells = listOf(CellName.Subtract1Tens),
                highlightCells = listOf(CellName.DividendTens, CellName.Multiply1Tens),
                subtractLineTargets = setOf(CellName.Subtract1Tens)
            )

            steps += PhaseStep(
                phase = DivisionPhaseV2.InputBringDown,
                editableCells = listOf(CellName.Subtract1Ones),
                highlightCells = listOf(CellName.DividendOnes),
            )

            steps += PhaseStep(
                phase = DivisionPhaseV2.InputQuotient,
                editableCells = listOf(CellName.QuotientOnes),
                highlightCells = listOf(CellName.DivisorOnes, CellName.Subtract1Tens, CellName.Subtract1Ones),
            )

            if (needs2DigitMul) {
                steps += PhaseStep(
                    phase = DivisionPhaseV2.InputMultiply,
                    editableCells = listOf(CellName.Multiply2Tens, CellName.Multiply2Ones),
                    highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientOnes),
                )
            } else {
                steps += PhaseStep(
                    phase = DivisionPhaseV2.InputMultiply,
                    editableCells = listOf(CellName.Multiply2Ones),
                    highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientOnes),
                )
            }

            steps += PhaseStep(
                phase = DivisionPhaseV2.InputSubtract,
                editableCells = listOf(CellName.Subtract2Ones),
                highlightCells = listOf(CellName.Subtract1Ones, CellName.Multiply2Ones),
                subtractLineTargets = setOf(CellName.Subtract2Ones)
            )



        }

        // [6] 완료 단계
        steps += PhaseStep(phase = DivisionPhaseV2.Complete)

        return PhaseSequence(
            pattern = DivisionPatternV2.TwoByOne,
            steps = steps
        )
    }



}
