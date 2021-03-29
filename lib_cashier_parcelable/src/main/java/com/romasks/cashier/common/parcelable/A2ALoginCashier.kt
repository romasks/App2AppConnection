package com.romasks.cashier.common.parcelable

import android.os.Parcelable
import com.romasks.cashier.common.domain.CashierType
import com.romasks.cashier.common.domain.LoginProcedureType
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDateTime

sealed class A2ALoginCashier : Parcelable, A2AManagementRequestBase

@Parcelize
data class A2ALoginCashierRequest(
    val type: LoginProcedureType,
    val refreshToken: String?,
    val token: String?,
    override val senderPackageName: String
) : A2ALoginCashier()

@Parcelize
data class A2ALoginCashierResponse(
    val expirationDateTime: LocalDateTime,
    val refreshToken: String,
    val cashierName: String,
    val cashierType: CashierType,
    val cashierPassword: String,
    override val senderPackageName: String
) : A2ALoginCashier()

@Parcelize
data class A2ALoginDefaultResponse(
    val defaultPassword: String,
    override val senderPackageName: String
) : A2ALoginCashier()
