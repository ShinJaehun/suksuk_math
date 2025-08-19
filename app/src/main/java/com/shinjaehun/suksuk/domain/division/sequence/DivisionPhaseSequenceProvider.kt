package com.shinjaehun.suksuk.domain.division.sequence

import com.shinjaehun.suksuk.domain.division.info.DivisionStateInfo
import com.shinjaehun.suksuk.domain.division.sequence.creator.ThreeByTwoDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.sequence.creator.TwoByOneDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.sequence.creator.TwoByTwoDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.pattern.DivisionPatternV2
import javax.inject.Inject

class DivisionPhaseSequenceProvider @Inject constructor(
    private val twoByOneCreator: TwoByOneDivPhaseSequenceCreator,
    private val twoByTwoCreator: TwoByTwoDivPhaseSequenceCreator,
    private val threeByTwoCreator: ThreeByTwoDivPhaseSequenceCreator,
) {

    fun make(pattern: DivisionPatternV2, info: DivisionStateInfo): DivisionPhaseSequence =
        when (pattern) {
            DivisionPatternV2.TwoByOne -> twoByOneCreator.create(info)
            DivisionPatternV2.TwoByTwo -> twoByTwoCreator.create(info)
            DivisionPatternV2.ThreeByTwo -> threeByTwoCreator.create(info)
        }
}
