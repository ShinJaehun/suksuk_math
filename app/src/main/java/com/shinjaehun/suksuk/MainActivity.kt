package com.shinjaehun.suksuk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.shinjaehun.suksuk.presentation.division.DivisionScreenV2
import com.shinjaehun.suksuk.presentation.division.DivisionViewModelV2
import com.shinjaehun.suksuk.presentation.division.legacy.DivisionScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm: DivisionViewModelV2 = hiltViewModel()
            LaunchedEffect(Unit) {
                vm.startNewProblem(210, 11) // 원하는 기본값
            }
            DivisionScreenV2(viewModel = vm)
        }
    }
}
