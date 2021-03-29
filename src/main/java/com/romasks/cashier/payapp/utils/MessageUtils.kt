package com.romasks.cashier.payapp.utils

import android.os.Bundle
import android.os.Message
import android.os.Messenger
import android.os.Parcelable
import android.util.Log
import com.romasks.cashier.common.parcelable.A2AManagementRequestBase

private const val TAG = "MessageUtils"

object MessageUtils {

  fun readParcelable(bundle: Bundle): Parcelable? {
    bundle.classLoader = A2AManagementRequestBase::class.java.classLoader
    return bundle.getParcelable(Keys.CASHIER_DATA)
  }

  fun sendMessage(parcelable: Parcelable, localMessenger: Messenger, remoteMessenger: Messenger?) {
    remoteMessenger?.send(createMessage(parcelable).apply { replyTo = localMessenger })
      ?: Log.e(TAG, "Client messenger is null")
  }

  private fun createMessage(parcelable: Parcelable): Message = Message.obtain(null, Keys.CASHIER_ACTION).apply {
    data = Bundle().also { it.putParcelable(Keys.CASHIER_DATA, parcelable) }
  }
}
