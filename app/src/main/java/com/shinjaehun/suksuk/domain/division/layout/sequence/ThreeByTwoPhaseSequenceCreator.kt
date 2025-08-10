package com.shinjaehun.suksuk.domain.division.layout.sequence

import com.shinjaehun.suksuk.domain.division.DivisionStateInfo
import com.shinjaehun.suksuk.domain.division.layout.PhaseSequence
import com.shinjaehun.suksuk.domain.division.layout.PhaseStep
import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import javax.inject.Inject

class ThreeByTwoPhaseSequenceCreator @Inject constructor() : PhaseSequenceCreator {
    override fun create(info: DivisionStateInfo): PhaseSequence {

        val steps = mutableListOf<PhaseStep>()

        if (info.needsTensQuotient) {
            // [1] 몫 십의 자리 입력
            steps += PhaseStep(
                phase = DivisionPhaseV2.InputQuotient,
                editableCells = listOf(CellName.QuotientTens),
                highlightCells = listOf(CellName.DividendHundreds, CellName.DividendTens, CellName.DivisorTens, CellName.DivisorOnes)
            )

            // [2] 1차 곱셈 (몫 십의 자리 × 제수) -- Carry 없음
            if(info.needsCarryInMultiply1){
                steps += PhaseStep(
                    phase = DivisionPhaseV2.InputMultiply1,
                    editableCells = listOf(CellName.CarryDivisorTensMul1, CellName.Multiply1Tens),
                    highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientTens),
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
                highlightCells = buildList {
                    if(info.needsCarryInMultiply1) {
                        add(CellName.CarryDivisorTensMul1)
                    }
                    add(CellName.QuotientTens)
                    add(CellName.DivisorTens)
                }
            )

            if(info.needsBorrowFromDividendHundredsInSubtract1) {
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
                    if(info.needsBorrowFromDividendHundredsInSubtract1){
                        add(CellName.Borrowed10DividendTens)
                    }
                    add(CellName.DividendTens)
                    add(CellName.Multiply1Tens)
                },
                presetValues = if (info.needsBorrowFromDividendHundredsInSubtract1)
                    mapOf(CellName.Borrowed10DividendTens to "10")
                else
                    emptyMap(),
                strikeThroughCells = if(info.needsBorrowFromDividendHundredsInSubtract1)
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
                presetValues = if(info.needsEmptySubtract1Tens){
                    mapOf(CellName.Subtract1Tens to "")
                } else {
                    emptyMap()
                }
            )

            // [5] 몫 일의 자리 입력
            steps += PhaseStep(
                phase = DivisionPhaseV2.InputQuotient,
                editableCells = listOf(CellName.QuotientOnes),
                highlightCells = listOf(CellName.Subtract1Tens, CellName.Subtract1Ones),
                presetValues = if(info.needsCarryInMultiply1)
                    mapOf(CellName.CarryDivisorTensMul1 to "")
                else
                    emptyMap(),
            )

            // [6] 2차 곱셈 (몫 일의 자리 × 제수)
            if(!info.needsSkipMultiply2AndSubtract2) {

                if(info.needsCarryInMultiply2){
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputMultiply2,
                        editableCells = listOf(CellName.CarryDivisorTensMul2, CellName.Multiply2Ones),
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
                    highlightCells = buildList {
                        if(info.needsCarryInMultiply2) {
                            add(CellName.CarryDivisorTensMul2)
                        }
                        add(CellName.QuotientOnes)
                        add(CellName.DivisorTens)
                    }
                )

                if(info.needsBorrowFromSubtract1TensInSubtract2){
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
                        if(info.needsBorrowFromSubtract1TensInSubtract2) {
                            add(CellName.Borrowed10Subtract1Ones)
                        }
                        add(CellName.Subtract1Ones)
                        add(CellName.Multiply2Ones)
                    },
                    presetValues = if(info.needsBorrowFromSubtract1TensInSubtract2)
                        mapOf(CellName.Borrowed10Subtract1Ones to "10")
                    else
                        emptyMap(),
                    strikeThroughCells = if(info.needsBorrowFromSubtract1TensInSubtract2)
                        listOf(CellName.Subtract1Tens)
                    else
                        emptyList(),
                    subtractLineTargets = setOf(CellName.Subtract2Ones)
                )

                if(info.needs2DigitRem) {
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputSubtract2,
                        editableCells = listOf(CellName.Subtract2Tens),
                        highlightCells = buildList {
                            if (info.needsBorrowFromSubtract1TensInSubtract2) {
                                add(CellName.BorrowSubtract1Tens)
                            } else {
                                add(CellName.Subtract1Tens)
                            }
                            add(CellName.Multiply2Tens)
                        }
                    )
                }
            }
        }

        // [8] 완료 단계
        steps += PhaseStep(phase = DivisionPhaseV2.Complete)

        steps.forEachIndexed { idx, step ->
            println("[$idx] phase=${step.phase}, editableCells=${step.editableCells}")
        }

        return PhaseSequence(
            pattern = DivisionPatternV2.ThreeByTwo,
            steps = steps
        )
    }
}
