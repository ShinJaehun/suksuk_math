package com.shinjaehun.suksuk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.shinjaehun.suksuk.domain.ProblemSessionFactory
import com.shinjaehun.suksuk.presentation.common.effects.AudioPlayer
import com.shinjaehun.suksuk.presentation.common.effects.LocalAudioPlayer
import com.shinjaehun.suksuk.ui.theme.SukSukTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var problemFactory: ProblemSessionFactory
    @Inject
    lateinit var audioPlayer: AudioPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            SukSukTheme {
                CompositionLocalProvider(LocalAudioPlayer provides audioPlayer) {
                    AppNavHost(problemFactory)

                    // 디버깅 예제
//                    DivisionScreenEntry(
//                        problemFactory = problemFactory,
//                        mode = SessionMode.Practice,
//                        pattern = null,
//                        overrideOperands = 859 to 32,
//                        onExit = { /* 뒤로가기 */ }
//                    )
                }
            }
        }
    }
}
