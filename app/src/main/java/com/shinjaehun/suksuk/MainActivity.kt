package com.shinjaehun.suksuk

import android.os.Bundle
import android.util.Log
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
//            MultiplicationScreen(multiplicand=48, multiplier=36)
//            MultiplicationScreen(multiplicand=99, multiplier=99)
//            MultiplicationScreen(multiplicand=55, multiplier=55)
//            MultiplicationScreen(multiplicand=25, multiplier=40) // 이 경우 해결해야 함...
//            MultiplicationScreen(multiplicand=80, multiplier=47)
//            MultiplicationScreen(multiplicand=11, multiplier=11)
//            MultiplicationScreen(multiplicand=76, multiplier=89)
//            MultiplicationScreen(multiplicand=76, multiplier=80)
//            MultiplicationScreen(multiplicand=234, multiplier=50)
//            DivisionScreen(dividend=610, divisor=13)

            SukSukTheme {
                CompositionLocalProvider(LocalAudioPlayer provides audioPlayer) {
                    AppNavHost(problemFactory)
                }
            }
        }
    }
}
