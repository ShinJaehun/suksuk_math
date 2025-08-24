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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.shinjaehun.suksuk.R
import com.shinjaehun.suksuk.presentation.common.device.DeviceClasses
import com.shinjaehun.suksuk.presentation.common.device.LocalDeviceClasses
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
    val deviceClasses = LocalDeviceClasses.current

    var showPicker by remember { mutableStateOf(false) }

    Box(
        Modifier.fillMaxSize()
    ) {
        // 전체 배경 이미지
        Image(
            painter = painterResource(R.drawable.background), // ← 네 배경 리소스
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // ===== 가운데 가로 리본(진한 반투명) =====
        LogoBackdrop(
            modifier = Modifier.align(Alignment.Center)
                .zIndex(1f)
        ) {
            val (w, h) = if (deviceClasses.isLarge) 400.dp to 220.dp else 260.dp to 150.dp

            // 타이틀 (리소스 분기는 폴더로 자동 처리: drawable-nodpi / drawable-sw600dp-nodpi)
            Image(
                painter = painterResource(R.drawable.title),
                contentDescription = "쑥쑥수학",
                modifier = Modifier.width(w).height(h),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(20.dp))

            OutlinedWhiteButton(
                label = "문제풀기",
                onClick = {
                    showPicker = true
//                    Log.d("MainScreen", "showPicker -> $showPicker")

                },
                modifier = Modifier
                    .widthIn(min = 160.dp)
                    .height(48.dp)
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
private fun LogoBackdrop(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val dc = LocalDeviceClasses.current
    val isSmallPortrait = !dc.isLarge && !dc.isLandscape

    // baseline 사이즈
    val backdropWidth  = if (dc.isLarge) 660.dp else 340.dp
    val backdropHeight = when {
        isSmallPortrait -> 310.dp
        dc.isLarge      -> 530.dp
        else            -> 280.dp
    }

    val cornerShape = when {
        dc.isLarge      -> RoundedCornerShape(40.dp)
        dc.isLandscape  -> RoundedCornerShape(24.dp)
        else            -> RectangleShape
    }

    val boxModifier = modifier
        // 작은 화면 세로만 패딩 없음, 나머지는 32dp
        .then(if (isSmallPortrait) Modifier else Modifier.padding(32.dp))
        // ⬇️ 작은 화면 세로만 가로 꽉 채우기, 그 외는 고정 폭
        .then(if (isSmallPortrait) Modifier.fillMaxWidth() else Modifier.width(backdropWidth))
        .height(backdropHeight)
        .background(Color.Black.copy(alpha = 0.62f), cornerShape)

    Box(modifier = boxModifier, contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            content = content
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640, name = "Phone", apiLevel = 34)
@Composable
fun MainScreenPhonePreview() {
    SukSukTheme {
        CompositionLocalProvider(
            LocalDeviceClasses provides DeviceClasses.Default.copy(
                isLarge = false,
                isLandscape = false,
                isSmallLandscape = false,
                widthDp = 360,
                heightDp = 640
            )
        ) {
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
}

@Preview(showBackground = true, widthDp = 800, heightDp = 1280, name = "Tablet", apiLevel = 34)
@Composable
fun MainScreenTabletPreview() {
    SukSukTheme {
        CompositionLocalProvider(
            LocalDeviceClasses provides DeviceClasses.Default.copy(
                isLarge = true,
                isLandscape = false,
                isSmallLandscape = false,
                widthDp = 800,
                heightDp = 1280
            )
        ) {
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
}

@Preview(showBackground = true, widthDp = 640, heightDp = 360, name = "Phone – Landscape", apiLevel = 34)
@Composable
fun MainScreenPhoneLandscapePreview() {
    SukSukTheme {
        CompositionLocalProvider(
            LocalDeviceClasses provides DeviceClasses.Default.copy(
                isLarge = false,
                isLandscape = true,
                isSmallLandscape = true,   // 비태블릿 + 가로
                widthDp = 640,
                heightDp = 360
            )
        ) {
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
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800, name = "Tablet – Landscape", apiLevel = 34)
@Composable
fun MainScreenTabletLandscapePreview() {
    SukSukTheme {
        CompositionLocalProvider(
            LocalDeviceClasses provides DeviceClasses.Default.copy(
                isLarge = true,
                isLandscape = true,
                isSmallLandscape = false,
                widthDp = 1280,
                heightDp = 800
            )
        ) {
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
}