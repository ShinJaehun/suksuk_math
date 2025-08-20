package com.shinjaehun.suksuk.presentation.common.effects

import android.content.Context
import android.media.SoundPool
import com.shinjaehun.suksuk.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundEffects @Inject constructor(
    @ApplicationContext private val context: Context
) : AudioPlayer {
    private val soundPool by lazy { SoundPool.Builder().setMaxStreams(2).build() }
    private val ids by lazy {
        mapOf(
            "beep" to soundPool.load(context, R.raw.beep, 1),
            "tada" to soundPool.load(context, R.raw.tada, 1),
        )
    }

    fun preload() { ids.size /* touch to load */ }

    override fun playClick() { ids["beep"]?.let { soundPool.play(it,1f,1f,0,0,1f) } }
    override fun playTada() { ids["tada"]?.let { soundPool.play(it,1f,1f,0,0,1f) } }
}