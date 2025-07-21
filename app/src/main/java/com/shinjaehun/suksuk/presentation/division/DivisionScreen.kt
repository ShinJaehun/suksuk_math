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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
    val phasesState by viewModel.uiState.collectAsState()
    val currentInput = viewModel.currentInput
    val uiState = mapPhasesToCells(phasesState, currentInput)  // 변환 함수 사용!
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
                divisorRef, dividendTenRef, dividendOneRef,
                quotientTenRef, quotientOneRef,
                multiply1Ref, subtract1Ref, bringDownRef,
                multiply2TenRef, multiply2OneRef, remainderRef
            ) = createRefs()

            val (dividendTenBorrowRef, subtract1BorrowRef) = createRefs()
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
            NumberText(
                text = uiState.dividend.toString()[0].toString(),
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(dividendTenRef) {
                        top.linkTo(bracketRef.top, margin = 40.dp)
                        start.linkTo(bracketRef.start, margin = 60.dp)
                    }
            )

            // dividend tens borrow
            BorrowText(
                text = "?",
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(dividendTenBorrowRef) {
                        start.linkTo(dividendTenRef.start)
                        bottom.linkTo(dividendTenRef.top)
                    }
            )

            // dividend ones
            NumberText(
                text = uiState.dividend.toString()[0].toString(),
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(dividendOneRef) {
                        start.linkTo(dividendTenRef.end)
                        baseline.linkTo(dividendTenRef.baseline)
                    }
            )

            // divisor
            NumberText(
                text = uiState.divisor.toString(),
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(divisorRef) {
                        end.linkTo(dividendTenRef.start, margin = 40.dp)
                        baseline.linkTo(dividendTenRef.baseline)
                    }
            )

            // 몫(십의 자리)
            val cellTen = uiState.quotientCells[0]
            NumberText(
                text = "?",
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(quotientTenRef) {
                        start.linkTo(dividendTenRef.start)
                        bottom.linkTo(dividendTenRef.top, margin = 40.dp)
                    }
            )

            // 몫(일의 자리)
            val cellOne = uiState.quotientCells[1]
            NumberText(
                text = "?",
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(quotientOneRef) {
                        start.linkTo(quotientTenRef.end)
                        baseline.linkTo(quotientTenRef.baseline)
                    }
            )

            // 1차 곱셈(7)
            val mul1 = uiState.multiply1Cell
            NumberText(
                text = "?",
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(multiply1Ref) {
                        top.linkTo(dividendTenRef.bottom, margin = 10.dp)
                        start.linkTo(dividendTenRef.start)
                    }
            )

            Image(
                painter = painterResource(id = R.drawable.ic_horizontal_line),
                contentDescription = "Subtraction Line",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.constrainAs(subtract1LineRef) {
                    top.linkTo(dividendTenRef.bottom, margin = 60.dp)
                    start.linkTo(dividendTenRef.start, margin = (-10).dp)
                    width = Dimension.value(100.dp)
                    height = Dimension.value(4.dp)
                }
            )

            // 1차 뺄셈(2)
            val sub1 = uiState.subtract1Cell
            NumberText(
                text = "?",
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(subtract1Ref) {
                        top.linkTo(dividendTenRef.bottom, margin = 90.dp)
                        start.linkTo(dividendTenRef.start)
                    }
            )

            // first subtraction borrow
            BorrowText(
                text = "?",
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(subtract1BorrowRef) {
                        start.linkTo(subtract1Ref.start)
                        bottom.linkTo(subtract1Ref.top)
                    }
            )

            // bringDown(2), 뺄셈 오른쪽
            val bringDown = uiState.bringDownCell
            NumberText(
                text = "?",
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(bringDownRef) {
                        start.linkTo(subtract1Ref.end)
                        baseline.linkTo(subtract1Ref.baseline)
                    }
            )

            // 2차 곱셈(21), 두 칸: 22 아래
            val mul2Ten = uiState.multiply2Ten
            NumberText(
                text = "?",
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(multiply2TenRef) {
                        top.linkTo(dividendTenRef.bottom, margin = 150.dp)
                        start.linkTo(dividendTenRef.start)
                    }
            )

            val mul2One = uiState.multiply2One
            NumberText(
                text = "?",
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(multiply2OneRef) {
                        start.linkTo(multiply2TenRef.end)
                        baseline.linkTo(multiply2TenRef.baseline)
                    }
            )

            Image(
                painter = painterResource(id = R.drawable.ic_horizontal_line),
                contentDescription = "Subtraction Line",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.constrainAs(subtract2LineRef) {
                    top.linkTo(dividendTenRef.bottom, margin = 200.dp)
                    start.linkTo(dividendTenRef.start, margin = (-10).dp)
                    width = Dimension.value(100.dp)
                    height = Dimension.value(4.dp)
                }
            )


            // 나머지(1) 한 칸만!
            val remainderCell = uiState.remainderCell
            NumberText(
                text = "?",
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(remainderRef) {
                        top.linkTo(dividendTenRef.bottom, margin = 220.dp)
                        start.linkTo(dividendOneRef.start)
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
                uiState.feedback?.let {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun NumberText(
    text: String,
    color: Color = Color.Black,
    fontSize: TextUnit = 40.sp,
    width: Dp = 42.dp,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = fontSize,
        textAlign = TextAlign.Center,
        color = color,
        modifier = modifier.width(width)
    )
}

@Composable
fun BorrowText(
    text: String,
    color: Color = Color.Black,
    fontSize: TextUnit = 20.sp,
    width: Dp = 42.dp,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = fontSize,
        color = color,
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
                        -2 -> CircleButton(label = "↵", onClick = onEnter)
                        else -> CircleButton(label = num.toString()) { onNumber(num) }
                    }
                    Spacer(Modifier.width(8.dp))
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun CircleButton(label: String, onClick: () -> Unit) {
    Box(
        Modifier
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