package com.shinjaehun.suksuk.domain.division.layout

import com.shinjaehun.suksuk.domain.division.DivisionInfoBuilder
import com.shinjaehun.suksuk.domain.division.layout.sequence.ThreeByTwoPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.layout.sequence.TwoByOnePhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.layout.sequence.TwoByTwoPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import javax.inject.Inject

class DivisionPhaseSequenceProvider @Inject constructor(
    private val twoByOneCreator: TwoByOnePhaseSequenceCreator,
    private val twoByTwoCreator: TwoByTwoPhaseSequenceCreator,
    private val threeByTwoCreator: ThreeByTwoPhaseSequenceCreator,

    ) {
    // 추후 provider 없이 di로 제공할 예정

    fun makeTwoByOnePhaseSequence(dividend: Int, divisor: Int): PhaseSequence {
        val info = DivisionInfoBuilder.from(dividend, divisor)
        return twoByOneCreator.create(info)
    }

    fun makeTwoByTwoPhaseSequence(dividend: Int, divisor: Int): PhaseSequence {
        val info = DivisionInfoBuilder.from(dividend, divisor)
        return twoByTwoCreator.create(info)
    }

    fun makeThreeByTwoPhaseSequence(dividend: Int, divisor: Int): PhaseSequence {
        val info = DivisionInfoBuilder.from(dividend, divisor)
        return threeByTwoCreator.create(info)
    }
}
