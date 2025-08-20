package com.shinjaehun.suksuk.presentation.common.feedback

sealed interface FeedbackEvent {
    data class Wrong(val message: String): FeedbackEvent
    data class Correct(val message: String): FeedbackEvent
    data object ProblemCompleted: FeedbackEvent
}