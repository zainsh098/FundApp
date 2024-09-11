package com.example.fundapp.repository

import com.example.fundapp.constants.TransactionConstant
import com.example.fundapp.model.TransactionUser
import com.example.fundapp.remote.TransactionDataSource

class TransactionRepository(private val dataSource: TransactionDataSource) {

    suspend fun depositAmount(transaction: TransactionUser) {
        dataSource.depositAmount(transaction)
    }

    suspend fun acceptWithdrawalRequest(
        transactionId: String,
        userId: String,
        withdrawAmount: Int
    ) {
        dataSource.updateRequestStatus(transactionId, TransactionConstant.KEY_ACCEPTED)
        updateWithdrawBalance(userId, withdrawAmount)
    }

    suspend fun acceptDepositRequest(
        transactionId: String,
        userId: String,
        depositAmount: Int
    ) {
        updateDepositBalance(userId, depositAmount)
        dataSource.updateDepositRequestStatus(transactionId, TransactionConstant.KEY_ACCEPTED)
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

    suspend fun updateTransaction(transactionId: String, withdrawProof: String) {
        return dataSource.updateTransaction(transactionId, withdrawProof)
    }

    suspend fun rejectRequest(transactionId: String) {
        dataSource.updateRequestStatus(transactionId, "rejected")
        dataSource.removeTransaction(transactionId)

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


    suspend fun getAllDepositRequests(): List<TransactionUser> {
        val users = dataSource.getAllUsers()
        val depositRequests = mutableListOf<TransactionUser>()
        for (user in users) {
            val userDepositRequests = dataSource.getAllDepositRequest(user.userId)
            depositRequests.addAll(userDepositRequests)
        }
        return depositRequests
    }
}


