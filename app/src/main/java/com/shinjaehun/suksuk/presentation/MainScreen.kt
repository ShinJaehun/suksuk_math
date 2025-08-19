package com.shinjaehun.suksuk.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shinjaehun.suksuk.R
import com.shinjaehun.suksuk.presentation.component.OutlinedWhiteButton
import com.shinjaehun.suksuk.ui.theme.SukSukTheme

@Composable
fun MainScreen(
    onChooseDivision2x1: () -> Unit,
    onChooseDivision2x2: () -> Unit,
    onChooseDivision3x2: () -> Unit,
    onChooseMultiply2x2: () -> Unit,
    onChooseMultiply3x2: () -> Unit,
    onChooseChallenge: () -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        // 전체 배경 이미지
        Image(
            painter = painterResource(R.drawable.background), // ← 네 배경 리소스
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // ===== 가운데 가로 리본(진한 반투명) =====
        DarkRibbon(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            // 타이틀 이미지 (레거시 title.png)
            Image(
                painter = painterResource(R.drawable.title),
                contentDescription = "쑥쑥수학",
                modifier = Modifier
                    .width(260.dp)              // 레거시 비율과 유사
                    .height(150.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(20.dp))

            OutlinedWhiteButton(
                label = "문제풀기",
                onClick = { showPicker = true },
                modifier = Modifier
                    .widthIn(min = 160.dp)
                    .height(48.dp)
                    .semantics { testTag = "main_problem_button" }
            )
        }

        // 하단 저작권
        Text(
            text = "2016, 신재훈",
            fontSize = 10.sp,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        )
    }

    if (showPicker) {
        ProblemPickerDialog(
            onDismiss = { showPicker = false },
            onChooseMultiply2x2 = {
                showPicker = false
                onChooseMultiply2x2()
            },
            onChooseMultiply3x2 = {
                showPicker = false
                onChooseMultiply3x2()
            },
            onChooseDivision2x1 = {
                showPicker = false
                onChooseDivision2x1()
            },
            onChooseDivision2x2 = {
                showPicker = false
                onChooseDivision2x2()
            },
            onChooseDivision3x2 = {
                showPicker = false
                onChooseDivision3x2()
            },
            onChooseChallenge = {
                showPicker = false
                onChooseChallenge()
            }
        )
    }
}

@Composable
private fun DarkRibbon(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    // 레거시보다 한 단계 더 어두운 반투명 (alpha 0.62)
    val ribbonColor = Color.Black.copy(alpha = 0.62f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 300.dp)
            .background(ribbonColor) // 모서리 둥글림/그림자 없음 = 리본처럼 화면 끝까지
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            content = content
        )
    }
}


//@Composable
//private fun ProblemButton(
//    text: String,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Card(
//        onClick = onClick,
//        shape = MaterialTheme.shapes.large,
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
//        elevation = CardDefaults.cardElevation(8.dp),
//        modifier = modifier
//            .height(64.dp)
//            .widthIn(min = 160.dp)
//    ) {
//        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//            Text(
//                text = text,
//                fontSize = 18.sp,
//                fontWeight = FontWeight.SemiBold,
//                color = Color.White
//            )
//        }
//    }
//}

@Preview(showBackground = true, widthDp = 360, heightDp = 640, apiLevel = 34)
@Composable
fun MainScreenPreview() {
    SukSukTheme { // 네 프로젝트 테마로 교체
        MainScreen(
            onChooseDivision2x1 = {},
            onChooseDivision2x2 = {},
            onChooseDivision3x2 = {},
            onChooseMultiply2x2 = {},
            onChooseMultiply3x2 = {},
            onChooseChallenge = {}
        )
    }
}
