package com.shinjaehun.suksuk.di

import com.shinjaehun.suksuk.domain.division.detector.DivisionPatternDetectorV2
import com.shinjaehun.suksuk.domain.multiplication.detector.MulPatternDetector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object PatternDetectorModule {

    @Provides
    fun provideDivisionPatternDetector(): DivisionPatternDetectorV2 = DivisionPatternDetectorV2

    @Provides
    fun provideMultiplicationPatternDetector(): MulPatternDetector = MulPatternDetector
}