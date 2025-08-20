package com.shinjaehun.suksuk.presentation.common.effects

import androidx.compose.runtime.staticCompositionLocalOf

val LocalAudioPlayer = staticCompositionLocalOf<AudioPlayer> {
    error("AudioPlayer not provided")
}
