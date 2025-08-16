package com.shinjaehun.suksuk.presentation.division.legacy

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.shinjaehun.suksuk.domain.division.legacy.model.DivisionPattern
import com.shinjaehun.suksuk.presentation.component.NumberPad


@Composable
fun DivisionScreen(
    viewModel: DivisionViewModel = hiltViewModel(),
    previewAll: Boolean = false
) {

    val domainState by viewModel.domainState.collectAsState()
    val currentInput = viewModel.currentInput

    val currentUiState = remember(domainState, currentInput, previewAll) {
        DivisionUiStateBuilder.mapToUiState(domainState, currentInput, previewAll)
    }

    val cellWidth = 42.dp

    val isTwoByOne = when (domainState.pattern) {
        DivisionPattern.TwoByOne_TensQuotient_NoBorrow_2DigitMul,
        DivisionPattern.TwoByOne_TensQuotient_Borrow_2DigitMul,
        DivisionPattern.TwoByOne_TensQuotient_NoBorrow_1DigitMul,
        DivisionPattern.TwoByOne_TensQuotient_SkipBorrow_1DigitMul,
        DivisionPattern.TwoByOne_OnesQuotient_Borrow_2DigitMul,
        DivisionPattern.TwoByOne_OnesQuotient_NoBorrow_2DigitMul -> true
        else -> false
    }

    val bracketStartMargin = if (isTwoByOne) 60.dp else 90.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 30.dp)
        ) {
            val (
                divisorTensRef, divisorOnesRef, dividendTensRef, dividendOnesRef, divisorTensCarryRef
            ) = createRefs()

            val (
                quotientTensRef, quotientOnesRef,
                multiply1TensRef, multiply1OnesRef, subtract1TensRef, subtract1OnesRef,
                multiply2TensRef, multiply2OnesRef, subtract2TensRef, subtract2OnesRef
            ) = createRefs()

            val (
                dividendTenBorrowRef, subtract1TensBorrowRef,
                dividendOnesBorrowed10Ref, subtract1OnesBorrowed10Ref
            ) = createRefs()

            val (bracketRef, subtract1LineRef, subtract2LineRef) = createRefs()

            Image(
                painter = painterResource(id = R.drawable.ic_division_bracket_short),
                contentDescription = "Division Bracket",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.constrainAs(bracketRef) {
                    top.linkTo(parent.top, margin = 70.dp)
                    start.linkTo(parent.start, margin = bracketStartMargin)
                    width = Dimension.value(150.dp)
                    height = Dimension.value(120.dp)
                }
            )

            val dividendTensCell = currentUiState.dividendTens
            NumberText(
                cell = dividendTensCell,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(dividendTensRef) {
                        top.linkTo(bracketRef.top, margin = 40.dp)
                        start.linkTo(bracketRef.start, margin = 60.dp)
                    }
            )

            val dividendTensBorrowCell = currentUiState.borrowDividendTens
            BorrowText(
                cell = dividendTensBorrowCell,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(dividendTenBorrowRef) {
                        start.linkTo(dividendTensRef.start)
                        bottom.linkTo(dividendTensRef.top)
                    }
            )

            val dividendOnesCell = currentUiState.dividendOnes
            NumberText(
                cell = dividendOnesCell,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(dividendOnesRef) {
                        start.linkTo(dividendTensRef.end)
                        baseline.linkTo(dividendTensRef.baseline)
                    }
            )

            val dividendOnesBorrowed10Cell = currentUiState.borrowed10DividendOnes
            BorrowText(
                cell = dividendOnesBorrowed10Cell,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(dividendOnesBorrowed10Ref) {
                        start.linkTo(dividendOnesRef.start)
                        bottom.linkTo(dividendOnesRef.top)
                    }
                    .testTag("borrowed10-dividend-cell")
            )

            val divisorOnesCell = currentUiState.divisorOnes
            NumberText(
                cell = divisorOnesCell,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(divisorOnesRef) {
                        end.linkTo(dividendTensRef.start, margin = 40.dp)
                        baseline.linkTo(dividendTensRef.baseline)
                    }
            )

            val divisorTensCell = currentUiState.divisorTens
            NumberText(
                cell = divisorTensCell,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(divisorTensRef) {
                        end.linkTo(divisorOnesRef.start)
                        baseline.linkTo(dividendTensRef.baseline)
                    }
            )

            val divisorTensCarryCell = currentUiState.carryDivisorTens
            BorrowText(
                cell = divisorTensCarryCell,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(divisorTensCarryRef) {
                        start.linkTo(divisorTensRef.start)
                        bottom.linkTo(divisorTensRef.top)
                    }
            )

            val quotientTensCell = currentUiState.quotientTens
            NumberText(
                cell = quotientTensCell,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(quotientTensRef) {
                        start.linkTo(dividendTensRef.start)
                        bottom.linkTo(dividendTensRef.top, margin = 40.dp)
                    }
            )

            val quotientOnesCell = currentUiState.quotientOnes
            NumberText(
                cell = quotientOnesCell,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(quotientOnesRef) {
                        start.linkTo(quotientTensRef.end)
                        baseline.linkTo(quotientTensRef.baseline)
                    }
            )

            val multiply1TensCell = currentUiState.multiply1Tens
            NumberText(
                cell = multiply1TensCell,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(multiply1TensRef) {
                        top.linkTo(dividendTensRef.bottom, margin = 10.dp)
                        start.linkTo(dividendTensRef.start)
                    }
            )

            val multiply1OnesCell = currentUiState.multiply1Ones
            NumberText(
                cell = multiply1OnesCell,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(multiply1OnesRef) {
                        start.linkTo(multiply1TensRef.end)
                        baseline.linkTo(multiply1TensRef.baseline)
                    }
            )

            println("🔥 subtraction1-line should be shown? ${currentUiState.subtractLines.showSubtract1}")
            if(currentUiState.subtractLines.showSubtract1){
                Image(
                    painter = painterResource(id = R.drawable.ic_horizontal_line_short),
                    contentDescription = "Subtraction Line 1",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.constrainAs(subtract1LineRef) {
                        top.linkTo(dividendTensRef.bottom, margin = 60.dp)
                        start.linkTo(dividendTensRef.start, margin = (-10).dp)
                        width = Dimension.value(100.dp)
                        height = Dimension.value(4.dp)
                    }
                        .testTag("subtraction1-line")
                )
            }

            val subtract1TensCell = currentUiState.subtract1Tens
            NumberText(
                cell = subtract1TensCell,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(subtract1TensRef) {
                        top.linkTo(dividendTensRef.bottom, margin = 90.dp)
                        start.linkTo(dividendTensRef.start)
                    }
            )

            val subtract1BorrowCell = currentUiState.borrowSubtract1Tens
            BorrowText(
                cell = subtract1BorrowCell,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(subtract1TensBorrowRef) {
                        start.linkTo(subtract1TensRef.start)
                        bottom.linkTo(subtract1TensRef.top)
                    }
            )

            val subtract1OnesCell = currentUiState.subtract1Ones
            NumberText(
                cell = subtract1OnesCell,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(subtract1OnesRef) {
                        start.linkTo(subtract1TensRef.end)
                        baseline.linkTo(subtract1TensRef.baseline)
                    }
            )

            val subtract1Borrowed10Cell = currentUiState.borrowed10Subtract1Ones
            BorrowText(
                cell = subtract1Borrowed10Cell,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(subtract1OnesBorrowed10Ref) {
                        start.linkTo(subtract1OnesRef.start)
                        bottom.linkTo(subtract1OnesRef.top)
                    }
                    .testTag("borrowed10-sub1-cell")
            )

            val multiply2TensCell = currentUiState.multiply2Tens
            NumberText(
                cell = multiply2TensCell,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(multiply2TensRef) {
                        top.linkTo(dividendTensRef.bottom, margin = 150.dp)
                        start.linkTo(dividendTensRef.start)
                    }
            )

            val multiply2OnesCell = currentUiState.multiply2Ones
            NumberText(
                cell = multiply2OnesCell,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(multiply2OnesRef) {
                        start.linkTo(multiply2TensRef.end)
                        baseline.linkTo(multiply2TensRef.baseline)
                    }
            )

            if (currentUiState.subtractLines.showSubtract2){
                Image(
                    painter = painterResource(id = R.drawable.ic_horizontal_line_short),
                    contentDescription = "Subtraction Line",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.constrainAs(subtract2LineRef) {
                        top.linkTo(dividendTensRef.bottom, margin = 200.dp)
                        start.linkTo(dividendTensRef.start, margin = (-10).dp)
                        width = Dimension.value(100.dp)
                        height = Dimension.value(4.dp)
                    }
                        .testTag("subtraction2-line")
                )
            }

            val subtract2TensCell = currentUiState.subtract2Tens
            NumberText(
                cell = subtract2TensCell,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(subtract2TensRef) {
                        top.linkTo(dividendTensRef.bottom, margin = 220.dp)
                        start.linkTo(dividendTensRef.start)
                    }
            )

            val subtract2OnesCell = currentUiState.subtract2Ones
            NumberText(
                cell = subtract2OnesCell,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(subtract2OnesRef) {
                        start.linkTo(subtract2TensRef.end)
                        baseline.linkTo(subtract2TensRef.baseline)
                    }
            )
        }

//        LaunchedEffect(currentUiState.feedback) {
//            println("🟢 [UI] feedback=${currentUiState.feedback}")
//        }

        // Number pad & feedback
        Box(modifier = Modifier.fillMaxSize()) {
            Column( // 얘를 component로 분할했더니 테스트를 계속 실패함... 원인을 찾기 너무 어려웠음..ㅠㅠ
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            ) {
                NumberPad(
                    onNumber = viewModel::onDigitInput,
                    onClear = viewModel::onClear,
                    onEnter = viewModel::onEnter
                )
                currentUiState.feedback?.let {
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