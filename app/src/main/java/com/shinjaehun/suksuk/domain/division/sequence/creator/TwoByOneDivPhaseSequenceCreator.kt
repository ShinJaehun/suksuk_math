package com.shinjaehun.suksuk.domain.division.sequence.creator

import com.shinjaehun.suksuk.domain.division.info.DivisionStateInfo
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseSequence
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseStep
import com.shinjaehun.suksuk.domain.division.model.DivisionCell
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import com.shinjaehun.suksuk.domain.pattern.DivisionPatternV2
import javax.inject.Inject

class TwoByOneDivPhaseSequenceCreator @Inject constructor() : DivisionPhaseSequenceCreator {
    override fun create(info: DivisionStateInfo): DivisionPhaseSequence {

        val is2DigitsMultiply = info.quotientOnes * info.divisor >= 10

        val steps = mutableListOf<DivisionPhaseStep>()

        if (info.hasTensQuotient) {
            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputQuotient,
                editableCells = listOf(DivisionCell.QuotientTens),
                highlightCells = listOf(DivisionCell.DividendTens, DivisionCell.DivisorOnes)
            )

            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputMultiply1,
                editableCells = listOf(DivisionCell.Multiply1Tens),
                highlightCells = listOf(DivisionCell.DivisorOnes, DivisionCell.QuotientTens),
            )

            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputSubtract1,
                editableCells = listOf(DivisionCell.Subtract1Tens),
                highlightCells = listOf(DivisionCell.DividendTens, DivisionCell.Multiply1Tens),
                subtractLineTargets = setOf(DivisionCell.Subtract1Tens)
            )

            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputBringDown,
                editableCells = listOf(DivisionCell.Subtract1Ones),
                highlightCells = listOf(DivisionCell.DividendOnes),
                presetValues = if (info.shouldLeaveSubtract1TensEmpty){
                    mapOf(DivisionCell.Subtract1Tens to "")
                } else {
                    emptyMap()
                }
            )

            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputQuotient,
                editableCells = listOf(DivisionCell.QuotientOnes),
                highlightCells = listOf(DivisionCell.DivisorOnes, DivisionCell.Subtract1Tens, DivisionCell.Subtract1Ones),
            )

            if(!info.shouldBypassM2AndS2) {

                if (is2DigitsMultiply) {
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputMultiply2,
                        editableCells = listOf(DivisionCell.Multiply2Tens, DivisionCell.Multiply2Ones),
                        highlightCells = listOf(DivisionCell.DivisorOnes, DivisionCell.QuotientOnes),
                    )
                } else {
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputMultiply2,
                        editableCells = listOf(DivisionCell.Multiply2Ones),
                        highlightCells = listOf(DivisionCell.DivisorOnes, DivisionCell.QuotientOnes),
                    )
                }

                if (info.performedTensBorrowInS2) {
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputBorrow,
                        editableCells = listOf(DivisionCell.BorrowSubtract1Tens),
                        highlightCells = listOf(
                            DivisionCell.Subtract1Tens,
                            DivisionCell.Subtract1Ones,
                            DivisionCell.Multiply2Ones
                        ),
                        needsBorrow = true,
                        strikeThroughCells = listOf(DivisionCell.Subtract1Tens),
                        subtractLineTargets = setOf(DivisionCell.BorrowSubtract1Tens)
                    )
                }

                steps += DivisionPhaseStep(
                    phase = DivisionPhaseV2.InputSubtract2,
                    editableCells = listOf(DivisionCell.Subtract2Ones),
                    highlightCells = buildList {
                        if (info.performedTensBorrowInS2) {
                            add(DivisionCell.Borrowed10Subtract1Ones)
                        }
                        if (info.skipTensBorrowInS2WhenTensIsOne) {
                            add(DivisionCell.Subtract1Tens)
                        }
                        add(DivisionCell.Subtract1Ones)
                        add(DivisionCell.Multiply2Ones)
                    },
                    presetValues = if (info.performedTensBorrowInS2)
                        mapOf(DivisionCell.Borrowed10Subtract1Ones to "10")
                    else
                        emptyMap(),
                    strikeThroughCells = if (info.performedTensBorrowInS2)
                        listOf(DivisionCell.Subtract1Tens)
                    else
                        emptyList(),
                    subtractLineTargets = setOf(DivisionCell.Subtract2Ones)
                )
            }
        } else {

            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputQuotient,
                editableCells = listOf(DivisionCell.QuotientOnes),
                highlightCells = listOf(DivisionCell.DividendTens, DivisionCell.DividendOnes, DivisionCell.DivisorOnes)
            )

            when {
                is2DigitsMultiply -> {
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(DivisionCell.Multiply1Tens, DivisionCell.Multiply1Ones),
                        highlightCells = listOf(DivisionCell.DivisorOnes, DivisionCell.QuotientOnes)
                    )
                }
                else -> {
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(DivisionCell.Multiply1Ones),
                        highlightCells = listOf(DivisionCell.DivisorOnes, DivisionCell.QuotientOnes)
                    )
                }
            }

            if (info.needsTensBorrowInS1 && !info.skipTensBorrowInS1) {
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

                steps += DivisionPhaseStep(
                    phase = DivisionPhaseV2.InputSubtract1,
                    editableCells = listOf(DivisionCell.Subtract1Ones),
                    highlightCells = listOf(DivisionCell.Borrowed10DividendOnes, DivisionCell.Multiply1Ones, DivisionCell.DividendOnes),
                    presetValues = mapOf(DivisionCell.Borrowed10DividendOnes to "10"),
                    strikeThroughCells = listOf(DivisionCell.DividendTens),
                    subtractLineTargets = setOf(DivisionCell.Subtract1Ones)
                )
            } else {
                steps += DivisionPhaseStep(
                    phase = DivisionPhaseV2.InputSubtract1,
                    editableCells = listOf(DivisionCell.Subtract1Ones),
                    highlightCells = buildList {
                        if(info.skipTensBorrowInS1) add(DivisionCell.DividendTens)
                        add(DivisionCell.DividendOnes)
                        add(DivisionCell.Multiply1Ones)
                    },
                    subtractLineTargets = setOf(DivisionCell.Subtract1Ones)
                )
            }

        }

        steps += DivisionPhaseStep(phase = DivisionPhaseV2.Complete)

        return DivisionPhaseSequence(
            steps = steps,
            pattern = DivisionPatternV2.TwoByOne
        )
    }
}