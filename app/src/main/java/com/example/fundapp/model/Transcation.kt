package com.example.fundapp.model

import android.annotation.SuppressLint
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.example.fundapp.R


data class TransactionUser(
    val name:String="",
    val transactionId: String = "",
    val userId: String = "",
    val amount: Int = 0,
    val date: String = "",
    val type: String = "",
    val status: String = "",
    val photoUrl: String? = null,
    val proofOfDeposit: String? = null,
    val proofOfWithdraw: String? = null,
    val reason: String? = null,
)
//todo will look into this
//data class TransactionUserD(
//
//    val transactionId: String = "",
//
//    val amount: Int = 0,
//    val date: String = "",
//    val type: String = "",
//    @SuppressLint("ResourceAsColor") @ColorInt
//    val color: Int = R.color.green,
//    @DrawableRes
//    val icon: Int = R.drawable.arrowdown,
//)


