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
fun DivisionStageScreen(viewModel: DivisionViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    // 단계별 cover visibility - replace with actual uiState flags
    val dividendTenCoverVisible = remember { mutableStateOf(false) }
    val dividendOneCoverVisible = remember { mutableStateOf(false) }
    val ansFirstLineVisible = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // 1. Division sign
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
////                .wrapContentHeight()
//                .background(Color.Gray) // 부모 영역 확인용
//                .padding(top = 20.dp)
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.ic_division_bracket),
//                contentDescription = "Division Sign",
//                contentScale = ContentScale.Fit,
//                modifier = Modifier
//                    // 여기에 원하는 크기를 지정해보세요.
//                    // 예: .height(50.dp) // 높이를 50dp로 지정
//                    // 또는 .width(150.dp) // 너비를 150dp로 지정
//                    // 또는 .size(width = 150.dp, height = 50.dp)
////                    .width(500.dp) // 너비를 150dp로 지정
//                    .size(width = 100.dp, height = 150.dp)
////                    .wrapContentSize() // 기본적으로 이미지 원본 크기를 사용하려 하지만, 명시적 크기 지정이 우선될 수 있음
//                    .background(Color.Red) // Image 영역 확인용
////                    .padding(top = 37.dp)
//                    .offset(x = (-8).dp, y = (-2).dp)
//                    .align(Alignment.TopCenter)
//            )
//        }

//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .wrapContentHeight()
//                .padding(top = 20.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            // Quotient row
//            Row(
//                modifier = Modifier
//                    .wrapContentSize()
//                    .padding(top = 10.dp),
//                horizontalArrangement = Arrangement.Center
//            ) {
//                uiState.quotient.toString().padStart(2, ' ').forEach { digit ->
//                    Text(
//                        text = digit.toString(),
//                        textAlign = TextAlign.Center,
//                        color = Color.Black,
//                        fontSize = 30.sp,
//                        modifier = Modifier.padding(horizontal = 6.dp)
//                    )
//                }
//            }
//
//            // Main division area
//            Row(
//                modifier = Modifier
//                    .wrapContentSize()
//                    .padding(top = 5.dp, end = 40.dp),
//            ) {
//                // Divisor
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Spacer(modifier = Modifier.height(12.dp)) // divisor를 dividend 아래에 맞춰 정렬
//                    Text(
//                        text = uiState.divisor.toString(),
//                        textAlign = TextAlign.Center,
//                        color = Color.Black,
//                        fontSize = 30.sp
//                    )
//                }
//
//                Spacer(Modifier.width(16.dp))
//
//                // Dividend digits
//                uiState.dividend.toString().forEach { digit ->
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier.padding(horizontal = 6.dp)
//                    ) {
//                        Text(
//                            text = digit.toString(),
//                            textAlign = TextAlign.Center,
//                            color = Color.Black,
//                            fontSize = 10.sp
//                        )
//                        Row {
//                            Text(
//                                text = digit.toString(),
//                                textAlign = TextAlign.Center,
//                                fontSize = 30.sp
//                            )
//                            if (dividendTenCoverVisible.value || dividendOneCoverVisible.value) {
//                                Image(
//                                    painter = painterResource(id = R.drawable.slash_gray),
//                                    contentDescription = null,
////                                    modifier = Modifier.align(Alignment.Center)
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//
//            Row(
//                modifier = Modifier
//                    .wrapContentSize()
//                    .padding(top = 10.dp),
//                horizontalArrangement = Arrangement.Center
//            ) {
//                uiState.dividend.toString().forEach { digit ->
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier.padding(horizontal = 6.dp)
//                    ) {
//                        Text(
//                            text = digit.toString(),
//                            textAlign = TextAlign.Center,
//                            color = Color.Black,
//                            fontSize = 10.sp
//                        )
//                        Box {
//                            Text(
//                                text = digit.toString(),
//                                textAlign = TextAlign.Center,
//                                fontSize = 30.sp
//                            )
//                            if (dividendTenCoverVisible.value || dividendOneCoverVisible.value) {
//                                Image(
//                                    painter = painterResource(id = R.drawable.slash_gray),
//                                    contentDescription = null,
//                                    modifier = Modifier.align(Alignment.Center)
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//
//        }

//        ConstraintLayout(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 24.dp, vertical = 24.dp) // 전체적인 패딩
//        ) {
            // createRefs()를 사용하여 각 Composable에 대한 참조 생성
