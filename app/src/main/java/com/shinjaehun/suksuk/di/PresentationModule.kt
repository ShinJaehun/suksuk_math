package com.shinjaehun.suksuk.di

import android.content.Context
import com.shinjaehun.suksuk.presentation.common.effects.AudioPlayer
import com.shinjaehun.suksuk.presentation.common.effects.SoundEffects
import com.shinjaehun.suksuk.presentation.common.feedback.FeedbackProvider
import com.shinjaehun.suksuk.presentation.common.feedback.FeedbackProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PresentationModule {

    @Provides
    @Singleton
    fun provideFeedback(impl: FeedbackProviderImpl): FeedbackProvider = impl

    @Provides
    @Singleton
    fun provideAudioPlayer(@ApplicationContext ctx: Context): AudioPlayer =
        SoundEffects(ctx).apply { preload() }
}