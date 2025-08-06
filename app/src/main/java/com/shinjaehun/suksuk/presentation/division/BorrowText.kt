package com.shinjaehun.suksuk.presentation.division

import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shinjaehun.suksuk.domain.division.Highlight
import com.shinjaehun.suksuk.domain.division.InputCellV2

@Composable
fun BorrowText(
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
    Text(
        text = cell.value ?: "",
        fontSize = fontSize,
        color = textColor,
        textAlign = TextAlign.Center,
        modifier = modifier.width(width)
            .testTag("${cell.cellName}-cell"),
    )
}