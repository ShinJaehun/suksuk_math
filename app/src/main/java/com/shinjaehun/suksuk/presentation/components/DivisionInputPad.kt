package com.shinjaehun.suksuk.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DivisionInputPad(
    onNumberClick: (Int) -> Unit,
    onClearClick: () -> Unit,
//    onEnterClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
    ) {
        // Clear 버튼 (좌측)
        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onClearClick,
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.LightGray, shape = CircleShape)
            ) {
                Icon(Icons.Default.Clear, contentDescription = "Clear")
            }
        }

        // 숫자 버튼 (가운데)
        Column(
            modifier = Modifier
                .width(180.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
        ) {
            listOf(
                listOf(1, 2, 3),
                listOf(4, 5, 6),
                listOf(7, 8, 9),
                listOf(null, 0, null)
            ).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    row.forEach { number ->
                        if (number != null) {
                            Button(
                                onClick = { onNumberClick(number) },
                                modifier = Modifier.size(40.dp),
                                colors = ButtonDefaults.buttonColors()
                            ) {
                                Text("$number", fontSize = 20.sp, color = Color.White)
                            }
                        } else {
                            Spacer(modifier = Modifier.size(40.dp))
                        }
                    }
                }
            }
        }

        // Enter 버튼 (우측)
        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
//                onClick = onEnterClick,
                onClick = {},
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.Green, shape = CircleShape)
            ) {
                Icon(Icons.Default.Check, contentDescription = "Enter", tint = Color.White)
            }
        }
    }
}