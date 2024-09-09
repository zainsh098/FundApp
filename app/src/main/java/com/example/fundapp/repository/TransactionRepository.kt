package com.example.fundapp.repository

import com.example.fundapp.model.TransactionUser
import com.example.fundapp.remote.TransactionDataSource

class TransactionRepository(private val dataSource: TransactionDataSource) {


    suspend fun depositAmount(transaction: TransactionUser) {
        dataSource.depositAmount(transaction)
        updateDepositBalance(transaction.userId, transaction.amount)
    }

    suspend fun acceptWithdrawalRequest(
        transactionId: String,
        userId: String,
        withdrawAmount: Int
    ) {
        dataSource.updateRequestStatus(transactionId, "accepted")
        updateWithdrawBalance(userId, withdrawAmount)
    }

    private suspend fun updateDepositBalance(userId: String, depositAmount: Int) {
        val user = dataSource.getUser(userId)
        user?.apply {
            totalDeposited = (totalDeposited ?: 0) + depositAmount
            currentBalance = (currentBalance ?: 0) + depositAmount
            dataSource.updateUserBalance(this)
        }
    }

    private suspend fun updateWithdrawBalance(userId: String, withdrawAmount: Int) {
        val user = dataSource.getUser(userId)
        user?.apply {
            totalWithdrawAmount = (totalWithdrawAmount ?: 0) + withdrawAmount
            currentBalance = (currentBalance ?: 0) - withdrawAmount
            dataSource.updateUserBalance(this)
        }
    }

    suspend fun withdrawAmount(transaction: TransactionUser) {
        dataSource.withdrawAmount(transaction)
    }

    suspend fun getTransactionHistory(userId: String): List<TransactionUser> {
        return dataSource.getTransactionHistoryData(userId)
    }

    suspend fun rejectWithdrawalRequest(transactionId: String) {
        dataSource.updateRequestStatus(transactionId, "rejected")
    }

    suspend fun getAllUsersTransactions(): List<TransactionUser> {
        val users = dataSource.getAllUsers()
        val usersTransactionsList = mutableListOf<TransactionUser>()

        for (user in users) {
            val userTransactions = dataSource.getTransactionHistoryData(user.userId)
            usersTransactionsList.addAll(userTransactions)
            val orgBalance = +user.currentBalance!!
        }

        return usersTransactionsList
    }

    suspend fun getAllWithdrawRequests(): List<TransactionUser> {
        val users = dataSource.getAllUsers()
        val withdrawRequests = mutableListOf<TransactionUser>()

        for (user in users) {
            val userWithdrawRequests = dataSource.getAllWithdrawRequests(user.userId)
            withdrawRequests.addAll(userWithdrawRequests)
        }
        return withdrawRequests
    }


//    suspend fun updateOrganizationBalance(amount: Int, isDeposit: Boolean) {
//        val allUsers = dataSource.getAllUsers()
//
//        var organizationBalance = allUsers.firstOrNull()?.organizationBalance ?: 0
//
//        println("Initial organization balance: $organizationBalance")
//
//        if (isDeposit) {
//            organizationBalance += amount // Increase balance on deposit
//        } else {
//            organizationBalance -= amount // Decrease balance on withdrawal
//        }
//
//        println("Updated organization balance: $organizationBalance")
//
//        for (user in allUsers) {
//            user.organizationBalance = organizationBalance
//            dataSource.updateUserBalance(user)
//        }
//    }

}
