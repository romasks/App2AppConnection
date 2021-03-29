package com.romasks.cashier.payapp.client

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.HandlerThread
import android.os.IBinder
import android.os.Messenger
import android.os.Parcelable
import android.util.Log
import com.romasks.cashier.payappinterface.CashierActionListener
import com.romasks.cashier.payapp.listener.ResponseMessageHandler
import com.romasks.cashier.payapp.utils.Keys
import com.romasks.cashier.payapp.utils.MessageUtils

private const val TAG = "CashierClient"
private const val pkgDest: String = "com.romasks.cashier.mngapp"

class CashierClient(
    private val context: Context
) {

    private var connectionListener: CashierActionListener? = null
    private var messageHandler: ResponseMessageHandler? = null
    private var handlerThread: HandlerThread? = null
    private var remoteMessenger: Messenger? = null

    fun startConnecting(listener: CashierActionListener? = null) {
        when {
            isConnected() -> {
                Log.e(TAG, "App $pkgDest already connected")
                listener?.onConnected(true)
            }
            isConnecting() -> {
                Log.e(TAG, "App $pkgDest connection already in progress")
                listener?.onConnected(false)
            }
            else -> {
                connectionListener = listener
                connectApp()
            }
        }
    }

    fun send(message: Parcelable): Boolean = when {
        isConnected() -> {
            MessageUtils.sendMessage(message, messageHandler!!.localMessenger, remoteMessenger)
            Log.d(TAG, "Sent message to $pkgDest")
            true
        }
        else -> {
            Log.e(TAG, "App $pkgDest client not connected")
            false
        }
    }

    fun disconnect() {
        connectionListener = null
        context.unbindService(serviceConnection)
        messageHandler = null
        handlerThread?.quit()
    }

    fun isConnected() = remoteMessenger != null && messageHandler != null

    private fun isConnecting() = connectionListener != null

    private fun connectApp() {
        Log.d(TAG, "Attempt to connect $pkgDest")

        handlerThread = HandlerThread("CashierListenerService").also {
            it.start()
            messageHandler = ResponseMessageHandler(it.looper)
        }

        val serviceIntent = IntentFactory.getAppServiceIntent("$pkgDest.action.CASHIER_MANAGEMENT")
        serviceIntent.putExtra(Keys.APPLICATION_ID, context.applicationInfo.packageName)

        try {
            context.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        } catch (ex: Exception) {
            Log.e(TAG, ex.localizedMessage)
        }
    }

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            Log.d(TAG, "App $pkgDest bound service connected")
            remoteMessenger = Messenger(binder)
            connectionListener?.onSuccess()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.d(TAG, "App $pkgDest service disconnected")
            connectionListener?.onFailure()
            remoteMessenger = null
        }

        override fun onBindingDied(name: ComponentName?) {
            Log.d(TAG, "App $pkgDest binding died")
            connectionListener?.onFailure()
        }

        override fun onNullBinding(name: ComponentName?) {
            Log.d(TAG, "App $pkgDest null binding")
            connectionListener?.onFailure()
        }
    }
}
