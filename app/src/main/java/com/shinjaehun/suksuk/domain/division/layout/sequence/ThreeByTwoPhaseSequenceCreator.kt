package com.shinjaehun.suksuk.domain.division.layout.sequence

import com.shinjaehun.suksuk.domain.division.DivisionStateInfo
import com.shinjaehun.suksuk.domain.division.layout.PhaseSequence
import com.shinjaehun.suksuk.domain.division.layout.PhaseStep
import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import javax.inject.Inject

class ThreeByTwoPhaseSequenceCreator @Inject constructor() : PhaseSequenceCreator {
    override fun create(info: DivisionStateInfo): PhaseSequence {

        val steps = mutableListOf<PhaseStep>()

        if (info.needsTensQuotient) {
            // [1] 몫 십의 자리 입력
            steps += PhaseStep(
                phase = DivisionPhaseV2.InputQuotient,
                editableCells = listOf(CellName.QuotientTens),
                highlightCells = listOf(CellName.DividendHundreds, CellName.DividendTens, CellName.DivisorTens, CellName.DivisorOnes)
            )

            // [2] 1차 곱셈 (몫 십의 자리 × 제수) -- Carry 없음
            if(info.needsCarryInMultiply1){
                steps += PhaseStep(
                    phase = DivisionPhaseV2.InputMultiply1,
                    editableCells = listOf(CellName.CarryDivisorTensMul1, CellName.Multiply1Tens),
                    highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientTens),
                    needsCarry = true
                )
            } else {
                steps += PhaseStep(
                    phase = DivisionPhaseV2.InputMultiply1,
                    editableCells = listOf(CellName.Multiply1Tens),
                    highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientTens),
                )
            }

            steps += PhaseStep(
                phase = DivisionPhaseV2.InputMultiply1,
                editableCells = listOf(CellName.Multiply1Hundreds),
                highlightCells = buildList {
                    if(info.needsCarryInMultiply1) {
                        add(CellName.CarryDivisorTensMul1)
                    }
                    add(CellName.QuotientTens)
                    add(CellName.DivisorTens)
                }
            )

            if(info.needsBorrowFromDividendHundredsInSubtract1) {
                steps += PhaseStep(
                    phase = DivisionPhaseV2.InputBorrow,
                    editableCells = listOf(CellName.BorrowDividendHundreds),
                    highlightCells = listOf(CellName.DividendHundreds),
                    needsBorrow = true,
                    strikeThroughCells = listOf(CellName.DividendHundreds),
                    subtractLineTargets = setOf(CellName.BorrowDividendHundreds)
                )
            }

            // [3] 1차 뺄셈 (Borrow 없음)
            steps += PhaseStep(
                phase = DivisionPhaseV2.InputSubtract1,
                editableCells = listOf(CellName.Subtract1Tens),
                highlightCells = buildList {
                    if(info.needsBorrowFromDividendHundredsInSubtract1){
                        add(CellName.Borrowed10DividendTens)
                    }
                    add(CellName.DividendTens)
                    add(CellName.Multiply1Tens)
                },
                presetValues = if (info.needsBorrowFromDividendHundredsInSubtract1)
                    mapOf(CellName.Borrowed10DividendTens to "10")
                else
                    emptyMap(),
                strikeThroughCells = if(info.needsBorrowFromDividendHundredsInSubtract1)
                    listOf(CellName.DividendHundreds)
                else
                    emptyList(),
                subtractLineTargets = setOf(CellName.Subtract1Tens)
            )

            if (info.needs2DigitsInSubtract1) {
               steps += PhaseStep(
                   phase = DivisionPhaseV2.InputSubtract1,
                   editableCells = listOf(CellName.Subtract1Hundreds),
                   highlightCells = buildList {
                       if(info.needsBorrowFromDividendHundredsInSubtract1){
                           add(CellName.BorrowDividendHundreds)
                       } else {
                           add(CellName.DividendHundreds)
                       }
                       add(CellName.Multiply1Hundreds)
                   }

               )
            }

