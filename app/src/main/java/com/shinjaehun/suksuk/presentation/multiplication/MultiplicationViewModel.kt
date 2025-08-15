package com.shinjaehun.suksuk.presentation.multiplication

import com.shinjaehun.suksuk.common.viewmodel.BaseViewModel
import com.shinjaehun.suksuk.domain.multiplication.evaluator.MulPhaseEvaluator
import com.shinjaehun.suksuk.domain.multiplication.factory.MulDomainStateFactory
import com.shinjaehun.suksuk.domain.multiplication.model.MulCellName
import com.shinjaehun.suksuk.domain.multiplication.model.MulDomainState
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseStep
import com.shinjaehun.suksuk.presentation.multiplication.model.MulUiState
import com.shinjaehun.suksuk.presentation.multiplication.model.mapMultiplicationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MultiplicationViewModel @Inject constructor(
    private val evaluator: MulPhaseEvaluator,
    private val factory: MulDomainStateFactory,
//    private val generateProblem: GenerateMultiplicationProblem // ← 옵션2
) : BaseViewModel<MulUiState, MulDomainState, MulPhaseStep, MulCellName>() {

    fun start(multiplicand: Int, multiplier: Int) = startNewProblem(multiplicand, multiplier)

//    fun startRandom() {
//        val (a, b) = generateProblem()
//        startNewProblem(a, b)
//    }

    override fun createDomain(vararg args: Int) =
        factory.create(args[0], args[1])

    override fun steps(domain: MulDomainState) =
        domain.phaseSequence.steps

    override fun currentStepIndex(domain: MulDomainState) =
        domain.currentStepIndex

    override fun editableCells(step: MulPhaseStep) =
        step.editableCells

    override fun evaluate(domain: MulDomainState, inputsForThisStep: List<String>) =
        evaluator.evaluate(domain, inputsForThisStep)

    override fun advance(domain: MulDomainState, addedInputs: List<String>, nextStepIndex: Int) =
        domain.copy(inputs = domain.inputs + addedInputs, currentStepIndex = nextStepIndex)

    override fun mapToUi(domain: MulDomainState, currentInput: String) =
        mapMultiplicationUiState(domain, currentInput)
}