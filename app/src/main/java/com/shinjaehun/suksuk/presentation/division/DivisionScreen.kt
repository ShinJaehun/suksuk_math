package com.shinjaehun.suksuk.presentation.division

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
                divisorRef, bracketRef, dividendRowRef,
                quotientTenRef, quotientOneRef,
                multiply1Ref, subtract1Ref, bringDownRef,
                quotient2Ref, multiply2RowRef, remainderRef
            ) = createRefs()

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
            val dividendString = uiState.dividend.toString().padStart(2, ' ')
            Row(
                modifier = Modifier.constrainAs(dividendRowRef) {
                    top.linkTo(bracketRef.top, margin = 25.dp)
                    start.linkTo(bracketRef.start, margin = 45.dp)
                    width = Dimension.wrapContent
                }
            ) {
                dividendString.forEach { digit ->
                    Text(
                        text = digit.toString(),
                        fontSize = 60.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }

            // Divisor
            Text(
                text = uiState.divisor.toString(),
                fontSize = 60.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.constrainAs(divisorRef) {
                    top.linkTo(dividendRowRef.top)
                    end.linkTo(bracketRef.start, margin = (-10).dp)
                    width = Dimension.wrapContent
                }
            )

            // 몫(십의 자리)
            val cellTen = uiState.quotientCells[0]
            Text(
                text = if (cellTen.value.isBlank() && cellTen.editable) "?" else cellTen.value,
                fontSize = 60.sp,
                color = when {
                    cellTen.editable -> Color.Blue
                    cellTen.correct -> Color.Green
                    else -> Color.Black
                },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .constrainAs(quotientTenRef) {
                        end.linkTo(quotientOneRef.start)
                        baseline.linkTo(quotientOneRef.baseline)
                    }
            )

            // 몫(일의 자리)
            val cellOne = uiState.quotientCells[1]
            Text(
                text = if (cellOne.value.isBlank() && cellOne.editable) "?" else cellOne.value,
                fontSize = 60.sp,
                color = when {
                    cellOne.editable -> Color.Blue
                    cellOne.correct -> Color.Green
                    else -> Color.Black
                },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .constrainAs(quotientOneRef) {
                        end.linkTo(dividendRowRef.end)
                        bottom.linkTo(bracketRef.top, margin = (-2).dp)
                    }
            )

            // 1차 곱셈(7)
            val mul1 = uiState.multiply1Cell
            Text(
                text = if (mul1.value.isBlank() && mul1.editable) "?" else mul1.value,
                fontSize = 36.sp,
                color = when {
                    mul1.editable -> Color.Green
                    mul1.correct -> Color.Green
                    else -> Color.Black
                },
                modifier = Modifier
                    .constrainAs(multiply1Ref) {
                        top.linkTo(dividendRowRef.bottom, margin = 24.dp)
                        start.linkTo(dividendRowRef.start)
                    }
            )

            // 1차 뺄셈(2)
            val sub1 = uiState.subtract1Cell
            Text(
                text = if (sub1.value.isBlank() && sub1.editable) "?" else sub1.value,
                fontSize = 36.sp,
                color = when {
                    sub1.editable -> Color.Red
                    sub1.correct -> Color.Green
                    else -> Color.Black
                },
                modifier = Modifier
                    .constrainAs(subtract1Ref) {
                        top.linkTo(multiply1Ref.bottom, margin = 8.dp)
                        start.linkTo(multiply1Ref.start)
                    }
            )

            // bringDown(2), 뺄셈 오른쪽
            val bringDown = uiState.bringDownCell
            Text(
                text = if (bringDown.value.isBlank() && bringDown.editable) "?" else bringDown.value,
                fontSize = 36.sp,
                color = when {
                    bringDown.editable -> Color.Magenta
                    bringDown.correct -> Color.Green
                    else -> Color.Black
                },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .constrainAs(bringDownRef) {
                        top.linkTo(subtract1Ref.top)
                        start.linkTo(subtract1Ref.end, margin = 16.dp)
                    }
            )

            // 2차 곱셈(21), 두 칸: 22 아래
            Row(
                modifier = Modifier.constrainAs(multiply2RowRef) {
                    top.linkTo(subtract1Ref.bottom, margin = 40.dp)
                    start.linkTo(subtract1Ref.start)
                }
            ) {
                val mul2Ten = uiState.multiply2Ten
                val mul2One = uiState.multiply2One
                Text(
                    text = if (mul2Ten.value.isBlank() && mul2Ten.editable) "?" else mul2Ten.value,
                    fontSize = 36.sp,
                    color = when {
                        mul2Ten.editable -> Color.Green
                        mul2Ten.correct -> Color.Green
                        else -> Color.Black
                    },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Spacer(Modifier.width(20.dp))
                Text(
                    text = if (mul2One.value.isBlank() && mul2One.editable) "?" else mul2One.value,
                    fontSize = 36.sp,
                    color = when {
                        mul2One.editable -> Color.Green
                        mul2One.correct -> Color.Green
                        else -> Color.Black
                    },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            // 나머지(1) 한 칸만!
            val remainderCell = uiState.remainderCell
            Text(
                text = if (remainderCell.value.isBlank() && remainderCell.editable) "?" else remainderCell.value,
                fontSize = 36.sp,
                color = when {
                    remainderCell.editable -> Color.Magenta
                    remainderCell.correct -> Color.Green
                    else -> Color.Black
                },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .constrainAs(remainderRef) {
                        top.linkTo(multiply2RowRef.bottom, margin = 20.dp)
                        start.linkTo(multiply2RowRef.start, margin = 16.dp)
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


// DivisionPhasesState의 현재 단계와 입력값을 기반으로, 화면에서 사용할 InputCell 리스트 생성
fun mapPhasesToCells(state: DivisionPhasesState, currentInput: String): DivisionUiState {
    val inputs = state.inputs
    val phase = state.phases.getOrNull(state.currentPhaseIndex)

    return DivisionUiState(
        divisor = state.divisor,
        dividend = state.dividend,
        quotientCells = listOf(
            InputCell(
                value = if (phase is DivisionPhase.InputQuotientTens && currentInput.isNotEmpty())
                    currentInput else inputs.getOrNull(0) ?: "",
                editable = phase is DivisionPhase.InputQuotientTens
            ),
            InputCell(
                value = if (phase is DivisionPhase.InputQuotientOnes && currentInput.isNotEmpty())
                    currentInput else inputs.getOrNull(4) ?: "",
                editable = phase is DivisionPhase.InputQuotientOnes
            )
        ),
        multiply1Cell = InputCell(
            value = if (phase is DivisionPhase.InputFirstProduct && currentInput.isNotEmpty())
                currentInput else inputs.getOrNull(1) ?: "",
            editable = phase is DivisionPhase.InputFirstProduct
        ),
        subtract1Cell = InputCell(
            value = if (phase is DivisionPhase.InputFirstSubtraction && currentInput.isNotEmpty())
                currentInput else inputs.getOrNull(2) ?: "",
            editable = phase is DivisionPhase.InputFirstSubtraction
        ),
        bringDownCell = InputCell(
            value = if (phase is DivisionPhase.InputBringDown && currentInput.isNotEmpty())
                currentInput else inputs.getOrNull(3) ?: "",
            editable = phase is DivisionPhase.InputBringDown
        ),
        multiply2Ten = InputCell(
            value = if (phase is DivisionPhase.InputSecondProduct && currentInput.isNotEmpty())
                currentInput else inputs.getOrNull(5) ?: "",
            editable = phase is DivisionPhase.InputSecondProduct
        ),
        multiply2One = InputCell(), // 필요시 구현
        subtract2Cell = InputCell(
            value = if (phase is DivisionPhase.InputSecondSubtraction && currentInput.isNotEmpty())
                currentInput else inputs.getOrNull(6) ?: "",
            editable = phase is DivisionPhase.InputSecondSubtraction
        ),
        remainderCell = InputCell(
            value = if (phase is DivisionPhase.Complete && currentInput.isNotEmpty())
                currentInput else inputs.getOrNull(7) ?: "",
            editable = phase is DivisionPhase.Complete
        ),
        stage = state.currentPhaseIndex,
        feedback = state.feedback
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

// Preview for Divide21Screen
//@Preview(showBackground = true)
//@Composable
//fun PreviewDivisionStageScreen() {
//    DivisionScreen()
//}