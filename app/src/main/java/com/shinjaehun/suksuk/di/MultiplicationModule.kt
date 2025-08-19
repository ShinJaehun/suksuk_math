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

    @Singleton
    @Provides
    fun provideMulPhaseEvaluator(): MulPhaseEvaluator = MulPhaseEvaluator()

    @Singleton
    @Provides
    fun provideMulPhaseSequenceProvider(
        twoByTwoCreator: TwoByTwoMulPhaseSequenceCreator,
        threeByTwoCreator: ThreeByTwoMulPhaseSequenceCreator,
    ): MulPhaseSequenceProvider = MulPhaseSequenceProvider(
        twoByTwoCreator,
        threeByTwoCreator,
    )
}