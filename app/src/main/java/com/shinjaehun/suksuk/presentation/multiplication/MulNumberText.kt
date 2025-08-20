package com.shinjaehun.suksuk.presentation.multiplication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shinjaehun.suksuk.presentation.common.Highlight
import com.shinjaehun.suksuk.presentation.multiplication.model.MulInputCell

@Composable
fun MulNumberText(
    cell: MulInputCell,
    defaultColor: Color = Color.Black,
    fontSize: TextUnit = 40.sp,
    width: Dp = 42.dp,
    modifier: Modifier = Modifier
) {
    val textColor = when (cell.highlight) {
        Highlight.Editing -> Color.Red
        Highlight.Related -> Color.Blue
        else -> defaultColor
    }

    Box(modifier = modifier.width(width), contentAlignment = Alignment.Center) {
        Text(
            text = cell.value ?: "",
            fontSize = fontSize,
            color = textColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag("${cell.cellName}-cell"),
            maxLines = 1
        )
    }
}