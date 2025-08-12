package com.shinjaehun.suksuk.domain.division.layout.sequence

import com.shinjaehun.suksuk.domain.division.DivisionStateInfo
import com.shinjaehun.suksuk.domain.division.layout.PhaseSequence
import com.shinjaehun.suksuk.domain.division.layout.PhaseStep
import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import javax.inject.Inject

class TwoByOnePhaseSequenceCreator @Inject constructor() : PhaseSequenceCreator {
    override fun create(info: DivisionStateInfo): PhaseSequence {

        val is2DigitsMultiply2 = info.quotientOnes * info.divisor >= 10

        val isBorrowRequiredInSubtract1 = info.dividendOnes < (info.multiplyQuotientOnes % 10)
        val shouldSkipBorrowInSubtract2 =
            (info.subtract1TensOnly == 1) && isBorrowRequiredInSubtract1
        val isBorrowPerformedInSubtract2 =
            isBorrowRequiredInSubtract1 && !shouldSkipBorrowInSubtract2
        val shouldSkipBorrowInSubtract1 =
            (info.dividendTens == 1) && isBorrowRequiredInSubtract1

        val steps = mutableListOf<PhaseStep>()

        if (info.hasTensQuotient) {
            steps += PhaseStep(
                phase = DivisionPhaseV2.InputQuotient,
                editableCells = listOf(CellName.QuotientTens),
                highlightCells = listOf(CellName.DividendTens, CellName.DivisorOnes)
            )

            steps += PhaseStep(
                phase = DivisionPhaseV2.InputMultiply1,
                editableCells = listOf(CellName.Multiply1Tens),
                highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientTens),
            )

            steps += PhaseStep(
                phase = DivisionPhaseV2.InputSubtract1,
                editableCells = listOf(CellName.Subtract1Tens),
                highlightCells = listOf(CellName.DividendTens, CellName.Multiply1Tens),
                subtractLineTargets = setOf(CellName.Subtract1Tens)
            )

            steps += PhaseStep(
                phase = DivisionPhaseV2.InputBringDown,
                editableCells = listOf(CellName.Subtract1Ones),
                highlightCells = listOf(CellName.DividendOnes),
                presetValues = if (info.isEmptySubtract1Tens){
                    mapOf(CellName.Subtract1Tens to "")
                } else {
                    emptyMap()
                }
            )

            steps += PhaseStep(
                phase = DivisionPhaseV2.InputQuotient,
                editableCells = listOf(CellName.QuotientOnes),
                highlightCells = listOf(CellName.DivisorOnes, CellName.Subtract1Tens, CellName.Subtract1Ones),
            )

            if(!info.shouldBypassM2AndS2) {

                if (is2DigitsMultiply2) {
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputMultiply2,
                        editableCells = listOf(CellName.Multiply2Tens, CellName.Multiply2Ones),
                        highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientOnes),
                    )
                } else {
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputMultiply2,
                        editableCells = listOf(CellName.Multiply2Ones),
                        highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientOnes),
                    )
                }

                if (isBorrowPerformedInSubtract2) {
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputBorrow,
                        editableCells = listOf(CellName.BorrowSubtract1Tens),
                        highlightCells = listOf(CellName.Subtract1Tens),
                        needsBorrow = true,
                        strikeThroughCells = listOf(CellName.Subtract1Tens),
                        subtractLineTargets = setOf(CellName.BorrowSubtract1Tens)
                    )
                }

                steps += PhaseStep(
                    phase = DivisionPhaseV2.InputSubtract2,
                    editableCells = listOf(CellName.Subtract2Ones),
                    highlightCells = buildList {
                        if (isBorrowPerformedInSubtract2) {
                            add(CellName.Borrowed10Subtract1Ones)
                        }
                        if (shouldSkipBorrowInSubtract2) {
                            add(CellName.Subtract1Tens)
                        }
                        add(CellName.Subtract1Ones)
                        add(CellName.Multiply2Ones)
                    },
                    presetValues = if (isBorrowPerformedInSubtract2)
                        mapOf(CellName.Borrowed10Subtract1Ones to "10")
                    else
                        emptyMap(),
                    strikeThroughCells = if (isBorrowPerformedInSubtract2)
                        listOf(CellName.Subtract1Tens)
                    else
                        emptyList(),
                    subtractLineTargets = setOf(CellName.Subtract2Ones)
                )
            }
        } else {

            steps += PhaseStep(
                phase = DivisionPhaseV2.InputQuotient,
                editableCells = listOf(CellName.QuotientOnes),
                highlightCells = listOf(CellName.DividendTens, CellName.DividendOnes, CellName.DivisorOnes)
            )

            when {
                is2DigitsMultiply2 -> {
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(CellName.Multiply1Tens, CellName.Multiply1Ones),
                        highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientOnes)
                    )
                }
                else -> {
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(CellName.Multiply1Ones),
                        highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientOnes)
                    )
                }
            }

            if (isBorrowRequiredInSubtract1 && !shouldSkipBorrowInSubtract1) {
                steps += PhaseStep(
                    phase = DivisionPhaseV2.InputBorrow,
                    editableCells = listOf(CellName.BorrowDividendTens),
                    highlightCells = listOf(CellName.DividendTens),
                    needsBorrow = true,
                    strikeThroughCells = listOf(CellName.DividendTens),
                    subtractLineTargets = setOf(CellName.BorrowDividendTens)
                )

                steps += PhaseStep(
                    phase = DivisionPhaseV2.InputSubtract1,
                    editableCells = listOf(CellName.Subtract1Ones),
                    highlightCells = listOf(CellName.Borrowed10DividendOnes, CellName.Multiply1Ones, CellName.DividendOnes),
                    presetValues = mapOf(CellName.Borrowed10DividendOnes to "10"),
                    strikeThroughCells = listOf(CellName.DividendTens),
                )
            } else {
                steps += PhaseStep(
                    phase = DivisionPhaseV2.InputSubtract1,
                    editableCells = listOf(CellName.Subtract1Ones),
//                    highlightCells = listOf(CellName.DividendOnes, CellName.Multiply1Ones),
                    highlightCells = buildList {
                        if(shouldSkipBorrowInSubtract1) add(CellName.DividendTens)
                        add(CellName.DividendOnes)
                        add(CellName.Multiply1Ones)
                    },
                    subtractLineTargets = setOf(CellName.Subtract1Ones)
                )
            }

        }

        steps += PhaseStep(phase = DivisionPhaseV2.Complete)

        return PhaseSequence(
            pattern = DivisionPatternV2.TwoByOne,
            steps = steps
        )
    }
}