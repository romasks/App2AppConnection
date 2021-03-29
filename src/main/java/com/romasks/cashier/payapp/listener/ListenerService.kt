package com.romasks.cashier.payapp.listener

import android.app.Service
import android.content.Intent
import android.os.HandlerThread
import android.os.IBinder
import android.util.Log

private const val TAG = "cashierListnrService"

class ListenerService : Service() {

    private lateinit var handlerThread: HandlerThread
    private lateinit var messageHandler: ResponseMessageHandler

    override fun onCreate() {
        handlerThread = HandlerThread("CashierListenerService")
        handlerThread.start()

        messageHandler = ResponseMessageHandler(handlerThread.looper)

        Log.d(TAG, "Messenger service created")
    }

    override fun onBind(intent: Intent): IBinder = messageHandler.localMessenger.binder

    override fun onUnbind(intent: Intent): Boolean = false

    override fun onDestroy() {
        handlerThread.quit()
        super.onDestroy()
    }

    /*fun startExpireTokenTimeout(refreshToken: String, expirationDateTime: LocalDateTime) {
      val dateExpire = expirationDateTime.let {
        Date(it.year, it.monthValue, it.dayOfMonth, it.hour, it.minute, it.second).time
      }
      val dateNow = LocalDateTime.now().let {
        Date(it.year, it.monthValue, it.dayOfMonth, it.hour, it.minute, it.second).time
      }
      messageHandler.postDelayed(
        { CashierClient.requestProcessor!!.requestCashierLogin(LoginProcedureType.STATUS, refreshToken) },
        dateExpire - dateNow
      )
    }*/
}
