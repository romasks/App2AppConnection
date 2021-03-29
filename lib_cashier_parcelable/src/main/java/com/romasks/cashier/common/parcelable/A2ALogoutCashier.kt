package com.romasks.cashier.common.parcelable

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class A2ALogoutCashier : Parcelable, A2AManagementRequestBase

@Parcelize
data class A2ALogoutCashierRequest(
    val token: String?,
    override val senderPackageName: String
) : A2ALogoutCashier()

@Parcelize
data class A2ALogoutCashierResponse(
    override val senderPackageName: String
) : A2ALogoutCashier()
