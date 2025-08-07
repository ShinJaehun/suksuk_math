package com.shinjaehun.suksuk.di

import com.shinjaehun.suksuk.domain.division.detector.PatternDetectorV2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object PatternDetectorModule {

    @Provides
    fun providePatternDetector(): PatternDetectorV2 = PatternDetectorV2
}