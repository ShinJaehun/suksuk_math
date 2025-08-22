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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.shinjaehun.suksuk.R
import com.shinjaehun.suksuk.domain.division.model.DivisionCell
import com.shinjaehun.suksuk.domain.pattern.DivisionPattern
import com.shinjaehun.suksuk.presentation.division.model.DivisionUiState
import com.shinjaehun.suksuk.presentation.division.model.SubtractLineType

@Composable
fun DivisionBoard2By1And2By2(
    uiState: DivisionUiState,
//    pattern: DivisionPattern,
    modifier: Modifier = Modifier
) {

    val cellWidth = 42.dp

//    val bracketStartMargin = when(pattern) {
//        DivisionPattern.TwoByOne -> 60.dp
//        else -> 90.dp
//    }
    val bracketStartMargin = 30.dp

    ConstraintLayout(
        modifier = modifier
//            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
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

        uiState.cells[DivisionCell.DividendTens]?.let { c ->
            DivNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(dividendTensRef) {
                        top.linkTo(bracketRef.top, margin = 40.dp)
                        start.linkTo(bracketRef.start, margin = 60.dp)
                    }
            )
        }

        uiState.cells[DivisionCell.BorrowDividendTens]?.let { c ->
            DivAuxNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(dividendTenBorrowRef) {
                        start.linkTo(dividendTensRef.start)
                        bottom.linkTo(dividendTensRef.top)
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
                        baseline.linkTo(dividendTensRef.baseline)
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
                    .semantics(mergeDescendants = true) { testTag="borrowed10-dividend-cell" }    // 하위 구성 요소(Box -> Text)로 tag를 넘기는 듯
            )
        }


        uiState.cells[DivisionCell.DivisorOnes]?.let { c ->
            DivNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(divisorOnesRef) {
                        end.linkTo(dividendTensRef.start, margin = 40.dp)
                        baseline.linkTo(dividendTensRef.baseline)
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
//                        baseline.linkTo(dividendTensRef.baseline) // 이거 문제 원인 알아내는데 정말 고생했음...
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
                    .constrainAs(divisorTensCarryRef) {
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

        uiState.cells[DivisionCell.Multiply1Tens]?.let { c ->
            DivNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(multiply1TensRef) {
                        top.linkTo(dividendTensRef.bottom, margin = 10.dp)
                        start.linkTo(dividendTensRef.start)
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
                        baseline.linkTo(multiply1TensRef.baseline)
                    }
            )
        }

        if (uiState.cells.values.any { it.subtractLineType == SubtractLineType.SubtractLine1 }) {
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
                    .semantics { testTag = "subtraction1-line" }
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

        uiState.cells[DivisionCell.BorrowSubtract1Tens]?.let { c ->
            DivAuxNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(subtract1TensBorrowRef) {
                        start.linkTo(subtract1TensRef.start)
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

        uiState.cells[DivisionCell.Multiply2Tens]?.let { c ->
            DivNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(multiply2TensRef) {
                        top.linkTo(dividendTensRef.bottom, margin = 150.dp)
                        start.linkTo(dividendTensRef.start)
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
                        baseline.linkTo(multiply2TensRef.baseline)
                    }
            )
        }

        if (uiState.cells.values.any { it.subtractLineType == SubtractLineType.SubtractLine2 }) {
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