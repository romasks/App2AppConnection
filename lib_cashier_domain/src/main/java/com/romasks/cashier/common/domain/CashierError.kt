package com.romasks.cashier.common.domain

enum class CashierError {
    GENERAL,
    TIMEOUT,
    INVALID_REFRESH_TOKEN,
    LOGIN_ABORTED,
    BAD_LOGIN_DATA,
    UNSUPPORTED_APPLICATION,
    WRONG_APPLICATION_ID
}
