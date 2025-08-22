package com.shinjaehun.suksuk.presentation.division

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.shinjaehun.suksuk.R
import com.shinjaehun.suksuk.domain.division.model.DivisionCell
import com.shinjaehun.suksuk.domain.pattern.DivisionPattern
import com.shinjaehun.suksuk.presentation.division.model.DivisionInputCell
import com.shinjaehun.suksuk.presentation.division.model.DivisionUiState
import com.shinjaehun.suksuk.presentation.division.model.SubtractLineType

@Composable
fun DivisionBoard3By2(
    uiState: DivisionUiState,
    modifier: Modifier = Modifier
) {
    val cellWidth = 42.dp
    val borrowCellMinWidth = 32.dp

    val bracketStartMargin = 60.dp

    ConstraintLayout(
        modifier = modifier
//            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
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

        uiState.cells[DivisionCell.DividendHundreds]?.let { c ->
            DivNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(dividendHundredsRef) {
                        top.linkTo(bracketRef.top, margin = 40.dp)
                        start.linkTo(bracketRef.start, margin = 60.dp)
                    }
            )
        }

        uiState.cells[DivisionCell.BorrowDividendHundreds]?.let { c ->
            DivAuxNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(dividendHundredsBorrowRef) {
                        start.linkTo(dividendHundredsRef.start)
                        bottom.linkTo(dividendHundredsRef.top)
                    }
            )
        }

        uiState.cells[DivisionCell.DividendTens]?.let { c ->
            DivNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(dividendTensRef) {
                        start.linkTo(dividendHundredsRef.end)
                        baseline.linkTo(dividendHundredsRef.baseline)
                    }
            )
        }

        uiState.cells[DivisionCell.Borrowed10DividendTens]?.let { c ->
            DivAuxNumberText(
                cell = c,
                modifier = Modifier
                    .width(borrowCellMinWidth)
                    .padding(horizontal = 4.dp)
                    .constrainAs(dividendTensBorrowed10Ref) {
                        bottom.linkTo(dividendTensRef.top)
                        start.linkTo(dividendTensRef.start, margin = (-8).dp)
                    }
                    .semantics { testTag = "borrowed10-dividend-tens-cell" }
            )
        }

        uiState.cells[DivisionCell.BorrowDividendTens]?.let { c ->
            DivAuxNumberText(
                cell = c,
                modifier = Modifier
                    .width(borrowCellMinWidth)
                    .padding(horizontal = 4.dp)
                    .constrainAs(dividendTensBorrowRef) {
                        bottom.linkTo(dividendTensRef.top)
                        start.linkTo(dividendTensBorrowed10Ref.end, margin = (-8).dp)
                    }
            )
        }

        uiState.cells[DivisionCell.DividendOnes]?.let { c ->
            DivNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(dividendOnesRef) {
                        start.linkTo(dividendTensRef.end)
                        baseline.linkTo(dividendHundredsRef.baseline)
                    }
            )
        }

        uiState.cells[DivisionCell.Borrowed10DividendOnes]?.let { c ->
            DivAuxNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(dividendOnesBorrowed10Ref) {
                        start.linkTo(dividendOnesRef.start)
                        bottom.linkTo(dividendOnesRef.top)
                    }
                    .semantics { testTag = "borrowed10-dividend-ones-cell" }
            )
        }

        uiState.cells[DivisionCell.DivisorOnes]?.let { c ->
            DivNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(divisorOnesRef) {
                        end.linkTo(dividendHundredsRef.start, margin = 40.dp)
                        baseline.linkTo(dividendHundredsRef.baseline)
                    }
            )
        }

        uiState.cells[DivisionCell.DivisorTens]?.let { c ->
            DivNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(divisorTensRef) {
                        end.linkTo(divisorOnesRef.start)
                        top.linkTo(divisorOnesRef.top)
                    }
            )
        }

        uiState.cells[DivisionCell.CarryDivisorTensM1]?.let { c ->
            DivAuxNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(divisorTensCarryMul1Ref) {
                        start.linkTo(divisorTensRef.start)
                        bottom.linkTo(divisorTensRef.top)
                    }
            )
        }

        uiState.cells[DivisionCell.CarryDivisorTensM2]?.let { c ->
            DivAuxNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(divisorTensCarryMul2Ref) {
                        start.linkTo(divisorTensRef.start)
                        bottom.linkTo(divisorTensRef.top)
                    }
            )
        }

        uiState.cells[DivisionCell.QuotientTens]?.let { c ->
            DivNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(quotientTensRef) {
                        start.linkTo(dividendTensRef.start)
                        bottom.linkTo(dividendTensRef.top, margin = 40.dp)
                    }
            )
        }

        uiState.cells[DivisionCell.QuotientOnes]?.let { c ->
            DivNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(quotientOnesRef) {
                        start.linkTo(dividendOnesRef.start)
                        bottom.linkTo(dividendTensRef.top, margin = 40.dp)
                    }
            )
        }

        uiState.cells[DivisionCell.Multiply1Hundreds]?.let { c ->
            DivNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(multiply1HundredsRef) {
                        top.linkTo(dividendHundredsRef.bottom, margin = 10.dp)
                        start.linkTo(dividendHundredsRef.start)
                    }
            )
        }

        uiState.cells[DivisionCell.Multiply1Tens]?.let { c ->
            DivNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(multiply1TensRef) {
                        start.linkTo(multiply1HundredsRef.end)
                        baseline.linkTo(multiply1HundredsRef.baseline)
                    }
            )
        }

        uiState.cells[DivisionCell.Multiply1Ones]?.let { c ->
            DivNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(multiply1OnesRef) {
                        start.linkTo(multiply1TensRef.end)
                        baseline.linkTo(multiply1HundredsRef.baseline)
                    }
            )
        }

        if (uiState.cells.values.any { it.subtractLineType == SubtractLineType.SubtractLine1 }) {
            Image(
                painter = painterResource(id = R.drawable.ic_horizontal_line),
                contentDescription = "Subtraction Line 1",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .constrainAs(subtract1LineRef) {
                        top.linkTo(dividendHundredsRef.bottom, margin = 60.dp)
                        start.linkTo(dividendHundredsRef.start, margin = (-10).dp)
                        width = Dimension.value(150.dp)
                        height = Dimension.value(4.dp)
                    }
                    .semantics { testTag = "subtraction1-line" }
            )
        }

        uiState.cells[DivisionCell.Subtract1Hundreds]?.let { c ->
            DivNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(subtract1HundredsRef) {
                        top.linkTo(dividendHundredsRef.bottom, margin = 90.dp)
                        start.linkTo(dividendHundredsRef.start)
                    }
            )
        }

        uiState.cells[DivisionCell.BorrowSubtract1Hundreds]?.let { c ->
            DivAuxNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(subtract1HundredsBorrowRef) {
                        start.linkTo(subtract1HundredsRef.start)
                        bottom.linkTo(subtract1HundredsRef.top)
                    }
            )
        }

        uiState.cells[DivisionCell.Subtract1Tens]?.let { c ->
            DivNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(subtract1TensRef) {
                        top.linkTo(dividendTensRef.bottom, margin = 90.dp)
                        start.linkTo(dividendTensRef.start)
                    }
            )
        }

        uiState.cells[DivisionCell.Borrowed10Subtract1Tens]?.let { c ->
            DivAuxNumberText(
                cell = c,
                modifier = Modifier
                    .width(borrowCellMinWidth)
                    .padding(horizontal = 4.dp)
                    .constrainAs(subtract1TensBorrowed10Ref) {
                        start.linkTo(subtract1TensRef.start, margin = (-10).dp)
                        bottom.linkTo(subtract1TensRef.top)
                    }
            )
        }

        uiState.cells[DivisionCell.BorrowSubtract1Tens]?.let { c ->
            DivAuxNumberText(
                cell = c,
                modifier = Modifier
                    .width(borrowCellMinWidth)
                    .padding(horizontal = 4.dp)
                    .constrainAs(subtract1TensBorrowRef) {
                        start.linkTo(subtract1TensBorrowed10Ref.end, margin = (-6).dp)
                        bottom.linkTo(subtract1TensRef.top)
                    }
            )
        }

        uiState.cells[DivisionCell.Subtract1Ones]?.let { c ->
            DivNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(subtract1OnesRef) {
                        start.linkTo(subtract1TensRef.end)
                        baseline.linkTo(subtract1TensRef.baseline)
                    }
            )
        }

        uiState.cells[DivisionCell.Borrowed10Subtract1Ones]?.let { c ->
            DivAuxNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(subtract1OnesBorrowed10Ref) {
                        start.linkTo(subtract1OnesRef.start)
                        bottom.linkTo(subtract1OnesRef.top)
                    }
                    .semantics { testTag = "borrowed10-sub1-cell" }
            )
        }

        uiState.cells[DivisionCell.Multiply2Hundreds]?.let { c ->
            DivNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(multiply2HundredsRef) {
                        top.linkTo(dividendHundredsRef.bottom, margin = 150.dp)
                        start.linkTo(dividendHundredsRef.start)
                    }
            )
        }

        uiState.cells[DivisionCell.Multiply2Tens]?.let { c ->
            DivNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(multiply2TensRef) {
                        start.linkTo(multiply2HundredsRef.end)
                        baseline.linkTo(multiply2HundredsRef.baseline)
                    }
            )
        }

        uiState.cells[DivisionCell.Multiply2Ones]?.let { c ->
            DivNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(multiply2OnesRef) {
                        start.linkTo(multiply2TensRef.end)
                        baseline.linkTo(multiply2HundredsRef.baseline)
                    }
            )
        }

        if (uiState.cells.values.any { it.subtractLineType == SubtractLineType.SubtractLine2 }) {
            Image(
                painter = painterResource(id = R.drawable.ic_horizontal_line),
                contentDescription = "Subtraction Line",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .constrainAs(subtract2LineRef) {
                        top.linkTo(dividendHundredsRef.bottom, margin = 200.dp)
                        start.linkTo(dividendHundredsRef.start, margin = (-10).dp)
                        width = Dimension.value(150.dp)
                        height = Dimension.value(4.dp)
                    }
                    .semantics { testTag = "subtraction2-line" }
            )
        }

        uiState.cells[DivisionCell.Subtract2Tens]?.let { c ->
            DivNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(subtract2TensRef) {
                        top.linkTo(dividendTensRef.bottom, margin = 220.dp)
                        start.linkTo(dividendTensRef.start)
                    }
            )
        }

        uiState.cells[DivisionCell.Subtract2Ones]?.let { c ->
            DivNumberText(
                cell = c,
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
}

@Preview(showBackground = true)
@Composable
fun PreviewDivisionStageScreen() {
    val allCells = listOf(
        DivisionCell.DividendHundreds,
        DivisionCell.DividendTens,
        DivisionCell.DividendOnes,

        DivisionCell.DivisorTens,
        DivisionCell.DivisorOnes,

        DivisionCell.QuotientTens,
        DivisionCell.QuotientOnes,

        DivisionCell.Multiply1Hundreds,
        DivisionCell.Multiply1Tens,
        DivisionCell.Multiply1Ones,

        DivisionCell.Subtract1Hundreds,
        DivisionCell.Subtract1Tens,
        DivisionCell.Subtract1Ones,

        DivisionCell.Multiply2Hundreds,
        DivisionCell.Multiply2Tens,
        DivisionCell.Multiply2Ones,

        DivisionCell.Subtract2Tens,
        DivisionCell.Subtract2Ones,

        DivisionCell.BorrowDividendHundreds,
        DivisionCell.BorrowDividendTens,
        DivisionCell.Borrowed10DividendTens,
        DivisionCell.Borrowed10DividendOnes,

        DivisionCell.BorrowSubtract1Hundreds,
        DivisionCell.BorrowSubtract1Tens,
        DivisionCell.Borrowed10Subtract1Tens,
        DivisionCell.Borrowed10Subtract1Ones,

        DivisionCell.CarryDivisorTensM1,
        DivisionCell.CarryDivisorTensM2
    )

    val fakeUiState = DivisionUiState(
        cells = allCells.associateWith { cellName ->
            DivisionInputCell(cellName = cellName, value = "?")
        },
        pattern = DivisionPattern.ThreeByTwo,
    )

    val newUiState = fakeUiState.copy(
        cells = fakeUiState.cells.toMutableMap().apply {
            this[DivisionCell.Borrowed10Subtract1Tens] =
                DivisionInputCell(
                    cellName = DivisionCell.Borrowed10Subtract1Tens,
                    value = "10"
                )
        }
    )
    DivisionBoard3By2(
        uiState = newUiState,
    )
}