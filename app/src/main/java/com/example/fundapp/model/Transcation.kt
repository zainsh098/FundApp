package com.example.fundapp.model


data class TransactionUser(
    val transactionId: String,
    val userId: String,
    val type: String, // "deposit" or "withdrawal"
    val amount: Int,
    val date: String,
    val proofOfDeposit: String? = null,
    val comments: String? = null,
    val reason: String? = null,
    val status: String = "pending" // "pending", "accepted", "rejected"
)

