package com.shinjaehun.suksuk.presentation.division

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shinjaehun.suksuk.R
import com.shinjaehun.suksuk.domain.division.model.CrossOutType
import com.shinjaehun.suksuk.domain.division.model.Highlight
import com.shinjaehun.suksuk.domain.division.model.InputCellV2

@Composable
fun BorrowTextV2(
    cell: InputCellV2,
    defaultColor: Color = Color.Black,
    fontSize: TextUnit = 20.sp,
    width: Dp = 42.dp,
    modifier: Modifier = Modifier
) {
    val textColor = when (cell.highlight) {
        Highlight.Editing -> Color.Red
        Highlight.Related -> Color(0xFF1976D2) // 파란색 (Material blue)
        else -> defaultColor
    }

    Box(
        modifier = modifier.width(width),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = cell.value ?: "",
            fontSize = fontSize,
            color = textColor,
            textAlign = TextAlign.Center,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Clip,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("${cell.cellName}-cell"),
        )
        if (cell.crossOutType != CrossOutType.None) {
            val painter = when(cell.crossOutType){
                CrossOutType.Pending -> painterResource(R.drawable.ic_strikethrough_pending)
                CrossOutType.Confirmed -> painterResource(R.drawable.ic_strikethrough_confirmed)
                else -> null
            }
            painter?.let {
                Image(
                    painter = it,
                    contentDescription = "취소선",
                    modifier = Modifier
                        .matchParentSize()
                        .testTag("${cell.cellName}-crossed")
                )
            }
        }
    }

}