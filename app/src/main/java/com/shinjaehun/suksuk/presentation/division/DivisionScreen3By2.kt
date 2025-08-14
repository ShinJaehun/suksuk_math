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
import com.shinjaehun.suksuk.domain.division.model.DivisionCellName
import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2
import com.shinjaehun.suksuk.presentation.division.component.BorrowTextV2
import com.shinjaehun.suksuk.presentation.division.component.NumberTextV2
import com.shinjaehun.suksuk.presentation.division.model.DivisionInputCellV2
import com.shinjaehun.suksuk.presentation.division.model.DivisionUiStateV2
import com.shinjaehun.suksuk.presentation.division.model.SubtractLineType


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

        val dividendHundredsCell =
            uiState.cells[DivisionCellName.DividendHundreds] ?:
            DivisionInputCellV2(cellName = DivisionCellName.DividendHundreds)
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

        val dividendHundredsBorrowCell =
            uiState.cells[DivisionCellName.BorrowDividendHundreds] ?:
            DivisionInputCellV2(cellName = DivisionCellName.BorrowDividendHundreds)
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

        val dividendTensCell =
            uiState.cells[DivisionCellName.DividendTens] ?:
            DivisionInputCellV2(cellName = DivisionCellName.DividendTens)
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

        val dividendTensBorrowed10Cell =
            uiState.cells[DivisionCellName.Borrowed10DividendTens] ?:
            DivisionInputCellV2(cellName = DivisionCellName.Borrowed10DividendTens)
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

        val dividendTensBorrowCell =
            uiState.cells[DivisionCellName.BorrowDividendTens] ?:
            DivisionInputCellV2(cellName = DivisionCellName.BorrowDividendTens)
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

        val dividendOnesCell =
            uiState.cells[DivisionCellName.DividendOnes] ?:
            DivisionInputCellV2(cellName = DivisionCellName.DividendOnes)
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

        val dividendOnesBorrowed10Cell =
            uiState.cells[DivisionCellName.Borrowed10DividendOnes] ?:
            DivisionInputCellV2(cellName = DivisionCellName.Borrowed10DividendOnes)
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
            uiState.cells[DivisionCellName.DivisorOnes] ?: DivisionInputCellV2(cellName = DivisionCellName.DivisorOnes)
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
            uiState.cells[DivisionCellName.DivisorTens] ?: DivisionInputCellV2(cellName = DivisionCellName.DivisorTens)
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

        val divisorTensMul1CarryCell =
            uiState.cells[DivisionCellName.CarryDivisorTensM1] ?:
            DivisionInputCellV2(cellName = DivisionCellName.CarryDivisorTensM1)
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

        val divisorTensMul2CarryCell =
            uiState.cells[DivisionCellName.CarryDivisorTensM2] ?:
            DivisionInputCellV2(cellName = DivisionCellName.CarryDivisorTensM2)
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

        val quotientTensCell =
            uiState.cells[DivisionCellName.QuotientTens] ?:
            DivisionInputCellV2(cellName = DivisionCellName.QuotientTens)
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

        val quotientOnesCell =
            uiState.cells[DivisionCellName.QuotientOnes] ?:
            DivisionInputCellV2(cellName = DivisionCellName.QuotientOnes)
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

        val multiply1HundredsCell = uiState.cells[DivisionCellName.Multiply1Hundreds] ?:
            DivisionInputCellV2(cellName = DivisionCellName.Multiply1Hundreds)
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

        val multiply1TensCell = uiState.cells[DivisionCellName.Multiply1Tens] ?:
            DivisionInputCellV2(cellName = DivisionCellName.Multiply1Tens)
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

        val multiply1OnesCell = uiState.cells[DivisionCellName.Multiply1Ones] ?:
            DivisionInputCellV2(cellName = DivisionCellName.Multiply1Ones)
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
                        top.linkTo(dividendHundredsRef.bottom, margin = 60.dp)
                        start.linkTo(dividendHundredsRef.start, margin = (-10).dp)
                        width = Dimension.value(150.dp)
                        height = Dimension.value(4.dp)
                    }
                    .testTag("subtraction1-line")
            )
        }

        val subtract1HundredsCell = uiState.cells[DivisionCellName.Subtract1Hundreds] ?:
            DivisionInputCellV2(cellName = DivisionCellName.Subtract1Hundreds)
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

        val subtract1HundredsBorrowCell =
            uiState.cells[DivisionCellName.BorrowSubtract1Hundreds] ?:
            DivisionInputCellV2(cellName = DivisionCellName.BorrowSubtract1Hundreds)
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

        val subtract1TensCell = uiState.cells[DivisionCellName.Subtract1Tens] ?:
            DivisionInputCellV2(cellName = DivisionCellName.Subtract1Tens)
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

        val subtract1TensBorrowed10Cell =
            uiState.cells[DivisionCellName.Borrowed10Subtract1Tens] ?:
            DivisionInputCellV2(cellName = DivisionCellName.Borrowed10Subtract1Tens)
        BorrowTextV2(
            cell = subtract1TensBorrowed10Cell,
            modifier = Modifier
                .width(borrowCellMinWidth)
                .padding(horizontal = 4.dp)
                .constrainAs(subtract1TensBorrowed10Ref) {
                    start.linkTo(subtract1TensRef.start, margin = (-10).dp)
                    bottom.linkTo(subtract1TensRef.top)
                }
        )

        val subtract1TensBorrowCell =
            uiState.cells[DivisionCellName.BorrowSubtract1Tens] ?:
            DivisionInputCellV2(cellName = DivisionCellName.BorrowSubtract1Tens)
        BorrowTextV2(
            cell = subtract1TensBorrowCell,
            modifier = Modifier
                .width(borrowCellMinWidth)
                .padding(horizontal = 4.dp)
                .constrainAs(subtract1TensBorrowRef) {
                    start.linkTo(subtract1TensBorrowed10Ref.end, margin = (-6).dp)
                    bottom.linkTo(subtract1TensRef.top)
                }
        )


        val subtract1OnesCell = uiState.cells[DivisionCellName.Subtract1Ones] ?:
            DivisionInputCellV2(cellName = DivisionCellName.Subtract1Ones)
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

        val subtract1OnesBorrowed10Cell =
            uiState.cells[DivisionCellName.Borrowed10Subtract1Ones] ?:
            DivisionInputCellV2(cellName = DivisionCellName.Borrowed10Subtract1Ones)
        BorrowTextV2(
            cell = subtract1OnesBorrowed10Cell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(subtract1OnesBorrowed10Ref) {
                    start.linkTo(subtract1OnesRef.start)
                    bottom.linkTo(subtract1OnesRef.top)
                }
                .testTag("borrowed10-sub1-cell")
        )

        val multiply2HundredsCell = uiState.cells[DivisionCellName.Multiply2Hundreds] ?:
            DivisionInputCellV2(cellName = DivisionCellName.Multiply2Hundreds)
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

        val multiply2TensCell = uiState.cells[DivisionCellName.Multiply2Tens] ?:
            DivisionInputCellV2(cellName = DivisionCellName.Multiply2Tens)
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

        val multiply2OnesCell = uiState.cells[DivisionCellName.Multiply2Ones] ?:
            DivisionInputCellV2(cellName = DivisionCellName.Multiply2Ones)
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
                    top.linkTo(dividendHundredsRef.bottom, margin = 200.dp)
                    start.linkTo(dividendHundredsRef.start, margin = (-10).dp)
                    width = Dimension.value(150.dp)
                    height = Dimension.value(4.dp)
                }
                    .testTag("subtraction2-line")
            )
        }

        val subtract2TensCell = uiState.cells[DivisionCellName.Subtract2Tens] ?:
            DivisionInputCellV2(cellName = DivisionCellName.Subtract2Tens)
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

        val subtract2OnesCell = uiState.cells[DivisionCellName.Subtract2Ones] ?:
            DivisionInputCellV2(cellName = DivisionCellName.Subtract2Ones)
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

