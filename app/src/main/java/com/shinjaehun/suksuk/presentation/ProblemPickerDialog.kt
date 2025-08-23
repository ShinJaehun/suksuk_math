package com.shinjaehun.suksuk.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.shinjaehun.suksuk.R
import com.shinjaehun.suksuk.presentation.common.device.DeviceClasses
import com.shinjaehun.suksuk.presentation.common.device.HeightClass
import com.shinjaehun.suksuk.presentation.common.device.LocalDeviceClasses
import com.shinjaehun.suksuk.presentation.common.device.WidthClass
import com.shinjaehun.suksuk.presentation.component.OutlinedWhiteButton
import com.shinjaehun.suksuk.ui.theme.SukSukTheme

//@Composable
//fun ProblemPickerDialog(
//    onDismiss: () -> Unit,
//    onChooseMultiply2x2: () -> Unit,
//    onChooseMultiply3x2: () -> Unit,
//    onChooseDivision2x1: () -> Unit,
//    onChooseDivision2x2: () -> Unit,
//    onChooseDivision3x2: () -> Unit,
//    onChooseChallenge: () -> Unit,
//) {
//    val maxW = dimensionResource(id = R.dimen.dialog_max_width)
//    val maxH = dimensionResource(id = R.dimen.dialog_max_height)
//    val pad  = dimensionResource(id = R.dimen.dialog_padding)
//
//    Dialog(
//        onDismissRequest = onDismiss,
//        properties = DialogProperties(usePlatformDefaultWidth = true)
//    ) {
//        Surface(
//            color = Color(0xFF455A64),
//            shape = RoundedCornerShape(16.dp),
//            tonalElevation = 6.dp,
//            modifier = Modifier
//                .padding(horizontal = pad, vertical = pad)
//                .widthIn(min = 280.dp, max = maxW)
//                .heightIn(max = maxH)
//        ) {
//            Column(
//                modifier = Modifier.padding(pad),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                val gap = dimensionResource(id = R.dimen.tile_gap)
//
//                Column(
//                    modifier = Modifier
//                        .verticalScroll(rememberScrollState())
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .padding(vertical = gap / 2)
//                            .align(Alignment.CenterHorizontally),
//                        horizontalArrangement = Arrangement.spacedBy(gap)
//                    ) {
//                        OptionTile(R.drawable.multiply22, "두 자리 X 두 자리") { onChooseMultiply2x2() }
//                        OptionTile(R.drawable.multiply32, "세 자리 X 두 자리") { onChooseMultiply3x2() }
//                    }
//
//                    Row(
//                        modifier = Modifier
//                            .padding(vertical = gap / 2)
//                            .align(Alignment.CenterHorizontally),
//                        horizontalArrangement = Arrangement.spacedBy(gap)
//                    ) {
//                        OptionTile(R.drawable.divide21, "두 자리 ÷ 한 자리") { onChooseDivision2x1() }
//                        OptionTile(R.drawable.divide22, "두 자리 ÷ 두 자리") { onChooseDivision2x2() }
//                    }
//
//                    Row(
//                        modifier = Modifier
//                            .padding(vertical = gap / 2)
//                            .align(Alignment.CenterHorizontally),
//                        horizontalArrangement = Arrangement.spacedBy(gap)
//                    ) {
//                        OptionTile(R.drawable.divide32, "세 자리 ÷ 두 자리") { onChooseDivision3x2() }
//                        OptionTile(R.drawable.challenge, "도전! 문제풀기") { onChooseChallenge() }
//                    }
//                }
//            }
//        }
//    }
//}

