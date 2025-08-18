package com.shinjaehun.suksuk.presentation.division

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2
import com.shinjaehun.suksuk.presentation.component.InputPanel
import com.shinjaehun.suksuk.presentation.component.NumberPad

@Composable
fun DivisionScreenV2(
    dividend: Int,
    divisor: Int,
    viewModel: DivisionViewModelV2 = hiltViewModel(),
    previewAll: Boolean = false
) {
    LaunchedEffect(dividend, divisor) {
        viewModel.startNewProblem(dividend, divisor)
    }

    val uiState = viewModel.uiState.collectAsState().value
    if (uiState.pattern == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize()){
        when(uiState.pattern) {
            DivisionPatternV2.TwoByOne,
            DivisionPatternV2.TwoByTwo -> DivisionScreen2By1And2By2(uiState, uiState.pattern)
            DivisionPatternV2.ThreeByTwo ->DivisionScreen3By2(uiState)
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
