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

            // Division Bracket (나누기 기호)
            Image(
                painter = painterResource(id = R.drawable.ic_division_bracket_short), // 실제 리소스 확인
                contentDescription = "Division Bracket",
                contentScale = ContentScale.FillBounds, // 비율 유지를 위해 Fit 또는 Crop 고려
                modifier = Modifier.constrainAs(bracketRef) {
                    top.linkTo(parent.top, margin = 60.dp)     // 화면 상단에서 70dp 아래
                    start.linkTo(parent.start, margin = 60.dp) // 화면 좌측에서 60dp 오른쪽
                    width = Dimension.value(130.dp)            // 너비 150dp
                    height = Dimension.value(110.dp)           // 높이 120dp
                }
            )

            // 숫자들의 시작 위치를 결정할 기준 마진
            val numbersStartMargin = 110.dp
            val numbersTopMargin = 95.dp   // 이 값을 조절하여 숫자 그룹 전체의 세로 위치 변경

            // Dividend Row (피제수)
            val dividendString = uiState.dividend.toString().padStart(2, ' ')
            Row(
                modifier = Modifier.constrainAs(dividendRowRef) {
                    top.linkTo(parent.top, margin = numbersTopMargin) // 세로 위치 마진 적용
                    start.linkTo(parent.start, margin = numbersStartMargin) // 가로 위치 마진 적용
                    width = Dimension.wrapContent
                },
            ) {
                dividendString.forEachIndexed { index, digit ->
                    Text(
                        text = digit.toString(),
                        fontSize = 40.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }

            // Divisor (제수)
            Text(
                text = uiState.divisor.toString(),
                fontSize = 40.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.constrainAs(divisorRef) {
                    baseline.linkTo(dividendRowRef.baseline) // 피제수와 기준선 정렬
                    end.linkTo(bracketRef.start, margin = (-8).dp) // 브라켓 시작점에서 8dp 왼쪽
                    width = Dimension.wrapContent
                }
            )

            // Quotient (몫) - 십의 자리 (bracketRef보다 먼저 정의되어야 bracketRef.end에서 참조 가능할 수 있음)
            // 또는 bracketRef.end를 dividendRowRef.end 기준으로 설정
            val cellTen = uiState.quotientCells[0]
            Text(
                text = if (cellTen.value.isBlank() && cellTen.editable) "?" else cellTen.value,
                fontSize = 40.sp,
                color = when { cellTen.editable -> Color.Blue; cellTen.correct -> Color.Green; else -> Color.Black },
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp).constrainAs(quotientTenRef) {
                    start.linkTo(dividendRowRef.start) // 피제수 십의 자리 위
                    top.linkTo(bracketRef.top, margin = (-50).dp) // 피제수 바로 위
                }
            )

            // Quotient (몫) - 일의 자리
            val cellOne = uiState.quotientCells[1]
            Text(
                text = if (cellOne.value.isBlank() && cellOne.editable) "?" else cellOne.value,
                fontSize = 40.sp,
                color = when { cellOne.editable -> Color.Blue; cellOne.correct -> Color.Green; else -> Color.Black },
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp).constrainAs(quotientOneRef) {
                    start.linkTo(quotientTenRef.end) // 몫 십의 자리 오른쪽
                    baseline.linkTo(quotientTenRef.baseline) // 같은 높이
                }
            )

            // --- 나머지 계산 과정들 ---
            // 1차 곱셈 결과 (multiply1CellRef)
            val mul1 = uiState.multiply1Cell
            Text(
                text = if (mul1.value.isBlank() && mul1.editable) "?" else mul1.value,
                fontSize = 40.sp,
                color = when { mul1.editable -> Color.Green; mul1.correct -> Color.Green; else -> Color.Black },
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp).constrainAs(multiply1CellRef) {
                    top.linkTo(dividendRowRef.bottom, margin = 8.dp)
                    start.linkTo(dividendRowRef.start)
                }
            )

            // 1차 뺄셈 결과 (subtract1CellRef)
            val sub1 = uiState.subtract1Cell
            Text(
                text = if (sub1.value.isBlank() && sub1.editable) "?" else sub1.value,
                fontSize = 40.sp,
                color = when { sub1.editable -> Color.Red; sub1.correct -> Color.Green; else -> Color.Black },
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp).constrainAs(subtract1CellRef) {
                    top.linkTo(multiply1CellRef.bottom, margin = 8.dp)
                    start.linkTo(multiply1CellRef.start)
                }
            )

            // 내려쓴 숫자 (bringDownCellRef)
            val bringDown = uiState.bringDownCell
            Text(
                text = if (bringDown.value.isBlank() && bringDown.editable) "?" else bringDown.value,
                fontSize = 40.sp,
                color = when { bringDown.editable -> Color.Magenta; bringDown.correct -> Color.Green; else -> Color.Black },
                textAlign = TextAlign.Center, // 중앙 정렬 추가
                modifier = Modifier
                    .padding(horizontal = 8.dp) // 좌우 패딩은 유지
                    .constrainAs(bringDownCellRef) {
                        top.linkTo(subtract1CellRef.top) // 1차 뺄셈 결과와 같은 높이

                        // 시작점을 몫의 일의 자리(quotientOneRef)의 시작점과 연결하여
                        // 피제수의 일의 자리와 수직 정렬되도록 함
                        start.linkTo(quotientOneRef.start)

                        // 이전에 있던 end 제약은 제거하거나, 필요시 quotientOneRef.end로 변경
                        // end.linkTo(quotientOneRef.end) // 주석 처리 또는 삭제
                    }
            )

            // 2차 곱셈 결과 (multiply2RowRef)
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
                val minCharWidth = 24.dp // 예시 값, 40.sp 폰트와 8.dp 패딩 고려하여 조정

                Text(
                    text = if (mul2Ten.value.isBlank() && mul2Ten.editable) "?" else mul2Ten.value,
                    fontSize = 40.sp,
                    color = when { mul2Ten.editable -> Color.Green; mul2Ten.correct -> Color.Green; else -> Color.Black },
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .widthIn(min = minCharWidth) // 최소 너비
                )
                Text(
                    text = if (mul2One.value.isBlank() && mul2One.editable) "?" else mul2One.value,
                    fontSize = 40.sp,
                    color = when { mul2One.editable -> Color.Green; mul2One.correct -> Color.Green; else -> Color.Black },
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .widthIn(min = minCharWidth) // 최소 너비
                )
            }

            // 나머지 (remainderCellRef)
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
                    .constrainAs(remainderCellRef) {
                        top.linkTo(multiply2RowRef.bottom, margin = 8.dp) // 2차 곱셈 결과 아래
                        end.linkTo(multiply2RowRef.end) // 2차 곱셈 결과와 오른쪽 끝 정렬
                        width = Dimension.wrapContent
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
