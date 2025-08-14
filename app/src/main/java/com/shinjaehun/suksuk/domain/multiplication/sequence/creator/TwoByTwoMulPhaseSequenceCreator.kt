package com.shinjaehun.suksuk.domain.multiplication.sequence.creator

import com.shinjaehun.suksuk.domain.multiplication.info.MulStateInfo
import com.shinjaehun.suksuk.domain.multiplication.model.MulCellName
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
                editableCells = listOf(MulCellName.CarryP1Tens, MulCellName.P1Ones),
                highlightCells = listOf(MulCellName.MultiplicandOnes, MulCellName.MultiplierOnes),
                needsCarry = true
            )
        } else {
           steps += MulPhaseStep(
               phase = MulPhase.InputMultiply1,
               editableCells = listOf(MulCellName.P1Ones),
               highlightCells = listOf(MulCellName.MultiplicandOnes, MulCellName.MultiplierOnes)
           )
        }

        // Tens
        if (info.carryP1Hundreds > 0) {
            steps += MulPhaseStep(
                phase = MulPhase.InputMultiply1,
                editableCells = listOf(MulCellName.P1Hundreds, MulCellName.P1Tens),
                highlightCells = listOf(
                    MulCellName.MultiplicandTens,
                    MulCellName.MultiplierOnes,
                    MulCellName.CarryP1Tens
                ),
                needsCarry = true
            )
        } else {
            steps += MulPhaseStep(
                phase = MulPhase.InputMultiply1,
                editableCells = listOf(MulCellName.P1Tens),
                highlightCells = listOf(MulCellName.MultiplicandTens, MulCellName.MultiplierOnes)
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

        if (info.carryP2Tens > 0) {
            steps += MulPhaseStep(
                phase = MulPhase.InputMultiply2,
                editableCells = listOf(MulCellName.CarryP2Tens, MulCellName.P2Tens),
                highlightCells = listOf(MulCellName.MultiplicandOnes, MulCellName.MultiplierTens),
                needsCarry = true
            )
        } else {
            steps += MulPhaseStep(
                phase = MulPhase.InputMultiply2,
                editableCells = listOf(MulCellName.P2Tens),
                highlightCells = listOf(MulCellName.MultiplicandOnes, MulCellName.MultiplierTens)
            )
        }

        // Hundreds
        if (info.carryP2Hundreds > 0) {
            steps += MulPhaseStep(
                phase = MulPhase.InputMultiply2,
                editableCells = listOf(MulCellName.P2Thousands, MulCellName.P2Hundreds),
                highlightCells = listOf(
                    MulCellName.MultiplicandTens,
                    MulCellName.MultiplierTens,
                    MulCellName.CarryP2Tens
                ),
                needsCarry = true
            )
        } else {
            steps += MulPhaseStep(
                phase = MulPhase.InputMultiply2,
                editableCells = listOf(MulCellName.P2Hundreds),
                highlightCells = listOf(MulCellName.MultiplicandTens, MulCellName.MultiplierTens)
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
            editableCells = listOf(MulCellName.SumOnes),
            highlightCells = listOf(MulCellName.P1Ones)
        )

        if (info.carrySumHundreds > 0) {
            steps += MulPhaseStep(
                phase = MulPhase.InputSum,
                editableCells = listOf(MulCellName.CarrySumHundreds, MulCellName.SumTens),
                highlightCells = listOf(MulCellName.P1Tens, MulCellName.P2Tens),
                needsCarry = true
            )
        } else {
            steps += MulPhaseStep(
                phase = MulPhase.InputSum,
                editableCells = listOf(MulCellName.SumTens),
                highlightCells = listOf(MulCellName.P1Tens, MulCellName.P2Tens)
            )
        }

        if (info.carrySumThousands > 0) {
            steps += MulPhaseStep(
                phase = MulPhase.InputSum,
                editableCells = listOf(MulCellName.CarrySumThousands, MulCellName.SumHundreds),
                highlightCells = listOf(
                    MulCellName.P1Hundreds,
                    MulCellName.P2Hundreds,
                    MulCellName.CarrySumHundreds
                ),
                needsCarry = true
            )
        } else {
            steps += MulPhaseStep(
                phase = MulPhase.InputSum,
                editableCells = listOf(MulCellName.SumHundreds),
                highlightCells = listOf(
                    MulCellName.P1Hundreds,
                    MulCellName.P2Hundreds,
                    MulCellName.CarrySumHundreds
                )
            )
        }

        // Thousands
        if (info.sumThousands > 0) {
            steps += MulPhaseStep(
                phase = MulPhase.InputSum,
                editableCells = listOf(MulCellName.SumThousands),
                highlightCells = listOf(
                    MulCellName.P1Thousands,
                    MulCellName.P2Thousands,
                    MulCellName.CarrySumThousands
                ),
            )
        }

        steps += MulPhaseStep(phase = MulPhase.Complete)

        return MulPhaseSequence(
            steps = steps
        )
    }
}