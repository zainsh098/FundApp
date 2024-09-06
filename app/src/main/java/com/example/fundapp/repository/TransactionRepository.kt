package com.example.fundapp.repository

import android.util.Log
import com.example.fundapp.model.TransactionUser
import com.example.fundapp.remote.TransactionDataSource

class TransactionRepository(private val dataSource: TransactionDataSource) {

    suspend fun depositAmount(transaction: TransactionUser) {
        dataSource.depositAmount(transaction)
        updateDepositBalance(transaction.userId, transaction.amount)
        updateOrganizationBalance(transaction.amount)
    }

    suspend fun withdrawAmount(transaction: TransactionUser) {
        dataSource.withdrawAmount(transaction)
    }

    suspend fun acceptWithdrawalRequest(transactionId: String, userId: String, withdrawAmount: Int) {
        dataSource.updateRequestStatus(transactionId, "accepted")
        updateWithdrawBalance(userId, withdrawAmount)
        updateOrganizationBalance(-withdrawAmount)
        Log.d("TransactionRepository", "Withdraw amount: $withdrawAmount")
    }

    suspend fun rejectWithdrawalRequest(transactionId: String) {
        dataSource.updateRequestStatus(transactionId, "rejected")
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

    suspend fun updateOrganizationBalance(amount:Int) {
        val allUsers = dataSource.getAllUsers()
        var totalDeposited = 0
        var totalWithdrawn = 0

        for (user in allUsers) {
            totalDeposited += user.totalDeposited ?: 0
            totalWithdrawn += user.totalWithdrawAmount ?: 0
        }

        val organizationBalance = totalDeposited - totalWithdrawn

        for (user in allUsers) {
            user.organizationBalance = organizationBalance
            updateWithdrawBalance(user.userId,amount)
            organizationBalance-amount
            dataSource.updateUserBalance(user)
        }
    }

    suspend fun getTransactionHistory(userId: String): List<TransactionUser> {
        return dataSource.getTransactionHistoryData(userId)
    }

    suspend fun getAllUsersTransactions(): List<TransactionUser> {
        val users = dataSource.getAllUsers()
        val usersTransactionsList = mutableListOf<TransactionUser>()

        for (user in users) {
            val userTransactions = dataSource.getTransactionHistoryData(user.userId)
            usersTransactionsList.addAll(userTransactions)
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
}
