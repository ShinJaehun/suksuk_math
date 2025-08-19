package com.shinjaehun.suksuk.presentation.division

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shinjaehun.suksuk.domain.pattern.DivisionPattern
import com.shinjaehun.suksuk.presentation.component.InputPanel

@Composable
fun DivisionScreen(
    dividend: Int,
    divisor: Int,
    viewModel: DivisionViewModel = hiltViewModel(),
    previewAll: Boolean = false
) {
    LaunchedEffect(dividend, divisor) {
        viewModel.startNewProblem(dividend, divisor)
    }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    if (uiState.pattern == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize()){
        when(uiState.pattern) {
            DivisionPattern.TwoByOne,
            DivisionPattern.TwoByTwo -> DivisionBoard2By1And2By2(uiState, uiState.pattern)
            DivisionPattern.ThreeByTwo ->DivisionBoard3By2(uiState)
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            InputPanel(
                feedback = uiState.feedback,
                onDigitInput = viewModel::onDigitInput,
                onClear = viewModel::onClear,
                onEnter = viewModel::onEnter,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
