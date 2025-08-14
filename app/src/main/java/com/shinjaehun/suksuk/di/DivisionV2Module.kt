package com.shinjaehun.suksuk.di

import com.shinjaehun.suksuk.domain.division.factory.DivisionDomainStateV2Factory
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.division.detector.DivisionPatternDetectorV2
import com.shinjaehun.suksuk.domain.division.evaluator.DivisionPhaseEvaluatorV2
import com.shinjaehun.suksuk.domain.division.sequence.creator.ThreeByTwoDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.sequence.creator.TwoByOneDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.sequence.creator.TwoByTwoDivPhaseSequenceCreator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DivisionV2Module {

    @Provides
    fun providePhaseEvaluatorV2(): DivisionPhaseEvaluatorV2 = DivisionPhaseEvaluatorV2()

    @Provides
    fun providePhaseSequenceProvider(
        twoByOneCreator: TwoByOneDivPhaseSequenceCreator,
        twoByTwoCreator: TwoByTwoDivPhaseSequenceCreator,
        threeByTwoCreator: ThreeByTwoDivPhaseSequenceCreator,
    ): DivisionPhaseSequenceProvider = DivisionPhaseSequenceProvider(
        twoByOneCreator,
        twoByTwoCreator,
        threeByTwoCreator,
    )

    @Singleton
    @Provides
    fun provideDomainState2Factory(
        patternDetector: DivisionPatternDetectorV2,
        phaseSequenceProvider: DivisionPhaseSequenceProvider,
    ): DivisionDomainStateV2Factory = DivisionDomainStateV2Factory(patternDetector, phaseSequenceProvider)
}