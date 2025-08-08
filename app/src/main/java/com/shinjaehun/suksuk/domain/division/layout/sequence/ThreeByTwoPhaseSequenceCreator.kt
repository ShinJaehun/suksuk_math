package com.shinjaehun.suksuk.domain.division.layout.sequence

import com.shinjaehun.suksuk.domain.division.layout.PhaseSequence
import com.shinjaehun.suksuk.domain.division.layout.PhaseStep
import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import javax.inject.Inject

class ThreeByTwoPhaseSequenceCreator @Inject constructor() : PhaseSequenceCreator {
    override fun create(dividend: Int, divisor: Int): PhaseSequence {
        val quotient = dividend / divisor
        val quotientTens = quotient / 10
        val quotientOnes = quotient % 10

        val dividendHundreds = dividend / 100
        val dividendTens = dividend / 10 % 10
        val dividendOnes = dividend % 10

        val divisorTens = divisor / 10
        val divisorOnes = divisor % 10

        val needsTensQuotient = quotient >= 10

        val multiplyQuotientTens = divisor * quotientTens
        val multiplyQuotientOnes = divisor * quotientOnes

        val needsBorrowDividendHundredsInSubtract1 = ((dividend / 10) % 10) < (multiplyQuotientTens % 10)

        val bringDownInSubtract1 = dividend % 10
        val subtract1 = (dividend / 10) - (quotientTens * divisor)

        val needsCarryInMultiply1 = (quotientTens * divisorOnes) >= 10
        val needsCarryInMultiply2 = (quotientOnes * divisorOnes) >= 10

        val needsBorrowSubtract1TensInSubtract2 =
            (subtract1 * 10 + bringDownInSubtract1) % 10 < multiplyQuotientOnes % 10

        val needsBorrowSubtract1HundredsInSubtract2 =
            ((subtract1 * 10 + bringDownInSubtract1) / 10) % 10 < (multiplyQuotientOnes / 10) % 10

        val remainder = dividend - divisor * quotient
        val needs2DigitRem = remainder >= 10

        val steps = mutableListOf<PhaseStep>()

        if (needsTensQuotient) {
            // [1] 몫 십의 자리 입력
            steps += PhaseStep(
                phase = DivisionPhaseV2.InputQuotient,
                editableCells = listOf(CellName.QuotientTens),
                highlightCells = listOf(CellName.DividendHundreds, CellName.DividendTens, CellName.DivisorTens, CellName.DivisorOnes)
            )

            // [2] 1차 곱셈 (몫 십의 자리 × 제수) -- Carry 없음
            if(needsCarryInMultiply1){
                steps += PhaseStep(
                    phase = DivisionPhaseV2.InputMultiply1,
                    editableCells = listOf(CellName.CarryDivisorTens, CellName.Multiply1Tens),
                    highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientOnes),
                    needsCarry = true
                )
            } else {
                steps += PhaseStep(
                    phase = DivisionPhaseV2.InputMultiply1,
                    editableCells = listOf(CellName.Multiply1Tens),
                    highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientTens),
                )
            }

            steps += PhaseStep(
                phase = DivisionPhaseV2.InputMultiply1,
                editableCells = listOf(CellName.Multiply1Hundreds),
                highlightCells = listOf(CellName.QuotientTens, CellName.DivisorTens)
            )

            if(needsBorrowDividendHundredsInSubtract1) {
                steps += PhaseStep(
                    phase = DivisionPhaseV2.InputBorrow,
                    editableCells = listOf(CellName.BorrowDividendHundreds),
                    highlightCells = listOf(CellName.DividendHundreds),
                    needsBorrow = true,
                    strikeThroughCells = listOf(CellName.DividendHundreds),
                    subtractLineTargets = setOf(CellName.BorrowDividendHundreds)
                )
            }

