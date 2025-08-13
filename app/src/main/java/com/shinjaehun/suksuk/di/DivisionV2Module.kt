package com.shinjaehun.suksuk.di

import com.shinjaehun.suksuk.domain.division.factory.DivisionDomainStateV2Factory
import com.shinjaehun.suksuk.domain.division.layout.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.division.detector.PatternDetectorV2
import com.shinjaehun.suksuk.domain.division.evaluator.PhaseEvaluatorV2
import com.shinjaehun.suksuk.domain.division.layout.sequence.ThreeByTwoPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.layout.sequence.TwoByOnePhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.layout.sequence.TwoByTwoPhaseSequenceCreator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DivisionV2Module {

    @Provides
    fun providePhaseEvaluatorV2(): PhaseEvaluatorV2 = PhaseEvaluatorV2()

    @Provides
    fun providePhaseSequenceProvider(
        twoByOneCreator: TwoByOnePhaseSequenceCreator,
        twoByTwoCreator: TwoByTwoPhaseSequenceCreator,
        threeByTwoCreator: ThreeByTwoPhaseSequenceCreator,
    ): DivisionPhaseSequenceProvider = DivisionPhaseSequenceProvider(
        twoByOneCreator,
        twoByTwoCreator,
        threeByTwoCreator,
    )

    @Singleton
    @Provides
    fun provideDomainState2Factory(
        patternDetector: PatternDetectorV2,
        phaseSequenceProvider: DivisionPhaseSequenceProvider,
    ): DivisionDomainStateV2Factory = DivisionDomainStateV2Factory(patternDetector, phaseSequenceProvider)
}