            // [4] Bring down 일의 자리
            steps += PhaseStep(
                phase = DivisionPhaseV2.InputBringDown,
                editableCells = listOf(CellName.Subtract1Ones),
                highlightCells = listOf(CellName.DividendOnes),
                presetValues = if(info.needsEmptySubtract1Tens){
                    mapOf(CellName.Subtract1Tens to "")
                } else {
                    emptyMap()
                }
            )

            // [5] 몫 일의 자리 입력
            steps += PhaseStep(
                phase = DivisionPhaseV2.InputQuotient,
                editableCells = listOf(CellName.QuotientOnes),
                highlightCells = buildList {
                    if(info.needs2DigitsInSubtract1) add(CellName.Subtract1Hundreds)
                    add(CellName.Subtract1Tens)
                    add(CellName.Subtract1Ones)
                    add(CellName.DivisorTens)
                    add(CellName.DivisorOnes)
                },
                presetValues = if(info.needsCarryInMultiply1)
                    mapOf(CellName.CarryDivisorTensMul1 to "")
                else
                    emptyMap(),
            )

            // [6] 2차 곱셈 (몫 일의 자리 × 제수)
            if(!info.needsSkipMultiply2AndSubtract2) {
                when {
                    // 3자리 + 캐리 있음 (예: 13×8=104)
                    info.needs3DigitsInMultiply2 && info.needsCarryInMultiply2 -> {
                        // Step 6-1: carry + ones
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(CellName.CarryDivisorTensMul2, CellName.Multiply2Ones),
                            highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientOnes),
                            needsCarry = true
                        )
                        // Step 6-2: Hundreds → Tens 동시 입력
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(CellName.Multiply2Hundreds, CellName.Multiply2Tens),
                            highlightCells = listOf(CellName.QuotientOnes, CellName.DivisorTens, CellName.CarryDivisorTensMul2)
                        )
                    }

                    // 3자리 + 캐리 없음 (예: 21×9=189)  << 419÷21 케이스
                    info.needs3DigitsInMultiply2 && !info.needsCarryInMultiply2 -> {
                        // Step 6-1: ones만
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(CellName.Multiply2Ones),
                            highlightCells = listOf(CellName.QuotientOnes, CellName.DivisorOnes),
                            // 혹시 이전 케이스 잔여값 방지
                            presetValues = mapOf(CellName.CarryDivisorTensMul2 to "")
                        )
                        // Step 6-2: Hundreds → Tens 동시 입력 (carry 입력 없이)
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(CellName.Multiply2Hundreds, CellName.Multiply2Tens),
                            highlightCells = listOf(CellName.QuotientOnes, CellName.DivisorTens)
                        )
                    }

                    // 2자리 + 캐리 있음 (예: 27×4=108은 3자리라 위로 감, 이 케이스는 예: 19×6=114? → 3자리라 위로. 실제 2자리+캐리는 드뭄)
                    !info.needs3DigitsInMultiply2 && info.needsCarryInMultiply2 -> {
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(CellName.CarryDivisorTensMul2, CellName.Multiply2Ones),
                            highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientOnes),
                            needsCarry = true
                        )
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(CellName.Multiply2Tens),
                            highlightCells = listOf(CellName.QuotientOnes, CellName.DivisorTens, CellName.CarryDivisorTensMul2)
                        )
                    }

                    // 2자리 + 캐리 없음 (예: 60×1=60)
                    else -> {
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(CellName.Multiply2Ones),
                            highlightCells = listOf(CellName.QuotientOnes, CellName.DivisorOnes),
                            presetValues = mapOf(CellName.CarryDivisorTensMul2 to "")
                        )
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(CellName.Multiply2Tens),
                            highlightCells = listOf(CellName.QuotientOnes, CellName.DivisorTens)
                        )
                    }
                }
