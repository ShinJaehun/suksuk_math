package com.shinjaehun.suksuk.di

import com.shinjaehun.suksuk.data.DefaultProblemSessionFactory
import com.shinjaehun.suksuk.domain.ProblemSessionFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProblemSourceModule {

    @Binds
    @Singleton
    abstract fun bindProblemSessionFactory(
        impl: DefaultProblemSessionFactory
    ): ProblemSessionFactory
}