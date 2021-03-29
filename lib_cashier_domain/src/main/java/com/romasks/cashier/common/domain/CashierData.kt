package com.romasks.cashier.common.domain

import org.threeten.bp.LocalDateTime

sealed class CashierData

data class LoggedCashierData(
    val expirationDateTime: LocalDateTime,
    val refreshToken: String,
    val cashierType: CashierType,
    val cashierName: String,
    val cashierPassword: String // SHA1
) : CashierData()

data class DefaultCashierData(
    val defaultPassword: String // SHA1
) : CashierData()