//                when{
//                    info.needs3DigitsInMultiply2 -> {
//                        steps += PhaseStep(
//                            phase = DivisionPhaseV2.InputMultiply2,
//                            editableCells = listOf(CellName.CarryDivisorTensMul2, CellName.Multiply2Ones),
//                            highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientOnes),
//                            needsCarry = true
//                        )
//
//                        steps += PhaseStep(
//                            phase = DivisionPhaseV2.InputMultiply2,
//                            editableCells = listOf(CellName.Multiply2Hundreds, CellName.Multiply2Tens),
//                            highlightCells = listOf(
//                                CellName.CarryDivisorTensMul2,
//                                CellName.QuotientOnes,
//                                CellName.DivisorTens
//                            )
//                        )
//                    }
//                    info.needsCarryInMultiply2 -> {
//                        steps += PhaseStep(
//                            phase = DivisionPhaseV2.InputMultiply2,
//                            editableCells = listOf(CellName.CarryDivisorTensMul2, CellName.Multiply2Ones),
//                            highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientOnes),
//                            needsCarry = true
//                        )
//
//                        steps += PhaseStep(
//                            phase = DivisionPhaseV2.InputMultiply2,
//                            editableCells = listOf(CellName.Multiply2Tens),
//                            highlightCells = listOf(
//                                CellName.CarryDivisorTensMul2,
//                                CellName.QuotientOnes,
//                                CellName.DivisorTens
//                            )
//                        )
//                    }
//                    else -> {
//                        steps += PhaseStep(
//                            phase = DivisionPhaseV2.InputMultiply2,
//                            editableCells = listOf(CellName.Multiply2Ones),
//                            highlightCells = listOf(CellName.QuotientOnes, CellName.DivisorOnes)
//                        )
//
//                        steps += PhaseStep(
//                            phase = DivisionPhaseV2.InputMultiply2,
//                            editableCells = listOf(CellName.Multiply2Tens),
//                            highlightCells = listOf(CellName.QuotientOnes, CellName.DivisorTens)
//                        )
//                    }
//                }

//                if(info.needsCarryInMultiply2){
//                    steps += PhaseStep(
//                        phase = DivisionPhaseV2.InputMultiply2,
//                        editableCells = listOf(CellName.CarryDivisorTensMul2, CellName.Multiply2Ones),
//                        highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientOnes),
//                        needsCarry = true
//                    )
//                } else {
//                    steps += PhaseStep(
//                        phase = DivisionPhaseV2.InputMultiply2,
//                        editableCells = listOf(CellName.Multiply2Ones),
//                        highlightCells = listOf(CellName.QuotientOnes, CellName.DivisorOnes)
//                    )
//                }
//
//                steps += PhaseStep(
//                    phase = DivisionPhaseV2.InputMultiply2,
//                    editableCells = listOf(CellName.Multiply2Tens),
//                    highlightCells = buildList {
//                        if(info.needsCarryInMultiply2) {
//                            add(CellName.CarryDivisorTensMul2)
//                        }
//                        add(CellName.QuotientOnes)
//                        add(CellName.DivisorTens)
//                    }
//                )
//
//                if(info.needsBorrowFromSubtract1TensInSubtract2){
//                    steps += PhaseStep(
//                        phase = DivisionPhaseV2.InputBorrow,
//                        editableCells = listOf(CellName.BorrowSubtract1Tens),
//                        highlightCells = listOf(CellName.Subtract1Tens),
//                        needsBorrow = true,
//                        strikeThroughCells = listOf(CellName.Subtract1Tens),
//                        subtractLineTargets = setOf(CellName.BorrowSubtract1Tens)
//                    )
//                }
//
//                // [7] 2차 뺄셈 (Borrow 없음)
//                steps += PhaseStep(
//                    phase = DivisionPhaseV2.InputSubtract2,
//                    editableCells = listOf(CellName.Subtract2Ones),
//                    highlightCells = buildList {
//                        if(info.needsBorrowFromSubtract1TensInSubtract2) {
//                            add(CellName.Borrowed10Subtract1Ones)
//                        }
//                        add(CellName.Subtract1Ones)
//                        add(CellName.Multiply2Ones)
//                    },
//                    presetValues = if(info.needsBorrowFromSubtract1TensInSubtract2)
//                        mapOf(CellName.Borrowed10Subtract1Ones to "10")
//                    else
//                        emptyMap(),
//                    strikeThroughCells = if(info.needsBorrowFromSubtract1TensInSubtract2)
//                        listOf(CellName.Subtract1Tens)
//                    else
//                        emptyList(),
//                    subtractLineTargets = setOf(CellName.Subtract2Ones)
//                )
//
//                if(info.needs2DigitRem) {
//                    steps += PhaseStep(
//                        phase = DivisionPhaseV2.InputSubtract2,
//                        editableCells = listOf(CellName.Subtract2Tens),
//                        highlightCells = buildList {
//                            if (info.needsBorrowFromSubtract1TensInSubtract2) {
//                                add(CellName.BorrowSubtract1Tens)
//                            } else {
//                                add(CellName.Subtract1Tens)
//                            }
//                            add(CellName.Multiply2Tens)
//                        }
//                    )
//                }

