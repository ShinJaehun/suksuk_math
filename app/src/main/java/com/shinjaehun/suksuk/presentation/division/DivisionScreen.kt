package com.shinjaehun.suksuk.presentation.division

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shinjaehun.suksuk.R

@Composable
fun DivisionScreen(
    viewModel: DivisionViewModel = viewModel(),
    previewAll: Boolean = false
) {
    val phasesState by viewModel.phaseState.collectAsState()
    val currentInput = viewModel.currentInput

    val currentUiState = remember(phasesState, currentInput) {
//        mapPhasesToCells(phasesState, currentInput)
        DivisionUiStateBuilder.mapToUiState(phasesState, currentInput)
    }

    val cellWidth = 42.dp

    // 단계별 cover visibility - replace with actual uiState flags
//    val dividendTenCoverVisible = remember { mutableStateOf(false) }
//    val dividendOneCoverVisible = remember { mutableStateOf(false) }
//    val ansFirstLineVisible = remember { mutableStateOf(false) }

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
                divisorRef, dividendTensRef, dividendOnesRef,
                quotientTensRef, quotientOnesRef,
                multiply1TensRef, multiply1OnesRef, subtract1TensRef, subtract1OnesRef,
                multiply2TensRef, multiply2OnesRef, remainderRef
            ) = createRefs()

            val (
                dividendTenBorrowRef, subtract1BorrowRef,
                dividendOnesBorrowed10Ref, subtract1OnesBorrowed10Ref
            ) = createRefs()
            val (bracketRef, subtract1LineRef, subtract2LineRef) = createRefs()

            // Division Bracket
            Image(
                painter = painterResource(id = R.drawable.ic_division_bracket_short),
                contentDescription = "Division Bracket",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.constrainAs(bracketRef) {
                    top.linkTo(parent.top, margin = 70.dp)
                    start.linkTo(parent.start, margin = 60.dp)
                    width = Dimension.value(150.dp)
                    height = Dimension.value(120.dp)
                }
            )

            // dividend tens
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

            // dividend tens borrow
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

            // dividend ones
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
                    .testTag("borrowed-dividend-cell")
            )

            // divisor
            val divisorCell = currentUiState.divisor
            NumberText(
                cell = divisorCell,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(divisorRef) {
                        end.linkTo(dividendTensRef.start, margin = 40.dp)
                        baseline.linkTo(dividendTensRef.baseline)
                    }
            )

            // 몫(십의 자리)
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

            // 몫(일의 자리)
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

            // 1차 곱셈(7)
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

            if(currentUiState.subtractLines.showSubtract1){
                Image(
                    painter = painterResource(id = R.drawable.ic_horizontal_line),
                    contentDescription = "Subtraction Line",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.constrainAs(subtract1LineRef) {
                        top.linkTo(dividendTensRef.bottom, margin = 60.dp)
                        start.linkTo(dividendTensRef.start, margin = (-10).dp)
                        width = Dimension.value(100.dp)
                        height = Dimension.value(4.dp)
                    }
                        .testTag("subtraction-line")

                )
            }

            // 1차 뺄셈(2)
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

            // first subtraction borrow
            val subtract1BorrowCell = currentUiState.borrowSubtract1Tens
            BorrowText(
                cell = subtract1BorrowCell,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(subtract1BorrowRef) {
                        start.linkTo(subtract1TensRef.start)
                        bottom.linkTo(subtract1TensRef.top)
                    }
            )

            // bringDown(2), 뺄셈 오른쪽
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
                    .testTag("borrowed-sub1-cell")
            )

            // 2차 곱셈(21), 두 칸: 22 아래
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
                    painter = painterResource(id = R.drawable.ic_horizontal_line),
                    contentDescription = "Subtraction Line",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.constrainAs(subtract2LineRef) {
                        top.linkTo(dividendTensRef.bottom, margin = 200.dp)
                        start.linkTo(dividendTensRef.start, margin = (-10).dp)
                        width = Dimension.value(100.dp)
                        height = Dimension.value(4.dp)
                    }
                )
            }


            // 나머지(1) 한 칸만!
            val remainderCell = currentUiState.subtract2Ones
            NumberText(
                cell = remainderCell,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(remainderRef) {
                        top.linkTo(dividendTensRef.bottom, margin = 220.dp)
                        start.linkTo(dividendOnesRef.start)
                    }
            )
        }

        // Number pad & feedback
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
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

@Composable
fun NumberText(
    cell: InputCell,
    defaultColor: Color = Color.Black,
    fontSize: TextUnit = 40.sp,
    width: Dp = 42.dp,
    modifier: Modifier = Modifier
) {
    val textColor = when (cell.highlight) {
        Highlight.Editing -> Color.Red
        Highlight.Related -> Color(0xFF1976D2) // 파란색 (Material blue)
        else -> defaultColor
    }

    Box(
        modifier = modifier.width(width),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = cell.value ?: "",
            fontSize = fontSize,
            color = textColor,
            textAlign = TextAlign.Center,
        )

        if (cell.crossOutColor != CrossOutColor.None) {
            val painter = when(cell.crossOutColor){
                CrossOutColor.Pending -> painterResource(R.drawable.ic_strikethrough_pending)
                CrossOutColor.Confirmed -> painterResource(R.drawable.ic_strikethrough_confirmed)
                else -> null
            }
            painter?.let {
                Image(
                    painter = it,
                    contentDescription = "취소선",
                    modifier = Modifier
                        .matchParentSize()
                )
            }
        }
    }
}

@Composable
fun BorrowText(
    cell: InputCell,
    defaultColor: Color = Color.Black,
    fontSize: TextUnit = 20.sp,
    width: Dp = 42.dp,
    modifier: Modifier = Modifier
) {
    val textColor = when (cell.highlight) {
        Highlight.Editing -> Color.Red
        Highlight.Related -> Color(0xFF1976D2) // 파란색 (Material blue)
        else -> defaultColor
    }
    Text(
        text = cell.value ?: "",
        fontSize = fontSize,
        color = textColor,
        textAlign = TextAlign.Center,
        modifier = modifier.width(width)
    )
}

@Composable
fun NumberPad(onNumber: (Int) -> Unit, onClear: () -> Unit, onEnter: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        listOf(listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9), listOf(-1, 0, -2)).forEach { row ->
            Row {
                row.forEach { num ->
                    when (num) {
                        -1 -> CircleButton(label = "⟲", onClick = onClear)
                        -2 -> CircleButton(
                            label = "↵",
                            modifier = Modifier.testTag("numpad-enter"),
                            onClick = onEnter
                        )
                        else -> CircleButton(
                            label = num.toString(),
                            modifier = Modifier.testTag("numpad-$num")
                        ) { onNumber(num) }
                    }
                    Spacer(Modifier.width(8.dp))
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun CircleButton(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier
            .size(48.dp)
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(label, fontSize = 20.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDivisionStageScreen() {
    DivisionScreen()
}