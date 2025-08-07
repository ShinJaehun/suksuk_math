package com.shinjaehun.suksuk.domain.division.layout.sequence

import com.shinjaehun.suksuk.domain.division.layout.PhaseSequence
import com.shinjaehun.suksuk.domain.division.layout.PhaseStep
import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import javax.inject.Inject

class ThreeByTwoPhaseSequenceCreator @Inject constructor() : PhaseSequenceCreator {
    override fun create(dividend: Int, divisor: Int): PhaseSequence {
        // 예시: dividend = 432, divisor = 12
        val quotient = dividend / divisor // ex: 36
        val quotientTens = quotient / 10  // 3
        val quotientOnes = quotient % 10  // 6

        val steps = mutableListOf<PhaseStep>()

        // [1] 몫 십의 자리 입력
        steps += PhaseStep(
            phase = DivisionPhaseV2.InputQuotient,
            editableCells = listOf(CellName.QuotientTens),
            highlightCells = listOf(CellName.DividendHundreds, CellName.DividendTens, CellName.DivisorTens, CellName.DivisorOnes)
        )

        // [2] 1차 곱셈 (몫 십의 자리 × 제수) -- Carry 없음
        steps += PhaseStep(
            phase = DivisionPhaseV2.InputMultiply,
            editableCells = listOf(CellName.Multiply1Tens),
            highlightCells = listOf(CellName.QuotientTens, CellName.DivisorOnes)
        )

        steps += PhaseStep(
            phase = DivisionPhaseV2.InputMultiply,
            editableCells = listOf(CellName.Multiply1Hundreds),
            highlightCells = listOf(CellName.QuotientTens, CellName.DivisorTens)
        )

        // [3] 1차 뺄셈 (Borrow 없음)
        steps += PhaseStep(
            phase = DivisionPhaseV2.InputSubtract,
            editableCells = listOf(CellName.Subtract1Tens),
            highlightCells = listOf(CellName.DividendTens, CellName.Multiply1Tens),
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
            highlightCells = listOf(CellName.Subtract1Tens, CellName.Subtract1Ones)
        )

        // [6] 2차 곱셈 (몫 일의 자리 × 제수) -- Carry 없음
        steps += PhaseStep(
            phase = DivisionPhaseV2.InputMultiply,
            editableCells = listOf(CellName.Multiply2Ones),
            highlightCells = listOf(CellName.QuotientOnes, CellName.DivisorOnes)
        )

        steps += PhaseStep(
            phase = DivisionPhaseV2.InputMultiply,
            editableCells = listOf(CellName.Multiply2Tens),
            highlightCells = listOf(CellName.QuotientOnes, CellName.DivisorTens)
        )

        // [7] 2차 뺄셈 (Borrow 없음)
        steps += PhaseStep(
            phase = DivisionPhaseV2.InputSubtract,
            editableCells = listOf(CellName.Subtract2Ones),
            highlightCells = listOf(CellName.Subtract1Ones, CellName.Multiply2Ones),
            subtractLineTargets = setOf(CellName.Subtract2Ones)
        )

        // [8] 완료 단계
        steps += PhaseStep(phase = DivisionPhaseV2.Complete)

        return PhaseSequence(
            pattern = DivisionPatternV2.ThreeByTwo,
            steps = steps
        )
    }
}
