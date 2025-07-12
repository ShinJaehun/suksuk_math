package com.shinjaehun.suksuk.presentation.division

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shinjaehun.suksuk.presentation.components.DivisionInputPad

@Composable
fun DivisionStageScreen(
    viewModel: DivisionViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // 문제 표시
        Text(text = "${uiState.dividend} ÷ ${uiState.divisor} = ?", style = MaterialTheme.typography.headlineMedium)

        // 사용자 입력 표시
        Text(text = uiState.userInput.ifBlank { "__" }, style = MaterialTheme.typography.displayLarge)

        // 피드백
        uiState.feedback?.let {
            Text(text = it, color = MaterialTheme.colorScheme.primary)
        }

        // 입력 패드
        DivisionInputPad(
            onNumberClick = { viewModel.onDigitInput(it) },
            onClearClick = { viewModel.onClear() },
//            onEnterClick = TODO()
        )
    }
}
