package com.shinjaehun.suksuk.presentation.division

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.shinjaehun.suksuk.domain.division.model.DivisionUiStateV2

@Composable
fun DivisionInputPanel(
    uiState: DivisionUiStateV2,
    onDigitInput: (Int) -> Unit,
    onClear: () -> Unit,
    onEnter: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(bottom = 32.dp)
    ) {
        NumberPad(
            onNumber = onDigitInput,
            onClear = onClear,
            onEnter = onEnter
        )
        uiState.feedback?.let {
            Spacer(Modifier.height(16.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .testTag("feedback")
            )
        }
    }
}
