package com.shinjaehun.suksuk.presentation.division

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.shinjaehun.suksuk.R
import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2
import com.shinjaehun.suksuk.domain.division.model.InputCellV2
import com.shinjaehun.suksuk.domain.division.model.SubtractLineType
import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.domain.division.model.DivisionUiStateV2

@Composable
fun DivisionScreenV2(
    viewModel: DivisionViewModelV2 = hiltViewModel(),
    previewAll: Boolean = false
) {

//    val domainState by viewModel.domainState.collectAsState()
//    val currentInput = viewModel.currentInput
//
//    val currentUiState = remember(domainState, currentInput, previewAll) {
//        DivisionUiStateBuilder.mapToUiState(domainState, currentInput, previewAll)
//    }

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
//
//@SuppressLint("ViewModelConstructorInComposable")
//@Preview(showBackground = true)
//@Composable
//fun PreviewDivisionStageScreen() {

//    val savedStateHandle = SavedStateHandle(mapOf("autoStart" to true))
//    val phaseEvaluator = PhaseEvaluator()
//    val patternDetector = PatternDetector
//    val uiLayoutRegistry = DivisionPatternUiLayoutRegistry
//    val feedbackProvider = FeedbackMessageProvider()
//    val domainStateFactory = DivisionDomainStateFactory(uiLayoutRegistry, patternDetector)
//    val viewModel = DivisionViewModel(
//        savedStateHandle,
//        phaseEvaluator,
//        domainStateFactory,
//        feedbackProvider
//    )
//
//    DivisionScreen(viewModel = viewModel)
//}