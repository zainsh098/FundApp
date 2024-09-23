package com.example.fundapp.repository

import com.example.fundapp.constants.TransactionConstant
import com.example.fundapp.model.TransactionUser
import com.example.fundapp.remote.TransactionDataSource

/**
 * Repository class for managing transaction operations.
 * @property dataSource The data source for accessing transaction-related data.
 */
class TransactionRepository {

    private val dataSource: TransactionDataSource = TransactionDataSource()

    /**
     * Processes a deposit transaction.
     * @param transaction The transaction details.
     */
    suspend fun depositAmount(transaction: TransactionUser) {
        dataSource.depositAmount(transaction)
    }

    /**
     * Accepts a withdrawal request and updates the user's balance.
     * @param transactionId The ID of the transaction to be accepted.
     * @param userId The ID of the user making the withdrawal.
     * @param withdrawAmount The amount to be withdrawn.
     */
    suspend fun acceptWithdrawalRequest(
        transactionId: String,
        userId: String,
        withdrawAmount: Int
    ) {
        dataSource.updateRequestStatus(transactionId, TransactionConstant.KEY_ACCEPTED)
        updateWithdrawBalance(userId, withdrawAmount)
    }

    /**
     * Accepts a deposit request and updates the user's balance.
     * @param transactionId The ID of the transaction to be accepted.
     * @param userId The ID of the user making the deposit.
     * @param depositAmount The amount to be deposited.
     */
    suspend fun acceptDepositRequest(
        transactionId: String,
        userId: String,
        depositAmount: Int
    ) {
        dataSource.updateDepositRequestStatus(transactionId, TransactionConstant.KEY_ACCEPTED)
        updateDepositBalance(userId, depositAmount)
    }

    /**
     * Updates the user's balance after a deposit.
     * @param userId The ID of the user.
     * @param depositAmount The amount deposited.
     */
    private suspend fun updateDepositBalance(userId: String, depositAmount: Int) {
        val user = dataSource.getUser(userId)
        user?.apply {
            totalDeposited = (totalDeposited ?: 0) + depositAmount
            currentBalance = (currentBalance ?: 0) + depositAmount
            dataSource.updateUserBalance(this)
        }
    }

    /**
     * Updates the user's balance after a withdrawal.
     * @param userId The ID of the user.
     * @param withdrawAmount The amount withdrawn.
     */
    private suspend fun updateWithdrawBalance(userId: String, withdrawAmount: Int) {
        val user = dataSource.getUser(userId)
        user?.apply {
            totalWithdrawAmount = (totalWithdrawAmount ?: 0) + withdrawAmount
            currentBalance = (currentBalance ?: 0) - withdrawAmount
            dataSource.updateUserBalance(this)
        }
    }

    /**
     * Processes a withdrawal transaction.
     * @param transaction The transaction details.
     */
    suspend fun withdrawAmount(transaction: TransactionUser) {
        dataSource.withdrawAmount(transaction)
    }

    /**
     * Retrieves the transaction history for a user.
     * @param userId The ID of the user.
     * @return A list of transactions for the user.
     */
    suspend fun getTransactionHistory(userId: String): List<TransactionUser> {
        return dataSource.getTransactionHistoryData(userId)
    }

    /**
     * Updates a transaction with a proof of withdrawal.
     * @param transactionId The ID of the transaction.
     * @param withdrawProof The URL of the withdrawal proof.
     */
    suspend fun updateTransaction(transactionId: String, withdrawProof: String) {
        return dataSource.updateTransaction(transactionId, withdrawProof)
    }

    /**
     * Rejects a request by updating its status.
     * @param transactionId The ID of the transaction to be rejected.
     */
    suspend fun rejectRequestStatus(transactionId: String) {
        dataSource.updateRequestStatus(transactionId, TransactionConstant.KEY_REJECTED)
    }

    /**
     * Accepts a request by updating its status.
     * @param transactionId The ID of the transaction to be accepted.
     */
    suspend fun acceptRequestStatus(transactionId: String) {
        dataSource.updateRequestStatus(transactionId, TransactionConstant.KEY_ACCEPTED)
    }

    /**
     * Retrieves all withdrawal requests from all users.
     * @return A list of all withdrawal requests.
     */
    suspend fun getAllWithdrawRequests(): List<TransactionUser> {
        val users = dataSource.getAllUsers()
        val withdrawRequests = mutableListOf<TransactionUser>()
        for (user in users) {
            val userWithdrawRequests = dataSource.getAllWithdrawRequests(user.userId)
            withdrawRequests.addAll(userWithdrawRequests)
        }
        return withdrawRequests
    }

    /**
     * Retrieves all deposit requests from all users.
     * @return A list of all deposit requests.
     */
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
