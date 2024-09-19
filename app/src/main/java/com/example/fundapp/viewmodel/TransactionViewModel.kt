package com.example.fundapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundapp.model.TransactionUser
import com.example.fundapp.repository.TransactionRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for managing transaction-related operations and data.
 */
open class TransactionViewModel : ViewModel() {

    private val transactionRepository = TransactionRepository()

    // LiveData for holding transaction history and loading state
    val transactionHistory: MutableLiveData<List<TransactionUser?>> = MutableLiveData()
    val isLoading = MutableLiveData<Boolean>()

    /**
     * Submits a deposit transaction.
     * @param transaction The deposit transaction details.
     */
    fun submitDeposit(transaction: TransactionUser) {
        viewModelScope.launch {
            transactionRepository.depositAmount(transaction)
        }
    }

    /**
     * Submits a withdrawal transaction.
     * @param transaction The withdrawal transaction details.
     */
    fun withdrawAmount(transaction: TransactionUser) {
        viewModelScope.launch {
            transactionRepository.withdrawAmount(transaction)
        }
    }

    /**
     * Retrieves transaction history for a specific user.
     * @param userId The ID of the user whose transaction history is to be retrieved.
     */
    fun getTransactionHistory(userId: String) {
        viewModelScope.launch {
            isLoading.value = true
            transactionHistory.value = transactionRepository.getTransactionHistory(userId)
            isLoading.value = false
        }
    }

    /**
     * Accepts a withdrawal request.
     * @param transactionId The ID of the transaction to be accepted.
     * @param userId The ID of the user making the request.
     * @param withdrawAmount The amount to withdraw.
     */
    fun acceptRequest(transactionId: String, userId: String, withdrawAmount: Int) {
        viewModelScope.launch {
            transactionRepository.acceptWithdrawalRequest(transactionId, userId, withdrawAmount)
        }
    }

    /**
     * Accepts a deposit request and updates its status.
     * @param transactionId The ID of the deposit transaction.
     * @param userId The ID of the user making the deposit.
     * @param withdrawAmount The amount to deposit.
     */
    fun acceptDepositRequest(transactionId: String, userId: String, withdrawAmount: Int) {
        viewModelScope.launch {
            transactionRepository.acceptDepositRequest(transactionId, userId, withdrawAmount)
            transactionRepository.acceptRequestStatus(transactionId)
        }
    }

    /**
     * Rejects a transaction request.
     * @param transactionId The ID of the transaction to be rejected.
     */
    fun rejectRequest(transactionId: String) {
        viewModelScope.launch {
            transactionRepository.rejectRequestStatus(transactionId)
        }
    }

    /**
     * Updates the proof of a transaction.
     * @param transactionId The ID of the transaction to be updated.
     * @param proofUrl The URL of the proof document.
     */
    fun updateTransactionProof(transactionId: String, proofUrl: String) {
        viewModelScope.launch {
            transactionRepository.updateTransaction(transactionId, proofUrl)
        }
    }
}
