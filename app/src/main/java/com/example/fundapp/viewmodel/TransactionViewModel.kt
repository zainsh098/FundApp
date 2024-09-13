package com.example.fundapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundapp.model.TransactionUser
import com.example.fundapp.repository.TransactionRepository
import kotlinx.coroutines.launch

open class TransactionViewModel : ViewModel() {

    private val transactionRepository = TransactionRepository()
    val transactionHistory: MutableLiveData<List<TransactionUser?>> = MutableLiveData()
    val isLoading = MutableLiveData<Boolean>()

    fun submitDeposit(transaction: TransactionUser) {
        viewModelScope.launch {
            transactionRepository.depositAmount(transaction)
        }
    }

    fun withdrawAmount(transaction: TransactionUser) {
        viewModelScope.launch {
            transactionRepository.withdrawAmount(transaction)
        }
    }

    fun getTransactionHistory(userId: String) {
        viewModelScope.launch {
            isLoading.value = true
            val history = transactionRepository.getTransactionHistory(userId)
            transactionHistory.value = history
            isLoading.value = false
        }
    }

    fun acceptRequest(transactionId: String, userId: String, withdrawAmount: Int) {
        viewModelScope.launch {
            transactionRepository.acceptWithdrawalRequest(transactionId, userId, withdrawAmount)

        }
    }

    fun acceptDepositRequest(transactionId: String, userId: String, withdrawAmount: Int) {
        viewModelScope.launch {
            transactionRepository.acceptDepositRequest(transactionId, userId, withdrawAmount)
            transactionRepository.acceptRequestStatus(transactionId)

        }
    }

    fun rejectRequest(transactionId: String) {
        viewModelScope.launch {
            transactionRepository.rejectRequestStatus(transactionId)
        }
    }

    fun updateTransactionProof(transactionId: String, proofUrl: String) {
        viewModelScope.launch {
            transactionRepository.updateTransaction(transactionId, proofUrl)
        }
    }

}
