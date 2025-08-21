package com.shinjaehun.suksuk.presentation.common.effects

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.shinjaehun.suksuk.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

//@Singleton
//class SoundEffects @Inject constructor(
//    @ApplicationContext private val context: Context
//) : AudioPlayer {
//    private val soundPool by lazy { SoundPool.Builder().setMaxStreams(2).build() }
//    private val ids by lazy {
//        mapOf(
//            "beep" to soundPool.load(context, R.raw.beep, 1),
//            "tada" to soundPool.load(context, R.raw.tada, 1),
//        )
//    }
//
//    fun preload() { ids.size /* touch to load */ }
//
//    override fun playClick() { ids["beep"]?.let { soundPool.play(it,1f,1f,0,0,1f) } }
//    override fun playTada() { ids["tada"]?.let { soundPool.play(it,1f,1f,0,0,1f) } }
//}

@Singleton
class SoundEffects @Inject constructor(
    @ApplicationContext private val context: Context
) : AudioPlayer {

    private val attrs = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_GAME) // MEDIA 볼륨 경로
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

    private val soundPool by lazy {
        SoundPool.Builder()
            .setMaxStreams(4)                   // (선택) 동시 재생 여유
            .setAudioAttributes(attrs)          // ✅ 예전 STREAM_MUSIC에 해당
            .build()
    }

    @Volatile private var loaded = false
    private val ids by lazy {
        soundPool.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) loaded = true
        }
        mapOf(
            "beep" to soundPool.load(context, R.raw.beep, 1),
            "tada" to soundPool.load(context, R.raw.tada, 1),
        )
    }

    fun preload() { ids.size /* touch to load; setOnLoadComplete로 loaded=true 대기 */ }

    override fun playClick() {
        val id = ids["beep"] ?: return
        if (!loaded) return
        soundPool.play(id, /*L*/0.5f, /*R*/0.5f, /*priority*/2, /*loop*/0, /*rate*/1f)
    }

    override fun playTada() {
        val id = ids["tada"] ?: return
        if (!loaded) return
        val streamId = soundPool.play(id, 1f, 1f, /*priority*/10, 0, 1f)
        // 혹시 믹서가 낮춰두면 즉시 보정
        soundPool.setVolume(streamId, 1f, 1f)
    }
}