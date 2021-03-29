package com.romasks.cashier.common.parcelable

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class A2AManagementCode(
    val code: String,
    override val senderPackageName: String
) : Parcelable, A2AManagementRequestBase
