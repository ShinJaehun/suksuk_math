package com.shinjaehun.suksuk.presentation

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.shinjaehun.suksuk.Operation
import com.shinjaehun.suksuk.R
import com.shinjaehun.suksuk.presentation.component.OutlinedWhiteButton
import com.shinjaehun.suksuk.ui.theme.SukSukTheme


@Composable
fun ProblemPickerDialog(
    onDismiss: () -> Unit,
    onSelect: (Operation) -> Unit
) {

    DisposableEffect(Unit) {
        onDispose { Log.d("DIALOG", "disposed") }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = Color(0xFF455A64), // 레거시 배경색
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .widthIn(max = 320.dp)
                        .wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                            .heightIn(max = 480.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(vertical = 6.dp)
                                .align(Alignment.CenterHorizontally),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            OptionTile(
                                imageRes = R.drawable.multiply22,
                                label = "두 자리 X 두 자리"
                            ) { onSelect(Operation.Multiply22) }

                            OptionTile(
                                imageRes = R.drawable.multiply32,
                                label = "세 자리 X 두 자리"
                            ) { onSelect(Operation.Multiply32) }
                        }

                        Row(
                            modifier = Modifier
                                .padding(vertical = 6.dp)
                                .align(Alignment.CenterHorizontally),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            OptionTile(
                                imageRes = R.drawable.divide21,
                                label = "두 자리 ÷ 한 자리"
                            ) { onSelect(Operation.Divide21) }

                            OptionTile(
                                imageRes = R.drawable.divide22,
                                label = "두 자리 ÷ 두 자리"
                            ) { onSelect(Operation.Divide22) }
                        }

                        Row(
                            modifier = Modifier
                                .padding(vertical = 6.dp)
                                .align(Alignment.CenterHorizontally),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            OptionTile(
                                imageRes = R.drawable.divide32,
                                label = "세 자리 ÷ 두 자리"
                            ) { onSelect(Operation.Divide32) }

                            OptionTile(
                                imageRes = R.drawable.challenge,
                                label = "도전! 문제풀기"
                            ) { onSelect(Operation.Challenge) }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    OutlinedWhiteButton(
                        label = "처음 화면으로",
                        onClick = onDismiss,
                        modifier = Modifier
                            .width(150.dp)
                            .height(48.dp)           // 살짝 높이 주면 보기 좋음
                            .testTag("dialog_dismiss_button")
                    )
                }
            }
        }
    }
}

@Composable
private fun OptionTile(
    @DrawableRes imageRes: Int,
    label: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .height(140.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = label,
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .width(100.dp)
                .height(30.dp)
                .background(Color(0xFF607D8B)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 400, apiLevel = 34)
@Composable
fun ProblemPickerDialogPreview() {
    SukSukTheme {
        Surface {
            ProblemPickerDialog(
                onDismiss = {},
                onSelect = {}
            )
        }
    }
}
