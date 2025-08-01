package com.shinjaehun.suksuk.presentation.division21

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel

// legacy code

//@Composable
//fun Division21Screen(
//    viewModel: Division21ViewModel = hiltViewModel(),
//) {
//    val state by viewModel.uiState.collectAsState()
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        Text("문제: ${state.problem.dividend} ÷ ${state.problem.divisor}")
//
//        OutlinedTextField(
//            value = state.userInput,
//            onValueChange = { viewModel.onInputChange(it) },
//            label = { Text("정답을 입력하세요") }
//        )
//
//        Button(onClick = { viewModel.checkAnswer() }) {
//            Text("정답 확인")
//        }
//
//        state.result?.let {
//            Text(if (it) "정답입니다." else "틀렸습니다.")
//        }
//    }
//
//}