//            val (
//                divisorTextRef, dividendTextRef, quotientTextRef, productTextRef,
//             horizontalLineRef, // 필요하다면 가로선 참조
//                divisionBracketPlaceholderRef, // 나눗셈 기호 위치 참조용 (선택적)
//            ) = createRefs()

//            // 가이드라인 생성
//            // 제수와 피제수/몫 영역을 나누는 수직 가이드라인 (왼쪽에서 30% 지점)
//            val verticalGuideline = createGuidelineFromStart(0.3f)
//            // 몫을 위한 상단 가이드라인 (상단에서 40dp)
//            val topGuidelineForQuotient = createGuidelineFromTop(40.dp)
//            // 피제수를 위한 상단 가이드라인 (상단에서 85dp, 몫보다 아래)
//            val topGuidelineForDividend = createGuidelineFromTop(120.dp)
//            // Product를 위한 상단 가이드라인 (상단에서 125dp, 피제수보다 아래)
//            val topGuidelineForProduct = createGuidelineFromTop(125.dp)
//            // (선택적) 나머지를 위한 상단 가이드라인
//            // val topGuidelineForRemainder = createGuidelineFromTop(165.dp)
//
//
//            // 나눗셈 기호의 대략적인 위치를 참조하기 위한 투명한 Placeholder (선택적)
//            // 실제 나눗셈 기호 Composable의 Modifier.constrainAs()를 사용하는 것이 더 정확합니다.
//            // 이 Placeholder는 나눗셈 기호의 수직선 부분이라고 가정합니다.
//            Spacer(
//                modifier = Modifier.constrainAs(divisionBracketPlaceholderRef) {
//                    top.linkTo(topGuidelineForDividend, margin = (-25).dp) // 피제수보다 약간 위
//                    end.linkTo(verticalGuideline) // 수직 가이드라인의 왼쪽에 붙임 (수직선 역할)
//                    start.linkTo(verticalGuideline, margin = (-20).dp) // 수직선 두께만큼 왼쪽으로 (예시)
//                    width = Dimension.value(20.dp) // 나눗셈 기호 수직선의 너비 (예시)
//                    height = Dimension.value(70.dp) // 나눗셈 기호의 전체 높이 (예시)
//                }
//                 .background(Color.Cyan.copy(alpha = 0.3f)) // 영역 확인용
//            )
//
//            // 1. Divisor (제수)
//            Text(
//                text = uiState.divisor.toString(),
//                fontSize = 60.sp,
//                textAlign = TextAlign.End, // 오른쪽 정렬 (제수는 보통 오른쪽에 붙음)
//                modifier = Modifier.constrainAs(divisorTextRef) {
//                    // 피제수와 같은 높이에서 시작 (baseline 정렬도 고려 가능)
//                    top.linkTo(topGuidelineForDividend)
//                    // verticalGuideline의 왼쪽에 위치, 약간의 마진
//                    end.linkTo(verticalGuideline, margin = 8.dp)
//                    // 너비를 wrapContent로 설정하여 내용에 맞게 크기 조절
//                    width = Dimension.wrapContent
//                }
//            )
//
//            // 2. Quotient (몫)
//            Row(
//                modifier = Modifier.constrainAs(quotientTextRef) {
//                    // 몫을 위한 상단 가이드라인에 맞춰 정렬
//                    top.linkTo(topGuidelineForQuotient)
//                    // verticalGuideline의 오른쪽에서 시작
//                    start.linkTo(verticalGuideline, margin = 8.dp)
//                    // 몫이 길어질 수 있으므로, 오른쪽 끝을 부모의 끝 또는 다른 요소에 연결 가능
//                    // end.linkTo(parent.end, margin = 8.dp) // 필요시
//                    width = Dimension.wrapContent // Row 내용에 맞게 너비 조절
//                }
//            ) {
//                // uiState.quotient를 문자열로 변환하여 각 자리수 표시
//                // 예: " 0", "12" (두 자리로 가정)
//                val quotientStr = uiState.quotient.toString().padStart(2, ' ')
//                quotientStr.forEach { digitChar ->
//                    Text(
//                        text = digitChar.toString(),
//                        fontSize = 60.sp,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.padding(horizontal = 4.dp) // 숫자 간 간격
//                    )
//                }
//            }
//
//            // 3. Dividend (피제수)
//            Row(
//                modifier = Modifier.constrainAs(dividendTextRef) {
//                    // 피제수를 위한 상단 가이드라인에 맞춰 정렬
//                    top.linkTo(topGuidelineForDividend)
//                    // verticalGuideline의 오른쪽에서 시작 (몫과 같은 시작점)
//                    start.linkTo(quotientTextRef.start) // 몫의 시작점에 맞춤
//                    // 피제수가 길어질 수 있으므로, 오른쪽 끝을 부모의 끝 또는 다른 요소에 연결 가능
//                    // end.linkTo(parent.end, margin = 8.dp) // 필요시
//                    width = Dimension.wrapContent // Row 내용에 맞게 너비 조절
//                }
//            ) {
//                // uiState.dividend를 문자열로 변환하여 각 자리수 표시
//                uiState.dividend.toString().forEachIndexed { index, digitChar ->
//                    // 각 자리수 위에 작은 숫자(carrying) 등을 표시하려면 Column 사용 가능
//                    // 슬래시(/) 표시 로직도 여기에 추가
//                    Text(
//                        text = digitChar.toString(),
//                        fontSize = 60.sp,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.padding(horizontal = 4.dp) // 숫자 간 간격
//                    )
//                }
//            }
//
//            // (선택적) 피제수와 Product 사이의 가로선
//             Image(
//                 painter = painterResource(id = R.drawable.ic_horizontal_line),
//                 contentDescription = "horizontal line",
//                 modifier = Modifier
//                     .constrainAs(horizontalLineRef) {
//                         top.linkTo(dividendTextRef.bottom, margin = 4.dp)
//                         start.linkTo(quotientTextRef.start)
//                         end.linkTo(dividendTextRef.end) // 피제수 너비에 맞춤
//                         height = Dimension.value(5.dp)
//                         width = Dimension.fillToConstraints // start와 end 사이를 채움
//                     }
//
//             )

