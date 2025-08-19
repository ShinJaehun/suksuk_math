package com.shinjaehun.suksuk.di

import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.division.evaluator.DivisionPhaseEvaluator
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
object DivisionModule {

    @Singleton
    @Provides
    fun providePhaseEvaluator(): DivisionPhaseEvaluator = DivisionPhaseEvaluator()

    @Singleton
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
}