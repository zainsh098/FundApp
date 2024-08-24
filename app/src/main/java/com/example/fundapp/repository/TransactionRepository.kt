package com.example.fundapp.repository

import com.example.fundapp.model.TransactionUser
import com.example.fundapp.remote.TransactionDataSource

class TransactionRepository(private val dataSource: TransactionDataSource) {
    suspend fun depositAmount(transaction: TransactionUser) {
        dataSource.depositAmount(transaction)
        updateDepositBalance(transaction.userId, transaction.amount)
    }

    suspend fun withdrawAmount(transaction: TransactionUser) {
        dataSource.withdrawAmount(transaction)
        updateWithdrawBalance(transaction.userId, transaction.amount)
    }

    private suspend fun updateDepositBalance(userId: String, depositAmount: Int) {
        val user = dataSource.getUser(userId)
        user?.let {
            // Update current balance and total deposited amount
            it.currentBalance = (it.currentBalance ?: 0) + depositAmount
            it.totalDeposited = (it.totalDeposited ?: 0) + depositAmount
            dataSource.updateUserBalance(it)
        }
    }

    private suspend fun updateWithdrawBalance(userId: String, withdrawAmount: Int) {
        val user = dataSource.getUser(userId)
        user?.let {
            // Update total withdrawn amount
            it.totalWithdrawAmount       = (it.totalWithdrawAmount ?: 0) + withdrawAmount
            // Update current balance and total deposited amount
            it.currentBalance = (it.currentBalance ?: 0) - withdrawAmount
            it.totalDeposited = (it.totalDeposited ?: 0) - withdrawAmount
            dataSource.updateUserBalance(it)
        }
    }

    suspend fun getTransactionHistory(userId: String): List<TransactionUser> {
        return dataSource.getTransactionHistory(userId).filterNotNull()
    }
}
