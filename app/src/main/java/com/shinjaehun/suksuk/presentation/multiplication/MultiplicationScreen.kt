package com.shinjaehun.suksuk.presentation.multiplication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shinjaehun.suksuk.presentation.component.InputPanel

@Composable
fun MultiplicationScreen(
    viewModel: MultiplicationViewModel = hiltViewModel(),
    previewAll: Boolean = false
) {

    val uiStateNullable = viewModel.uiState.collectAsState().value
    val uiState = uiStateNullable ?: run {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MultiplicationBoard2x2(uiState)

        InputPanel(
            feedback = uiState.feedback,
            onDigitInput = { viewModel.onDigitInput(it.toString()) },
            onClear = viewModel::onClear,
            onEnter = viewModel::onEnter,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}