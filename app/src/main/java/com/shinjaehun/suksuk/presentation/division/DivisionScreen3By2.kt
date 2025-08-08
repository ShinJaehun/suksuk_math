package com.shinjaehun.suksuk.presentation.division

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.shinjaehun.suksuk.R
import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.domain.division.model.DivisionUiStateV2
import com.shinjaehun.suksuk.domain.division.model.InputCellV2
import com.shinjaehun.suksuk.domain.division.model.SubtractLineType


@Composable
fun DivisionScreen3By2(
    uiState: DivisionUiStateV2,
) {

    val cellWidth = 42.dp
    val borrowCellMinWidth = 32.dp

    val bracketStartMargin = 60.dp


    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 30.dp)
    ) {
        val (
            divisorTensRef, divisorOnesRef, divisorTensCarryMul1Ref, divisorTensCarryMul2Ref,
            dividendHundredsRef, dividendTensRef, dividendOnesRef,
        ) = createRefs()

        val (
            quotientTensRef, quotientOnesRef,
            multiply1HundredsRef, multiply1TensRef, multiply1OnesRef,
            subtract1HundredsRef, subtract1TensRef, subtract1OnesRef,
            multiply2HundredsRef, multiply2TensRef, multiply2OnesRef,
            subtract2TensRef, subtract2OnesRef,
        ) = createRefs()


        val (
            dividendHundredsBorrowRef, dividendTensBorrowRef,
            dividendTensBorrowed10Ref, dividendOnesBorrowed10Ref,
            subtract1HundredsBorrowRef, subtract1TensBorrowRef,
            subtract1TensBorrowed10Ref, subtract1OnesBorrowed10Ref,
        ) = createRefs()

        val (bracketRef, subtract1LineRef, subtract2LineRef) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.ic_division_bracket),
            contentDescription = "Division Bracket",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.constrainAs(bracketRef) {
                top.linkTo(parent.top, margin = 70.dp)
                start.linkTo(parent.start, margin = bracketStartMargin)
                width = Dimension.value(200.dp)
                height = Dimension.value(120.dp)
            }
        )

        val dividendHundredsCell = uiState.cells[CellName.DividendHundreds]
            ?: InputCellV2(cellName = CellName.DividendHundreds)
        NumberTextV2(
            cell = dividendHundredsCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(dividendHundredsRef) {
                    top.linkTo(bracketRef.top, margin = 40.dp)
                    start.linkTo(bracketRef.start, margin = 60.dp)
                }
        )

        val dividendHundredsBorrowCell = uiState.cells[CellName.BorrowDividendHundreds]
            ?: InputCellV2(
                cellName = CellName.BorrowDividendHundreds
            )
        BorrowTextV2(
            cell = dividendHundredsBorrowCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(dividendHundredsBorrowRef) {
                    start.linkTo(dividendHundredsRef.start)
                    bottom.linkTo(dividendHundredsRef.top)
                }
        )

        val dividendTensCell = uiState.cells[CellName.DividendTens]
            ?: InputCellV2(cellName = CellName.DividendTens)
        NumberTextV2(
            cell = dividendTensCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(dividendTensRef) {
                    start.linkTo(dividendHundredsRef.end)
                    baseline.linkTo(dividendHundredsRef.baseline)
                }
        )

        val dividendTensBorrowed10Cell = uiState.cells[CellName.Borrowed10DividendTens]
            ?: InputCellV2(cellName = CellName.Borrowed10DividendTens)
        BorrowTextV2(
            cell = dividendTensBorrowed10Cell,
            modifier = Modifier
                .width(borrowCellMinWidth)
                .padding(horizontal = 4.dp)
                .constrainAs(dividendTensBorrowed10Ref) {
                    bottom.linkTo(dividendTensRef.top)
                    start.linkTo(dividendTensRef.start, margin = (-8).dp)
                }
                .testTag("borrowed10-dividend-cell")
        )

        val dividendTensBorrowCell = uiState.cells[CellName.BorrowDividendTens] ?: InputCellV2(
            cellName = CellName.BorrowDividendTens
        )
        BorrowTextV2(
            cell = dividendTensBorrowCell,
            modifier = Modifier
                .width(borrowCellMinWidth)
                .padding(horizontal = 4.dp)
                .constrainAs(dividendTensBorrowRef) {
                    bottom.linkTo(dividendTensRef.top)
                    start.linkTo(dividendTensBorrowed10Ref.end, margin = (-8).dp)
                }
        )

        val dividendOnesCell = uiState.cells[CellName.DividendOnes]
            ?: InputCellV2(cellName = CellName.DividendOnes)
        NumberTextV2(
            cell = dividendOnesCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(dividendOnesRef) {
                    start.linkTo(dividendTensRef.end)
                    baseline.linkTo(dividendHundredsRef.baseline)
                }
        )

        val dividendOnesBorrowed10Cell = uiState.cells[CellName.Borrowed10DividendOnes]
            ?: InputCellV2(cellName = CellName.Borrowed10DividendOnes)
        BorrowTextV2(
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

        val divisorOnesCell =
            uiState.cells[CellName.DivisorOnes] ?: InputCellV2(cellName = CellName.DivisorOnes)
        NumberTextV2(
            cell = divisorOnesCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(divisorOnesRef) {
                    end.linkTo(dividendHundredsRef.start, margin = 40.dp)
                    baseline.linkTo(dividendHundredsRef.baseline)
                }
        )

        val divisorTensCell =
            uiState.cells[CellName.DivisorTens] ?: InputCellV2(cellName = CellName.DivisorTens)
        NumberTextV2(
            cell = divisorTensCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(divisorTensRef) {
                    end.linkTo(divisorOnesRef.start)
                    baseline.linkTo(dividendTensRef.baseline)
                }
        )

        val divisorTensMul1CarryCell = uiState.cells[CellName.CarryDivisorTensMul1] ?: InputCellV2(
            cellName = CellName.CarryDivisorTensMul1
        )
        BorrowTextV2(
            cell = divisorTensMul1CarryCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(divisorTensCarryMul1Ref) {
                    start.linkTo(divisorTensRef.start)
                    bottom.linkTo(divisorTensRef.top)
                }
        )

        val divisorTensMul2CarryCell = uiState.cells[CellName.CarryDivisorTensMul2] ?: InputCellV2(
            cellName = CellName.CarryDivisorTensMul2
        )
        BorrowTextV2(
            cell = divisorTensMul2CarryCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(divisorTensCarryMul2Ref) {
                    start.linkTo(divisorTensRef.start)
                    bottom.linkTo(divisorTensRef.top)
                }
        )

        val quotientTensCell = uiState.cells[CellName.QuotientTens]
            ?: InputCellV2(cellName = CellName.QuotientTens)
        NumberTextV2(
            cell = quotientTensCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(quotientTensRef) {
                    start.linkTo(dividendTensRef.start)
                    bottom.linkTo(dividendTensRef.top, margin = 40.dp)
                }
        )

        val quotientOnesCell = uiState.cells[CellName.QuotientOnes]
            ?: InputCellV2(cellName = CellName.QuotientOnes)
        NumberTextV2(
            cell = quotientOnesCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(quotientOnesRef) {
                    start.linkTo(quotientTensRef.end)
                    baseline.linkTo(quotientTensRef.baseline)
                }
        )

        val multiply1HundredsCell = uiState.cells[CellName.Multiply1Hundreds]
            ?: InputCellV2(cellName = CellName.Multiply1Hundreds)
        NumberTextV2(
            cell = multiply1HundredsCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(multiply1HundredsRef) {
                    top.linkTo(dividendHundredsRef.bottom, margin = 10.dp)
                    start.linkTo(dividendHundredsRef.start)
                }
        )

        val multiply1TensCell = uiState.cells[CellName.Multiply1Tens]
            ?: InputCellV2(cellName = CellName.Multiply1Tens)
        NumberTextV2(
            cell = multiply1TensCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(multiply1TensRef) {
                    start.linkTo(multiply1HundredsRef.end)
                    baseline.linkTo(multiply1HundredsRef.baseline)
                }
        )

        val multiply1OnesCell = uiState.cells[CellName.Multiply1Ones]
            ?: InputCellV2(cellName = CellName.Multiply1Ones)
        NumberTextV2(
            cell = multiply1OnesCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(multiply1OnesRef) {
                    start.linkTo(multiply1TensRef.end)
                    baseline.linkTo(multiply1HundredsRef.baseline)
                }
        )

        if (uiState.cells.values.any { it.subtractLineType == SubtractLineType.SubtractLine1 }) {
            Image(
                painter = painterResource(id = R.drawable.ic_horizontal_line),
                contentDescription = "Subtraction Line 1",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .constrainAs(subtract1LineRef) {
                        top.linkTo(dividendTensRef.bottom, margin = 60.dp)
                        start.linkTo(dividendTensRef.start, margin = (-10).dp)
                        width = Dimension.value(100.dp)
                        height = Dimension.value(4.dp)
                    }
                    .testTag("subtraction1-line")
            )
        }

        val subtract1HundredsCell = uiState.cells[CellName.Subtract1Hundreds]
            ?: InputCellV2(cellName = CellName.Subtract1Hundreds)
        NumberTextV2(
            cell = subtract1HundredsCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(subtract1HundredsRef) {
                    top.linkTo(dividendHundredsRef.bottom, margin = 90.dp)
                    start.linkTo(dividendHundredsRef.start)
                }
        )

        val subtract1HundredsBorrowCell = uiState.cells[CellName.BorrowSubtract1Hundreds] ?: InputCellV2(
            cellName = CellName.BorrowSubtract1Hundreds
        )
        BorrowTextV2(
            cell = subtract1HundredsBorrowCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(subtract1HundredsBorrowRef) {
                    start.linkTo(subtract1HundredsRef.start)
                    bottom.linkTo(subtract1HundredsRef.top)
                }
        )

        val subtract1TensCell = uiState.cells[CellName.Subtract1Tens]
            ?: InputCellV2(cellName = CellName.Subtract1Tens)
        NumberTextV2(
            cell = subtract1TensCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(subtract1TensRef) {
                    top.linkTo(dividendTensRef.bottom, margin = 90.dp)
                    start.linkTo(dividendTensRef.start)
                }
        )

        val subtract1TensBorrowed10Cell = uiState.cells[CellName.Borrowed10Subtract1Tens] ?: InputCellV2(
            cellName = CellName.Borrowed10Subtract1Tens
        )
        BorrowTextV2(
            cell = subtract1TensBorrowed10Cell,
            modifier = Modifier
                .width(borrowCellMinWidth)
                .padding(horizontal = 4.dp)
                .constrainAs(subtract1TensBorrowed10Ref) {
                    start.linkTo(subtract1TensRef.start, margin = (-8).dp)
                    bottom.linkTo(subtract1TensRef.top)
                }
        )

        val subtract1TensBorrowCell = uiState.cells[CellName.BorrowSubtract1Tens] ?: InputCellV2(
            cellName = CellName.BorrowSubtract1Tens
        )
        BorrowTextV2(
            cell = subtract1TensBorrowCell,
            modifier = Modifier
                .width(borrowCellMinWidth)
                .padding(horizontal = 4.dp)
                .constrainAs(subtract1TensBorrowRef) {
                    start.linkTo(subtract1TensBorrowed10Ref.end, margin = (-8).dp)
                    bottom.linkTo(subtract1TensRef.top)
                }
        )


        val subtract1OnesCell = uiState.cells[CellName.Subtract1Ones]
            ?: InputCellV2(cellName = CellName.Subtract1Ones)
        NumberTextV2(
            cell = subtract1OnesCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(subtract1OnesRef) {
                    start.linkTo(subtract1TensRef.end)
                    baseline.linkTo(subtract1TensRef.baseline)
                }
        )

        val subtract1Borrowed10Cell = uiState.cells[CellName.Borrowed10Subtract1Ones]
            ?: InputCellV2(cellName = CellName.Borrowed10Subtract1Ones)
        BorrowTextV2(
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

        val multiply2HundredsCell = uiState.cells[CellName.Multiply2Hundreds]
            ?: InputCellV2(cellName = CellName.Multiply2Hundreds)
        NumberTextV2(
            cell = multiply2HundredsCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(multiply2HundredsRef) {
                    top.linkTo(dividendHundredsRef.bottom, margin = 150.dp)
                    start.linkTo(dividendHundredsRef.start)
                }
        )

        val multiply2TensCell = uiState.cells[CellName.Multiply2Tens]
            ?: InputCellV2(cellName = CellName.Multiply2Tens)
        NumberTextV2(
            cell = multiply2TensCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(multiply2TensRef) {
                    start.linkTo(multiply2HundredsRef.end)
                    baseline.linkTo(multiply2HundredsRef.baseline)
                }
        )

        val multiply2OnesCell = uiState.cells[CellName.Multiply2Ones]
            ?: InputCellV2(cellName = CellName.Multiply2Ones)
        NumberTextV2(
            cell = multiply2OnesCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(multiply2OnesRef) {
                    start.linkTo(multiply2TensRef.end)
                    baseline.linkTo(multiply2HundredsRef.baseline)
                }
        )

        if (uiState.cells.values.any { it.subtractLineType == SubtractLineType.SubtractLine2 }) {
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
                    .testTag("subtraction2-line")
            )
        }

        val subtract2TensCell = uiState.cells[CellName.Subtract2Tens]
            ?: InputCellV2(cellName = CellName.Subtract2Tens)
        NumberTextV2(
            cell = subtract2TensCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(subtract2TensRef) {
                    top.linkTo(dividendTensRef.bottom, margin = 220.dp)
                    start.linkTo(dividendTensRef.start)
                }
        )

        val subtract2OnesCell = uiState.cells[CellName.Subtract2Ones]
            ?: InputCellV2(cellName = CellName.Subtract2Ones)
        NumberTextV2(
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

}

@Preview(showBackground = true)
@Composable
fun PreviewDivisionStageScreen() {
    val allCells = listOf(
        CellName.DividendHundreds,
        CellName.DividendTens,
        CellName.DividendOnes,

        CellName.DivisorTens,
        CellName.DivisorOnes,

        CellName.QuotientTens,
        CellName.QuotientOnes,

        CellName.Multiply1Hundreds,
        CellName.Multiply1Tens,
        CellName.Multiply1Ones,

        CellName.Subtract1Hundreds,
        CellName.Subtract1Tens,
        CellName.Subtract1Ones,

        CellName.Multiply2Hundreds,
        CellName.Multiply2Tens,
        CellName.Multiply2Ones,

        CellName.Subtract2Tens,
        CellName.Subtract2Ones,

        CellName.BorrowDividendHundreds,
        CellName.BorrowDividendTens,
        CellName.Borrowed10DividendTens,
        CellName.Borrowed10DividendOnes,

        CellName.BorrowSubtract1Hundreds,
        CellName.BorrowSubtract1Tens,
        CellName.Borrowed10Subtract1Tens,
        CellName.Borrowed10Subtract1Ones,

        CellName.CarryDivisorTensMul1
    )

    val fakeUiState = DivisionUiStateV2(
        cells = allCells.associateWith { cellName ->
            InputCellV2(cellName = cellName, value = "?")
        },
        feedback = null // 또는 "정답입니다!" 등
    )

    val newUiState = fakeUiState.copy(
        cells = fakeUiState.cells.toMutableMap().apply {
            this[CellName.Borrowed10DividendTens] = InputCellV2(cellName = CellName.Borrowed10DividendTens, value = "5")
        }
    )
    DivisionScreen3By2(
        uiState = newUiState,
    )
}