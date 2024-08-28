package com.example.fundapp.model


data class TransactionUser(

    val name:String="",
    val transactionId: String = "",
    val userId: String = "",
    val amount: Int = 0,
    val dateDeposit: String = "",
    val dateWithdraw: String = "",
    val type: String = "",
    val status: String = "",
    val photoUrl: String? = null,
    val proofOfDeposit: String? = null,
    val reason: String? = null,
)


