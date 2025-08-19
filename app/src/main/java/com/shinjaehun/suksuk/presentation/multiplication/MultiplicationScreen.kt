package com.shinjaehun.suksuk.presentation.multiplication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    if (uiState.pattern == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MultiplicationBoard(uiState)

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