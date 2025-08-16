package com.shinjaehun.suksuk.domain.multiplication.sequence.creator

import com.shinjaehun.suksuk.domain.multiplication.info.MulStateInfo
import com.shinjaehun.suksuk.domain.multiplication.model.MulCell
import com.shinjaehun.suksuk.domain.multiplication.model.MulPhase
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseSequence
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseStep
import javax.inject.Inject

class TwoByTwoMulPhaseSequenceCreator @Inject constructor() : MulPhaseSequenceCreator {

    override fun create(info: MulStateInfo): MulPhaseSequence {
        val steps = mutableListOf<MulPhaseStep>()

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
//
//        // Hundreds (+Thousands)
//        if(info.product1Thousands > 0) {
//            steps += MulPhaseStep(
//                phase = MulPhase.InputMultiply1,
//                editableCells = listOf(MulCellName.P1Thousands, MulCellName.P1Hundreds),
//                highlightCells = listOf(
//                    MulCellName.MultiplicandHundreds,
//                    MulCellName.MultiplierOnes,
//                    MulCellName.CarryP1Hundreds
//                ),
//            )
//        } else {
//            steps += MulPhaseStep(
//                phase = MulPhase.InputMultiply1,
//                editableCells = listOf(MulCellName.P1Hundreds),
//                highlightCells = listOf(
//                    MulCellName.MultiplicandHundreds,
//                    MulCellName.MultiplierOnes,
//                    MulCellName.CarryP1Hundreds
//                ),
//            )
//        }

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

//        // Thousands (+ TenThousands )
//        if(info.product2Thousands > 0) {
//            steps += MulPhaseStep(
//                phase = MulPhase.InputMultiply2,
//                editableCells = listOf(MulCellName.P2Thousands, MulCellName.P2Hundreds),
//                highlightCells = listOf(
//                    MulCellName.MultiplicandHundreds,
//                    MulCellName.MultiplierTens,
//                    MulCellName.CarryP2Hundreds
//                ),
//            )
//        } else {
//            steps += MulPhaseStep(
//                phase = MulPhase.InputMultiply2,
//                editableCells = listOf(MulCellName.P2Thousands),
//                highlightCells = listOf(
//                    MulCellName.MultiplicandHundreds,
//                    MulCellName.MultiplierTens,
//                    MulCellName.CarryP2Hundreds
//                )
//            )
//        }

        // ---------- SUM ----------
        steps += MulPhaseStep( // Ones
            phase = MulPhase.InputSum,
            editableCells = listOf(MulCell.SumOnes),
            highlightCells = listOf(MulCell.P1Ones)
        )

        if (info.carrySumHundreds > 0) {
            steps += MulPhaseStep(
                phase = MulPhase.InputSum,
                editableCells = listOf(MulCell.CarrySumHundreds, MulCell.SumTens),
                highlightCells = listOf(MulCell.P1Tens, MulCell.P2Tens),
                needsCarry = true
            )
        } else {
            steps += MulPhaseStep(
                phase = MulPhase.InputSum,
                editableCells = listOf(MulCell.SumTens),
                highlightCells = listOf(MulCell.P1Tens, MulCell.P2Tens)
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
                needsCarry = true
            )
        } else {
            steps += MulPhaseStep(
                phase = MulPhase.InputSum,
                editableCells = listOf(MulCell.SumHundreds),
                highlightCells = listOf(
                    MulCell.P1Hundreds,
                    MulCell.P2Hundreds,
                    MulCell.CarrySumHundreds
                )
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
            )
        }

        steps += MulPhaseStep(phase = MulPhase.Complete)

        return MulPhaseSequence(
            steps = steps
        )
    }
}