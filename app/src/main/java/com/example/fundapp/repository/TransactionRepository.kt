package com.example.fundapp.repository

import com.example.fundapp.model.TransactionUser
import com.example.fundapp.remote.TransactionDataSource

class TransactionRepository(private val dataSource: TransactionDataSource) {

    suspend fun depositAmount(transaction: TransactionUser) {
        dataSource.depositAmount(transaction)
        updateUserBalance(transaction.userId, transaction.amount)
    }


    private suspend fun updateUserBalance(userId: String, depositAmount: Int) {

        val user = dataSource.getUser(userId)
        val updatedBalance = user?.totalDeposited?.plus(depositAmount)
        user?.let {
            if (updatedBalance != null) {
                it.totalDeposited = updatedBalance
            }
            dataSource.updateUserBalance(it)
        }
    }
}