//@Preview(showBackground = true)
//@Composable
//fun PreviewDivisionStageScreen() {
//    val allCells = listOf(
//        DivisionCellName.DividendHundreds,
//        DivisionCellName.DividendTens,
//        DivisionCellName.DividendOnes,
//
//        DivisionCellName.DivisorTens,
//        DivisionCellName.DivisorOnes,
//
//        DivisionCellName.QuotientTens,
//        DivisionCellName.QuotientOnes,
//
//        DivisionCellName.Multiply1Hundreds,
//        DivisionCellName.Multiply1Tens,
//        DivisionCellName.Multiply1Ones,
//
//        DivisionCellName.Subtract1Hundreds,
//        DivisionCellName.Subtract1Tens,
//        DivisionCellName.Subtract1Ones,
//
//        DivisionCellName.Multiply2Hundreds,
//        DivisionCellName.Multiply2Tens,
//        DivisionCellName.Multiply2Ones,
//
//        DivisionCellName.Subtract2Tens,
//        DivisionCellName.Subtract2Ones,
//
//        DivisionCellName.BorrowDividendHundreds,
//        DivisionCellName.BorrowDividendTens,
//        DivisionCellName.Borrowed10DividendTens,
//        DivisionCellName.Borrowed10DividendOnes,
//
//        DivisionCellName.BorrowSubtract1Hundreds,
//        DivisionCellName.BorrowSubtract1Tens,
//        DivisionCellName.Borrowed10Subtract1Tens,
//        DivisionCellName.Borrowed10Subtract1Ones,
//
//        DivisionCellName.CarryDivisorTensM1,
//        DivisionCellName.CarryDivisorTensM2
//    )
//
//    val fakeUiState = DivisionUiStateV2(
//        cells = allCells.associateWith { cellName ->
//            DivisionInputCellV2(cellName = cellName, value = "?")
//        },
//        pattern = DivisionPatternV2.ThreeByTwo,
//        feedback = null // 또는 "정답입니다!" 등
//    )
//
//    val newUiState = fakeUiState.copy(
//        cells = fakeUiState.cells.toMutableMap().apply {
//            this[DivisionCellName.Borrowed10Subtract1Tens] =
//                DivisionInputCellV2(
//                    cellName = DivisionCellName.Borrowed10Subtract1Tens,
//                    value = "10"
//                )
//        }
//    )
//    DivisionScreen3By2(
//        uiState = newUiState,
//    )
//}