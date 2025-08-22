package com.shinjaehun.suksuk

import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import com.shinjaehun.suksuk.domain.DomainStateFactory
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.division.sequence.creator.ThreeByTwoDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.sequence.creator.TwoByOneDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.sequence.creator.TwoByTwoDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.multiplication.sequence.creator.ThreeByTwoMulPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.multiplication.sequence.creator.TwoByTwoMulPhaseSequenceCreator
import com.shinjaehun.suksuk.presentation.common.effects.AudioPlayer
import com.shinjaehun.suksuk.presentation.common.feedback.FeedbackEvent
import com.shinjaehun.suksuk.presentation.common.feedback.FeedbackProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object TestFactoryBuilders {
    fun unifiedFactoryForDivision(): DomainStateFactory {
        // Division 실물
        val divProvider = DivisionPhaseSequenceProvider(
            TwoByOneDivPhaseSequenceCreator(),
            TwoByTwoDivPhaseSequenceCreator(),
            ThreeByTwoDivPhaseSequenceCreator()
        )
        // Mul은 호출되지 않지만 생성자 충족 위해 간단 생성
        val mulProvider = MulPhaseSequenceProvider(
            TwoByTwoMulPhaseSequenceCreator(),
            ThreeByTwoMulPhaseSequenceCreator()
        )
        return DomainStateFactory(mulProvider, divProvider)
    }

    fun unifiedFactoryForMultiplication(): DomainStateFactory {
        val mulProvider = MulPhaseSequenceProvider(
            TwoByTwoMulPhaseSequenceCreator(),
            ThreeByTwoMulPhaseSequenceCreator()
        )
        val divProvider = DivisionPhaseSequenceProvider(
            TwoByOneDivPhaseSequenceCreator(),
            TwoByTwoDivPhaseSequenceCreator(),
            ThreeByTwoDivPhaseSequenceCreator()
        )
        return DomainStateFactory(mulProvider, divProvider)
    }
}

object DummyFeedbackProvider : FeedbackProvider {
    override val events: SharedFlow<FeedbackEvent> =
        MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    override fun wrong(message: String) { /* no-op */ }
    override fun correct(message: String) { /* no-op */ }
    override fun phaseCompleted() { /* no-op */ }
}

object NoopAudioPlayer : AudioPlayer {
    override fun playTada() {}
    override fun playBeep() {}
}

object NoopHaptic : HapticFeedback {
    override fun performHapticFeedback(hapticFeedbackType: HapticFeedbackType) { /* no-op */ }
}