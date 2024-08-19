package com.example.fundapp.model

data class User (
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val totalDeposited: Int = 0,
    val totalWithdrawn: Int = 0,
    val currentBalance: Int = 0,
    val totalSubmittedAmount: Int = 0,
    val role: String = "user",
    val photoUrl: String? = null
)
