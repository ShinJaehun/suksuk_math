package com.shinjaehun.suksuk.domain.multiplication.info

object MulStateInfoBuilder {
    fun from(multiplicand: Int, multiplier: Int): MulStateInfo {
        val McandH = (multiplicand / 100) % 10
        val McandT = (multiplicand / 10) % 10
        val McandO = multiplicand % 10

        val MlierT = (multiplier / 10) % 10
        val MlierO = multiplier % 10

        val m1OO = McandO * MlierO
        val p1Ones = m1OO % 10
        val carryP1Tens = m1OO / 10

        val m1TO = McandT * MlierO + carryP1Tens
        val p1Tens = m1TO % 10
        val carryP1Hundreds = m1TO / 10

        val isMultiplierOnesZero = MlierO == 0

        val m1HO = McandH * MlierO + carryP1Hundreds
        val p1Hundreds = m1HO % 10
        val p1Thousands = m1HO / 10

        val m2OT = McandO * MlierT
        val p2Tens = m2OT % 10
        val carryP2Tens = m2OT / 10

        val m2TT = McandT * MlierT + carryP2Tens
        val p2Hundreds = m2TT % 10
        val carryP2Hundreds = m2TT / 10

        val m2HT = McandH * MlierT + carryP2Hundreds
        val p2Thousands = m2HT % 10
        val p2TenThousands = m2HT / 10

        val sumOnes = p1Ones
        val sumTens = (p1Tens + p2Tens) % 10
        val carrySumHundreds = (p1Tens + p2Tens) / 10

        val sumHundreds = (p1Hundreds + p2Hundreds + carrySumHundreds) % 10
        val carrySumThousands = (p1Hundreds + p2Hundreds + carrySumHundreds) / 10

        val sumThousands = (p1Thousands + p2Thousands + carrySumThousands) % 10
        val carrySumTenThousands = (p1Thousands + p2Thousands + carrySumThousands) / 10

        val sumTenThousands = p2TenThousands + carrySumTenThousands

        val total = multiplicand * multiplier

        return MulStateInfo(
            multiplicand = multiplicand,
            multiplier = multiplier,
            product1Ones = p1Ones,
            product1Tens = p1Tens,
            product1Hundreds = p1Hundreds,
            product1Thousands = p1Thousands,
            carryP1Tens = carryP1Tens,
            carryP1Hundreds = carryP1Hundreds,
            product2Tens = p2Tens,
            product2Hundreds = p2Hundreds,
            product2Thousands = p2Thousands,
            product2TenThousands = p2TenThousands,
            carryP2Tens = carryP2Tens,
            carryP2Hundreds = carryP2Hundreds,
            sumOnes = sumOnes,
            sumTens = sumTens,
            sumHundreds = sumHundreds,
            sumThousands = sumThousands,
            sumTenThousands = sumTenThousands,
            carrySumHundreds = carrySumHundreds,
            carrySumThousands = carrySumThousands,
            carrySumTenThousands = carrySumTenThousands,
            total = total,
            isMultiplierOnesZero = isMultiplierOnesZero
        )
    }
}