//            if (uiState.showProduct) { // Product를 표시해야 하는 조건 (ViewModel에서 관리)
//                Row(
//                    modifier = Modifier.constrainAs(productTextRef) {
//                        // Product를 위한 상단 가이드라인에 맞춰 정렬
//                        // 또는 피제수 아래에 그린 가로선(horizontalLineRef)이 있다면 그 아래에 배치:
//                        // top.linkTo(horizontalLineRef.bottom, margin = 4.dp)
//                        top.linkTo(topGuidelineForProduct)
//
//                        // 몫/피제수와 같은 시작점에 정렬 (왼쪽 정렬)
//                        start.linkTo(quotientTextRef.start)
//
//                        // Product의 오른쪽 끝을 피제수의 오른쪽 끝에 맞추거나 (end 정렬 시 유용),
//                        // 내용에 맞게 wrapContent로 둘 수 있습니다.
//                        // 만약 오른쪽 정렬을 원한다면:
//                        // end.linkTo(dividendTextRef.end)
//                        // width = Dimension.wrapContent // 또는 Dimension.fillToConstraints 와 함께 end 제약 사용
//
//                        // 너비를 내용에 맞게 조절
//                        width = Dimension.wrapContent
//                    }
//                ) {
//                    // currentProduct 값을 문자열로 변환하여 각 자리수 표시
//                    // 피제수와 자릿수를 맞추기 위해 padStart 등을 사용할 수 있습니다.
//                    // 예를 들어, 피제수가 두 자리라면 Product도 두 자리로 표시 (공백 포함)
//                    val dividendLength = uiState.dividend.toString().length
//                    val productStr = currentProduct.toString().padStart(dividendLength, ' ')
//
//                    productStr.forEach { digitChar ->
//                        Text(
//                            text = digitChar.toString(),
//                            fontSize = 30.sp, // 피제수와 같은 글자 크기
//                            textAlign = TextAlign.Center,
//                            modifier = Modifier.padding(horizontal = 4.dp) // 숫자 간 간격 (피제수와 동일하게)
//                        )
//                    }
//                }

