package com.romasks.cashier.payapp.listener

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.util.Log
import com.romasks.cashier.common.domain.DefaultCashierData
import com.romasks.cashier.common.domain.LoggedCashierData
import com.romasks.cashier.common.domain.LogoutType
import com.romasks.cashier.common.parcelable.*
import com.romasks.cashier.payapp.client.CashierClient
import com.romasks.cashier.payappinterface.MessageListener
import com.romasks.cashier.payappinterface.RequestProcessor
import org.koin.core.KoinComponent
import org.koin.core.inject
import com.romasks.cashier.payapp.utils.MessageUtils

private const val TAG = "cashierMsgHandler"

class ResponseMessageHandler(
    looper: Looper
) : Handler(looper), KoinComponent {

    private val requestProcessor: RequestProcessor by inject()
    private val messageListener: MessageListener by inject()
    private val cashierClient: CashierClient by inject()

    private var remoteMessenger: Messenger? = null

    val localMessenger: Messenger = Messenger(this)

    override fun handleMessage(incMsg: Message) {
        Log.d(TAG, "Handle received message")

        remoteMessenger = incMsg.replyTo

        MessageUtils.readParcelable(incMsg.data)
            .also { Log.d(TAG, "data: $it") }
            ?.let {
                when (it) {
                  is A2ALoginCashierResponse -> handleLoginResponse(it)
                  is A2ALoginDefaultResponse -> handleLoginDefaultResponse(it)
                  is A2ALogoutCashierResponse -> handleLogoutResponse(it)
                  is A2AManagementCode -> handleManagementCode(it)
                  is A2AManagementError -> handleError(it)
                    else -> onMessageNotSupported()
                }
            }
    }

    private fun handleLoginResponse(response: A2ALoginCashierResponse) {
        Log.d(TAG, "Login data: $response")
        // service.startExpireTokenTimeout(response.refreshToken, response.expirationDateTime)
        messageListener.onLogin(
            LoggedCashierData(
                response.expirationDateTime,
                response.refreshToken,
                response.cashierType,
                response.cashierName,
                response.cashierPassword
            )
        )
    }

    private fun handleLoginDefaultResponse(response: A2ALoginDefaultResponse) {
        Log.d(TAG, "Default password data: $response")
        messageListener.onLogin(DefaultCashierData(response.defaultPassword))
    }

    private fun handleLogoutResponse(response: A2ALogoutCashierResponse) {
        Log.d(TAG, "Logout: $response")
        messageListener.onLogout(LogoutType.RESPONSE)
    }

    private fun handleManagementCode(response: A2AManagementCode) {
        Log.d(TAG, "Get management code: $response")
        when (val pendingRequest = (requestProcessor as RequestProcessorHandler).getPendingRequest()) {
          is A2ALoginCashierRequest -> cashierClient.send(
              A2ALoginCashierRequest(
                  type = pendingRequest.type,
                  refreshToken = pendingRequest.refreshToken,
                  token = response.code,
                  senderPackageName = pendingRequest.senderPackageName
              )
          )
          is A2ALogoutCashierRequest -> cashierClient.send(
              A2ALogoutCashierRequest(
                  token = response.code,
                  senderPackageName = pendingRequest.senderPackageName
              )
          )
        }
        (requestProcessor as RequestProcessorHandler).clearPendingRequest()
        messageListener.setToken(response.code)
    }

    private fun handleError(error: A2AManagementError) {
        Log.e(TAG, "Error: $error")
        messageListener.onError(error.cashierError)
    }

    private fun onMessageNotSupported() {
        Log.e(TAG, "Message not supported")
        MessageUtils.sendMessage(A2AMessageNotSupportedError, localMessenger, remoteMessenger)
    }
}
