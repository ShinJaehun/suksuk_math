package com.shinjaehun.suksuk.domain.multiplication.info

data class MulStateInfo (
    val multiplicand: Int,
    val multiplier: Int,

    val product1Ones: Int,
    val product1Tens: Int,
    val product1Hundreds: Int,
    val product1Thousands: Int,

    val carryP1Tens: Int,
    val carryP1Hundreds: Int,

    val product2Tens: Int,
    val product2Hundreds: Int,
    val product2Thousands: Int,
    val product2TenThousands: Int,

    val carryP2Tens: Int,
    val carryP2Hundreds: Int,

    val sumOnes: Int,
    val sumTens: Int,
    val sumHundreds: Int,
    val sumThousands: Int,
    val sumTenThousands: Int,

    val carrySumHundreds: Int,
    val carrySumThousands: Int,
    val carrySumTenThousands: Int,

    val total: Int
)