@Composable
fun ProblemPickerDialog(
    onDismiss: () -> Unit,
    onChooseMultiply2x2: () -> Unit,
    onChooseMultiply3x2: () -> Unit,
    onChooseDivision2x1: () -> Unit,
    onChooseDivision2x2: () -> Unit,
    onChooseDivision3x2: () -> Unit,
    onChooseChallenge: () -> Unit,
) {
    val dc  = LocalDeviceClasses.current
    val maxW = dimensionResource(id = R.dimen.dialog_max_width)
    val maxH = dimensionResource(id = R.dimen.dialog_max_height)
    val pad  = dimensionResource(id = R.dimen.dialog_padding)

    val dialogProps = remember(dc.isSmallLandscape) {
        DialogProperties(usePlatformDefaultWidth = !dc.isSmallLandscape)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = dialogProps
    ) {
        Surface(
            color = Color(0xFF455A64),
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 6.dp,
            modifier = if (dc.isSmallLandscape) {
                Modifier
                    // 🔸 작은 화면 + 가로에서만 거의 꽉 차도록
                    .fillMaxWidth(0.7f)
                    .windowInsetsPadding(WindowInsets.systemBars)
                    .padding(horizontal = 16.dp)   // 바깥 여백 살짝만
                    .heightIn(max = maxH)
            } else {
                Modifier
                    .padding(horizontal = pad, vertical = pad)
                    .widthIn(min = 280.dp, max = maxW)
                    .heightIn(max = maxH)
            }
        ) {
            Column(
                modifier = Modifier.padding(pad),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val gap = dimensionResource(id = R.dimen.tile_gap)

                // 한 번만 정의
                val items = listOf(
                    Triple(R.drawable.multiply22, "두 자리 X 두 자리", onChooseMultiply2x2),
                    Triple(R.drawable.multiply32, "세 자리 X 두 자리", onChooseMultiply3x2),
                    Triple(R.drawable.divide21,   "두 자리 ÷ 한 자리", onChooseDivision2x1),
                    Triple(R.drawable.divide22,   "두 자리 ÷ 두 자리", onChooseDivision2x2),
                    Triple(R.drawable.divide32,   "세 자리 ÷ 두 자리", onChooseDivision3x2),
                    Triple(R.drawable.challenge,  "도전! 문제풀기",     onChooseChallenge),
                )

                // ⬅️ 작은 화면 가로면 3열, 그 외엔 2열(기존과 동일 체감)
                val columns = if (dc.isSmallLandscape) 3 else 2
                val rows = remember(items, columns) { items.chunked(columns) }

                Column(Modifier.verticalScroll(rememberScrollState())) {
                    rows.forEach { row ->
                        Row(
                            modifier = Modifier
                                .padding(vertical = gap / 2)
                                .align(Alignment.CenterHorizontally),
                            horizontalArrangement = Arrangement.spacedBy(gap)
                        ) {
                            row.forEach { (img, label, onClick) ->
                                OptionTile(img, label, onClick = onClick)
                            }
                            // 마지막 줄 비어있는 칸 채워 간격 유지 (OptionTile 기본 폭 120dp 기준)
                            repeat(columns - row.size) {
                                Spacer(Modifier.width(120.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OptionTile(
    @DrawableRes imageRes: Int,
    label: String,
    tileSize: Dp = 120.dp,          // 이미지 정사각 크기
    spacing: Dp = 8.dp,              // 이미지-텍스트 사이 간격
    onClick: () -> Unit,
) {
    // 상위에 Card/Surface로 shape 주지 않고, clip 도 안 줌 (둥근 모서리 제거)
    val interaction = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .width(tileSize)                 // ← 텍스트 포함 전체 폭을 이미지 폭과 동일하게 고정
            .clickable(onClick = onClick),   // ← 단순/안전
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 이미지 영역 = 정확히 tileSize 정사각
        Box(
            modifier = Modifier
                .size(tileSize)              // ← Box 크기 = 이미지 크기와 동일
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = label,
                modifier = Modifier
                    .matchParentSize()       // ← Box와 정확히 동일
                // .clip(RectangleShape) // 기본값이 직사각형이므로 굳이 필요 없음
                // 둥근 모서리 유발하는 어떤 clip/shape 도 사용하지 않음
                ,
                contentScale = ContentScale.Crop // 이미지 꽉 채우기(여백 없이)
            )
        }

        Spacer(Modifier.height(spacing))

        // 텍스트 폭도 이미지 폭과 동일하게 고정 → “미묘한 크기 차이” 방지
        Text(
            text = label,
            modifier = Modifier.width(tileSize),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

//@Composable
//fun ProblemPickerDialog(
//    onDismiss: () -> Unit,
//    onChooseMultiply2x2: () -> Unit,
//    onChooseMultiply3x2: () -> Unit,
//    onChooseDivision2x1: () -> Unit,
//    onChooseDivision2x2: () -> Unit,
//    onChooseDivision3x2: () -> Unit,
//    onChooseChallenge: () -> Unit,
//) {
//    val dc = LocalDeviceClasses.current
//
//    val pad  = dimensionResource(id = R.dimen.dialog_padding)
//    val gap  = dimensionResource(id = R.dimen.tile_gap)
//
//    // 화면 비율 기반으로 Dialog 최대치 동적 설정
//    // - 작은 세로: 높이 92%까지 → 마지막 행 안 잘림
//    // - 작은 가로: 폭 98%까지 → 가로 공간 넉넉
//    // - 큰 화면: 폭/높이 80~85% → 여백 줄이고 배치 균등
//    val maxWidthDp  = when {
//        dc.isLarge && dc.isLandscape -> (dc.widthDp.dp * 0.85f)
//        dc.isLarge -> (dc.widthDp.dp * 0.80f)
//        dc.isLandscape -> (dc.widthDp.dp * 0.98f)
//        else -> (dc.widthDp.dp * 0.92f)
//    }
//    val maxHeightDp = when {
//        dc.isLarge && dc.isLandscape -> (dc.heightDp.dp * 0.85f)
//        dc.isLarge -> (dc.heightDp.dp * 0.85f)
//        dc.isLandscape -> (dc.heightDp.dp * 0.80f)
//        else -> (dc.heightDp.dp * 0.92f)
//    }
//
//    // 가로/세로에 따라 열 수: 세로=2, 가로=3
//    val columns = if (dc.isLandscape) 3 else 2
//
//    val items = listOf(
//        Triple(R.drawable.multiply22, "두 자리 X 두 자리", onChooseMultiply2x2),
//        Triple(R.drawable.multiply32, "세 자리 X 두 자리", onChooseMultiply3x2),
//        Triple(R.drawable.divide21,   "두 자리 ÷ 한 자리", onChooseDivision2x1),
//        Triple(R.drawable.divide22,   "두 자리 ÷ 두 자리", onChooseDivision2x2),
//        Triple(R.drawable.divide32,   "세 자리 ÷ 두 자리", onChooseDivision3x2),
//        Triple(R.drawable.challenge,  "도전! 문제풀기",     onChooseChallenge)
//    )
//
//    Dialog(
//        onDismissRequest = onDismiss,
//        properties = DialogProperties(usePlatformDefaultWidth = true)
//    ) {
//        Surface(
//            color = Color(0xFF455A64),
//            shape = RoundedCornerShape(16.dp),
//            tonalElevation = 6.dp,
//            modifier = Modifier
//                .padding(pad)
//                .widthIn(min = 280.dp, max = maxWidthDp)
//                .heightIn(max = maxHeightDp)
//        ) {
//            // 내부 패딩은 최소화(타일에 공간을 양보)
//            BoxWithConstraints(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = pad, vertical = pad)
//            ) {
//                val rows = kotlin.math.ceil(items.size / columns.toFloat()).toInt()
//
//                // 이미지-텍스트 간격/텍스트높이(대략치) — tileSize 계산용
//                val spacingBetweenImageAndText = 8.dp
//                val textApproxHeight = 20.dp
//
//                val availW = maxWidth
//                val availH = maxHeight
//
//                // 1) 타일 크기 산정 (너비/높이 제약 동시 고려)
//                val tileFromWidth =
//                    (availW - gap * (columns - 1)) / columns
//
//                val tileFromHeight =
//                    (availH
//                            - gap * (rows - 1)
//                            - rows * (spacingBetweenImageAndText + textApproxHeight)
//                            ) / rows
//
//                val targetTile = if (dc.isLarge) 140.dp else 120.dp
//                val minTile    = if (dc.isLarge) 96.dp  else 64.dp
//
//                val tileSize = listOf(targetTile, tileFromWidth, tileFromHeight)
//                    .minBy { it.value }
//                    .coerceAtLeast(minTile)
//
//                // 2) 가로 간격 동적 보정(특히 작은 가로모드에서 텍스트 잘림 방지)
//                //    남는 가로 여백을 columns-1개 간격에 균등 분배하되, 최소 gap은 유지
//                val usedW = columns * tileSize
//                val extraW = (availW - usedW).coerceAtLeast(0.dp)
//                val hGapDynamic = if (columns > 1)
//                    maxOf(gap, extraW / (columns - 1))
//                else 0.dp
//
//                // 3) 큰 화면에서는 세로로 균등 분배하여 빈 공간 없애기
//                //    작은 화면에서는 기존 간격 사용(가독성)
//                val verticalArrangement =
//                    if (dc.isLarge) Arrangement.SpaceEvenly else Arrangement.Top
//
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = verticalArrangement
//                ) {
//                    items.chunked(columns).forEach { row ->
//                        Row(
//                            horizontalArrangement = Arrangement.spacedBy(hGapDynamic),
//                            modifier = Modifier
//                                .fillMaxWidth()
//                        ) {
//                            // 가운데 정렬 효과를 위해 남는 양 옆 여백을 Spacer로 균등 분배
//                            val sideSpace = (availW - (columns * tileSize + (columns - 1) * hGapDynamic))
//                                .coerceAtLeast(0.dp) / 2
//                            Spacer(Modifier.width(sideSpace))
//
//                            row.forEach { (res, label, onClick) ->
//                                OptionTile(
//                                    imageRes = res,
//                                    label = label,
//                                    tileSize = tileSize,
//                                    spacing = spacingBetweenImageAndText,
//                                    onClick = onClick
//                                )
//                            }
//
//                            // 마지막 행 보정 (아이템 부족 시 빈칸으로 셀 채움)
//                            repeat(columns - row.size) {
//                                Spacer(Modifier.size(tileSize))
//                            }
//
//                            Spacer(Modifier.width(sideSpace))
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun OptionTile(
//    @DrawableRes imageRes: Int,
//    label: String,
//    tileSize: Dp = 120.dp,   // 고정 프레임(흰 배경) 정사각
//    spacing: Dp = 8.dp,
//    onClick: () -> Unit,
//) {
//    Column(
//        modifier = Modifier
//            .width(tileSize)
//            .clickable(onClick = onClick),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Box(
//            modifier = Modifier
//                .size(tileSize)
//                .background(Color.White)   // 각진 사각 프레임(이미지 크기 일정화)
//        ) {
//            Image(
//                painter = painterResource(id = imageRes),
//                contentDescription = label,
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(6.dp),        // 원하면 0~10dp 사이로 조절
//                contentScale = ContentScale.Fit
//            )
//        }
//        Spacer(Modifier.height(spacing))
//        Text(
//            text = label,
//            modifier = Modifier.width(tileSize),
//            textAlign = TextAlign.Center,
//            style = MaterialTheme.typography.bodyMedium,
//            color = Color.White,           // 어두운 배경 대비 밝게
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis
//        )
//    }
//}

@Preview(showBackground = true, widthDp = 400, apiLevel = 34)
@Composable
fun ProblemPickerDialogPreview() {
    SukSukTheme {
        Surface {
            ProblemPickerDialog(
                onDismiss = {},
                onChooseMultiply2x2 = {},
                onChooseMultiply3x2 = {},
                onChooseDivision2x1 = {},
                onChooseDivision2x2 = {},
                onChooseDivision3x2 = {},
                onChooseChallenge  = {}
            )
        }
    }
}


@Preview(showBackground = true, widthDp = 640, heightDp = 360, name = "Dialog – Phone Landscape", apiLevel = 34)
@Composable
fun ProblemPickerDialogPhoneLandPreview() {
    SukSukTheme {
        // 📱 작은 화면 가로
        CompositionLocalProvider(
            LocalDeviceClasses provides DeviceClasses.Default.copy(
                isLarge = false, isLandscape = true, isSmallLandscape = true,
                widthDp = 640, heightDp = 360,
                widthClass = WidthClass.Compact, heightClass = HeightClass.Compact
            )
        ) {
            Surface {
                ProblemPickerDialog(
                    onDismiss = {},
                    onChooseMultiply2x2 = {},
                    onChooseMultiply3x2 = {},
                    onChooseDivision2x1 = {},
                    onChooseDivision2x2 = {},
                    onChooseDivision3x2 = {},
                    onChooseChallenge  = {}
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800, name = "Dialog – Tablet Landscape", apiLevel = 34)
@Composable
fun ProblemPickerDialogTabletLandPreview() {
    SukSukTheme {
        // 태블릿 가로
        CompositionLocalProvider(
            LocalDeviceClasses provides DeviceClasses.Default.copy(
                isLarge = true, isLandscape = true,
                widthDp = 1280, heightDp = 800,
                widthClass = WidthClass.Expanded, heightClass = HeightClass.Medium
            )
        ) {
            Surface {
                ProblemPickerDialog(
                    onDismiss = {},
                    onChooseMultiply2x2 = {},
                    onChooseMultiply3x2 = {},
                    onChooseDivision2x1 = {},
                    onChooseDivision2x2 = {},
                    onChooseDivision3x2 = {},
                    onChooseChallenge  = {}
                )
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 800, heightDp = 1280, name = "Dialog – Tablet Portrait", apiLevel = 34)
@Composable
fun ProblemPickerDialogTabletPortPreview() {
    SukSukTheme {
        // 💻 태블릿 세로
        CompositionLocalProvider(
            LocalDeviceClasses provides DeviceClasses.Default.copy(
                isLarge = true, isLandscape = false, isSmallLandscape = false,
                widthDp = 800, heightDp = 1280,
                widthClass = WidthClass.Medium, heightClass = HeightClass.Expanded
            )
        ) {
            Surface {
                ProblemPickerDialog(
                    onDismiss = {},
                    onChooseMultiply2x2 = {},
                    onChooseMultiply3x2 = {},
                    onChooseDivision2x1 = {},
                    onChooseDivision2x2 = {},
                    onChooseDivision3x2 = {},
                    onChooseChallenge  = {}
                )
            }
        }
    }
}