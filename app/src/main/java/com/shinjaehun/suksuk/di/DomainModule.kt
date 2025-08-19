package com.shinjaehun.suksuk.di

import com.shinjaehun.suksuk.domain.DomainStateFactory
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseSequenceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Singleton
    @Provides
    fun provideDomainStateFactory(
        mul: MulPhaseSequenceProvider,
        div: DivisionPhaseSequenceProvider
    ): DomainStateFactory = DomainStateFactory(mul, div)
}