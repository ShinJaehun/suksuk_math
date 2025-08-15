package com.shinjaehun.suksuk.presentation.division

import com.shinjaehun.suksuk.common.viewmodel.BaseViewModel
import com.shinjaehun.suksuk.domain.division.model.DivisionDomainStateV2
import com.shinjaehun.suksuk.domain.division.factory.DivisionDomainStateV2Factory
import com.shinjaehun.suksuk.domain.division.evaluator.DivisionPhaseEvaluatorV2
import com.shinjaehun.suksuk.domain.division.model.DivisionCellName
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseStep
import com.shinjaehun.suksuk.presentation.division.model.DivisionUiStateV2
import com.shinjaehun.suksuk.presentation.division.model.mapDivisionUiStateV2
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DivisionViewModelV2 @Inject constructor(
    private val evaluator: DivisionPhaseEvaluatorV2,
    private val factory: DivisionDomainStateV2Factory,
//    private val generateProblem: GenerateDivisionProblem
) : BaseViewModel<DivisionUiStateV2 ,DivisionDomainStateV2 ,DivisionPhaseStep ,DivisionCellName>() {

    fun start(dividend: Int, divisor: Int) = startNewProblem(dividend, divisor)

//    fun startRandom() {
//        val (dividend, divisor) = generateProblem()
//        startNewProblem(dividend, divisor)
//    }

    override fun createDomain(vararg args: Int) =
        factory.create(args[0], args[1])

    override fun steps(domain: DivisionDomainStateV2) =
        domain.phaseSequence.steps

    override fun currentStepIndex(domain: DivisionDomainStateV2) =
        domain.currentStepIndex

    override fun editableCells(step: DivisionPhaseStep) =
        step.editableCells

    override fun evaluate(domain: DivisionDomainStateV2, inputsForThisStep: List<String>) =
        evaluator.evaluate(domain, inputsForThisStep)

    override fun advance(domain: DivisionDomainStateV2, addedInputs: List<String>, nextStepIndex: Int) =
        domain.copy(inputs = domain.inputs + addedInputs, currentStepIndex = nextStepIndex)

    override fun mapToUi(domain: DivisionDomainStateV2, currentInput: String) =
        mapDivisionUiStateV2(domain, currentInput)
}