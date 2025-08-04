package com.shinjaehun.suksuk.domain.division

//import com.shinjaehun.suksuk.domain.division.model.CellName
//
//val twoByTwoPhaseSequence = PhaseSequence(
//    pattern = DivisionPatternV2.TwoByTwo,
//    steps = listOf(
//        PhaseStep(
//            phase = DivisionPhaseV2.InputQuotient,
//            editableCells = listOf(CellName.QuotientOnes),
//            highlightCells = listOf(
//                CellName.DividendTens, CellName.DividendOnes,
//                CellName.DivisorTens, CellName.DivisorOnes
//            )
//        ),
//        PhaseStep(
//            phase = DivisionPhaseV2.InputMultiply,
//            editableCells = listOf(CellName.CarryDivisorTens, CellName.Multiply1Ones),
//            highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientOnes),
//            needsCarry = true      // Carry가 필요 없는 경우엔 false로
//        ),
//        PhaseStep(
//            phase = DivisionPhaseV2.InputMultiply,
//            editableCells = listOf(CellName.Multiply1Tens),
//            highlightCells = listOf(CellName.CarryDivisorTens, CellName.DivisorTens, CellName.QuotientOnes)
//        ),
//        PhaseStep(
//            phase = DivisionPhaseV2.InputBorrow,
//            editableCells = listOf(CellName.BorrowDividendTens),
//            highlightCells = listOf(CellName.DividendTens),
//            needsBorrow = true,    // Borrow가 필요 없는 경우엔 false로
//            crossOutCells = listOf(CellName.DividendTens)
//        ),
//        PhaseStep(
//            phase = DivisionPhaseV2.InputSubtract,
//            editableCells = listOf(CellName.Subtract1Ones),
//            highlightCells = listOf(
//                CellName.Borrowed10DividendOnes,
//                CellName.DividendOnes,
//                CellName.Multiply1Ones
//            ),
//            staticValues = mapOf(CellName.Borrowed10DividendOnes to "10"),
//            subtractLine = true
//        ),
//        PhaseStep(
//            phase = DivisionPhaseV2.Complete
//        )
//    )
//)
