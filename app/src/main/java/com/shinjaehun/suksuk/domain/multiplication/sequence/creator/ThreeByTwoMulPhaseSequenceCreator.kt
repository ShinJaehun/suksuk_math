package com.shinjaehun.suksuk.domain.multiplication.sequence.creator

import com.shinjaehun.suksuk.domain.multiplication.info.MulStateInfo
import com.shinjaehun.suksuk.domain.multiplication.model.MulCell
import com.shinjaehun.suksuk.domain.multiplication.model.MulPhase
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseSequence
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseStep
import com.shinjaehun.suksuk.domain.pattern.MulPattern
import javax.inject.Inject

class ThreeByTwoMulPhaseSequenceCreator @Inject constructor() : MulPhaseSequenceCreator {

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
                    editableCells = listOf(MulCell.CarryP1Hundreds, MulCell.P1Hundreds),
                    highlightCells = listOf(MulCell.MultiplicandTens, MulCell.MultiplierTens, MulCell.CarryP1Tens)
                )
            } else {
                steps += MulPhaseStep(
                    phase = MulPhase.InputMultiply1,
                    editableCells = listOf(MulCell.P1Hundreds),
                    highlightCells = listOf(MulCell.MultiplicandTens, MulCell.MultiplierTens)
                )
            }

            if (info.product2TenThousands > 0) {
                steps += MulPhaseStep(
                    phase = MulPhase.InputMultiply1,
                    editableCells = listOf(MulCell.P1TenThousands, MulCell.P1Thousands),
                    highlightCells = listOf(MulCell.MultiplicandHundreds, MulCell.MultiplierTens, MulCell.CarryP1Hundreds)
                )
            } else {
                steps += MulPhaseStep(
                    phase = MulPhase.InputMultiply1,
                    editableCells = listOf(MulCell.P1Thousands),
                    highlightCells = listOf(MulCell.MultiplicandHundreds, MulCell.MultiplierTens, MulCell.CarryP1Hundreds)
                )
            }

        } else {
            // ---------- PRODUCT #1 (× Multiplier Ones) ----------
            // Ones
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
                    highlightCells = listOf(MulCell.MultiplicandOnes, MulCell.MultiplierOnes),
                )
            }

            // Tens
            if (info.carryP1Hundreds > 0) {
                steps += MulPhaseStep(
                    phase = MulPhase.InputMultiply1,
                    editableCells = listOf(MulCell.CarryP1Hundreds, MulCell.P1Tens),
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
                    highlightCells = listOf(
                        MulCell.MultiplicandTens,
                        MulCell.MultiplierOnes,
                        MulCell.CarryP1Tens
                    )
                )
            }

            // Hundreds (+Thousands)
            if (info.product1Thousands > 0) {
                steps += MulPhaseStep(
                    phase = MulPhase.InputMultiply1,
                    editableCells = listOf(MulCell.P1Thousands, MulCell.P1Hundreds),
                    highlightCells = listOf(
                        MulCell.MultiplicandHundreds,
                        MulCell.MultiplierOnes,
                        MulCell.CarryP1Hundreds
                    )
                )
            } else {
                steps += MulPhaseStep(
                    phase = MulPhase.InputMultiply1,
                    editableCells = listOf(MulCell.P1Hundreds),
                    highlightCells = listOf(
                        MulCell.MultiplicandHundreds,
                        MulCell.MultiplierOnes,
                        MulCell.CarryP1Hundreds
                    )
                )
            }

            // 다음 연산 전 캐리 정리
            steps += MulPhaseStep(
                phase = MulPhase.PrepareNextOp,
                editableCells = emptyList(),
                clearCells = setOf(MulCell.CarryP1Tens, MulCell.CarryP1Hundreds)
            )

            // ---------- PRODUCT #2 (× Multiplier Tens; 1 자리 시프트 정렬) ----------
            // Tens
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
                    highlightCells = listOf(MulCell.MultiplicandOnes, MulCell.MultiplierTens),
                )
            }

            // Hundreds
            if (info.carryP2Hundreds > 0) {
                steps += MulPhaseStep(
                    phase = MulPhase.InputMultiply2,
                    editableCells = listOf(MulCell.CarryP2Hundreds, MulCell.P2Hundreds),
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
                    highlightCells = listOf(
                        MulCell.MultiplicandTens,
                        MulCell.MultiplierTens,
                        MulCell.CarryP2Tens
                    )
                )
            }

            // Thousands (+ TenThousands)
            if (info.product2TenThousands > 0) {
                steps += MulPhaseStep(
                    phase = MulPhase.InputMultiply2,
                    editableCells = listOf(MulCell.P2TenThousands, MulCell.P2Thousands),
                    highlightCells = listOf(
                        MulCell.MultiplicandHundreds,
                        MulCell.MultiplierTens,
                        MulCell.CarryP2Hundreds
                    )
                )
            } else {
                steps += MulPhaseStep(
                    phase = MulPhase.InputMultiply2,
                    editableCells = listOf(MulCell.P2Thousands),
                    highlightCells = listOf(
                        MulCell.MultiplicandHundreds,
                        MulCell.MultiplierTens,
                        MulCell.CarryP2Hundreds
                    )
                )
            }

            // ---------- SUM ----------
            // Ones
            steps += MulPhaseStep(
                phase = MulPhase.InputSum,
                editableCells = listOf(MulCell.SumOnes),
                highlightCells = listOf(MulCell.P1Ones),
                totalLineTargets = setOf(MulCell.SumOnes)
            )

            // Tens
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

            // Hundreds
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

            // Thousands (carry into thousands)