//                val N  = info.subtract1Result
//                val No = info.dividend % 10
//                val remainder1 = (N - No) / 10
//                val Nh = remainder1 / 10
//                val Nt = remainder1 % 10
//
//                val M  = info.multiplyQuotientOnes
//                val Mo = M % 10
//                val Mt = (M / 10) % 10
//
//                val hasHundreds = remainder1 >= 10
//                val tbs2 = No < Mo
//                val hbs2 = hasHundreds && ((Nt - (if (tbs2) 1 else 0)) < Mt)
//                val skipHbs2 = hbs2 && (Nh == 1)
//
//// ★ 더블 빌림은 "tbs2도 필요"할 때만
//                val doubleBorrow = hasHundreds && tbs2 && ((Nt - 1) < Mt)
//
//                val tensResFinal = when {
//                    doubleBorrow -> (Nt + 10 - 1) - Mt   // hbs → tbs 순서
//                    hbs2         -> (Nt + 10)     - Mt   // hbs만
//                    tbs2         -> (Nt - 1)      - Mt   // tbs만
//                    else         -> Nt            - Mt   // 빌림 없음
//                }
//                val needsSubtract2TensStep = info.needs2DigitRem || (tensResFinal != 0)
// ================================
// ★ 더블 빌림 우선 분기 (hbs → tbs → Ones → Tens)
// ================================

                if (info.doubleBorrow) {
                    // [DB1] hbs 입력
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputBorrow,
                        editableCells = listOf(CellName.BorrowSubtract1Hundreds),
                        highlightCells = listOf(CellName.Subtract1Hundreds),
                        needsBorrow = true,
                        strikeThroughCells = listOf(CellName.Subtract1Hundreds),
                        subtractLineTargets = setOf(CellName.BorrowSubtract1Hundreds)
                    )
                    // [DB2] tbs 입력
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputBorrow,
                        editableCells = listOf(CellName.BorrowSubtract1Tens),
                        highlightCells = listOf(CellName.Borrowed10Subtract1Tens),
                        presetValues = mapOf(CellName.Borrowed10Subtract1Tens to "10"),
                        needsBorrow = true,
                        strikeThroughCells = listOf(CellName.Subtract1Hundreds, CellName.Borrowed10Subtract1Tens)
                    )
                    // [DB3] Ones
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputSubtract2,
                        editableCells = listOf(CellName.Subtract2Ones),
                        highlightCells = listOf(CellName.Borrowed10Subtract1Ones, CellName.Multiply2Ones, CellName.Subtract1Ones),
                        presetValues = mapOf(CellName.Borrowed10Subtract1Ones to "10"),
                        strikeThroughCells = listOf(CellName.Borrowed10Subtract1Tens)
                    )
                    // [DB4] Tens (게이팅은 tensResFinal)
                    if (info.needsSubtract2TensStep) {
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputSubtract2,
                            editableCells = listOf(CellName.Subtract2Tens),
                            highlightCells = listOf(CellName.BorrowSubtract1Tens, CellName.Subtract1Tens, CellName.Multiply2Tens),
                            subtractLineTargets = setOf(CellName.Subtract2Tens)
                        )
                    }

                } else {
                    // ─ hbs만 ─
                    if (info.hbs2) {
                        // Ones 먼저 (예: 9−9=0)
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputSubtract2,
                            editableCells = listOf(CellName.Subtract2Ones),
                            highlightCells = listOf(CellName.Subtract1Ones, CellName.Multiply2Ones),
                            subtractLineTargets = setOf(CellName.Subtract2Ones)
                        )

                        if (info.skipHbs2) {
                            // ★ skip: BorrowHundreds 입력 단계 만들지 않음 (또는 editableCells=empty, needsBorrow=false)

                        } else {
                            // ★ Nh>1: 실제로 입력 받는 BorrowHundreds 단계
                            steps += PhaseStep(
                                phase = DivisionPhaseV2.InputBorrow,
                                editableCells = listOf(CellName.BorrowSubtract1Hundreds),
                                highlightCells = listOf(CellName.Subtract1Hundreds),
                                needsBorrow = true,
                                strikeThroughCells = listOf(CellName.Subtract1Hundreds), // 이거 어떻게 할꺼야? 일의 자리로 계산이 끝나면 pending 상태로 종료
                            )
                        }

                        // Tens 단계 (게이팅은 tensResFinal)
                        if (info.needsSubtract2TensStep) {
                            steps += PhaseStep(
                                phase = DivisionPhaseV2.InputSubtract2,
                                editableCells = listOf(CellName.Subtract2Tens),
                                highlightCells = buildList {
                                    add(CellName.Borrowed10Subtract1Tens)
                                    add(CellName.Subtract1Tens)
                                    add(CellName.Multiply2Tens)
                                },
                                presetValues = mapOf(CellName.Borrowed10Subtract1Tens to "10"),
                                strikeThroughCells = listOf(CellName.Subtract1Hundreds)
                            )
                        }
                    }
                    // ─ tbs만 ─
                    else if (info.tbs2) {
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputBorrow,
                            editableCells = listOf(CellName.BorrowSubtract1Tens),
                            highlightCells = listOf(CellName.Subtract1Tens),
                            needsBorrow = true,
                            strikeThroughCells = listOf(CellName.Subtract1Tens),
                            subtractLineTargets = setOf(CellName.BorrowSubtract1Tens)
                        )
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputSubtract2,
                            editableCells = listOf(CellName.Subtract2Ones),
                            highlightCells = listOf(CellName.Borrowed10Subtract1Ones, CellName.Multiply2Ones, CellName.Subtract1Ones),
                            presetValues = mapOf(CellName.Borrowed10Subtract1Ones to "10"),
                            strikeThroughCells = listOf(CellName.Subtract1Tens)
                        )

                        if (info.needsSubtract2TensStep) {
                            steps += PhaseStep(
                                phase = DivisionPhaseV2.InputSubtract2,
                                editableCells = listOf(CellName.Subtract2Tens),
                                highlightCells = listOf(CellName.Subtract1Tens, CellName.Multiply2Tens),
                            )
                        }
                    }
                    // ─ borrow 없음 ─
                    else {
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputSubtract2,
                            editableCells = listOf(CellName.Subtract2Ones),
                            highlightCells = listOf(CellName.Subtract1Ones, CellName.Multiply2Ones),
                            subtractLineTargets = setOf(CellName.Subtract2Ones)
                        )
                        if (info.needsSubtract2TensStep) {
                            steps += PhaseStep(
                                phase = DivisionPhaseV2.InputSubtract2,
                                editableCells = listOf(CellName.Subtract2Tens),
                                highlightCells = listOf(CellName.Subtract1Tens, CellName.Multiply2Tens),
                                subtractLineTargets = setOf(CellName.Subtract2Tens)
                            )
                        }
                    }
                }
            }
        }

        // [8] 완료 단계
        steps += PhaseStep(
            phase = DivisionPhaseV2.Complete,
        )

        steps.forEachIndexed { idx, step ->
            println("[$idx] phase=${step.phase}, editableCells=${step.editableCells}")
        }

        return PhaseSequence(
            pattern = DivisionPatternV2.ThreeByTwo,
            steps = steps
        )
    }
}
