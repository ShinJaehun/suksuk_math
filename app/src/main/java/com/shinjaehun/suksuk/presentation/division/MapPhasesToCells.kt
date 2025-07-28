package com.shinjaehun.suksuk.presentation.division

fun mapPhasesToCells(state: DivisionPhasesState, currentInput: String): DivisionUiState {
    val inputs = state.inputs
    val phase = state.phases.getOrNull(state.currentPhaseIndex)
//    val isComplete = phase == null || phase is DivisionPhase.Complete
    val isComplete = state.currentPhaseIndex >= state.phases.size

    val relatedCells: Set<CellName> = when (phase) {
        DivisionPhase.InputQuotientTens ->
            setOf(CellName.Divisor, CellName.DividendTens)

        DivisionPhase.InputMultiply1 ->
            setOf(CellName.Divisor, CellName.QuotientTens)

        DivisionPhase.InputMultiply1Tens,
        DivisionPhase.InputMultiply1Ones ->
            setOf(CellName.Divisor, CellName.QuotientOnes)

        DivisionPhase.InputSubtract1Result ->
            setOf(CellName.DividendOnes, CellName.Multiply1Ones, CellName.Borrowed10DividendOnes)

        DivisionPhase.InputSubtract1Tens ->
            setOf(CellName.DividendTens, CellName.Multiply1Tens)

        DivisionPhase.InputSubtract1Ones ->
            setOf(CellName.DividendOnes, CellName.Multiply1Ones)

        DivisionPhase.InputBringDownFromDividendOnes ->
            setOf(CellName.DividendOnes)

        DivisionPhase.InputQuotientOnes ->
            setOf(CellName.Divisor, CellName.Subtract1Tens, CellName.Subtract1Ones)

        DivisionPhase.InputQuotient ->
            setOf(CellName.Divisor, CellName.DividendTens, CellName.DividendOnes)

        DivisionPhase.InputMultiply2Tens,
        DivisionPhase.InputMultiply2Ones ->
            setOf(CellName.Divisor, CellName.QuotientOnes)

        DivisionPhase.InputSubtract2Result -> {
            if(state.pattern == DivisionPattern.TensQuotient_SkipBorrow_1DigitMul) {
                setOf(CellName.Subtract1Tens, CellName.Subtract1Ones, CellName.Multiply2Tens, CellName.Multiply2Ones, CellName.Borrowed10Subtract1Ones)
            } else {
                setOf(CellName.Subtract1Ones, CellName.Multiply2Ones, CellName.Borrowed10Subtract1Ones)
            }
        }

        DivisionPhase.InputBorrowFromSubtract1Tens ->
            setOf(CellName.Subtract1Tens)

        DivisionPhase.InputBorrowFromDividendTens ->
            setOf(CellName.DividendTens)

        else -> emptySet()
    }

//    fun cellValue(idx: Int, editing: Boolean): String =
//        if (editing)
//            if (currentInput.isEmpty()) "?" else currentInput
//        else
//            inputs.getOrNull(idx) ?: ""

    fun cellValue(idx: Int, editable: Boolean): String {
        val input = state.inputs.getOrNull(idx)
        val phaseForIdx = state.phases.getOrNull(idx)

        return when {
            input != null -> {
                // 입력된 값이 있는 경우
                if (phaseForIdx == DivisionPhase.InputSubtract1Tens && input == "0") "" else input
            }
            editable -> {
                // 입력 중이지만 아직 입력이 없으면 "?" 또는 currentInput
                if (currentInput.isEmpty()) "?" else currentInput
            }
            else -> ""
        }
    }

    // phase별로 몇 번째 입력값인지 미리 정해둠(패턴마다 phase, 입력 순서가 1:1)
    // 편의상 phase index를 각 칸에 할당!
    val quotientTensIdx      = state.phases.indexOfFirst { it == DivisionPhase.InputQuotientTens }
    val quotientOnesIdx      = state.phases.indexOfFirst { it == DivisionPhase.InputQuotientOnes || it == DivisionPhase.InputQuotient }
    val multiply1TensIdx         = state.phases.indexOfFirst { it == DivisionPhase.InputMultiply1Tens || it == DivisionPhase.InputMultiply1 }
    val multiply1OnesIdx         = state.phases.indexOfFirst { it == DivisionPhase.InputMultiply1Ones }
    val subtract1TensIdx         = state.phases.indexOfFirst { it == DivisionPhase.InputSubtract1Tens }
    val subtract1OnesIdx         = state.phases.indexOfFirst { it == DivisionPhase.InputBringDownFromDividendOnes || it == DivisionPhase.InputSubtract1Result }
    val multiply2TensIdx     = state.phases.indexOfFirst { it == DivisionPhase.InputMultiply2Tens }
    val multiply2OnesIdx     = state.phases.indexOfFirst { it == DivisionPhase.InputMultiply2Ones }
    val borrowSubtract1TensIdx = state.phases.indexOfFirst { it == DivisionPhase.InputBorrowFromSubtract1Tens }
    val borrowDividendTensIdx = state.phases.indexOfFirst { it == DivisionPhase.InputBorrowFromDividendTens }
    val subtract2OnesIdx         = state.phases.indexOfFirst { it == DivisionPhase.InputSubtract2Result }

    // makeCell: 공통 InputCell 생성
    fun makeCell(
        cellName: CellName,
        idx: Int,
        editing: Boolean,
        editable: Boolean = editing,
        crossOutColor: CrossOutColor = CrossOutColor.None
    ) = InputCell(
        value = cellValue(idx, editing && !isComplete),
        editable = editable,
        highlight = when {
            editing && !isComplete -> Highlight.Editing
            cellName in relatedCells -> Highlight.Related
            else -> Highlight.None
        },
        crossOutColor = crossOutColor
    )

    fun makeFixedCell(
        cellName: CellName,
        show: Boolean,
        value: String
    ): InputCell {
        return if (show)
            InputCell(
                value = value,
                highlight = if (cellName in relatedCells) Highlight.Related else Highlight.None
            )
        else
            InputCell()
    }

    // dividend, divisor는 입력 가능성이 없으므로 value만 별도 처리
    val dividendStr = state.dividend.toString().padStart(2, '0')

//    val crossOutDividendTens = state.inputs.getOrNull(borrowDividendTensIdx) != null ||
//                phase == DivisionPhase.InputBorrowFromDividendTens

    val crossOutColorDividendTens = when {
        state.inputs.getOrNull(borrowDividendTensIdx) != null -> CrossOutColor.Confirmed
        phase == DivisionPhase.InputBorrowFromDividendTens -> CrossOutColor.Pending
        else -> CrossOutColor.None
    }

    val showBorrowed10DividendOnes = state.inputs.getOrNull(borrowDividendTensIdx) != null

//    val crossOutSub1Tens = state.inputs.getOrNull(borrowSubtract1TensIdx) != null ||
//                phase == DivisionPhase.InputBorrowFromSubtract1Tens

    val crossOutColorSub1Tens = when {
        state.inputs.getOrNull(borrowSubtract1TensIdx) != null -> CrossOutColor.Confirmed
        phase == DivisionPhase.InputBorrowFromSubtract1Tens -> CrossOutColor.Pending
        else -> CrossOutColor.None
    }

    val showBorrowed10Sub1Ones = state.inputs.getOrNull(borrowSubtract1TensIdx) != null

    return DivisionUiState(
        divisor = InputCell(
            value = state.divisor.toString(),
            highlight = if (CellName.Divisor in relatedCells) Highlight.Related else Highlight.None
        ),
        dividendTens = InputCell(
            value = dividendStr[0].toString(),
            highlight = if (CellName.DividendTens in relatedCells) Highlight.Related else Highlight.None,
            crossOutColor = crossOutColorDividendTens
        ),
        dividendOnes = InputCell(
            value = dividendStr[1].toString(),
            highlight = if (CellName.DividendOnes in relatedCells) Highlight.Related else Highlight.None
        ),
        borrowed10DividendOnes = makeFixedCell(
            cellName = CellName.Borrowed10DividendOnes,
            show = showBorrowed10DividendOnes,
            value = "10"
        ),
        quotientTens = makeCell(
            cellName = CellName.QuotientTens,
            idx = quotientTensIdx,
            editing = phase == DivisionPhase.InputQuotientTens
        ),
        multiply1Tens = makeCell(
            cellName = CellName.Multiply1Tens,
            idx = multiply1TensIdx,
            editing = phase == DivisionPhase.InputMultiply1 || phase == DivisionPhase.InputMultiply1Tens
        ),
        multiply1Ones = makeCell(
            cellName = CellName.Multiply1Ones,
            idx = multiply1OnesIdx,
            editing = phase == DivisionPhase.InputMultiply1Ones
        ),
        subtract1Tens = makeCell(
            cellName = CellName.Subtract1Tens,
            idx = subtract1TensIdx,
            editing = phase == DivisionPhase.InputSubtract1Tens,
            crossOutColor = crossOutColorSub1Tens
        ),
        subtract1Ones = makeCell(
            cellName = CellName.Subtract1Ones,
            idx = subtract1OnesIdx,
            editing = phase == DivisionPhase.InputBringDownFromDividendOnes || phase == DivisionPhase.InputSubtract1Result
        ),
        borrowed10Subtract1Ones = makeFixedCell(
            cellName = CellName.Borrowed10Subtract1Ones,
            show = showBorrowed10Sub1Ones,
            value = "10"
        ),
        quotientOnes = makeCell(
            cellName = CellName.QuotientOnes,
            idx = quotientOnesIdx,
            editing = phase == DivisionPhase.InputQuotientOnes || phase == DivisionPhase.InputQuotient
        ),
        multiply2Tens = makeCell(
            cellName = CellName.Multiply2Tens,
            idx = multiply2TensIdx,
            editing = phase == DivisionPhase.InputMultiply2Tens
        ),
        multiply2Ones = makeCell(
            cellName = CellName.Multiply2Ones,
            idx = multiply2OnesIdx,
            editing = phase == DivisionPhase.InputMultiply2Ones
        ),
        subtract2Ones = makeCell(
            cellName = CellName.Subtract2Ones,
            idx = subtract2OnesIdx,
            editing = phase == DivisionPhase.InputSubtract2Result
        ),
        borrowDividendTens = makeCell(
            cellName = CellName.BorrowDividendTens,
            idx = borrowDividendTensIdx,
            editing = phase == DivisionPhase.InputBorrowFromDividendTens
        ),
        borrowSubtract1Tens = makeCell(
            cellName = CellName.BorrowSubtract1Tens,
            idx = borrowSubtract1TensIdx,
            editing = phase == DivisionPhase.InputBorrowFromSubtract1Tens
        ),
        stage = state.currentPhaseIndex,
        feedback = when {
            phase == DivisionPhase.Complete -> "정답입니다!"
            else -> state.feedback
        }
    )
}