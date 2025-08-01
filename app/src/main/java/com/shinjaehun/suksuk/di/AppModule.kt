package com.shinjaehun.suksuk.di

import com.shinjaehun.suksuk.domain.division.detector.PatternDetector
import com.shinjaehun.suksuk.domain.division.evaluator.PhaseEvaluator
import com.shinjaehun.suksuk.domain.division.factory.DivisionDomainStateFactory
import com.shinjaehun.suksuk.domain.division.layout.DivisionPatternUiLayoutRegistry
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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

    @Singleton
    @Provides
    fun provideDomainStateFactory(
        registry: DivisionPatternUiLayoutRegistry,
        detector: PatternDetector
    ): DivisionDomainStateFactory = DivisionDomainStateFactory(registry, detector)

}