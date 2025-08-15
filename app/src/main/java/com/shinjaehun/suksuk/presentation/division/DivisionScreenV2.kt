package com.shinjaehun.suksuk.presentation.division

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2
import com.shinjaehun.suksuk.presentation.division.component.DivisionInputPanel

@Composable
fun DivisionScreenV2(
    viewModel: DivisionViewModelV2 = hiltViewModel(),
    previewAll: Boolean = false
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState == null) {
        return
    }

    Box(modifier = Modifier.fillMaxSize()){
        when(uiState!!.pattern) {
            DivisionPatternV2.TwoByOne,
            DivisionPatternV2.TwoByTwo -> DivisionScreen2By1And2By2(uiState!!, uiState!!.pattern)
            DivisionPatternV2.ThreeByTwo -> DivisionScreen3By2(uiState!!)
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            DivisionInputPanel(
                uiState = uiState!!,
                onDigitInput = { viewModel.onDigitInput(it.toString()) },
                onClear = viewModel::onClear,
                onEnter = viewModel::onEnter,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }



    }

}
