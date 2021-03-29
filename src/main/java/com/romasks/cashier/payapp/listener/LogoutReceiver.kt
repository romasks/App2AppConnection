package com.romasks.cashier.payapp.listener

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.romasks.cashier.common.domain.LogoutType
import com.romasks.cashier.payappinterface.MessageListener
import org.koin.core.KoinComponent
import org.koin.core.inject

class LogoutReceiver : BroadcastReceiver(), KoinComponent {

    private val messageListener: MessageListener by inject()

    override fun onReceive(context: Context, intent: Intent?) {
        messageListener.onLogout(LogoutType.BROADCAST)
    }
}
