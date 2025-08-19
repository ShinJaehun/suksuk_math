package com.shinjaehun.suksuk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.shinjaehun.suksuk.ui.theme.SukSukTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
//            DivisionScreenV2(dividend=978, divisor=21)

            SukSukTheme {
                AppNavHost()
            }
        }
    }
}
