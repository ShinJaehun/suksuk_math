package com.shinjaehun.suksuk.presentation.multiplication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shinjaehun.suksuk.presentation.component.InputPanel

@Composable
fun MultiplicationScreen(
    multiplicand: Int,
    multiplier: Int,
    viewModel: MultiplicationViewModel = hiltViewModel(),
    previewAll: Boolean = false
) {

    LaunchedEffect(multiplicand, multiplier) {
        viewModel.startNewProblem(multiplicand, multiplier)
    }

    val uiState = viewModel.uiState.collectAsState().value

    Box(modifier = Modifier.fillMaxSize()) {
        MultiplicationBoard2x2(uiState)

        InputPanel(
            feedback = uiState.feedback,
            onDigitInput = viewModel::onDigitInput,
            onClear = viewModel::onClear,
            onEnter = viewModel::onEnter,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}