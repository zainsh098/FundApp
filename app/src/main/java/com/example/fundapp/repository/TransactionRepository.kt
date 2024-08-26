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
        user?.apply {
            currentBalance = (currentBalance ?: 0) + depositAmount
            totalDeposited = (totalDeposited ?: 0) + depositAmount
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
        return dataSource.getTransactionHistory(userId).filterNotNull()
    }

    fun addTransactionListener(
        userId: String,
        onTransactionChanged: (List<TransactionUser?>) -> Unit
    ) {
        dataSource.addTransactionListener(userId, onTransactionChanged)
    }

    fun removeTransactionListener() {
        dataSource.removeTransactionListener()
    }
}
