package com.shinjaehun.suksuk.domain.division.sequence

import com.shinjaehun.suksuk.domain.division.info.DivisionStateInfo
import com.shinjaehun.suksuk.domain.division.sequence.creator.ThreeByTwoDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.sequence.creator.TwoByOneDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.sequence.creator.TwoByTwoDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.pattern.DivisionPattern
import javax.inject.Inject

class DivisionPhaseSequenceProvider @Inject constructor(
    private val twoByOneCreator: TwoByOneDivPhaseSequenceCreator,
    private val twoByTwoCreator: TwoByTwoDivPhaseSequenceCreator,
    private val threeByTwoCreator: ThreeByTwoDivPhaseSequenceCreator,
) {

    fun make(pattern: DivisionPattern, info: DivisionStateInfo): DivisionPhaseSequence =
        when (pattern) {
            DivisionPattern.TwoByOne -> twoByOneCreator.create(info)
            DivisionPattern.TwoByTwo -> twoByTwoCreator.create(info)
            DivisionPattern.ThreeByTwo -> threeByTwoCreator.create(info)
        }
}
