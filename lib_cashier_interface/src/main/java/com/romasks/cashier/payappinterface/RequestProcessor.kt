package com.romasks.cashier.payappinterface

import com.romasks.cashier.common.domain.LoginProcedureType

interface RequestProcessor {
    fun requestCashierLogin(type: LoginProcedureType, refreshToken: String?, applicationId: String)
    fun requestCashierLogout(applicationId: String)
}
