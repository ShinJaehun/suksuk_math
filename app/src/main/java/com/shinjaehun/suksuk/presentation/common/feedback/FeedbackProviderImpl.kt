package com.shinjaehun.suksuk.presentation.common.feedback

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedbackProviderImpl @Inject constructor(): FeedbackProvider {
    private val _events = MutableSharedFlow<FeedbackEvent>(extraBufferCapacity = 16)
    override val events: SharedFlow<FeedbackEvent> = _events.asSharedFlow()

    override fun wrong(message: String) { _events.tryEmit(FeedbackEvent.Wrong(message)) }
    override fun correct(message: String) { _events.tryEmit(FeedbackEvent.Correct(message)) }
    override fun phaseCompleted() { _events.tryEmit(FeedbackEvent.ProblemCompleted) }
}