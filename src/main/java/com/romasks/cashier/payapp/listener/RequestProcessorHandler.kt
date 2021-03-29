package com.romasks.cashier.payapp.listener

import android.util.Log
import com.romasks.cashier.common.domain.LoginProcedureType
import com.romasks.cashier.common.parcelable.A2ALoginCashierRequest
import com.romasks.cashier.common.parcelable.A2ALogoutCashierRequest
import com.romasks.cashier.common.parcelable.A2AManagementRequestBase
import com.romasks.cashier.payapp.client.CashierClient
import com.romasks.cashier.payappinterface.MessageListener
import com.romasks.cashier.payappinterface.RequestProcessor
import org.koin.core.KoinComponent
import org.koin.core.inject

private const val TAG = "RequestProcessorHandler"

class RequestProcessorHandler() : RequestProcessor, KoinComponent {

    private val cashierClient: CashierClient by inject()
    private val messageListener: MessageListener by inject()

    private var pendingRequest: A2AManagementRequestBase? = null

    override fun requestCashierLogin(type: LoginProcedureType, refreshToken: String?, applicationId: String) {
        A2ALoginCashierRequest(type, refreshToken, messageListener.getToken(), applicationId).let {
            Log.d(TAG, "Login request: $it")
            pendingRequest = it
            cashierClient.send(it)
        }
    }

    override fun requestCashierLogout(applicationId: String) {
        A2ALogoutCashierRequest(messageListener.getToken(), applicationId).let {
            Log.d(TAG, "Logout request: $it")
            pendingRequest = it
            cashierClient.send(it)
        }
    }

    fun getPendingRequest(): A2AManagementRequestBase? = pendingRequest

    fun clearPendingRequest() {
        pendingRequest = null
    }
}
