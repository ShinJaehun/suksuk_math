package com.shinjaehun.suksuk.presentation.multiplication

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
import com.shinjaehun.suksuk.domain.multiplication.model.MulCellName
import com.shinjaehun.suksuk.domain.multiplication.model.MulPattern
import com.shinjaehun.suksuk.presentation.division.model.DivisionInputCellV2
import com.shinjaehun.suksuk.presentation.multiplication.model.MulInputCell
import com.shinjaehun.suksuk.presentation.multiplication.model.MulUiState


@Composable
fun MultiplicationBoard2x2(
    uiState: MulUiState
) {
    val cellWidth = 42.dp
    val horizPadding = 8.dp
    val anchorStart = 100.dp           // 곱셈 기호의 시작 마진 (division의 브래킷 기준과 유사) 3x2에서는 60/80이 좋을 거 같음
    val gapAbove = 16.dp              // X 기준 위(피승수) 거리
    val gapBelow = 16.dp              // X 기준 아래(승수) 거리
    val lineTopMargin = 12.dp         // 승수 아래 첫 가로선까지 거리

    val colGap = cellWidth + horizPadding * 2  // 한 열(셀+패딩) 간격
    val row1 = 12.dp
    val row2 = 52.dp
    val rowSum = 100.dp

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 30.dp)
    ) {
        val (
            timesRef, mulLineRef, totalLineRef,
            mcTensRef, mcOnesRef,
            mlTensRef, mlOnesRef
        ) = createRefs()

        val (
            p1HundredsRef, p1TensRef, p1OnesRef,
            p2ThousandsRef, p2HundredsRef, p2TensRef,
            sumThousandsRef, sumHundredsRef, sumTensRef, sumOnesRef
        ) = createRefs()

        val (carryP1TensRef, carryP2TensRef, carrySumHundredsRef, carrySumThousandsRef) = createRefs()

        // ── 1) 곱셈 기호(X) — parent 기준 앵커 ────────────────────────────────
        Image(
            painter = painterResource(R.drawable.ic_multiply),
            contentDescription = "×",
            contentScale = ContentScale.Fit,
            modifier = Modifier.constrainAs(timesRef) {
                top.linkTo(parent.top, margin = 100.dp)
                start.linkTo(parent.start, margin = anchorStart)
                width = Dimension.value(35.dp)
                height = Dimension.value(35.dp)
            }
        )

        // ── 2) 피승수(위 줄): X 기준으로 위쪽에 배치 ──────────────────────────
        val mcTensCell =
            uiState.cells[MulCellName.MultiplicandTens] ?: MulInputCell(MulCellName.MultiplicandTens)
        MulNumberText(
            cell = mcTensCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = horizPadding)
                .constrainAs(mcTensRef) {
                    bottom.linkTo(timesRef.top, margin = 15.dp) // ← X를 기준으로 '위'
                    start.linkTo(timesRef.end, margin = 40.dp) // ← X에서 오른쪽으로 적당히
                }
        )

        val carryP1TensCell =
            uiState.cells[MulCellName.CarryP1Tens] ?:
            MulInputCell(cellName = MulCellName.CarryP1Tens)
        MulAuxNumberText(
            cell = carryP1TensCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(carryP1TensRef) {
                    start.linkTo(mcTensRef.start)
                    bottom.linkTo(mcTensRef.top)
                }
        )

        val carryP2TensCell =
            uiState.cells[MulCellName.CarryP2Tens] ?:
            MulInputCell(cellName = MulCellName.CarryP2Tens)
        MulAuxNumberText(
            cell = carryP2TensCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = 8.dp)
                .constrainAs(carryP2TensRef) {
                    start.linkTo(mcTensRef.start)
                    bottom.linkTo(mcTensRef.top)
                }
        )

        val mcOnesCell =
            uiState.cells[MulCellName.MultiplicandOnes] ?: MulInputCell(MulCellName.MultiplicandOnes)
        MulNumberText(
            cell = mcOnesCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = horizPadding)
                .constrainAs(mcOnesRef) {
                    start.linkTo(mcTensRef.end)
                    baseline.linkTo(mcTensRef.baseline) // 나눗셈과 같은 간격/정렬
                }
        )

        // ── 3) 승수(아래 줄): X 기준으로 아래쪽에 배치 ─────────────────────────
        val mlTensCell =
            uiState.cells[MulCellName.MultiplierTens] ?: MulInputCell(MulCellName.MultiplierTens)
        MulNumberText(
            cell = mlTensCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = horizPadding)
                .constrainAs(mlTensRef) {
                    top.linkTo(mcTensRef.bottom, margin = 10.dp)
                    start.linkTo(mcTensRef.start)
                }
        )

        val mlOnesCell =
            uiState.cells[MulCellName.MultiplierOnes] ?: MulInputCell(MulCellName.MultiplierOnes)
        MulNumberText(
            cell = mlOnesCell,
            modifier = Modifier
                .width(cellWidth)
                .padding(horizontal = horizPadding)
                .constrainAs(mlOnesRef) {
                    start.linkTo(mlTensRef.end)
                    baseline.linkTo(mlTensRef.baseline)
                }
        )

        // ── 4) 가로선 (ic_horizontal_line.xml) — 승수 아래 기준선 ─────────────
        Image(
            painter = painterResource(id = R.drawable.ic_horizontal_line_long),
            contentDescription = "mul-line",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .constrainAs(mulLineRef) {
                    top.linkTo(timesRef.bottom, margin = lineTopMargin)
                    start.linkTo(timesRef.start, margin = (-20).dp) // division과 동일한 느낌의 좌측 여백
                    width = Dimension.value(200.dp)                  // 리소스 길이에 맞춰 조정
                    height = Dimension.value(4.dp)
                }
                .testTag("mul-line1")
        )

        // ─────────────────────────────
        // 부분곱 1 (ones × multiplicand)
        //   Hundreds  Tens  Ones
        //   (T-1col)  (T)   (O)
        // ─────────────────────────────
        uiState.cells[MulCellName.P1Hundreds]?.let { c ->
            MulNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = horizPadding)
                    .constrainAs(p1HundredsRef) {
//                        top.linkTo(lineRef.bottom, margin = row1)
                        baseline.linkTo(p1TensRef.baseline)
                        end.linkTo(mcTensRef.start) // Hundreds = T - 1col
                    }
            )
        }

        uiState.cells[MulCellName.CarrySumHundreds]?.let { c ->
            MulAuxNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(carrySumHundredsRef) {
                        start.linkTo(p1HundredsRef.start)
                        bottom.linkTo(p1HundredsRef.top)
                    }
            )
        }

        uiState.cells[MulCellName.P1Tens]?.let { c ->
            MulNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = horizPadding)
                    .constrainAs(p1TensRef) {
                        top.linkTo(mlTensRef.bottom, margin = 40.dp)
                        start.linkTo(mcTensRef.start)
                    }
            )
        }

        uiState.cells[MulCellName.P1Ones]?.let { c ->
            MulNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = horizPadding)
                    .constrainAs(p1OnesRef) {
                        start.linkTo(mcOnesRef.start)
                        baseline.linkTo(p1TensRef.baseline)
                    }
            )
        }

        // ─────────────────────────────
        // 부분곱 2 (tens × multiplicand, 1칸 시프트)
        //   Thousands  Hundreds  Tens
        //   (T-2col)   (T-1col)  (T)
        // ─────────────────────────────
        uiState.cells[MulCellName.P2Thousands]?.let { c ->
            MulNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = horizPadding)
                    .constrainAs(p2ThousandsRef) {
                        baseline.linkTo(p2TensRef.baseline)
                        end.linkTo(p2HundredsRef.start) // Thousands = T - 2col
                    }
            )
        }

        uiState.cells[MulCellName.CarrySumThousands]?.let { c ->
            MulAuxNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = 8.dp)
                    .constrainAs(carrySumThousandsRef) {
                        start.linkTo(p2ThousandsRef.start)
                        bottom.linkTo(p1HundredsRef.top)
                    }
            )
        }

        uiState.cells[MulCellName.P2Hundreds]?.let { c ->
            MulNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = horizPadding)
                    .constrainAs(p2HundredsRef) {
                        baseline.linkTo(p2TensRef.baseline)
                        end.linkTo(p2TensRef.start)     // Hundreds = T - 1col
                    }
            )
        }
        uiState.cells[MulCellName.P2Tens]?.let { c ->
            MulNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .padding(horizontal = horizPadding)
                    .constrainAs(p2TensRef) {
                        top.linkTo(p1TensRef.bottom, margin = 10.dp)
                        start.linkTo(mcTensRef.start)                        // Tens
                    }
            )
        }

        Image(
            painter = painterResource(id = R.drawable.ic_horizontal_line_long),
            contentDescription = "total-line",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .constrainAs(totalLineRef) {
                    top.linkTo(timesRef.bottom, margin = 160.dp)
                    start.linkTo(timesRef.start, margin = (-20).dp) // division과 동일한 느낌의 좌측 여백
                    width = Dimension.value(200.dp)                  // 리소스 길이에 맞춰 조정
                    height = Dimension.value(4.dp)
                }
                .testTag("total-line")
        )

        // ─────────────────────────────
        // 최종 합계
        //   Thousands  Hundreds  Tens  Ones
        //   (T-2col)   (T-1col)  (T)   (O)
        // ─────────────────────────────
        uiState.cells[MulCellName.SumThousands]?.let { c ->
            MulNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .constrainAs(sumThousandsRef) {
                        baseline.linkTo(sumTensRef.baseline)
                        end.linkTo(sumHundredsRef.start)
                    }
            )
        }
        uiState.cells[MulCellName.SumHundreds]?.let { c ->
            MulNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .constrainAs(sumHundredsRef) {
                        baseline.linkTo(sumTensRef.baseline)
                        end.linkTo(sumTensRef.start)
                    }
            )
        }
        uiState.cells[MulCellName.SumTens]?.let { c ->
            MulNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .constrainAs(sumTensRef) {
                        top.linkTo(p2TensRef.bottom, margin = 25.dp)
                        start.linkTo(mcTensRef.start)
                    }
            )
        }
        uiState.cells[MulCellName.SumOnes]?.let { c ->
            MulNumberText(
                cell = c,
                modifier = Modifier
                    .width(cellWidth)
                    .constrainAs(sumOnesRef) {
                        baseline.linkTo(sumTensRef.baseline)
                        start.linkTo(sumTensRef.end)
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMultiplicationBoard2x2() {
    // 2×2에서 주로 쓰일 셀들 모음
    val allCells = listOf(
        // 피승수(위)
        MulCellName.MultiplicandTens,
        MulCellName.MultiplicandOnes,

        // 승수(아래)
        MulCellName.MultiplierTens,
        MulCellName.MultiplierOnes,

        // 부분곱 1 (ones × multiplicand)
        MulCellName.P1Hundreds,
        MulCellName.P1Tens,
        MulCellName.P1Ones,

        // 부분곱 2 (tens × multiplicand, 한 칸 시프트)
        MulCellName.P2Thousands,
        MulCellName.P2Hundreds,
        MulCellName.P2Tens,
        // 필요 시 MulCellName.Product2Ones 도 포함 가능

        // 최종 합계
        MulCellName.SumThousands,
        MulCellName.SumHundreds,
        MulCellName.SumTens,
        MulCellName.SumOnes,

        // 캐리(보조 숫자) 쓰면 여기 추가: CarryP1Tens, CarryP1Hundreds, CarryP2Hundreds 등
        MulCellName.CarryP1Tens,
        MulCellName.CarrySumHundreds,
        MulCellName.CarrySumThousands

    )

    // 기본은 "?"로 채운 가짜 상태
    val fakeUiState = MulUiState(
        cells = allCells.associateWith { name ->
            MulInputCell(cellName = name, value = "?")
        },
        pattern = MulPattern.TwoByTwo,
        feedback = null
    )

    // 미리보기용으로 몇 개 값만 실제 숫자로 보이게 세팅
    val previewUiState = fakeUiState.copy(
        cells = fakeUiState.cells.toMutableMap().apply {
            // 피승수/승수
            this[MulCellName.MultiplicandTens]  = MulInputCell(MulCellName.MultiplicandTens,  value = "4")
            this[MulCellName.MultiplicandOnes]  = MulInputCell(MulCellName.MultiplicandOnes,  value = "8")
            this[MulCellName.MultiplierTens]    = MulInputCell(MulCellName.MultiplierTens,    value = "3")
            this[MulCellName.MultiplierOnes]    = MulInputCell(MulCellName.MultiplierOnes,    value = "6")

            // 부분곱(예시)
            this[MulCellName.P1Hundreds]      = MulInputCell(MulCellName.P1Hundreds,      value = "2")
            this[MulCellName.P1Tens]      = MulInputCell(MulCellName.P1Tens,      value = "2")
            this[MulCellName.P1Ones]      = MulInputCell(MulCellName.P1Ones,      value = "8")
            this[MulCellName.P2Thousands]  = MulInputCell(MulCellName.P2Thousands,  value = "1")
            this[MulCellName.P2Hundreds]  = MulInputCell(MulCellName.P2Hundreds,  value = "1")
            this[MulCellName.P2Tens]      = MulInputCell(MulCellName.P2Tens,      value = "4")

            // 합계(예시)
            this[MulCellName.SumThousands]    = MulInputCell(MulCellName.SumThousands,    value = "1")
            this[MulCellName.SumHundreds]        = MulInputCell(MulCellName.SumHundreds,        value = "7")
            this[MulCellName.SumTens]        = MulInputCell(MulCellName.SumTens,        value = "8")
            this[MulCellName.SumOnes]        = MulInputCell(MulCellName.SumOnes,        value = "8")

            this[MulCellName.CarryP1Tens]        = MulInputCell(MulCellName.CarryP1Tens,        value = "8")
            this[MulCellName.CarrySumHundreds]        = MulInputCell(MulCellName.CarrySumHundreds,        value = "8")
            this[MulCellName.CarrySumThousands]        = MulInputCell(MulCellName.CarrySumThousands,        value = "8")
        }
    )

    MultiplicationBoard2x2(
        uiState = previewUiState
    )
}