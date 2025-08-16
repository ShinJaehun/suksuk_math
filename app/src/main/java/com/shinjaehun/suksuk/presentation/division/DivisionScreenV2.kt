package com.shinjaehun.suksuk.presentation.division

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
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

    Box(modifier = Modifier.fillMaxSize()){
        when(uiState.pattern) {
            DivisionPatternV2.TwoByOne,
            DivisionPatternV2.TwoByTwo -> DivisionScreen2By1And2By2(uiState, uiState.pattern)
            DivisionPatternV2.ThreeByTwo -> DivisionScreen3By2(uiState)
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
//            Column(
//                modifier = Modifier
//                    .align(Alignment.BottomCenter)
//                    .padding(bottom = 32.dp)
//            ) {
//                NumberPad(
//                    onNumber =  viewModel::onDigitInput,
//                    onClear = viewModel::onClear,
//                    onEnter = viewModel::onEnter
//                )
//                uiState.feedback?.let {
//                    Spacer(Modifier.height(16.dp))
//                    Text(
//                        text = it,
//                        color = MaterialTheme.colorScheme.primary,
//                        modifier = Modifier
//                            .align(Alignment.CenterHorizontally)
//                            .testTag("feedback")
//                    )
//                }
//            }

        }
    }

}
