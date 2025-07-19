package com.shinjaehun.suksuk.presentation.division

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.forEach
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shinjaehun.suksuk.R
import com.shinjaehun.suksuk.presentation.components.DivisionInputPad
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kotlin.text.padStart


@Composable
fun DivisionStageScreen(viewModel: DivisionViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 30.dp)
        ) {
            val (
                divisorRef, bracketRef, dividendRowRef,
                quotientTenRef, quotientOneRef,
                multiply1CellRef,
                subtract1CellRef, bringDownCellRef,
                multiply2RowRef,
                remainderCellRef,
            ) = createRefs()

            // Division Bracket
            Image(
                painter = painterResource(id = R.drawable.ic_division_bracket_short),
                contentDescription = "Division Bracket",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.constrainAs(bracketRef) {
                    top.linkTo(parent.top, margin = 60.dp)
                    start.linkTo(parent.start, margin = 60.dp)
                    width = Dimension.value(130.dp)
                    height = Dimension.value(110.dp)
                }
            )

            val minCharWidth = 24.dp
            val numbersStartMargin = 110.dp
            val numbersTopMargin = 95.dp

            // Dividend Row
            val dividendString = uiState.dividend.toString().padStart(2, ' ')
            Row(
                modifier = Modifier.constrainAs(dividendRowRef) {
                    top.linkTo(parent.top, margin = numbersTopMargin)
                    start.linkTo(parent.start, margin = numbersStartMargin)
                    width = Dimension.wrapContent
                },
            ) {
                dividendString.forEachIndexed { _, digit ->
                    Text(
                        text = digit.toString(),
                        fontSize = 40.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .widthIn(min = minCharWidth) // Dividend 각 자리에도 적용
                    )
                }
            }

            // Divisor
            Text(
                text = uiState.divisor.toString(),
                fontSize = 40.sp,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .constrainAs(divisorRef) {
                        baseline.linkTo(dividendRowRef.baseline)
                        end.linkTo(bracketRef.start, margin = (-8).dp)
                        width = Dimension.wrapContent // Divisor는 전체 숫자를 표시하므로 wrapContent 유지 가능
                        // 또는 .widthIn(min = minCharWidth) 적용 고려
                    }
                    // .padding(horizontal = 8.dp) // 필요시 패딩 추가
                    .widthIn(min = minCharWidth) // Divisor에도 일관성 위해 적용
            )

            // Quotient - Tens
            val cellTen = uiState.quotientCells[0]
            Text(
                text = if (cellTen.value.isBlank() && cellTen.editable) "?" else cellTen.value,
                fontSize = 40.sp,
                color = when { cellTen.editable -> Color.Blue; cellTen.correct -> Color.Green; else -> Color.Black },
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .widthIn(min = minCharWidth) // 적용
                    .constrainAs(quotientTenRef) {
                        start.linkTo(dividendRowRef.start)
                        top.linkTo(bracketRef.top, margin = (-50).dp)
                    }
            )

            // Quotient - Ones
            val cellOne = uiState.quotientCells[1]
            Text(
                text = if (cellOne.value.isBlank() && cellOne.editable) "?" else cellOne.value,
                fontSize = 40.sp,
                color = when { cellOne.editable -> Color.Blue; cellOne.correct -> Color.Green; else -> Color.Black },
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .widthIn(min = minCharWidth) // 적용
                    .constrainAs(quotientOneRef) {
                        start.linkTo(quotientTenRef.end)
                        baseline.linkTo(quotientTenRef.baseline)
                    }
            )

            // Multiply 1 Cell
            val mul1 = uiState.multiply1Cell
            Text(
                text = if (mul1.value.isBlank() && mul1.editable) "?" else mul1.value,
                fontSize = 40.sp,
                color = when { mul1.editable -> Color.Green; mul1.correct -> Color.Green; else -> Color.Black },
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .widthIn(min = minCharWidth) // 적용
                    .constrainAs(multiply1CellRef) {
                        top.linkTo(dividendRowRef.bottom, margin = 8.dp)
                        start.linkTo(dividendRowRef.start)
                        // 만약 multiply1Cell이 두자리 이상이 될 수 있다면 end 제약도 고려
                    }
            )

            // Subtract 1 Cell
            val sub1 = uiState.subtract1Cell
            Text(
                text = if (sub1.value.isBlank() && sub1.editable) "?" else sub1.value,
                fontSize = 40.sp,
                color = when { sub1.editable -> Color.Red; sub1.correct -> Color.Green; else -> Color.Black },
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .widthIn(min = minCharWidth) // 적용
                    .constrainAs(subtract1CellRef) {
                        top.linkTo(multiply1CellRef.bottom, margin = 8.dp)
                        start.linkTo(multiply1CellRef.start)
                        // 만약 subtract1Cell이 두자리 이상이 될 수 있다면 end 제약도 고려
                    }
            )

            // Bring Down Cell
            val bringDown = uiState.bringDownCell
            Text(
                text = if (bringDown.value.isBlank() && bringDown.editable) "?" else bringDown.value,
                fontSize = 40.sp,
                color = when { bringDown.editable -> Color.Magenta; bringDown.correct -> Color.Green; else -> Color.Black },
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .widthIn(min = minCharWidth) // 적용
                    .constrainAs(bringDownCellRef) {
                        top.linkTo(subtract1CellRef.top)
                        start.linkTo(quotientOneRef.start)
                    }
            )

            // Multiply 2 Row
            Row(
                modifier = Modifier.constrainAs(multiply2RowRef) {
                    top.linkTo(subtract1CellRef.bottom, margin = 8.dp)
                    end.linkTo(quotientOneRef.end)
                    width = Dimension.wrapContent
                },
                horizontalArrangement = Arrangement.End
            ) {
                val mul2Ten = uiState.multiply2Ten
                val mul2One = uiState.multiply2One

                Text(
                    text = if (mul2Ten.value.isBlank() && mul2Ten.editable) "?" else mul2Ten.value,
                    fontSize = 40.sp,
                    color = when { mul2Ten.editable -> Color.Green; mul2Ten.correct -> Color.Green; else -> Color.Black },
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .widthIn(min = minCharWidth) // 이미 적용되어 있었음
                )
                Text(
                    text = if (mul2One.value.isBlank() && mul2One.editable) "?" else mul2One.value,
                    fontSize = 40.sp,
                    color = when { mul2One.editable -> Color.Green; mul2One.correct -> Color.Green; else -> Color.Black },
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .widthIn(min = minCharWidth) // 이미 적용되어 있었음
                )
            }

            // Remainder Cell
            val remainder = uiState.remainderCell
            Text(
                text = if (remainder.value.isBlank() && remainder.editable) "?" else remainder.value,
                fontSize = 40.sp,
                color = when {
                    remainder.editable -> Color.Magenta; remainder.correct -> Color.Green; else -> Color.Black
                },
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .widthIn(min = minCharWidth) // 적용
                    .constrainAs(remainderCellRef) {
                        top.linkTo(multiply2RowRef.bottom, margin = 8.dp)
                        end.linkTo(multiply2RowRef.end)
                        width = Dimension.wrapContent // 내용에 따라 너비 결정, widthIn이 최소 너비 보장
                    }
            )

        } // end of ConstraintLayout

        // Number pad & feedback (ConstraintLayout 외부, Column 내부에 배치)
        Spacer(modifier = Modifier.weight(1f)) // 남은 공간을 차지하여 NumberPad를 아래로 밀어냄
        Column(
            modifier = Modifier
                .fillMaxWidth() // 너비를 꽉 채움
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally // 내부 아이템들 중앙 정렬
        ) {
            NumberPad(
                onNumber = { viewModel.onDigitInput(it) }, // viewModel 사용 예시
                onClear = { viewModel.onClear() },
                onEnter = { viewModel.onEnter() }
            )
            uiState.feedback?.let {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.primary, // 테마 색상 사용
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    } // end of parent Column
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

// Preview for Divide21Screen
@Preview(showBackground = true)
@Composable
fun PreviewDivisionStageScreen() {
    DivisionStageScreen()
}
