package com.shinjaehun.suksuk.presentation.multiplication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shinjaehun.suksuk.presentation.common.Highlight
import com.shinjaehun.suksuk.presentation.multiplication.model.MulInputCell

@Composable
fun MulAuxNumberText(
    cell: MulInputCell,
    defaultColor: Color = Color.Black,
    fontSize: TextUnit = 20.sp,
    width: Dp = 42.dp,
    modifier: Modifier = Modifier
) {
    val textColor = when (cell.highlight) {
        Highlight.Editing -> Color.Red
        Highlight.Related -> Color.Blue
        else -> defaultColor
    }

    val text = cell.value.orEmpty()
    val dynamicWidth = if (text.length >= 2) (width + 10.dp) else width

    Box(modifier = modifier.width(dynamicWidth), contentAlignment = Alignment.Center) {
        Text(
            text = text,
            fontSize = fontSize,
            color = textColor,
            textAlign = TextAlign.Center,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Clip,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("${cell.cellName}-cell")
        )
    }
}