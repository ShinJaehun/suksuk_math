package com.shinjaehun.suksuk

import com.shinjaehun.suksuk.domain.DomainStateFactory
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.division.sequence.creator.ThreeByTwoDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.sequence.creator.TwoByOneDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.sequence.creator.TwoByTwoDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.multiplication.sequence.creator.ThreeByTwoMulPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.multiplication.sequence.creator.TwoByTwoMulPhaseSequenceCreator

object TestFactoryBuilders {

    fun unifiedFactoryForDivision(): DomainStateFactory {
        // Division 실물
        val divProvider = DivisionPhaseSequenceProvider(
            TwoByOneDivPhaseSequenceCreator(),
            TwoByTwoDivPhaseSequenceCreator(),
            ThreeByTwoDivPhaseSequenceCreator()
        )
        // Mul은 호출되지 않지만 생성자 충족 위해 간단 생성
        val mulProvider = MulPhaseSequenceProvider(
            TwoByTwoMulPhaseSequenceCreator(),
            ThreeByTwoMulPhaseSequenceCreator()
        )
        return DomainStateFactory(mulProvider, divProvider)
    }

    fun unifiedFactoryForMultiplication(): DomainStateFactory {
        val mulProvider = MulPhaseSequenceProvider(
            TwoByTwoMulPhaseSequenceCreator(),
            ThreeByTwoMulPhaseSequenceCreator()
        )
        val divProvider = DivisionPhaseSequenceProvider(
            TwoByOneDivPhaseSequenceCreator(),
            TwoByTwoDivPhaseSequenceCreator(),
            ThreeByTwoDivPhaseSequenceCreator()
        )
        return DomainStateFactory(mulProvider, divProvider)
    }
}
