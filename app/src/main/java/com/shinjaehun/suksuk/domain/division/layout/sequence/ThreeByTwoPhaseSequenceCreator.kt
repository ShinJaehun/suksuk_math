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

        val s1TOHundreds = info.subtract1TensOnly / 10
        val s1TOTens = info.subtract1TensOnly % 10
        val s1TOOnes =  info.dividendOnes

        val mQOTens = info.multiplyQuotientOnes / 10 % 10
        val mQOOnes = info.multiplyQuotientOnes % 10

        val tbs1 = info.dividendOnes < mQOOnes
        val hbs1 = (info.dividendTens - (if (tbs1) 1 else 0)) < mQOTens
        val skipHbs1 = hbs1 && (info.dividendHundreds == 1)
        val doubleBorrowS1 = tbs1 && ((info.dividendTens - 1) < mQOTens)

        val tbs2 = s1TOOnes < mQOOnes
        val hbs2 =(s1TOTens - (if (tbs2) 1 else 0)) < mQOTens
        val skipHbs2 = hbs2 && (s1TOHundreds == 1)
        val doubleBorrowS2 = tbs2 && ((s1TOTens - 1) < mQOTens)

        val steps = mutableListOf<PhaseStep>()

        if (info.hasTensQuotient) {
            // [1] 몫 십의 자리 입력
            steps += PhaseStep(
                phase = DivisionPhaseV2.InputQuotient,
                editableCells = listOf(CellName.QuotientTens),
                highlightCells = listOf(CellName.DividendHundreds, CellName.DividendTens, CellName.DivisorTens, CellName.DivisorOnes)
            )

            // [2] 1차 곱셈 (몫 십의 자리 × 제수) -- Carry 없음
            if(info.isCarryRequiredInMultiplyQuotientTens){
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
                    if(info.isCarryRequiredInMultiplyQuotientTens) {
                        add(CellName.CarryDivisorTensMul1)
                    }
                    add(CellName.QuotientTens)
                    add(CellName.DivisorTens)
                }
            )

            if(info.isBorrowFromDividendHundredsRequiredInS1) {
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
                    if(info.isBorrowFromDividendHundredsRequiredInS1){
                        add(CellName.Borrowed10DividendTens)
                    }
                    add(CellName.DividendTens)
                    add(CellName.Multiply1Tens)
                },
                presetValues = if (info.isBorrowFromDividendHundredsRequiredInS1)
                    mapOf(CellName.Borrowed10DividendTens to "10")
                else
                    emptyMap(),
                strikeThroughCells = if(info.isBorrowFromDividendHundredsRequiredInS1)
                    listOf(CellName.DividendHundreds)
                else
                    emptyList(),
                subtractLineTargets = setOf(CellName.Subtract1Tens)
            )

            if (info.is2DigitsInSubtract1) {
               steps += PhaseStep(
                   phase = DivisionPhaseV2.InputSubtract1,
                   editableCells = listOf(CellName.Subtract1Hundreds),
                   highlightCells = buildList {
                       if(info.isBorrowFromDividendHundredsRequiredInS1){
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
                presetValues = if(info.isEmptySubtract1Tens){
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
                    if(info.is2DigitsInSubtract1) add(CellName.Subtract1Hundreds)
                    add(CellName.Subtract1Tens)
                    add(CellName.Subtract1Ones)
                    add(CellName.DivisorTens)
                    add(CellName.DivisorOnes)
                },
                presetValues = if(info.isCarryRequiredInMultiplyQuotientTens)
                    mapOf(CellName.CarryDivisorTensMul1 to "")
                else
                    emptyMap(),
            )

            // [6] 2차 곱셈 (몫 일의 자리 × 제수)
            if(!info.shouldBypassM2AndS2) {
                when {
                    // 3자리 + 캐리 있음 (예: 13×8=104)
                    info.is3DigitsMultiplyQuotientOnes && info.isCarryRequiredInMultiplyQuotientOnes -> {
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
                    info.is3DigitsMultiplyQuotientOnes && !info.isCarryRequiredInMultiplyQuotientOnes -> {
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
                    !info.is3DigitsMultiplyQuotientOnes && info.isCarryRequiredInMultiplyQuotientOnes -> {
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

                if (doubleBorrowS2) {
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
                    if (info.shouldPerformSubtractTensStep) {
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputSubtract2,
                            editableCells = listOf(CellName.Subtract2Tens),
                            highlightCells = listOf(CellName.BorrowSubtract1Tens, CellName.Subtract1Tens, CellName.Multiply2Tens),
                            subtractLineTargets = setOf(CellName.Subtract2Tens)
                        )
                    }

                } else {
                    // ─ hbs만 ─
                    if (hbs2) {
                        // Ones 먼저 (예: 9−9=0)
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputSubtract2,
                            editableCells = listOf(CellName.Subtract2Ones),
                            highlightCells = listOf(CellName.Subtract1Ones, CellName.Multiply2Ones),
                            subtractLineTargets = setOf(CellName.Subtract2Ones)
                        )

                        if (!skipHbs2) {
                            steps += PhaseStep(
                                phase = DivisionPhaseV2.InputBorrow,
                                editableCells = listOf(CellName.BorrowSubtract1Hundreds),
                                highlightCells = listOf(CellName.Subtract1Hundreds),
                                needsBorrow = true,
                                strikeThroughCells = listOf(CellName.Subtract1Hundreds), // 이거 어떻게 할꺼야? 일의 자리로 계산이 끝나면 pending 상태로 종료
                            )
                        }

                        // Tens 단계 (게이팅은 tensResFinal)
                        if (info.shouldPerformSubtractTensStep) {
                            steps += PhaseStep(
                                phase = DivisionPhaseV2.InputSubtract2,
                                editableCells = listOf(CellName.Subtract2Tens),
                                highlightCells = buildList {
                                    if(skipHbs2) add(CellName.Subtract1Hundreds)
                                    add(CellName.Borrowed10Subtract1Tens)
                                    add(CellName.Subtract1Tens)
                                    add(CellName.Multiply2Tens)
                                },
                                presetValues = if(skipHbs2)
                                    emptyMap()
                                else
                                    mapOf(CellName.Borrowed10Subtract1Tens to "10"),
                                strikeThroughCells = if(skipHbs2)
                                    emptyList()
                                else
                                    listOf(CellName.Subtract1Hundreds)
                            )
                        }
                    }
                    // ─ tbs만 ─
                    else if (tbs2) {
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

                        if (info.shouldPerformSubtractTensStep) {
                            steps += PhaseStep(
                                phase = DivisionPhaseV2.InputSubtract2,
                                editableCells = listOf(CellName.Subtract2Tens),
                                highlightCells = listOf(CellName.BorrowSubtract1Tens, CellName.Multiply2Tens),
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
                        if (info.shouldPerformSubtractTensStep) {
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
        } else {

            steps += PhaseStep(
                phase = DivisionPhaseV2.InputQuotient,
                editableCells = listOf(CellName.QuotientOnes),
                highlightCells = listOf(
                    CellName.DividendHundreds, CellName.DividendTens, CellName.DividendOnes,
                    CellName.DivisorTens, CellName.DivisorOnes,
                ),
            )

            when {
                // 3자리 + 캐리
                info.is3DigitsMultiplyQuotientOnes && info.isCarryRequiredInMultiplyQuotientOnes -> {
                    // Step 2-1: carry + ones
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(
                            CellName.CarryDivisorTensMul1,
                            CellName.Multiply1Ones
                        ),
                        highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientOnes),
                        needsCarry = true
                    )
                    // Step 2-2: Hundreds → Tens 동시 입력
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(CellName.Multiply1Hundreds, CellName.Multiply1Tens),
                        highlightCells = listOf(
                            CellName.QuotientOnes,
                            CellName.DivisorTens,
                            CellName.CarryDivisorTensMul1
                        )
                    )
                }
                // 3자리 + 캐리 없음
                info.is3DigitsMultiplyQuotientOnes && !info.isCarryRequiredInMultiplyQuotientOnes -> {
                    // Step 2-1: ones만
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(CellName.Multiply1Ones),
                        highlightCells = listOf(CellName.QuotientOnes, CellName.DivisorOnes),
                    )
                    // Step 2-2: Hundreds → Tens
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(CellName.Multiply1Hundreds, CellName.Multiply1Tens),
                        highlightCells = listOf(CellName.QuotientOnes, CellName.DivisorTens)
                    )
                }
                //  2자리 + 캐리 있음
                !info.is3DigitsMultiplyQuotientOnes && info.isCarryRequiredInMultiplyQuotientOnes -> {
                    // carry + ones 먼저
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(CellName.CarryDivisorTensMul1, CellName.Multiply1Ones),
                        highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientOnes),
                        needsCarry = true
                    )
                    // 그 다음 tens (Hundreds는 없음)
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(CellName.Multiply1Tens),
                        highlightCells = listOf(
                            CellName.QuotientOnes, CellName.DivisorTens, CellName.CarryDivisorTensMul1
                        )
                    )
                }
                // 2자리 + 캐리 없음
                else -> {
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(CellName.Multiply1Ones),
                        highlightCells = listOf(CellName.QuotientOnes, CellName.DivisorOnes),
                    )
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(CellName.Multiply1Tens),
                        highlightCells = listOf(CellName.QuotientOnes, CellName.DivisorTens)
                    )
                }
            }

            // [3] Subtract1: (DOUBLE BORROW 포함)
            //  - subtract1에서의 borrow 패턴: (백→십) hbs1, (십→일) tbs1, 그리고 둘 다면 doubleBorrowInSubtract1
            when {
                doubleBorrowS1 -> {
                    // [DB1] 백의 자리에서 십의 자리로 차용
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputBorrow,
                        editableCells = listOf(CellName.BorrowDividendHundreds),
                        highlightCells = listOf(CellName.DividendHundreds),
                        needsBorrow = true,
                        strikeThroughCells = listOf(CellName.DividendHundreds),
                        subtractLineTargets = setOf(CellName.BorrowDividendHundreds)
                    )
                    // [DB2] 십의 자리에서 일의 자리로 차용 (Borrowed10Subtract1Tens=10 세팅)
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputBorrow,
                        editableCells = listOf(CellName.BorrowDividendTens),
                        highlightCells = listOf(CellName.Borrowed10DividendTens),
                        presetValues = mapOf(CellName.Borrowed10DividendTens to "10"),
                        needsBorrow = true,
                        strikeThroughCells = listOf(
                            CellName.DividendHundreds, CellName.Borrowed10DividendTens
                        )
                    )
                    // [DB3] 일의 자리 뺄셈 (Borrowed10Subtract1Ones=10 세팅)
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputSubtract1,
                        editableCells = listOf(CellName.Subtract1Ones),
                        highlightCells = listOf(
                            CellName.Borrowed10DividendOnes,
                            CellName.Multiply1Ones,
                            CellName.DividendOnes
                        ),
                        presetValues = mapOf(CellName.Borrowed10DividendOnes to "10"),
                        strikeThroughCells = listOf(CellName.Borrowed10DividendTens)
                    )
                    // [DB4] 십의 자리 뺄셈
                    if (info.shouldPerformSubtractTensStep) {
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputSubtract1,
                            editableCells = listOf(CellName.Subtract1Tens),
                            highlightCells = listOf(
                                CellName.BorrowDividendTens,
                                CellName.Multiply1Tens,
                                CellName.DividendTens
                            ),
                        )
                    }
                }

                // ─ hbs1만 (백→십) ─
                hbs1 -> {
                    // 일의 자리 먼저 처리 (차용 없이 가능한 경우가 많음)
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputSubtract1,
                        editableCells = listOf(CellName.Subtract1Ones),
                        highlightCells = listOf(CellName.DividendOnes, CellName.Multiply1Ones),
                        subtractLineTargets = setOf(CellName.Subtract1Ones)
                    )

                    if (!skipHbs1) {
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputBorrow,
                            editableCells = listOf(CellName.BorrowDividendHundreds),
                            highlightCells = listOf(CellName.DividendHundreds),
                            needsBorrow = true,
                            strikeThroughCells = listOf(CellName.DividendHundreds)
                        )
                    }

                    if(info.shouldPerformSubtractTensStep) {
                        // 십의 자리 (Borrowed10Subtract1Tens=10 preset)
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputSubtract1,
                            editableCells = listOf(CellName.Subtract1Tens),
                            highlightCells = buildList {
                                if(skipHbs1) add(CellName.DividendHundreds)
                                add(CellName.DividendTens)
                                add(CellName.Multiply1Tens)
                                add(CellName.Borrowed10DividendTens)
                            },
                            presetValues = if(skipHbs1)
                                emptyMap()
                            else
                                mapOf(CellName.Borrowed10DividendTens to "10"),
                            strikeThroughCells = if(skipHbs1)
                                emptyList()
                            else
                                listOf(CellName.DividendHundreds)
                        )
                    }
                }

                // ─ tbs1만 (십→일) ─
                tbs1 -> {
                    // 십→일 차용
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputBorrow,
                        editableCells = listOf(CellName.BorrowDividendTens),
                        highlightCells = listOf(CellName.DividendTens),
                        needsBorrow = true,
                        strikeThroughCells = listOf(CellName.DividendTens),
                        subtractLineTargets = setOf(CellName.BorrowDividendTens)
                    )
                    // 일의 자리
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputSubtract1,
                        editableCells = listOf(CellName.Subtract1Ones),
                        highlightCells = listOf(
                            CellName.Borrowed10DividendOnes,
                            CellName.Multiply1Ones,
                            CellName.DividendOnes
                        ),
                        presetValues = mapOf(CellName.Borrowed10DividendOnes to "10"),
                        strikeThroughCells = listOf(CellName.DividendTens)
                    )
                    // 십의 자리
                    if(info.shouldPerformSubtractTensStep) {
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputSubtract1,
                            editableCells = listOf(CellName.Subtract1Tens),
                            highlightCells = listOf(CellName.BorrowDividendTens, CellName.Multiply1Tens)
                        )
                    }
                }

                // ─ borrow 없음 ─
                else -> {
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputSubtract1,
                        editableCells = listOf(CellName.Subtract1Ones),
                        highlightCells = listOf(CellName.DividendOnes, CellName.Multiply1Ones),
                        subtractLineTargets = setOf(CellName.Subtract1Ones)
                    )
                    if(info.shouldPerformSubtractTensStep) {
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputSubtract1,
                            editableCells = listOf(CellName.Subtract1Tens),
                            highlightCells = listOf(CellName.DividendTens, CellName.Multiply1Tens),
                            subtractLineTargets = setOf(CellName.Subtract1Tens)
                        )
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
