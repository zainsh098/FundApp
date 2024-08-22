package com.example.fundapp.model


data class TransactionUser(
    val transactionId: String = "",
    val userId: String = "",
    val amount: Int = 0,
    val date: String = "",
    val type: String = "",
    val status: String = "",
    val proofOfDeposit: String? = null,
    val reason: String? = null,
    val comments: String? = null
)