//            if (info.carrySumTenThousands > 0) {
//                steps += MulPhaseStep(
//                    phase = MulPhase.InputSum,
//                    editableCells = listOf(MulCell.CarrySumTenThousands, MulCell.SumThousands),
//                    highlightCells = listOf(
//                        MulCell.P1Thousands,
//                        MulCell.P2Thousands,
//                        MulCell.CarrySumThousands
//                    ),
//                    totalLineTargets = setOf(MulCell.SumThousands)
//                )
//            } else {
//                // TenThousands가 0이면 Thousands에서 종료
//                steps += MulPhaseStep(
//                    phase = MulPhase.InputSum,
//                    editableCells = listOf(MulCell.SumThousands),
//                    highlightCells = listOf(
//                        MulCell.P1Thousands,
//                        MulCell.P2Thousands,
//                        MulCell.CarrySumThousands
//                    ),
//                    totalLineTargets = setOf(MulCell.SumThousands)
//                )
//            }
//
//            if (info.sumTenThousands > 0) {
//                steps += MulPhaseStep(
//                    phase = MulPhase.InputSum,
//                    editableCells = listOf(MulCell.SumTenThousands),
//                    highlightCells = listOf(
//                        MulCell.P2TenThousands,
//                        MulCell.CarrySumTenThousands
//                    )
//                )
//            }

            if (info.carrySumTenThousands > 0) {
                if(info.product2TenThousands > 0) {
                    steps += MulPhaseStep(
                        phase = MulPhase.InputSum,
                        editableCells = listOf(MulCell.CarrySumTenThousands, MulCell.SumThousands),
                        highlightCells = listOf(
                            MulCell.P1Thousands,
                            MulCell.P2Thousands,
                            MulCell.CarrySumThousands
                        ),
                        totalLineTargets = setOf(MulCell.SumThousands)
                    )

                    if (info.sumTenThousands > 0) {
                        steps += MulPhaseStep(
                            phase = MulPhase.InputSum,
                            editableCells = listOf(MulCell.SumTenThousands),
                            highlightCells = listOf(
                                MulCell.P2TenThousands,
                                MulCell.CarrySumTenThousands
                            )
                        )
                    }
                } else {
                    steps += MulPhaseStep(
                        phase = MulPhase.InputSum,
                        editableCells = listOf(MulCell.SumTenThousands, MulCell.SumThousands),
                        highlightCells = listOf(
                            MulCell.P1Thousands,
                            MulCell.P2Thousands,
                            MulCell.CarrySumThousands
                        ),
                        totalLineTargets = setOf(MulCell.SumThousands)
                    )
                }

            } else {
                // TenThousands가 0이면 Thousands에서 종료
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

                if (info.sumTenThousands > 0) {
                    steps += MulPhaseStep(
                        phase = MulPhase.InputSum,
                        editableCells = listOf(MulCell.SumTenThousands),
                        highlightCells = listOf(
                            MulCell.P2TenThousands,
                            MulCell.CarrySumTenThousands
                        )
                    )
                }
            }

        }

        steps += MulPhaseStep(phase = MulPhase.Complete)
        return MulPhaseSequence(
            steps = steps,
            pattern = MulPattern.ThreeByTwo
        )
    }
}