package com.romasks.cashier.common.parcelable

import android.os.Parcelable
import com.romasks.cashier.common.domain.CashierError
import kotlinx.android.parcel.Parcelize

@Parcelize
data class A2AManagementError(
    val cashierError: CashierError
) : Parcelable
