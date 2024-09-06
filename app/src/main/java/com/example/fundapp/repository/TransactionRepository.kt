package com.example.fundapp.repository

import com.example.fundapp.model.TransactionUser
import com.example.fundapp.remote.TransactionDataSource

class TransactionRepository(private val dataSource: TransactionDataSource) {

    suspend fun updateOrganizationBalance(amount: Int, isDeposit: Boolean) {
        val allUsers = dataSource.getAllUsers()
        var totalDeposited = 0
        var totalWithdrawn = 0

        for (user in allUsers) {
            totalDeposited += user.totalDeposited ?: 0
            totalWithdrawn += user.totalWithdrawAmount ?: 0
        }

        println("Total deposited: $totalDeposited")
        println("Total withdrawn: $totalWithdrawn")

        var organizationBalance = totalDeposited - totalWithdrawn

        println("Initial organization balance: $organizationBalance")

        if (isDeposit) {
            organizationBalance = totalDeposited - totalWithdrawn + amount
        } else {
            organizationBalance = totalDeposited - totalWithdrawn - amount
        }

        println("Updated organization balance: $organizationBalance")

        for (user in allUsers) {
            user.organizationBalance = organizationBalance
            dataSource.updateUserBalance(user)
        }
    }    suspend fun depositAmount(transaction: TransactionUser) {
        dataSource.depositAmount(transaction)
        updateDepositBalance(transaction.userId, transaction.amount)
        updateOrganizationBalance(transaction.amount, true) // Update organization balance for deposit
    }

    suspend fun acceptWithdrawalRequest(transactionId: String, userId: String, withdrawAmount: Int) {
        dataSource.updateRequestStatus(transactionId, "accepted")
        updateWithdrawBalance(userId, withdrawAmount)

        // After accepting withdrawal, update user's withdraw amount
        val user = dataSource.getUser(userId)
        user?.apply {
            totalWithdrawAmount = (totalWithdrawAmount ?: 0) + withdrawAmount
            dataSource.updateUserBalance(this)
        }

        // Update organization balance after withdrawal
        updateOrganizationBalance(withdrawAmount, false)
    }

    suspend fun withdrawAmount(transaction: TransactionUser) {
        dataSource.withdrawAmount(transaction)
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
