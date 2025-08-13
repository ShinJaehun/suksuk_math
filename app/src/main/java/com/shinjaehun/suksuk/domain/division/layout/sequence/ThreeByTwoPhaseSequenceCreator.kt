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

        val is2DigitsInSubtract1 = info.subtract1TensOnly >= 10
        val is3DigitsMultiplyQuotientOnes = info.multiplyQuotientOnes >= 100


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
                    editableCells = listOf(CellName.CarryDivisorTensM1, CellName.Multiply1Tens),
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
                        add(CellName.CarryDivisorTensM1)
                    }
                    add(CellName.QuotientTens)
                    add(CellName.DivisorTens)
                }
            )

            if(info.needsHundredsBorrowInS1) {
                steps += PhaseStep(
                    phase = DivisionPhaseV2.InputBorrow,
                    editableCells = listOf(CellName.BorrowDividendHundreds),
                    highlightCells = listOf(
                        CellName.DividendHundreds,
                        CellName.DividendTens,
                        CellName.Multiply1Tens,
                    ),
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
                    if(info.needsHundredsBorrowInS1){
                        add(CellName.Borrowed10DividendTens)
                    }
                    add(CellName.DividendTens)
                    add(CellName.Multiply1Tens)
                },
                presetValues = if (info.needsHundredsBorrowInS1)
                    mapOf(CellName.Borrowed10DividendTens to "10")
                else
                    emptyMap(),
                strikeThroughCells = if(info.needsHundredsBorrowInS1)
                    listOf(CellName.DividendHundreds)
                else
                    emptyList(),
                subtractLineTargets = setOf(CellName.Subtract1Tens)
            )

            if (is2DigitsInSubtract1) {
               steps += PhaseStep(
                   phase = DivisionPhaseV2.InputSubtract1,
                   editableCells = listOf(CellName.Subtract1Hundreds),
                   highlightCells = buildList {
                       if(info.needsHundredsBorrowInS1){
                           add(CellName.BorrowDividendHundreds)
                       } else {
                           add(CellName.DividendHundreds)
                       }
                       add(CellName.Multiply1Hundreds)
                   },
                   subtractLineTargets = setOf(CellName.Subtract1Hundreds)
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
                },
                subtractLineTargets = setOf(CellName.Subtract1Ones)
            )

            // [5] 몫 일의 자리 입력
            steps += PhaseStep(
                phase = DivisionPhaseV2.InputQuotient,
                editableCells = listOf(CellName.QuotientOnes),
                highlightCells = buildList {
                    if(is2DigitsInSubtract1) add(CellName.Subtract1Hundreds)
                    add(CellName.Subtract1Tens)
                    add(CellName.Subtract1Ones)
                    add(CellName.DivisorTens)
                    add(CellName.DivisorOnes)
                },
                presetValues = if(info.isCarryRequiredInMultiplyQuotientTens)
                    mapOf(CellName.CarryDivisorTensM1 to "")
                else
                    emptyMap(),
            )

            // [6] 2차 곱셈 (몫 일의 자리 × 제수)
            if(!info.shouldBypassM2AndS2) {
                when {
                    // 3자리 + 캐리 있음 (예: 13×8=104)
                    is3DigitsMultiplyQuotientOnes && info.isCarryRequiredInMultiplyQuotientOnes -> {
                        // Step 6-1: carry + ones
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(CellName.CarryDivisorTensM2, CellName.Multiply2Ones),
                            highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientOnes),
                            needsCarry = true
                        )
                        // Step 6-2: Hundreds → Tens 동시 입력
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(CellName.Multiply2Hundreds, CellName.Multiply2Tens),
                            highlightCells = listOf(CellName.QuotientOnes, CellName.DivisorTens, CellName.CarryDivisorTensM2)
                        )
                    }

                    // 3자리 + 캐리 없음 (예: 21×9=189)  << 419÷21 케이스
                    is3DigitsMultiplyQuotientOnes && !info.isCarryRequiredInMultiplyQuotientOnes -> {
                        // Step 6-1: ones만
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(CellName.Multiply2Ones),
                            highlightCells = listOf(CellName.QuotientOnes, CellName.DivisorOnes),
                            // 혹시 이전 케이스 잔여값 방지
                            presetValues = mapOf(CellName.CarryDivisorTensM2 to "")
                        )
                        // Step 6-2: Hundreds → Tens 동시 입력 (carry 입력 없이)
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(CellName.Multiply2Hundreds, CellName.Multiply2Tens),
                            highlightCells = listOf(CellName.QuotientOnes, CellName.DivisorTens)
                        )
                    }

                    // 2자리 + 캐리 있음 (예: 27×4=108은 3자리라 위로 감, 이 케이스는 예: 19×6=114? → 3자리라 위로. 실제 2자리+캐리는 드뭄)
                    !is3DigitsMultiplyQuotientOnes && info.isCarryRequiredInMultiplyQuotientOnes -> {
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(CellName.CarryDivisorTensM2, CellName.Multiply2Ones),
                            highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientOnes),
                            needsCarry = true
                        )
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(CellName.Multiply2Tens),
                            highlightCells = listOf(CellName.QuotientOnes, CellName.DivisorTens, CellName.CarryDivisorTensM2)
                        )
                    }

                    // 2자리 + 캐리 없음 (예: 60×1=60)
                    else -> {
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(CellName.Multiply2Ones),
                            highlightCells = listOf(CellName.QuotientOnes, CellName.DivisorOnes),
                            presetValues = mapOf(CellName.CarryDivisorTensM2 to "")
                        )
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(CellName.Multiply2Tens),
                            highlightCells = listOf(CellName.QuotientOnes, CellName.DivisorTens)
                        )
                    }
                }

                if (info.needsDoubleBorrowInS2) {
                    // [DB1] hbs 입력
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputBorrow,
                        editableCells = listOf(CellName.BorrowSubtract1Hundreds),
                        highlightCells = listOf(
                            CellName.Subtract1Hundreds,
                            CellName.Subtract1Tens,
                            CellName.Subtract1Ones,
                            CellName.Multiply2Tens,
                            CellName.Multiply2Ones
                        ),
                        needsBorrow = true,
                        strikeThroughCells = listOf(CellName.Subtract1Hundreds),
                        subtractLineTargets = setOf(CellName.BorrowSubtract1Hundreds)
                    )
                    // [DB2] tbs 입력
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputBorrow,
                        editableCells = listOf(CellName.BorrowSubtract1Tens),
                        highlightCells = listOf(
                            CellName.Borrowed10Subtract1Tens,
                            CellName.Subtract1Ones,
                            CellName.Multiply2Ones
                        ),
                        presetValues = mapOf(CellName.Borrowed10Subtract1Tens to "10"),
                        needsBorrow = true,
                        strikeThroughCells = listOf(CellName.Subtract1Hundreds, CellName.Borrowed10Subtract1Tens),
                        subtractLineTargets = setOf(CellName.BorrowSubtract1Tens)
                    )
                    // [DB3] Ones
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputSubtract2,
                        editableCells = listOf(CellName.Subtract2Ones),
                        highlightCells = listOf(
                            CellName.Borrowed10Subtract1Ones,
                            CellName.Multiply2Ones,
                            CellName.Subtract1Ones,
                        ),
                        presetValues = mapOf(CellName.Borrowed10Subtract1Ones to "10"),
                        strikeThroughCells = listOf(CellName.Borrowed10Subtract1Tens),
                        subtractLineTargets = setOf(CellName.Subtract2Ones)
                    )
                    // [DB4] Tens (게이팅은 tensResFinal)
                    if (info.shouldPerformSubtractTensStep) {
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputSubtract2,
                            editableCells = listOf(CellName.Subtract2Tens),
                            highlightCells = listOf(
                                CellName.BorrowSubtract1Tens,
                                CellName.Subtract1Tens,
                                CellName.Multiply2Tens
                            ),
                            subtractLineTargets = setOf(CellName.Subtract2Tens)
                        )
                    }

                } else {
                    // ─ hbs만 ─
                    if (info.needsHundredsBorrowInS2) {
                        // Ones 먼저 (예: 9−9=0)
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputSubtract2,
                            editableCells = listOf(CellName.Subtract2Ones),
                            highlightCells = listOf(CellName.Subtract1Ones, CellName.Multiply2Ones),
                            subtractLineTargets = setOf(CellName.Subtract2Ones)
                        )

                        if (!info.skipHundredsBorrowInS2) {
                            steps += PhaseStep(
                                phase = DivisionPhaseV2.InputBorrow,
                                editableCells = listOf(CellName.BorrowSubtract1Hundreds),
                                highlightCells = listOf(
                                    CellName.Subtract1Hundreds,
                                    CellName.Subtract1Tens,
                                    CellName.Multiply2Tens
                                ),
                                needsBorrow = true,
                                strikeThroughCells = listOf(CellName.Subtract1Hundreds),
                                subtractLineTargets = setOf(CellName.BorrowSubtract1Hundreds)
                            )
                        }

                        // Tens 단계 (게이팅은 tensResFinal)
                        if (info.shouldPerformSubtractTensStep) {
                            steps += PhaseStep(
                                phase = DivisionPhaseV2.InputSubtract2,
                                editableCells = listOf(CellName.Subtract2Tens),
                                highlightCells = buildList {
                                    if (info.skipHundredsBorrowInS2) add(CellName.Subtract1Hundreds)
                                    add(CellName.Borrowed10Subtract1Tens)
                                    add(CellName.Subtract1Tens)
                                    add(CellName.Multiply2Tens)
                                },
                                presetValues = if(info.skipHundredsBorrowInS2)
                                    emptyMap()
                                else
                                    mapOf(CellName.Borrowed10Subtract1Tens to "10"),
                                strikeThroughCells = if(info.skipHundredsBorrowInS2)
                                    emptyList()
                                else
                                    listOf(CellName.Subtract1Hundreds),
                                subtractLineTargets = setOf(CellName.Subtract2Tens)
                            )
                        }
                    }
                    // ─ tbs만 ─
                    else if (info.needsTensBorrowInS2) {
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputBorrow,
                            editableCells = listOf(CellName.BorrowSubtract1Tens),
                            highlightCells = listOf(
                                CellName.Subtract1Tens,
                                CellName.Subtract1Ones,
                                CellName.Multiply2Ones
                            ),
                            needsBorrow = true,
                            strikeThroughCells = listOf(CellName.Subtract1Tens),
                            subtractLineTargets = setOf(CellName.BorrowSubtract1Tens)
                        )
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputSubtract2,
                            editableCells = listOf(CellName.Subtract2Ones),
                            highlightCells = listOf(
                                CellName.Borrowed10Subtract1Ones,
                                CellName.Multiply2Ones,
                                CellName.Subtract1Ones
                            ),
                            presetValues = mapOf(CellName.Borrowed10Subtract1Ones to "10"),
                            strikeThroughCells = listOf(CellName.Subtract1Tens),
                            subtractLineTargets = setOf(CellName.Subtract2Ones)
                        )

                        if (info.shouldPerformSubtractTensStep) {
                            steps += PhaseStep(
                                phase = DivisionPhaseV2.InputSubtract2,
                                editableCells = listOf(CellName.Subtract2Tens),
                                highlightCells = listOf(CellName.BorrowSubtract1Tens, CellName.Multiply2Tens),
                                subtractLineTargets = setOf(CellName.Subtract2Tens)
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
            // one quotient
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
                is3DigitsMultiplyQuotientOnes && info.isCarryRequiredInMultiplyQuotientOnes -> {
                    // Step 2-1: carry + ones
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(
                            CellName.CarryDivisorTensM1,
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
                            CellName.CarryDivisorTensM1
                        )
                    )
                }
                // 3자리 + 캐리 없음
                is3DigitsMultiplyQuotientOnes && !info.isCarryRequiredInMultiplyQuotientOnes -> {
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
                !is3DigitsMultiplyQuotientOnes && info.isCarryRequiredInMultiplyQuotientOnes -> {
                    // carry + ones 먼저
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(CellName.CarryDivisorTensM1, CellName.Multiply1Ones),
                        highlightCells = listOf(CellName.DivisorOnes, CellName.QuotientOnes),
                        needsCarry = true
                    )
                    // 그 다음 tens (Hundreds는 없음)
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(CellName.Multiply1Tens),
                        highlightCells = listOf(
                            CellName.QuotientOnes, CellName.DivisorTens, CellName.CarryDivisorTensM1
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
                info.needsDoubleBorrowInS1 -> {
                    // [DB1] 백의 자리에서 십의 자리로 차용
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputBorrow,
                        editableCells = listOf(CellName.BorrowDividendHundreds),
//                        highlightCells = listOf(CellName.DividendHundreds),
                        highlightCells = listOf(
                            CellName.DividendHundreds,
                            CellName.DividendTens,
                            CellName.DividendOnes,
                            CellName.Multiply1Tens,
                            CellName.Multiply1Ones
                        ),
                        needsBorrow = true,
                        strikeThroughCells = listOf(CellName.DividendHundreds),
                        subtractLineTargets = setOf(CellName.BorrowDividendHundreds)
                    )
                    // [DB2] 십의 자리에서 일의 자리로 차용 (Borrowed10Subtract1Tens=10 세팅)
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputBorrow,
                        editableCells = listOf(CellName.BorrowDividendTens),
//                        highlightCells = listOf(CellName.Borrowed10DividendTens),
                        highlightCells = listOf(
                            CellName.Borrowed10DividendTens,
                            CellName.DividendOnes,
                            CellName.Multiply1Ones
                        ),
                        presetValues = mapOf(CellName.Borrowed10DividendTens to "10"),
                        needsBorrow = true,
                        strikeThroughCells = listOf(
                            CellName.DividendHundreds, CellName.Borrowed10DividendTens
                        ),
                        subtractLineTargets = setOf(CellName.BorrowDividendTens)
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
                        strikeThroughCells = listOf(CellName.Borrowed10DividendTens),
                        subtractLineTargets = setOf(CellName.Subtract1Ones)
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
                            subtractLineTargets = setOf(CellName.Subtract1Tens)
                        )
                    }
                }

                // ─ hbs1만 (백→십) ─
                info.needsHundredsBorrowInS1 -> {
                    // 일의 자리 먼저 처리 (차용 없이 가능한 경우가 많음)
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputSubtract1,
                        editableCells = listOf(CellName.Subtract1Ones),
                        highlightCells = listOf(CellName.DividendOnes, CellName.Multiply1Ones),
                        subtractLineTargets = setOf(CellName.Subtract1Ones)
                    )

                    if (!info.skipHundredsBorrowInS1) {
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputBorrow,
                            editableCells = listOf(CellName.BorrowDividendHundreds),
                            highlightCells = listOf(
                                CellName.DividendHundreds,
                                CellName.DividendTens,
                                CellName.Multiply1Tens
                            ),
                            needsBorrow = true,
                            strikeThroughCells = listOf(CellName.DividendHundreds),
                            subtractLineTargets = setOf(CellName.BorrowDividendHundreds)
                        )
                    }

                    if(info.shouldPerformSubtractTensStep) {
                        // 십의 자리 (Borrowed10Subtract1Tens=10 preset)
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputSubtract1,
                            editableCells = listOf(CellName.Subtract1Tens),
                            highlightCells = buildList {
                                if(info.skipHundredsBorrowInS1) add(CellName.DividendHundreds)
                                add(CellName.DividendTens)
                                add(CellName.Multiply1Tens)
                                add(CellName.Borrowed10DividendTens)
                            },
                            presetValues = if(info.skipHundredsBorrowInS1)
                                emptyMap()
                            else
                                mapOf(CellName.Borrowed10DividendTens to "10"),
                            strikeThroughCells = if(info.skipHundredsBorrowInS1)
                                emptyList()
                            else
                                listOf(CellName.DividendHundreds),
                            subtractLineTargets = setOf(CellName.Subtract1Tens)
                        )
                    }
                }

                // ─ tbs1만 (십→일) ─
                info.needsTensBorrowInS1 -> {
                    // 십→일 차용
                    steps += PhaseStep(
                        phase = DivisionPhaseV2.InputBorrow,
                        editableCells = listOf(CellName.BorrowDividendTens),
                        highlightCells = listOf(
                            CellName.DividendTens,
                            CellName.DividendOnes,
                            CellName.Multiply1Ones
                        ),
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
                        strikeThroughCells = listOf(CellName.DividendTens),
                        subtractLineTargets = setOf(CellName.Subtract1Ones)
                    )
                    // 십의 자리
                    if(info.shouldPerformSubtractTensStep) {
                        steps += PhaseStep(
                            phase = DivisionPhaseV2.InputSubtract1,
                            editableCells = listOf(CellName.Subtract1Tens),
                            highlightCells = listOf(CellName.BorrowDividendTens, CellName.Multiply1Tens),
                            subtractLineTargets = setOf(CellName.Subtract1Tens)
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
