package com.example.fundapp.model

data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    var totalDeposited: Int? = 0,
    var currentBalance: Int? = 0,
    var organizationBalance: Int? = 0,
    var totalSubmittedAmount: Int? = 0,
    var totalWithdrawAmount: Int? = 0,
    val role: String = "user",
    val photoUrl: String? = null
)


