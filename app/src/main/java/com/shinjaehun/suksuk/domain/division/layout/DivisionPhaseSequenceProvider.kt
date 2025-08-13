package com.shinjaehun.suksuk.domain.division.layout

import com.shinjaehun.suksuk.domain.division.DivisionStateInfo
import com.shinjaehun.suksuk.domain.division.layout.sequence.ThreeByTwoPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.layout.sequence.TwoByOnePhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.layout.sequence.TwoByTwoPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2
import javax.inject.Inject

class DivisionPhaseSequenceProvider @Inject constructor(
    private val twoByOneCreator: TwoByOnePhaseSequenceCreator,
    private val twoByTwoCreator: TwoByTwoPhaseSequenceCreator,
    private val threeByTwoCreator: ThreeByTwoPhaseSequenceCreator,
) {

    fun make(pattern: DivisionPatternV2, info: DivisionStateInfo): PhaseSequence = when (pattern) {
        DivisionPatternV2.TwoByOne -> twoByOneCreator.create(info)
        DivisionPatternV2.TwoByTwo -> twoByTwoCreator.create(info)
        DivisionPatternV2.ThreeByTwo -> threeByTwoCreator.create(info)
    }
}
