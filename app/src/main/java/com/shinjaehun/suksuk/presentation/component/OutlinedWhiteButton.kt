package com.shinjaehun.suksuk.presentation.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OutlinedWhiteButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interaction = remember { MutableInteractionSource() }
    val isPressed by interaction.collectIsPressedAsState()

    // 레거시 톤: 평소 투명, 눌림 시 살짝 밝은 회색, 비활성은 아주 옅게
    val targetBg = when {
        !enabled -> Color.White.copy(alpha = 0.06f)
        isPressed -> Color.White.copy(alpha = 0.18f)
        else -> Color.Transparent
    }
    val bg by animateColorAsState(targetValue = targetBg, label = "outlined-press-bg")

    val borderColor = if (enabled) Color.White else Color.White.copy(alpha = 0.4f)
    val contentColor = if (enabled) Color.White else Color.White.copy(alpha = 0.4f)

    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interaction,
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(2.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = bg,
            contentColor = contentColor,
            disabledContentColor = Color.White.copy(alpha = 0.4f),
            disabledContainerColor = Color.White.copy(alpha = 0.06f)
        ),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp
        )
    }
}