//            val (divisorTextRef, dividendTextRef, quotientTextRef, productTextRef,
//                divisionBracketRef,
//                productLineRef
//            ) = createRefs()
//
//            // 1. Division Bracket (나눗셈 기호)
//            Image(
//                painter = painterResource(id = R.drawable.ic_division_bracket),
//                contentDescription = "Division Bracket",
//                contentScale = ContentScale.FillBounds,
//                modifier = Modifier.constrainAs(divisionBracketRef) {
//                    // 폰트가 커졌으므로, 브라켓의 위치와 크기도 조정될 수 있습니다.
//                    // 이 값들은 실제 브라켓 이미지와 숫자들의 배치에 따라 크게 달라집니다.
//                    top.linkTo(parent.top, margin = 100.dp) // 기존 70.dp에서 증가 (몫 공간 확보)
//                    start.linkTo(parent.start, margin = 60.dp) // 기존 40.dp에서 증가 (제수 공간 확보)
//                    width = Dimension.value(200.dp)  // 기존 120.dp에서 증가 (예시)
//                    height = Dimension.value(120.dp) // 기존 70.dp에서 증가 (예시)
//                }
//            )
//
//            // 2. Divisor (제수)
//            Text(
//                text = uiState.divisor.toString(),
//                fontSize = 60.sp, // 30.sp -> 60.sp
//                textAlign = TextAlign.End,
//                modifier = Modifier.constrainAs(divisorTextRef) {
//                    top.linkTo(divisionBracketRef.top, margin = 20.dp) // 기존 10.dp에서 증가
//                    end.linkTo(divisionBracketRef.start, margin = (-10).dp) // 기존 -5.dp (조정 필요)
//                    width = Dimension.wrapContent
//                }
//            )
//
//            // 3. Quotient (몫)
//            Row(
//                modifier = Modifier.constrainAs(quotientTextRef) {
//                    bottom.linkTo(divisionBracketRef.top, margin = (-5).dp) // 기존 -2.dp (조정 필요)
//                    start.linkTo(divisionBracketRef.start, margin = 45.dp)  // 기존 25.dp에서 증가 (브라켓 수직선 고려)
//                    width = Dimension.wrapContent
//                }
//            ) {
//                val quotientStr = uiState.quotient.toString().padStart(2, ' ')
//                quotientStr.forEach { digitChar ->
//                    Text(
//                        text = digitChar.toString(),
//                        fontSize = 60.sp, // 30.sp -> 60.sp
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.padding(horizontal = 8.dp) // 기존 4.dp에서 증가
//                    )
//                }
//            }
//
//            // 4. Dividend (피제수)
//            Row(
//                modifier = Modifier.constrainAs(dividendTextRef) {
//                    top.linkTo(divisionBracketRef.top, margin = 15.dp) // 기존 8.dp에서 증가
//                    start.linkTo(quotientTextRef.start) // 몫과 같은 시작점 유지
//                    width = Dimension.wrapContent
//                }
//            ) {
//                uiState.dividend.toString().forEachIndexed { index, digitChar ->
//                    Text(
//                        text = digitChar.toString(),
//                        fontSize = 60.sp, // 30.sp -> 60.sp
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.padding(horizontal = 8.dp) // 기존 4.dp에서 증가
//                    )
//                }
//            }

            // 5. Horizontal Line (Product 위 가로선)
//            if (uiState.showProduct) {
//                Image(
//                    painter = painterResource(id = R.drawable.ic_horizontal_line),
//                    contentDescription = "Product Line",
//                    contentScale = ContentScale.FillBounds,
//                    modifier = Modifier.constrainAs(productLineRef) {
//                        top.linkTo(dividendTextRef.bottom, margin = 8.dp) // 기존 4.dp에서 증가
//                        start.linkTo(quotientTextRef.start)
//                        end.linkTo(dividendTextRef.end)
//                        height = Dimension.value(2.dp) // 선 두께는 유지하거나 약간 증가 (예: 1dp -> 2dp)
//                        width = Dimension.fillToConstraints
//                    }
//                )
//            }

//        }

