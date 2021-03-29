package com.romasks.cashier.payapp.client

import android.content.Intent

object IntentFactory {
    fun getAppServiceIntent(action: String) = Intent(action)
}
