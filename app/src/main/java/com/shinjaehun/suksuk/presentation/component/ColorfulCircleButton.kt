package com.shinjaehun.suksuk.presentation.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer

//@Composable
//fun ColorfulCircleButton(
//    label: String,
//    modifier: Modifier = Modifier,
//    brush: Brush,
//    onClick: () -> Unit,
//    pressedSound: (() -> Unit)? = null,
//    size: Dp = 56.dp,               // 접근성 고려해서 56~64dp 권장
//    fontSize: TextUnit = 22.sp,
//    corner: Shape = CircleShape,
//) {
//    val interaction = remember { MutableInteractionSource() }
//    val pressed by interaction.collectIsPressedAsState()
//    val scale by animateFloatAsState(if (pressed) 0.94f else 1f, label = "btn-scale")
//    val elevation by animateDpAsState(if (pressed) 2.dp else 8.dp, label = "btn-elev")
//
//    Box(
//        modifier
//            .size(size)
//            .graphicsLayer {
//                this.scaleX = scale
//                this.scaleY = scale
//                this.shadowElevation = elevation.toPx()
//                this.shape = corner
//                this.clip = true
//            }
//            .background(brush, corner)
//            .border(
//                width = 1.dp,
//                color = Color.White.copy(alpha = 0.25f), // 하이라이트 테두리
//                shape = corner
//            )
//            .clickable(
//                interactionSource = interaction,
//                indication = ripple(bounded = true)
//            ) {
//                pressedSound?.invoke()
//                onClick()
//            }
//            .padding(12.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        // 살짝 입체감: 상단 하이라이트 오버레이
//        Box(
//            Modifier
//                .matchParentSize()
//                .background(
//                    Brush.verticalGradient(
//                        0f to Color.White.copy(0.10f),
//                        0.4f to Color.Transparent,
//                        1f to Color.Transparent
//                    ),
//                    corner
//                )
//        )
//        Text(
//            label,
//            fontSize = fontSize,
//            fontWeight = FontWeight.SemiBold,
//            color = Color.White
//        )
//    }
//}

@Composable
fun ColorfulCircleButton(
    label: String,
    modifier: Modifier = Modifier,
    brush: Brush,
    onClick: () -> Unit,
    size: Dp = 56.dp,
    fontSize: TextUnit = 22.sp,
    corner: Shape = CircleShape,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.94f else 1f, label = "btn-scale")
    val elevation by animateDpAsState(if (pressed) 2.dp else 8.dp, label = "btn-elev")

    Box(
        modifier
            .size(size)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                shadowElevation = elevation.toPx()
                shape = corner
                clip = true
            }
            .background(brush, corner)
            .border(1.dp, Color.White.copy(alpha = 0.25f), corner)
            .clickable(
                interactionSource = interaction,
                indication = ripple(bounded = true)
            ) {
                onClick()
            }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        // 상단 하이라이트(입체감)
        Box(
            Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.White.copy(0.10f),
                        0.4f to Color.Transparent,
                        1f to Color.Transparent
                    ),
                    corner
                )
        )
        Text(
            text = label,
            fontSize = fontSize,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}