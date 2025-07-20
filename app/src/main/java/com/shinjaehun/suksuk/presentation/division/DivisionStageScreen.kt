package com.shinjaehun.suksuk.presentation.division

import androidx.compose.animation.core.copy
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shinjaehun.suksuk.R
import com.shinjaehun.suksuk.presentation.components.DivisionInputPad
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kotlin.ranges.coerceIn
import kotlin.text.padStart

//
//@Composable
//fun DivisionStageScreen(viewModel: DivisionViewModel = viewModel()) {
//    val uiState by viewModel.uiState.collectAsState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // 피드백 메시지 표시
//        uiState.feedback?.let {
//            Text(
//                text = it,
//                color = if (uiState.isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
//                style = MaterialTheme.typography.bodyLarge,
//                modifier = Modifier
//                    .fillMaxWidth() // 너비를 꽉 채워 중앙 정렬이 의미 있도록
//                    .padding(bottom = 16.dp),
//                textAlign = TextAlign.Center
//            )
//        }
//
//        // 나눗셈 레이아웃 (남은 공간 차지)
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(260.dp), // 충분히 큰 높이
//            contentAlignment = Alignment.TopCenter
//        ) {
//            DivisionRenderLayout(
//                uiState = uiState
//            )
//        }
//
//        // 입력 패드 (사용자 프로젝트의 DivisionInputPad 사용 가정)
//        DivisionInputPad(
//            onNumberInput = { viewModel.onDigitInput(it) },
//            onClear = { viewModel.onClear() },
//            onEnter = { viewModel.onEnter() }
//        )
//    }
//}
//
//
//@Composable
//fun DivisionRenderLayout(
//    uiState: DivisionUiState,
//    divisionBracketOffsetX: Dp = 36.dp, // 나눗셈 기호 이미지의 X 오프셋 (조정)
//    divisionBracketOffsetY: Dp = 60.dp, // 나눗셈 기호 이미지의 Y 오프셋 (조정)
//    divisionBracketWidth: Dp = 240.dp,  // 나눗셈 기호 이미지의 너비
//    divisionBracketHeight: Dp = 90.dp  // 나눗셈 기호 이미지의 높이 (몫과 피제수 사이 공간 고려)
//) {
//    val defaultFontSize = 48.dp
//    val minCharWidth = 32.dp
//    val verticalSpacing = 6.dp
//    val horizontalSpacing = 4.dp
//
//    val dividendAndDivisorTopPadding = divisionBracketOffsetY + (verticalSpacing / 2) + 5.dp
//
//
//    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//
//        ConstraintLayout(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp)
//                .height(120.dp)
//
//        ) {
//
//            // --- 나눗셈 기호 이미지 (배경) ---
//            val bracketImageRef = createRef()
//            Image(
//                painter = painterResource(id = R.drawable.ic_division_bracket_short),
//                contentDescription = "Division Bracket Background",
//                contentScale = ContentScale.FillBounds,
//                modifier = Modifier
//                    .constrainAs(bracketImageRef) {
//                        top.linkTo(parent.top, margin = divisionBracketOffsetY)
//                        start.linkTo(parent.start, margin = divisionBracketOffsetX)
//                        width = Dimension.value(divisionBracketWidth)
//                        height = Dimension.value(divisionBracketHeight)
//                    }
//                    .alpha(0.5f) // 숫자와 겹쳐 보이도록 약간 투명하게
//            )
//
//            val (
//                divisorRef,
//                multiply1Ref,
//                subtractLine1Ref,
//                subtract1ResultRef,
//                bringDownCellRef,
//                multiply2Ref,
//                subtractLine2Ref,
//                remainderResultRef
//            ) = createRefs()
//
//            // 피제수 참조 및 몫 참조를 위한 리스트 (remember 사용)
//            val dividendRefs = remember(uiState.dividendDigits.size) {
//                List(uiState.dividendDigits.size) { createRef() }
//            }
//            val quotientRefs = remember(uiState.quotientCells.size) {
//                List(uiState.quotientCells.size) { createRef() }
//            }
//
//            // --- 제수 (Divisor) ---
//            val divisorCell = uiState.divisor.toString().let {
//                InputCell(
//                    id = "divisor_static",
//                    value = it,
//                    editable = false,
//                    isQuestionMarkVisible = false,
//                    placeholderCount = it.length.coerceAtLeast(1)
//                )
//            }
//            EditableTextWithBorrow(
//                inputCell = divisorCell,
//                fontSize = defaultFontSize,
//                charWidth = minCharWidth,
//                modifier = Modifier.constrainAs(divisorRef) {
//                    top.linkTo(parent.top, margin = dividendAndDivisorTopPadding)
//                    // 제수의 start 위치는 나눗셈 기호의 시작 위치(divisionBracketOffsetX)를 기준으로 계산
//                    start.linkTo(parent.start, margin = divisionBracketOffsetX - (minCharWidth * divisorCell.placeholderCount) - horizontalSpacing)
//                }
//            )
//
//            // --- 피제수 (Dividend) ---
//            // 제수의 오른쪽, 그리고 몫의 아래에 위치 (몫이 먼저 정의된다면)
//            // 여기서는 제수 오른쪽, 몫은 피제수 위로 배치
//            uiState.dividendDigits.forEachIndexed { index, digitCell ->
//                EditableTextWithBorrow(
//                    inputCell = digitCell,
//                    fontSize = defaultFontSize,
//                    charWidth = minCharWidth,
//                    modifier = Modifier.constrainAs(dividendRefs[index]) {
//                        top.linkTo(parent.top, margin = dividendAndDivisorTopPadding)
//                        if (index == 0) {
//                            // 피제수의 시작 위치는 나눗셈 기호의 시작 위치(divisionBracketOffsetX)를 기준으로 계산
//                            start.linkTo(parent.start, margin = divisionBracketOffsetX + horizontalSpacing)
//                        } else {
//                            start.linkTo(dividendRefs[index - 1].end, margin = 0.dp)
//                        }
//                    }
//                )
//            }
//
//            // --- 몫 (Quotient) ---
//            uiState.quotientCells.forEachIndexed { index, quotientCell ->
//                val shouldShowQuotientDigit = if (index == 0) true else {
//                    (uiState.quotientCells.getOrNull(index - 1)?.value?.isNotEmpty() == true) ||
//                            quotientCell.editable || quotientCell.value.isNotEmpty() || quotientCell.isQuestionMarkVisible
//                }
//
//                if (shouldShowQuotientDigit && dividendRefs.isNotEmpty()) {
//                    EditableTextWithBorrow(
//                        inputCell = quotientCell,
//                        fontSize = defaultFontSize,
//                        charWidth = minCharWidth,
//                        modifier = Modifier.constrainAs(quotientRefs[index]) {
//                            // 피제수 위 (나눗셈 기호 가로선 위)
//                            bottom.linkTo(bracketImageRef.top, margin = verticalSpacing / 2)
//                            // 또는 top.linkTo(parent.top, margin = quotientTopPadding)
//
//                            val targetDividendIndex = index.coerceIn(0, dividendRefs.size - 1)
//                            end.linkTo(dividendRefs[targetDividendIndex].end)
//
//                            if (index == 0 && dividendRefs.isNotEmpty()) {
//                                start.linkTo(dividendRefs[0].start) // 첫 몫은 첫 피제수와 시작점 정렬
//                            } else if (index > 0 && quotientRefs.getOrNull(index-1) != null) {
//                                start.linkTo(quotientRefs[index-1].end, margin = 0.dp) // 나머지 몫은 이전 몫에 이어붙임
//                            }
//                        }
//                    )
//                }
//            }
//
//            // --- 1차 곱셈 (Multiply 1) / Subtrahend 1 ---
//            val showMultiply1 =
//                uiState.currentStage.ordinal >= DivisionStage.INPUT_MULTIPLY_1.ordinal &&
//                        (uiState.multiply1Cell.editable || uiState.multiply1Cell.value.isNotEmpty() || uiState.multiply1Cell.isQuestionMarkVisible) &&
//                        dividendRefs.isNotEmpty() // 피제수가 있어야 그 아래에 위치 가능
//
//            if (showMultiply1) {
//                EditableTextWithBorrow(
//                    inputCell = uiState.multiply1Cell,
//                    fontSize = defaultFontSize,
//                    charWidth = minCharWidth,
//                    // isQuestionMarkVisible 및 placeholderCharCount는 inputCell에서 가져옴
//                    modifier = Modifier.constrainAs(multiply1Ref) {
//                        // 피제수의 첫 번째 자리(또는 관련된 부분) 아래에 위치.
//                        // dividendRefs가 비어있지 않음은 showMultiply1 조건으로 보장됨.
//                        top.linkTo(dividendRefs[0].bottom, margin = verticalSpacing)
//
//                        // 몫의 첫 번째 자리와 오른쪽 끝을 정렬.
//                        // quotientRefs 리스트가 비어있지 않고, 첫 번째 요소가 유효한지 확인.
//                        if (quotientRefs.isNotEmpty() && uiState.quotientCells.getOrNull(0) != null) {
//                            // 몫의 첫번째 요소가 실제로 화면에 그려지는지 여부도 고려할 수 있으나,
//                            // 여기서는 참조가 존재하면 일단 연결 시도.
//                            // (shouldShowQuotientDigit 로직과 유사한 검사가 필요할 수 있음)
//                            end.linkTo(quotientRefs[0].end)
//                        } else if (dividendRefs.isNotEmpty()) {
//                            // 몫의 참조가 아직 없거나 유효하지 않은 경우,
//                            // 임시로 피제수의 첫 번째 자리와 오른쪽 끝을 정렬 (Fallback).
//                            // 이 경우는 몫이 아직 전혀 표시되지 않았을 때 발생 가능.
//                            end.linkTo(dividendRefs[0].end)
//                        }
//                        // 만약 multiply1Cell의 길이가 몫의 첫 자리보다 길 경우,
//                        // start 제약도 필요할 수 있음 (예: multiply1Ref.start.linkTo(someRef.start))
//                        // 현재는 end 정렬만 사용.
//                    }
//                )
//            }
//
//            // --- 1차 뺄셈 선 ---
//            // multiply1Ref가 실제로 화면에 그려질 때 (showMultiply1 == true)
//            // 그리고 subtract1Cell이 보여질 조건일 때 선을 그림
//            val showSubtract1Line = showMultiply1 &&
//                    uiState.currentStage.ordinal >= DivisionStage.INPUT_SUBTRACT_1.ordinal &&
//                    (uiState.subtract1Cell.editable || uiState.subtract1Cell.value.isNotEmpty() || uiState.subtract1Cell.isQuestionMarkVisible)
//
//            if (showSubtract1Line) {
//                Box(
//                    modifier = Modifier
//                        .height(1.dp)
//                        .background(MaterialTheme.colorScheme.onSurface) // MaterialTheme import 필요
//                        .constrainAs(subtractLine1Ref) {
//                            // multiply1Ref의 시작과 끝에 맞춰서 선을 그림
//                            start.linkTo(multiply1Ref.start)
//                            end.linkTo(multiply1Ref.end)
//                            top.linkTo(multiply1Ref.bottom, margin = verticalSpacing / 2)
//                            width = Dimension.fillToConstraints
//                        }
//                )
//            }
//
//            // --- 1차 뺄셈 결과 (Subtract 1 Result) ---
//            // showSubtract1Line 조건과 유사 (선이 보이면 결과도 보일 가능성 높음)
//            val showSubtract1Result =
//                uiState.currentStage.ordinal >= DivisionStage.INPUT_SUBTRACT_1.ordinal &&
//                        (uiState.subtract1Cell.editable || uiState.subtract1Cell.value.isNotEmpty() || uiState.subtract1Cell.isQuestionMarkVisible) &&
//                        showMultiply1 // 1차 곱셈 결과(multiply1Ref)가 보여야 그 아래에 위치
//
//            if (showSubtract1Result) {
//                EditableTextWithBorrow(
//                    inputCell = uiState.subtract1Cell,
//                    fontSize = defaultFontSize,
//                    charWidth = minCharWidth,
//                    modifier = Modifier.constrainAs(subtract1ResultRef) {
//                        // 1차 뺄셈 선(subtractLine1Ref) 아래에 위치
//                        // subtractLine1Ref가 존재할 때만 이 제약이 유효 (showSubtract1Line == true)
//                        top.linkTo(subtractLine1Ref.bottom, margin = verticalSpacing / 2)
//                        end.linkTo(multiply1Ref.end) // 1차 곱셈 결과와 오른쪽 정렬
//                    }
//                )
//            }
//
//            // --- 내려쓴 숫자 (Bring Down Digit) ---
//            // 1차 뺄셈 결과가 있고, 다음 피제수 자리가 있을 때 표시
//            val showBringDown =
//                uiState.currentStage.ordinal >= DivisionStage.BRING_DOWN.ordinal &&
//                        (uiState.bringDownCell.editable || uiState.bringDownCell.value.isNotEmpty() || uiState.bringDownCell.isQuestionMarkVisible) &&
//                        showSubtract1Result // 1차 뺄셈 결과(subtract1ResultRef)가 보여야 함
//
//            if (showBringDown) {
//                // 내려쓴 숫자의 위치 결정:
//                // 1. 1차 뺄셈 결과의 오른쪽에 붙여서 표시 (예: 4 옆에 3을 붙여 43)
//                // 2. 해당 피제수 자리 바로 아래로 내려오는 것처럼 표시
//                // 여기서는 1번 방식을 우선으로 하되, 2번 방식도 주석으로 남겨둡니다.
//                // uiState.bringDownCell.originalValue 에 내려쓸 숫자가 있을 것으로 예상.
//                // uiState.bringDownCell.placeholderCount 로 너비 계산에 도움.
//
//                EditableTextWithBorrow(
//                    inputCell = uiState.bringDownCell,
//                    fontSize = defaultFontSize,
//                    charWidth = minCharWidth,
//                    modifier = Modifier.constrainAs(bringDownCellRef) {
//                        // 옵션 1: 1차 뺄셈 결과의 오른쪽에 붙여서 표시
//                        top.linkTo(subtract1ResultRef.top) // 1차 뺄셈 결과와 같은 높이
//                        start.linkTo(subtract1ResultRef.end, margin = horizontalSpacing / 3) // 거의 붙도록 작은 간격
//
//                        // 옵션 2: 특정 피제수 자리 아래로 내려오는 것처럼 표시
//                        // 이 경우, 어떤 피제수 자리에서 내려오는지 알아야 함 (예: uiState.bringDownSourceIndex)
//                        // val bringDownSourceIndex = uiState.bringDownSourceIndex ?: 1 // ViewModel에서 제공 필요
//                        // if (dividendRefs.getOrNull(bringDownSourceIndex) != null) {
//                        //     top.linkTo(dividendRefs[bringDownSourceIndex].bottom, margin = verticalSpacing * 0.8f)
//                        //     end.linkTo(dividendRefs[bringDownSourceIndex].end)
//                        // } else { // Fallback if source index is not valid
//                        //     top.linkTo(subtract1ResultRef.bottom, margin = verticalSpacing)
//                        //     start.linkTo(subtract1ResultRef.start) // Or some other sensible default
//                        // }
//                    }
//                )
//            }
//
//            // --- 2차 곱셈 (Multiply 2) / Subtrahend 2 ---
//            // 몫의 두 번째 자리가 입력되었거나 입력 중이고 (INPUT_QUOTIENT_2 이후),
//            // multiply2Cell이 표시될 조건일 때.
//            // 또한, 1차 뺄셈 결과 또는 내려쓴 숫자가 있어야 그 아래에 위치.
//            val showMultiply2 =
//                uiState.currentStage.ordinal >= DivisionStage.INPUT_MULTIPLY_2.ordinal &&
//                        (uiState.multiply2Cell.editable || uiState.multiply2Cell.value.isNotEmpty() || uiState.multiply2Cell.isQuestionMarkVisible) &&
//                        (showSubtract1Result || showBringDown) && // 1차 뺄셈 결과 또는 내려쓴 숫자가 있어야 함
//                        quotientRefs.size > 1 // 몫의 두 번째 자리를 위한 참조가 있어야 함 (실제 값 존재 여부는 cell.value로)
//
//            if (showMultiply2) {
//                EditableTextWithBorrow(
//                    inputCell = uiState.multiply2Cell,
//                    fontSize = defaultFontSize,
//                    charWidth = minCharWidth,
//                    modifier = Modifier.constrainAs(multiply2Ref) {
//                        // 2차 곱셈은 1차 뺄셈 결과(subtract1ResultRef) 아래에 위치.
//                        // (내려쓴 숫자가 1차 뺄셈 결과와 같은 줄에 있다고 가정)
//                        top.linkTo(subtract1ResultRef.bottom, margin = verticalSpacing)
//
//                        // 몫의 두 번째 자리(quotientRefs[1])와 오른쪽 끝을 정렬.
//                        if (quotientRefs.size > 1 && uiState.quotientCells.getOrNull(1)?.value?.isNotEmpty() == true) {
//                            // 몫의 두 번째 자리가 있고 값이 채워져 있다면 그것과 정렬
//                            end.linkTo(quotientRefs[1].end)
//                        } else if (showBringDown) {
//                            // 몫의 두 번째 자리가 없거나 비어있지만, 내려쓴 숫자가 표시되는 경우
//                            // 내려쓴 숫자(bringDownCellRef)의 끝에 정렬
//                            end.linkTo(bringDownCellRef.end)
//                        } else {
//                            // 위의 두 조건 모두 해당하지 않으면, 1차 뺄셈 결과(subtract1ResultRef)의 끝에 정렬 (최후의 Fallback)
//                            end.linkTo(subtract1ResultRef.end)
//                        }
//                    }
//                )
//            }
//
//            // --- 2차 뺄셈 선 ---
//            // 2차 곱셈 결과가 있고, 나머지가 표시될 조건일 때
//            val showSubtract2Line = showMultiply2 &&
//                    uiState.currentStage.ordinal >= DivisionStage.INPUT_SUBTRACT_2_REMAINDER.ordinal &&
//                    (uiState.remainderCell.editable || uiState.remainderCell.value.isNotEmpty() || uiState.remainderCell.isQuestionMarkVisible)
//
//            if (showSubtract2Line) {
//                Box(
//                    modifier = Modifier
//                        .height(1.dp)
//                        .background(MaterialTheme.colorScheme.onSurface) // MaterialTheme import 필요
//                        .constrainAs(subtractLine2Ref) {
//                            start.linkTo(multiply2Ref.start)
//                            end.linkTo(multiply2Ref.end)
//                            top.linkTo(multiply2Ref.bottom, margin = verticalSpacing / 2)
//                            width = Dimension.fillToConstraints
//                        }
//                )
//            }
//
//            // --- 최종 나머지 (Remainder Result) ---
//            // 2차 뺄셈 선이 보이면 나머지도 보임 (위의 showSubtract2Line 조건과 동일)
//            val showRemainder = showSubtract2Line
//
//            if (showRemainder) {
//                EditableTextWithBorrow(
//                    inputCell = uiState.remainderCell,
//                    fontSize = defaultFontSize,
//                    charWidth = minCharWidth,
//                    modifier = Modifier.constrainAs(remainderResultRef) {
//                        top.linkTo(subtractLine2Ref.bottom, margin = verticalSpacing / 2) // 2차 뺄셈 선 아래
//                        end.linkTo(multiply2Ref.end) // 2차 곱셈 결과와 오른쪽 정렬
//                    }
//                )
//            }
//
//
//        } // end of ConstraintLayout
//    }
//
//}
//
//
//@Composable
//fun EditableTextWithBorrow(
//    inputCell: InputCell,
//    modifier: Modifier = Modifier,
//    fontSize: Dp = 24.dp,
//    charWidth: Dp = 20.dp,
//    // isQuestionMarkVisible 파라미터는 inputCell.isQuestionMarkVisible를 사용하므로 중복 제거
//    // placeholderCharCount 파라미터는 inputCell.placeholderCount를 사용하므로 중복 제거
//    textStyle: TextStyle = LocalTextStyle.current.copy(
//        fontSize = fontSize.value.sp,
//        textAlign = TextAlign.Center
//    ),
//    onValueChange: (String) -> Unit = {},
//    onEnterPressed: () -> Unit = {}
//) {
//    val focusManager = LocalFocusManager.current
//    val focusRequester = remember { FocusRequester() }
//
//    var textFieldValue by remember(inputCell.value) {
//        mutableStateOf(TextFieldValue(inputCell.value, TextRange(inputCell.value.length)))
//    }
//
//    val displayValue = when {
//        // inputCell의 isQuestionMarkVisible 사용
//        inputCell.isQuestionMarkVisible && inputCell.editable -> "?".repeat(inputCell.placeholderCount.coerceAtLeast(1))
//        inputCell.correct == false && inputCell.value.isNotEmpty() -> inputCell.value
//        else -> inputCell.value
//    }
//
//    val textColor = when (inputCell.correct) {
//        true -> Color.Green
//        false -> MaterialTheme.colorScheme.error
//        else -> MaterialTheme.colorScheme.onSurface
//    }
//
//    // inputCell의 placeholderCount 사용
//    val cellWidth = charWidth * inputCell.placeholderCount.coerceAtLeast(1)
//
//    Box(
//        modifier = modifier
//            .width(cellWidth)
//            .height(fontSize * 1.8f) // carriedValue 표시 공간 고려하여 높이 약간 더 확보
//            .then(
//                if (inputCell.isCrossedOut) {
//                    Modifier.drawBehind {
//                        // 취소선 (isCrossedOut)
//                        drawLine(
//                            color = Color.Gray,
//                            start = Offset(size.width * 0.1f, size.height * 0.5f), // 중앙 높이
//                            end = Offset(size.width * 0.9f, size.height * 0.5f),   // 중앙 높이
//                            strokeWidth = 1.dp.toPx()
//                        )
//                    }
//                } else Modifier
//            ),
//        contentAlignment = Alignment.Center
//    ) {
//        if (inputCell.editable && !inputCell.isQuestionMarkVisible) {
//            BasicTextField(
//                value = textFieldValue,
//                onValueChange = {
//                    textFieldValue = it
//                    onValueChange(it.text)
//                },
//                textStyle = textStyle.copy(color = textColor),
//                singleLine = true,
//                keyboardOptions = KeyboardOptions.Default.copy(
//                    keyboardType = KeyboardType.Number,
//                    imeAction = ImeAction.Done
//                ),
//                keyboardActions = KeyboardActions(
//                    onDone = {
//                        onEnterPressed()
//                        focusManager.clearFocus()
//                    }
//                ),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .focusRequester(focusRequester),
//                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
//            )
//        } else {
//            // isCrossedOut이 true일 때는 originalValue를, 아니면 displayValue를 기본으로 표시
//            val baseText = if (inputCell.isCrossedOut && inputCell.originalValue != null) {
//                inputCell.originalValue ?: ""
//            } else {
//                displayValue
//            }
//            Text(
//                text = baseText,
//                style = textStyle.copy(color = if (inputCell.isCrossedOut) Color.Gray else textColor),
//                modifier = Modifier.align(Alignment.Center)
//            )
//        }
//
//        // carriedValue 표시 (예: 받아올림/내림으로 생긴 숫자)
//        if (inputCell.carriedValue != null) {
//            Text(
//                text = inputCell.carriedValue!!,
//                style = textStyle.copy(
//                    color = MaterialTheme.colorScheme.secondary, // 다른 색상으로 표시
//                    fontSize = (fontSize.value * 0.6f).sp // 작게 표시
//                ),
//                modifier = Modifier
//                    .align(Alignment.TopStart) // 왼쪽 위에 표시 (또는 원하는 위치)
//                    .padding(start = 2.dp, top = 0.dp)
//            )
//        }
//    }
//}
//
//// 사용자 프로젝트의 DivisionInputPad Composable (예시, 실제 구현은 다를 수 있음)
//@Composable
//fun DivisionInputPad(
//    onNumberInput: (Int) -> Unit,
//    onClear: () -> Unit,
//    onEnter: () -> Unit,
//) {
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//            (1..3).forEach { number -> NumberButton(number, onNumberInput) }
//        }
//        Spacer(Modifier.height(8.dp))
//        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//            (4..6).forEach { number -> NumberButton(number, onNumberInput) }
//        }
//        Spacer(Modifier.height(8.dp))
//        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//            (7..9).forEach { number -> NumberButton(number, onNumberInput) }
//        }
//        Spacer(Modifier.height(8.dp))
//        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//            ActionButton("C", onClear) // Clear
//            NumberButton(0, onNumberInput)
//            ActionButton("↵", onEnter) // Enter
//        }
//    }
//}
//
//@Composable
//private fun NumberButton(number: Int, onClick: (Int) -> Unit) {
//    Button(onClick = { onClick(number) }, modifier = Modifier.size(64.dp)) {
//        Text(text = number.toString(), fontSize = 20.sp)
//    }
//}
//
//@Composable
//private fun ActionButton(text: String, onClick: () -> Unit) {
//    Button(onClick = onClick, modifier = Modifier.size(64.dp)) {
//        Text(text = text, fontSize = 20.sp)
//    }
//}
//@Preview(showBackground = true, apiLevel = 34)
//@Composable
//fun DefaultPreviewOfDivisionStageScreen() {
//    // ViewModel 모의 객체 또는 기본 상태로 Preview
//    // val previewViewModel = DivisionViewModel() // 실제 ViewModel 사용 시 Hilt/DI 설정 필요
//    // previewViewModel.setProblem(92, 7) // 예시 문제
//    MaterialTheme {
//        // DivisionStageScreen(viewModel = previewViewModel)
//        // 또는 DivisionRenderLayout만 Preview
//        DivisionRenderLayout(
//            uiState = DivisionUiState(
//                dividend = 92,
//                divisor = 7,
//                quotientCells = mutableListOf(
//                    InputCell(value = "1", editable = false, correct = true), // 몫 '1'은 입력 완료 및 정답 처리
//                    InputCell(value = "", editable = false) // 두 번째 몫은 아직
//                ),
//                // --- 여기가 중요! ---
//                multiply1Cell = InputCell(
//                    value = "", // 아직 입력 전이므로 비어있음
//                    editable = true, // 입력 가능 상태
//                    isQuestionMarkVisible = true, // "?" 표시 활성화
//                    correct = null // 아직 정답 여부 모름
//                ),
//                // --- 여기까지 ---
//                subtract1Cell = InputCell(editable = false),
//                bringDownCell = InputCell(editable = false),
//                multiply2Cell = InputCell(editable = false),
//                subtract2Cell = InputCell(editable = false),
//                remainderCell = InputCell(editable = false),
//
//                feedback = "7 곱하기 1은 얼마인가요?", // 사용자 안내 메시지
//                stage = 2, // ViewModel에서 1차 곱셈 입력 대기 stage 번호 (예시)
//
//                borrowTenCell = InputCell(
//                    value = (92 / 10).toString(), // "9"
//                    originalValue = (92 / 10).toString(),
//                    isCrossedOut = false,
//                    carriedValue = null,
//                    editable = false
//                ),
//                borrowOneCell = InputCell(
//                    value = (92 % 10).toString(),   // "2"
//                    originalValue = (92 % 10).toString(),
//                    isCrossedOut = false,
//                    carriedValue = null,
//                    editable = false
//                )
//            )
//        )
//    }
//}
//
//
//// 받아내림 시나리오를 위한 별도 Preview (예시)
//@Preview(showBackground = true, name = "Division Stage - Borrowing", apiLevel = 34)
//@Composable
//fun PreviewDivisionStageScreenWithBorrowing() {
//    MaterialTheme {
//        DivisionRenderLayout(
//            uiState = DivisionUiState(
//                dividend = 62, // 예: 62 / 7
//                divisor = 7,
//                stage = 21, // 1차 받아내림 - 십의자리 수정 중
//                quotientCells = mutableListOf(
//                    InputCell(value = "", editable = true, isQuestionMarkVisible = true), // 몫 첫째자리 입력 대기
//                    InputCell(value = "", editable = false)
//                ),
//                // 6을 5로 바꾸고, 2에 10을 주는 상황
//                borrowTenCell = InputCell(value = "5", originalValue = "6", isCrossedOut = true, editable = true, isQuestionMarkVisible = true, correct = null),
//                borrowOneCell = InputCell(value = "2", originalValue = "2", carriedValue = "10", isCrossedOut = false, editable = false, isQuestionMarkVisible = false), // 아직 12로 합쳐지기 전, 10만 표시
//                multiply1Cell = InputCell(editable = false, isQuestionMarkVisible = false),
//                subtract1Cell = InputCell(editable = false, isQuestionMarkVisible = false),
//                bringDownCell = InputCell(editable = false, isQuestionMarkVisible = false),
//                multiply2Cell = InputCell(editable = false, isQuestionMarkVisible = false),
//                subtract2Cell = InputCell(editable = false, isQuestionMarkVisible = false),
//                remainderCell = InputCell(editable = false, isQuestionMarkVisible = false),
//                feedback = "십의 자리 숫자를 수정하고, 받아내림을 확인하세요."
//            )
//        )
//    }
//}
//
//// Stage 7 (2차 뺄셈 입력) Preview
//@Preview(showBackground = true, name = "Division Stage - Stage 7", apiLevel = 34)
//@Composable
//fun PreviewDivisionStageScreenStage7() {
//    MaterialTheme {
//        DivisionRenderLayout(
//            uiState = DivisionUiState(
//                dividend = 92,
//                divisor = 7,
//                quotientCells = mutableListOf(
//                    InputCell(value = "1", editable = false, correct = true),
//                    InputCell(value = "3", editable = false, correct = true)
//                ),
//                multiply1Cell = InputCell(value = "7", editable = false, correct = true),
//                subtract1Cell = InputCell(value = "2", editable = false, correct = true),
//                bringDownCell = InputCell(value = "2", editable = false, correct = true),
//                multiply2Cell = InputCell(value = "21", editable = false, correct = true),
//                subtract2Cell = InputCell(value = "", editable = true, isQuestionMarkVisible = true), // 여기 입력 중
//                remainderCell = InputCell(value = "", editable = false), // 아직 확정 안됨
//                feedback = "나머지를 입력하세요.",
//                stage = 7
//            )
//        )
//    }
//}