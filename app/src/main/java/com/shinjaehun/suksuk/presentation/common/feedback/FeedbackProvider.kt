package com.shinjaehun.suksuk.presentation.common.feedback

import kotlinx.coroutines.flow.SharedFlow

interface FeedbackProvider {
    val events: SharedFlow<FeedbackEvent>
    fun wrong(message: String)
    fun correct(message: String)
    fun phaseCompleted()
}