//        ConstraintLayout(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 30.dp, vertical = 30.dp)
//        ) {
//            // 기본 참조들
//            val (divisorTextRef, divisionBracketRef) = createRefs()
//            // 피제수 전체를 담는 Row의 참조
//            val dividendRowRef = createRefs().component1() // Row 전체에 대한 참조
//            // 몫의 각 자리를 위한 참조 (최대 2자리 가정)
//            val (quotientOneTextRef, quotientTenTextRef) = createRefs()
//            // Product 및 관련 라인 참조 (필요시 추가)
//            // val (productLineRef, productTextRef) = createRefs()
//
//            // 몫 (Quotient) - 각 자리를 별도의 Text로 처리
//            val quotientString = uiState.quotient.toString()
//            val dividendString = uiState.dividend.toString() // 피제수 문자열 길이 참조용
//            val carryingNumbersString = listOf("4", "10")
//
//            // 나눗셈 기호 (이전과 동일)
//            Image(
//                painter = painterResource(id = R.drawable.ic_division_bracket),
//                contentDescription = "Division Bracket",
//                contentScale = ContentScale.FillBounds,
//                modifier = Modifier.constrainAs(divisionBracketRef) {
//                    top.linkTo(parent.top, margin = 100.dp)
//                    start.linkTo(parent.start, margin = 60.dp)
//                    width = Dimension.value(200.dp)
//                    height = Dimension.value(120.dp)
//                }
//            )
//
//            // 제수 (Divisor)
//            // 피제수에 보조 숫자가 추가되어 피제수 Row의 top이 기준이 되므로,
//            // 제수도 피제수의 큰 숫자와 시각적으로 정렬되도록 조정합니다.
//            Column( // 제수도 Column으로 감싸서 위쪽에 Spacer를 추가하여 정렬
//                modifier = Modifier.constrainAs(divisorTextRef) {
//                    // 피제수 Row의 상단에 맞춤
//                    top.linkTo(dividendRowRef.top)
//                    // 피제수 Row의 하단에도 연결하여 Column이 세로로 중앙 정렬되도록 시도 (선택적)
//                    // bottom.linkTo(dividendRowRef.bottom)
//                    end.linkTo(divisionBracketRef.start, margin = (-10).dp)
//                    width = Dimension.wrapContent
//                    // height = Dimension.wrapContent // 또는 dividendRowRef 높이에 맞춤
//                },
//                horizontalAlignment = Alignment.End
//            ) {
//                // 피제수의 보조 숫자 높이만큼 Spacer 추가 (대략적인 값, 미세 조정 필요)
//                // 피제수 보조숫자 Text의 height(16.sp)와 유사하게 맞춤
//                Spacer(modifier = Modifier.height(16.dp))
//                Text(
//                    text = uiState.divisor.toString(),
//                    fontSize = 60.sp,
//                    textAlign = TextAlign.End
//                    // 이 Text의 baseline을 피제수 큰 숫자의 baseline과 맞추는 것이 가장 이상적
//                )
//            }
//
//            // 피제수 Row: 각 자리 숫자를 Text로 포함
//            // 이 Row의 start와 end가 몫의 정렬 기준이 됩니다.
//            Row(
//                modifier = Modifier.constrainAs(dividendRowRef) {
//                    top.linkTo(divisionBracketRef.top, margin = 35.dp) // 브라켓 상단에서 적절히 아래
//                    // 피제수 Row의 시작점: 브라켓의 수직선 오른쪽 또는 특정 가이드라인
//                    start.linkTo(divisionBracketRef.start, margin = 45.dp) // 예시 값, 조정 필요
//                    // 피제수 Row의 너비는 내용에 맞게
//                    width = Dimension.wrapContent
//                },
//                verticalAlignment = Alignment.Top
//            ) {
//
//                dividendString.forEachIndexed { index, digitChar ->
//                    val carryingNumberPosition = dividendString.length - 1 - index
//                    val carryingNumber = if (carryingNumberPosition < carryingNumbersString.size) {
//                        carryingNumbersString.reversed()[carryingNumberPosition]
//                    } else ""
//
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier.padding(horizontal = 8.dp)
//                    ){
//                        Text(
//                            text = carryingNumber,
//                            fontSize = 12.sp,
//                            color = Color.Gray,
//                            textAlign = TextAlign.Center,
//                            modifier = Modifier.height(16.dp)
//                        )
//                        Text(
//                            text = digitChar.toString(),
//                            fontSize = 60.sp,
//                            textAlign = TextAlign.Center,
//                        )
//                    }
//                }
//            }
//
//            // 몫의 일의 자리 (항상 존재한다고 가정, 없으면 공백)
//            val quotientOneChar = if (quotientString.isNotEmpty()) quotientString.last() else ' '
//            Text(
//                text = quotientOneChar.toString(),
//                fontSize = 60.sp,
//                textAlign = TextAlign.Center,
//                modifier = Modifier
//                    .padding(horizontal = 8.dp) // 피제수 숫자와 동일한 패딩
//                    .constrainAs(quotientOneTextRef) {
//                        // 기준: 피제수 Row(dividendRowRef)의 끝(오른쪽)
//                        end.linkTo(dividendRowRef.end)
//                        // 위치: 나눗셈 기호 위쪽
//                        bottom.linkTo(divisionBracketRef.top, margin = (-5).dp)
//                    }
//            )
//
//            // 몫의 십의 자리 (몫이 두 자리 이상일 때만 표시)
//            if (quotientString.length > 1) {
//                val quotientTenChar = quotientString[quotientString.length - 2] // 끝에서 두 번째 글자
//                Text(
//                    text = quotientTenChar.toString(),
//                    fontSize = 60.sp,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .padding(horizontal = 8.dp) // 피제수 숫자와 동일한 패딩
//                        .constrainAs(quotientTenTextRef) {
//                            // 기준: 몫의 일의 자리(quotientOneTextRef)의 시작점(왼쪽)
//                            end.linkTo(quotientOneTextRef.start)
//                            // 위치: 몫의 일의 자리와 동일한 baseline 또는 top/bottom
//                            baseline.linkTo(quotientOneTextRef.baseline) // 또는 top.linkTo(quotientOneTextRef.top)
//                        }
//                )
//            } else if (dividendString.length > 1 && quotientString.length == 1) {
//                // (선택적) 몫이 한 자리이고 피제수가 두 자리 이상일 때,
//                // 몫의 십의 자리에 해당하는 위치에 투명한 공백을 넣어 공간을 차지하게 할 수 있음.
//                // 이렇게 하면 몫의 일의 자리가 피제수의 일의 자리에 더 정확히 정렬될 수 있음.
//                // 이 부분은 디자인에 따라 필요 없을 수도 있습니다.
//                Text(
//                    text = " ", // 공백
//                    fontSize = 60.sp,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .padding(horizontal = 8.dp)
//                        .constrainAs(quotientTenTextRef) { // 같은 참조 사용 또는 새 참조
//                            end.linkTo(quotientOneTextRef.start)
//                            baseline.linkTo(quotientOneTextRef.baseline)
//                        }
//                )
//            }
//        }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 30.dp) // 전체 패딩 유지
        ) {
            val (divisorTextRef, divisionBracketRef, dividendRowRef,
                quotientOneTextRef, quotientTenTextRef,
                productLineRef, productTextRef
            ) = createRefs()

            val quotientString = uiState.quotient.toString()
            val dividendString = uiState.dividend.toString() // 피제수 문자열 길이 참조용
            val carryingString = listOf("4", "10")

            val productionString = dividendString
            val productionCarryingString = carryingString

            // 나눗셈 기호: 전체적으로 위로 올림
            Image(
                painter = painterResource(id = R.drawable.ic_division_bracket_short),
                contentDescription = "Division Bracket",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.constrainAs(divisionBracketRef) {
                    top.linkTo(parent.top, margin = 70.dp) // 기존 100.dp에서 감소 (위로 이동)
                    start.linkTo(parent.start, margin = 60.dp)
                    width = Dimension.value(150.dp)
                    height = Dimension.value(120.dp)
                }
            )

            // 피제수 Row:
            Row(
                modifier = Modifier.constrainAs(dividendRowRef) {
                    // divisionBracketRef가 위로 이동했으므로, top 마진 재조정
                    top.linkTo(divisionBracketRef.top, margin = 25.dp) // 기존 35.dp에서 감소 (상대적 위치 유지 또는 미세조정)
                    start.linkTo(divisionBracketRef.start, margin = 45.dp)
                    width = Dimension.wrapContent
                },
                verticalAlignment = Alignment.Top
            ) {

                dividendString.forEachIndexed { index, digitChar ->
                    val carryingNumberPosition = dividendString.length - 1 - index
                    val carryingNumber = if (carryingNumberPosition < carryingString.size)
                        carryingString.reversed()[carryingNumberPosition] else ""

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        // 보조 숫자: 크기 증가
                        Text(
                            text = carryingNumber,
                            fontSize = 18.sp, // 기존 12.sp에서 증가
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.height(22.dp) // 기존 16.sp에서 증가 (fontSize에 맞게)
                        )
                        Text(
                            text = digitChar.toString(),
                            fontSize = 60.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = digitChar.toString(),
                            fontSize = 60.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // 제수 (Divisor)
            Column(
                modifier = Modifier.constrainAs(divisorTextRef) {
                    top.linkTo(dividendRowRef.top)
                    end.linkTo(divisionBracketRef.start, margin = (-10).dp)
                    width = Dimension.wrapContent
                },
                horizontalAlignment = Alignment.End
            ) {
                // Spacer 높이를 새로운 보조 숫자 높이에 맞게 조정
                Spacer(modifier = Modifier.height(22.dp)) // 기존 16.sp에서 증가
                Text(
                    text = uiState.divisor.toString(),
                    fontSize = 60.sp,
                    textAlign = TextAlign.End
                )
            }

            // 몫 (Quotient)
            val quotientOneChar = if (quotientString.isNotEmpty()) quotientString.last() else ' '
            Text(
                text = quotientOneChar.toString(),
                fontSize = 60.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .constrainAs(quotientOneTextRef) {
                        end.linkTo(dividendRowRef.end)
                        // divisionBracketRef가 위로 이동했으므로, bottom 마진 재조정
                        bottom.linkTo(divisionBracketRef.top, margin = (-2).dp) // 기존 -5.dp에서 조정 (더 가깝게)
                    }
            )

            if (quotientString.length > 1) {
                val quotientTenChar = quotientString[quotientString.length - 2]
                Text(
                    text = quotientTenChar.toString(),
                    fontSize = 60.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .constrainAs(quotientTenTextRef) {
                            end.linkTo(quotientOneTextRef.start)
                            baseline.linkTo(quotientOneTextRef.baseline)
                        }
                )
            } else if (uiState.dividend.toString().length > 1 && quotientString.length == 1) {
                Text(
                    text = " ",
                    fontSize = 60.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .constrainAs(quotientTenTextRef) {
                            end.linkTo(quotientOneTextRef.start)
                            baseline.linkTo(quotientOneTextRef.baseline)
                        }
                )
            }

            Image(
                painter = painterResource(id = R.drawable.ic_horizontal_line),
                contentDescription = "Product Line",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.constrainAs(productLineRef) {
                    // 피제수(dividendRowRef)의 아래에 위치
                    top.linkTo(dividendRowRef.bottom, margin = 8.dp) // 기존 4.dp에서 증가 (폰트 커짐 고려)
                    // 가로선의 시작점은 몫/피제수의 시작점에 맞춤
                    // 몫이 여러 자리일 경우, 가장 왼쪽 몫 숫자(quotientTenTextRef 또는 quotientOneTextRef)의 시작점에 맞추거나,
                    // 피제수(dividendRowRef)의 시작점에 맞출 수 있습니다.
                    // 여기서는 피제수의 시작점에 맞추겠습니다. (몫의 정렬 방식에 따라 달라질 수 있음)
                    start.linkTo(dividendRowRef.start)
                    // 가로선의 끝점은 피제수의 끝점에 맞춤
                    end.linkTo(dividendRowRef.end)
                    height = Dimension.value(5.dp) // 선 두께는 유지하거나 약간 증가 (예: 1dp -> 2dp)
                    width = Dimension.fillToConstraints // start와 end 제약 사이를 채움
                }
            )

            Row(
                modifier = Modifier.constrainAs(productTextRef) {
                    top.linkTo(productLineRef.bottom, margin = 8.dp) // 가로선 아래에 위치
                    start.linkTo(dividendRowRef.start) // 피제수 시작점에 맞춤
                    // Product Row의 너비는 내용에 맞게
                    width = Dimension.wrapContent
                    // 만약 Product도 피제수처럼 오른쪽 정렬을 원한다면 end 제약도 추가
                    // end.linkTo(dividendRowRef.end)
                    // width = Dimension.fillToConstraints (start와 end를 모두 사용시)
                }
            ) {
                // 임시로 피제수 값을 Product로 사용
                // 피제수와 자릿수를 맞추기 위해 padStart 등을 사용할 수 있으나,
                // 여기서는 피제수와 동일한 값을 사용하므로 자릿수가 이미 맞다고 가정합니다.
                // 만약 실제 Product 값이 피제수보다 자릿수가 적을 수 있다면,
                // val dividendLength = uiState.dividend.toString().length
                // val actualProductString = yourActualProductValue.toString().padStart(dividendLength, ' ')
                // 와 같이 처리해야 합니다.

                productionString.forEachIndexed { index, digitChar ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = digitChar.toString(),
                            fontSize = 60.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }



        }

        // TODO: Add ansFirstLine visibility and subsequent steps

        // Number pad and feedback
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
