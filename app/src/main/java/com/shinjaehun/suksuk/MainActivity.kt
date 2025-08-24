package com.shinjaehun.suksuk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.shinjaehun.suksuk.domain.ProblemSessionFactory
import com.shinjaehun.suksuk.domain.SessionMode
import com.shinjaehun.suksuk.presentation.common.device.ProvideDeviceClasses
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

        WindowCompat.setDecorFitsSystemWindows(window, false)
        hideSystemBars()

        setContent {

            SukSukTheme {

                ProvideDeviceClasses {

                    CompositionLocalProvider(LocalAudioPlayer provides audioPlayer) {
                        AppNavHost(problemFactory)
                    }

                    // 디버깅 예제
//                CompositionLocalProvider(LocalAudioPlayer provides audioPlayer) {
//                    DivisionScreenEntry(
//                        problemFactory = problemFactory,
//                        mode = SessionMode.Practice,
//                        pattern = null,
//                        overrideOperands = 859 to 32,
//                        onExit = { /* 뒤로가기 */ }
//                    )

//                    MultiplicationScreenEntry(
//                        problemFactory = problemFactory,
//                        mode = SessionMode.Practice,
//                        pattern = null,
////                        overrideOperands = 56 to 19,
//                        overrideOperands = 102 to 99,
//                        onExit = { /* 뒤로가기 */ }
//                    )
//                }
                }

            }
        }
    }

    private fun hideSystemBars() {
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // 전부 숨기기(상태바+내비바). 내비만 숨기고 싶으면 Type.navigationBars()만.
        controller.hide(WindowInsetsCompat.Type.systemBars())
    }

    // 포커스/IME/다이얼로그 등으로 시스템 바가 다시 나타난 뒤 포커스 복구되면 재적용
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemBars()
    }

    // 필요 시 다시 보이게 할 때
    private fun showSystemBars() {
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        controller.show(WindowInsetsCompat.Type.systemBars())
    }
}
