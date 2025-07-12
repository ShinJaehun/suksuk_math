package com.shinjaehun.suksuk.di

import com.shinjaehun.suksuk.domain.usecase.GenerateDivisionProblemUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideGenerateDivisionProblemUseCase(): GenerateDivisionProblemUseCase {
        return GenerateDivisionProblemUseCase()
    }
}