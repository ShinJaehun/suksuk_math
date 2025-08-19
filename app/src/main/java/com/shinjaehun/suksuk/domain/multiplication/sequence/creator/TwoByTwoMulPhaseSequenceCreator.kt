package com.shinjaehun.suksuk.domain.multiplication.sequence.creator

import com.shinjaehun.suksuk.domain.multiplication.info.MulStateInfo
import com.shinjaehun.suksuk.domain.multiplication.model.MulCell
import com.shinjaehun.suksuk.domain.multiplication.model.MulPhase
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseSequence
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseStep
import com.shinjaehun.suksuk.domain.pattern.MulPattern
import javax.inject.Inject

class TwoByTwoMulPhaseSequenceCreator @Inject constructor() : MulPhaseSequenceCreator {

    override fun create(info: MulStateInfo): MulPhaseSequence {
        val steps = mutableListOf<MulPhaseStep>()

        if(info.isMultiplierOnesZero) {
            steps += MulPhaseStep(
                phase = MulPhase.InputMultiply1,
                editableCells = listOf(MulCell.P1Ones),
                highlightCells = listOf(MulCell.MultiplicandOnes, MulCell.MultiplierOnes)
            )

            if (info.carryP2Tens > 0) {
                steps += MulPhaseStep(
                    phase = MulPhase.InputMultiply1,
                    editableCells = listOf(MulCell.CarryP1Tens, MulCell.P1Tens),
                    highlightCells = listOf(MulCell.MultiplicandOnes, MulCell.MultiplierTens)
                )
            } else {
                steps += MulPhaseStep(
                    phase = MulPhase.InputMultiply1,
                    editableCells = listOf(MulCell.P1Tens),
                    highlightCells = listOf(MulCell.MultiplicandOnes, MulCell.MultiplierTens)
                )
            }

            if (info.carryP2Hundreds > 0) {
                steps += MulPhaseStep(
                    phase = MulPhase.InputMultiply1,
                    editableCells = listOf(MulCell.P1Thousands, MulCell.P1Hundreds),
                    highlightCells = listOf(MulCell.MultiplicandTens, MulCell.MultiplierTens, MulCell.CarryP1Tens)
                )
            } else {
                steps += MulPhaseStep(
                    phase = MulPhase.InputMultiply1,
                    editableCells = listOf(MulCell.P1Hundreds),
                    highlightCells = listOf(MulCell.MultiplicandTens, MulCell.MultiplierTens)
                )
            }
        } else {

            if (info.carryP1Tens > 0) {
                steps += MulPhaseStep(
                    phase = MulPhase.InputMultiply1,
                    editableCells = listOf(MulCell.CarryP1Tens, MulCell.P1Ones),
                    highlightCells = listOf(MulCell.MultiplicandOnes, MulCell.MultiplierOnes),
                    needsCarry = true
                )
            } else {
                steps += MulPhaseStep(
                    phase = MulPhase.InputMultiply1,
                    editableCells = listOf(MulCell.P1Ones),
                    highlightCells = listOf(MulCell.MultiplicandOnes, MulCell.MultiplierOnes)
                )
            }

            // Tens
            if (info.carryP1Hundreds > 0) {
                steps += MulPhaseStep(
                    phase = MulPhase.InputMultiply1,
                    editableCells = listOf(MulCell.P1Hundreds, MulCell.P1Tens),
                    highlightCells = listOf(
                        MulCell.MultiplicandTens,
                        MulCell.MultiplierOnes,
                        MulCell.CarryP1Tens
                    ),
                    needsCarry = true
                )
            } else {
                steps += MulPhaseStep(
                    phase = MulPhase.InputMultiply1,
                    editableCells = listOf(MulCell.P1Tens),
                    highlightCells = listOf(MulCell.MultiplicandTens, MulCell.MultiplierOnes)
                )
            }

            steps += MulPhaseStep(
                phase = MulPhase.PrepareNextOp,
                editableCells = emptyList(),
                clearCells = setOf(MulCell.CarryP1Tens)
            )

            if (info.carryP2Tens > 0) {
                steps += MulPhaseStep(
                    phase = MulPhase.InputMultiply2,
                    editableCells = listOf(MulCell.CarryP2Tens, MulCell.P2Tens),
                    highlightCells = listOf(MulCell.MultiplicandOnes, MulCell.MultiplierTens),
                    needsCarry = true
                )
            } else {
                steps += MulPhaseStep(
                    phase = MulPhase.InputMultiply2,
                    editableCells = listOf(MulCell.P2Tens),
                    highlightCells = listOf(MulCell.MultiplicandOnes, MulCell.MultiplierTens)
                )
            }

            // Hundreds
            if (info.carryP2Hundreds > 0) {
                steps += MulPhaseStep(
                    phase = MulPhase.InputMultiply2,
                    editableCells = listOf(MulCell.P2Thousands, MulCell.P2Hundreds),
                    highlightCells = listOf(
                        MulCell.MultiplicandTens,
                        MulCell.MultiplierTens,
                        MulCell.CarryP2Tens
                    ),
                    needsCarry = true
                )
            } else {
                steps += MulPhaseStep(
                    phase = MulPhase.InputMultiply2,
                    editableCells = listOf(MulCell.P2Hundreds),
                    highlightCells = listOf(MulCell.MultiplicandTens, MulCell.MultiplierTens)
                )
            }

            // ---------- SUM ----------
            steps += MulPhaseStep( // Ones
                phase = MulPhase.InputSum,
                editableCells = listOf(MulCell.SumOnes),
                highlightCells = listOf(MulCell.P1Ones),
                totalLineTargets = setOf(MulCell.SumOnes)
            )

            if (info.carrySumHundreds > 0) {
                steps += MulPhaseStep(
                    phase = MulPhase.InputSum,
                    editableCells = listOf(MulCell.CarrySumHundreds, MulCell.SumTens),
                    highlightCells = listOf(MulCell.P1Tens, MulCell.P2Tens),
                    needsCarry = true,
                    totalLineTargets = setOf(MulCell.SumTens)
                )
            } else {
                steps += MulPhaseStep(
                    phase = MulPhase.InputSum,
                    editableCells = listOf(MulCell.SumTens),
                    highlightCells = listOf(MulCell.P1Tens, MulCell.P2Tens),
                    totalLineTargets = setOf(MulCell.SumTens)
                )
            }

            if (info.carrySumThousands > 0) {
                steps += MulPhaseStep(
                    phase = MulPhase.InputSum,
                    editableCells = listOf(MulCell.CarrySumThousands, MulCell.SumHundreds),
                    highlightCells = listOf(
                        MulCell.P1Hundreds,
                        MulCell.P2Hundreds,
                        MulCell.CarrySumHundreds
                    ),
                    needsCarry = true,
                    totalLineTargets = setOf(MulCell.SumHundreds)
                )
            } else {
                steps += MulPhaseStep(
                    phase = MulPhase.InputSum,
                    editableCells = listOf(MulCell.SumHundreds),
                    highlightCells = listOf(
                        MulCell.P1Hundreds,
                        MulCell.P2Hundreds,
                        MulCell.CarrySumHundreds
                    ),
                    totalLineTargets = setOf(MulCell.SumHundreds)
                )
            }

            // Thousands
            if (info.sumThousands > 0) {
                steps += MulPhaseStep(
                    phase = MulPhase.InputSum,
                    editableCells = listOf(MulCell.SumThousands),
                    highlightCells = listOf(
                        MulCell.P1Thousands,
                        MulCell.P2Thousands,
                        MulCell.CarrySumThousands
                    ),
                    totalLineTargets = setOf(MulCell.SumThousands)
                )
            }
        }


        steps += MulPhaseStep(phase = MulPhase.Complete)

        return MulPhaseSequence(
            steps = steps,
            pattern = MulPattern.TwoByTwo
        )
    }
}