            // [3] 1차 뺄셈 (Borrow 없음)
            steps += PhaseStep(
                phase = DivisionPhaseV2.InputSubtract1,
                editableCells = listOf(CellName.Subtract1Tens),
                highlightCells = buildList {
                    if(needsBorrowDividendHundredsInSubtract1){
                        add(CellName.Borrowed10DividendTens)
                    }
                    add(CellName.DividendTens)
                    add(CellName.Multiply1Tens)
                },
                presetValues = if (needsBorrowDividendHundredsInSubtract1)
                    mapOf(CellName.Borrowed10DividendTens to "10")
                else
                    emptyMap(),
                strikeThroughCells = if(needsBorrowDividendHundredsInSubtract1)
                    listOf(CellName.DividendHundreds)
                else
                    emptyList(),
                subtractLineTargets = setOf(CellName.Subtract1Tens)
            )

            // [4] Bring down 일의 자리
            steps += PhaseStep(
                phase = DivisionPhaseV2.InputBringDown,
                editableCells = listOf(CellName.Subtract1Ones),
                highlightCells = listOf(CellName.DividendOnes),
            )

            // [5] 몫 일의 자리 입력
            steps += PhaseStep(
                phase = DivisionPhaseV2.InputQuotient,
                editableCells = listOf(CellName.QuotientOnes),
                highlightCells = listOf(CellName.Subtract1Tens, CellName.Subtract1Ones),
                presetValues = mapOf(CellName.CarryDivisorTens to "")
            )

            // [6] 2차 곱셈 (몫 일의 자리 × 제수)
            if(needsCarryInMultiply2){
                steps += PhaseStep(
                    phase = DivisionPhaseV2.InputMultiply2,
                    editableCells = listOf(CellName.CarryDivisorTens, CellName.Multiply2Ones),
                    highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientOnes),
                    needsCarry = true
                )
            } else {
                steps += PhaseStep(
                    phase = DivisionPhaseV2.InputMultiply2,
                    editableCells = listOf(CellName.Multiply2Ones),
                    highlightCells = listOf(CellName.QuotientOnes, CellName.DivisorOnes)
                )
            }

            steps += PhaseStep(
                phase = DivisionPhaseV2.InputMultiply2,
                editableCells = listOf(CellName.Multiply2Tens),
                highlightCells = listOf(CellName.QuotientOnes, CellName.DivisorTens)
            )

            if(needsBorrowSubtract1TensInSubtract2){
                steps += PhaseStep(
                    phase = DivisionPhaseV2.InputBorrow,
                    editableCells = listOf(CellName.BorrowSubtract1Tens),
                    highlightCells = listOf(CellName.Subtract1Tens),
                    needsBorrow = true,
                    strikeThroughCells = listOf(CellName.Subtract1Tens),
                    subtractLineTargets = setOf(CellName.BorrowSubtract1Tens)
                )
            }

            // [7] 2차 뺄셈 (Borrow 없음)
            steps += PhaseStep(
                phase = DivisionPhaseV2.InputSubtract2,
                editableCells = listOf(CellName.Subtract2Ones),
                highlightCells = buildList {
                    if(needsBorrowSubtract1TensInSubtract2) {
                        add(CellName.Borrowed10Subtract1Ones)
                    }
                    add(CellName.Subtract1Ones)
                    add(CellName.Multiply2Ones)
                },
                presetValues = if(needsBorrowSubtract1TensInSubtract2)
                    mapOf(CellName.Borrowed10Subtract1Ones to "10")
                else
                    emptyMap(),
                strikeThroughCells = if(needsBorrowSubtract1TensInSubtract2)
                    listOf(CellName.Subtract1Tens)
                else
                    emptyList(),
                subtractLineTargets = setOf(CellName.Subtract2Ones)
            )

            if(needs2DigitRem) {
                steps += PhaseStep(
                    phase = DivisionPhaseV2.InputSubtract2,
                    editableCells = listOf(CellName.Subtract2Tens),
                    highlightCells = listOf(CellName.Multiply2Tens, CellName.Subtract1Tens)
                )
            }
        }



        // [8] 완료 단계
        steps += PhaseStep(phase = DivisionPhaseV2.Complete)

        return PhaseSequence(
            pattern = DivisionPatternV2.ThreeByTwo,
            steps = steps
        )
    }
}
