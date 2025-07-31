package com.shinjaehun.suksuk.di

import com.shinjaehun.suksuk.domain.PatternDetector
import com.shinjaehun.suksuk.domain.PhaseEvaluator
import com.shinjaehun.suksuk.presentation.division.DivisionPatternUiLayoutRegistry
import com.shinjaehun.suksuk.presentation.division.DivisionUiStateBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    @Provides
//    fun provideGenerateDivisionProblemUseCase(): GenerateDivisionProblemUseCase {
//        return GenerateDivisionProblemUseCase()
//    }

    @Provides
    fun providePhaseEvaluator(): PhaseEvaluator = PhaseEvaluator()

    @Provides
    fun providePatternDetector(): PatternDetector = PatternDetector

    @Provides
    fun provideUiLayoutRegistry(): DivisionPatternUiLayoutRegistry = DivisionPatternUiLayoutRegistry

}