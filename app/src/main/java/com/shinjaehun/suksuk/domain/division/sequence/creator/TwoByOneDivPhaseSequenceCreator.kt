package com.shinjaehun.suksuk.domain.division.sequence.creator

import com.shinjaehun.suksuk.domain.division.info.DivisionStateInfo
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseSequence
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseStep
import com.shinjaehun.suksuk.domain.division.model.DivisionCellName
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import javax.inject.Inject

class TwoByOneDivPhaseSequenceCreator @Inject constructor() : DivisionPhaseSequenceCreator {
    override fun create(info: DivisionStateInfo): DivisionPhaseSequence {

        val is2DigitsMultiply = info.quotientOnes * info.divisor >= 10

        val steps = mutableListOf<DivisionPhaseStep>()

        if (info.hasTensQuotient) {
            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputQuotient,
                editableCells = listOf(DivisionCellName.QuotientTens),
                highlightCells = listOf(DivisionCellName.DividendTens, DivisionCellName.DivisorOnes)
            )

            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputMultiply1,
                editableCells = listOf(DivisionCellName.Multiply1Tens),
                highlightCells = listOf(DivisionCellName.DivisorOnes, DivisionCellName.QuotientTens),
            )

            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputSubtract1,
                editableCells = listOf(DivisionCellName.Subtract1Tens),
                highlightCells = listOf(DivisionCellName.DividendTens, DivisionCellName.Multiply1Tens),
                subtractLineTargets = setOf(DivisionCellName.Subtract1Tens)
            )

            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputBringDown,
                editableCells = listOf(DivisionCellName.Subtract1Ones),
                highlightCells = listOf(DivisionCellName.DividendOnes),
                presetValues = if (info.shouldLeaveSubtract1TensEmpty){
                    mapOf(DivisionCellName.Subtract1Tens to "")
                } else {
                    emptyMap()
                }
            )

            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputQuotient,
                editableCells = listOf(DivisionCellName.QuotientOnes),
                highlightCells = listOf(DivisionCellName.DivisorOnes, DivisionCellName.Subtract1Tens, DivisionCellName.Subtract1Ones),
            )

            if(!info.shouldBypassM2AndS2) {

                if (is2DigitsMultiply) {
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputMultiply2,
                        editableCells = listOf(DivisionCellName.Multiply2Tens, DivisionCellName.Multiply2Ones),
                        highlightCells = listOf(DivisionCellName.DivisorOnes, DivisionCellName.QuotientOnes),
                    )
                } else {
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputMultiply2,
                        editableCells = listOf(DivisionCellName.Multiply2Ones),
                        highlightCells = listOf(DivisionCellName.DivisorOnes, DivisionCellName.QuotientOnes),
                    )
                }

                if (info.performedTensBorrowInS2) {
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputBorrow,
                        editableCells = listOf(DivisionCellName.BorrowSubtract1Tens),
                        highlightCells = listOf(
                            DivisionCellName.Subtract1Tens,
                            DivisionCellName.Subtract1Ones,
                            DivisionCellName.Multiply2Ones
                        ),
                        needsBorrow = true,
                        strikeThroughCells = listOf(DivisionCellName.Subtract1Tens),
                        subtractLineTargets = setOf(DivisionCellName.BorrowSubtract1Tens)
                    )
                }

                steps += DivisionPhaseStep(
                    phase = DivisionPhaseV2.InputSubtract2,
                    editableCells = listOf(DivisionCellName.Subtract2Ones),
                    highlightCells = buildList {
                        if (info.performedTensBorrowInS2) {
                            add(DivisionCellName.Borrowed10Subtract1Ones)
                        }
                        if (info.skipTensBorrowInS2WhenTensIsOne) {
                            add(DivisionCellName.Subtract1Tens)
                        }
                        add(DivisionCellName.Subtract1Ones)
                        add(DivisionCellName.Multiply2Ones)
                    },
                    presetValues = if (info.performedTensBorrowInS2)
                        mapOf(DivisionCellName.Borrowed10Subtract1Ones to "10")
                    else
                        emptyMap(),
                    strikeThroughCells = if (info.performedTensBorrowInS2)
                        listOf(DivisionCellName.Subtract1Tens)
                    else
                        emptyList(),
                    subtractLineTargets = setOf(DivisionCellName.Subtract2Ones)
                )
            }
        } else {

            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputQuotient,
                editableCells = listOf(DivisionCellName.QuotientOnes),
                highlightCells = listOf(DivisionCellName.DividendTens, DivisionCellName.DividendOnes, DivisionCellName.DivisorOnes)
            )

            when {
                is2DigitsMultiply -> {
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(DivisionCellName.Multiply1Tens, DivisionCellName.Multiply1Ones),
                        highlightCells = listOf(DivisionCellName.DivisorOnes, DivisionCellName.QuotientOnes)
                    )
                }
                else -> {
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(DivisionCellName.Multiply1Ones),
                        highlightCells = listOf(DivisionCellName.DivisorOnes, DivisionCellName.QuotientOnes)
                    )
                }
            }

            if (info.needsTensBorrowInS1 && !info.skipTensBorrowInS1) {
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

                steps += DivisionPhaseStep(
                    phase = DivisionPhaseV2.InputSubtract1,
                    editableCells = listOf(DivisionCellName.Subtract1Ones),
                    highlightCells = listOf(DivisionCellName.Borrowed10DividendOnes, DivisionCellName.Multiply1Ones, DivisionCellName.DividendOnes),
                    presetValues = mapOf(DivisionCellName.Borrowed10DividendOnes to "10"),
                    strikeThroughCells = listOf(DivisionCellName.DividendTens),
                    subtractLineTargets = setOf(DivisionCellName.Subtract1Ones)
                )
            } else {
                steps += DivisionPhaseStep(
                    phase = DivisionPhaseV2.InputSubtract1,
                    editableCells = listOf(DivisionCellName.Subtract1Ones),
                    highlightCells = buildList {
                        if(info.skipTensBorrowInS1) add(DivisionCellName.DividendTens)
                        add(DivisionCellName.DividendOnes)
                        add(DivisionCellName.Multiply1Ones)
                    },
                    subtractLineTargets = setOf(DivisionCellName.Subtract1Ones)
                )
            }

        }

        steps += DivisionPhaseStep(phase = DivisionPhaseV2.Complete)

        return DivisionPhaseSequence(
//            pattern = DivisionPatternV2.TwoByOne,
            steps = steps
        )
    }
}