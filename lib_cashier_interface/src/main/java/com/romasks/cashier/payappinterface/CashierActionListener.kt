package com.romasks.cashier.payappinterface

interface CashierActionListener {
    fun onSuccess()
    fun onFailure()
    fun onConnected(connected: Boolean)
}
