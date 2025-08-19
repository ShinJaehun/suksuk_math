package com.shinjaehun.suksuk.di

import com.shinjaehun.suksuk.domain.multiplication.evaluator.MulPhaseEvaluator
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.multiplication.sequence.creator.ThreeByTwoMulPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.multiplication.sequence.creator.TwoByTwoMulPhaseSequenceCreator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MultiplicationModule {

    @Provides
    fun provideMulPhaseEvaluatorV2(): MulPhaseEvaluator = MulPhaseEvaluator()

    @Provides
    fun provideMulPhaseSequenceProvider(
        twoByTwoCreator: TwoByTwoMulPhaseSequenceCreator,
        threeByTwoCreator: ThreeByTwoMulPhaseSequenceCreator,
    ): MulPhaseSequenceProvider = MulPhaseSequenceProvider(
        twoByTwoCreator,
        threeByTwoCreator,
    )

//    @Singleton
//    @Provides
//    fun provideMulDomainStateV2Factory(
//        patternDetector: MulPatternDetector,
//        phaseSequenceProvider: MulPhaseSequenceProvider,
//    ): MulDomainStateFactory = MulDomainStateFactory(
//        patternDetector,
//        phaseSequenceProvider
//    )
}