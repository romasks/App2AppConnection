package com.romasks.cashier.payappinterface

import com.romasks.cashier.common.domain.CashierData
import com.romasks.cashier.common.domain.CashierError
import com.romasks.cashier.common.domain.LogoutType

interface MessageListener {
    fun onLogin(data: CashierData)
    fun onLogout(type: LogoutType)
    fun onError(error: CashierError)
    fun setToken(token: String)
    fun getToken(): String?
}
