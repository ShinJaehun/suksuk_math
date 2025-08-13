package com.shinjaehun.suksuk.presentation.division

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2

@Composable
fun DivisionScreenV2(
    viewModel: DivisionViewModelV2 = hiltViewModel(),
    previewAll: Boolean = false
) {

    val uiState by viewModel.uiState.collectAsState()

    val overrideCells = mapOf(
        CellName.DividendHundreds to "6",
        CellName.BorrowDividendHundreds to "5",
        CellName.Borrowed10DividendTens to "10",

        CellName.DividendTens to "9",
        CellName.BorrowDividendTens to "8",
        CellName.Borrowed10DividendOnes to "10"
    )

    val previewUiState = if (previewAll) {
        uiState.copy(
            cells = uiState.cells.mapValues { (cellName, cell) ->
                cell.copy(
                    value = overrideCells[cellName] ?: "?"
                )
            }
        )
    } else uiState

    val pattern = viewModel.getCurrentPattern()

    Box(modifier = Modifier.fillMaxSize()){
        when(pattern) {
            DivisionPatternV2.TwoByOne,
            DivisionPatternV2.TwoByTwo -> DivisionScreen2By1And2By2(uiState, pattern)
            DivisionPatternV2.ThreeByTwo -> DivisionScreen3By2(uiState)
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            DivisionInputPanel(
                uiState = uiState,
                onDigitInput = { viewModel.onDigitInput(it.toString()) },
                onClear = viewModel::onClear,
                onEnter = viewModel::onEnter,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }



    }

}
