package com.example.fundapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundapp.model.TransactionUser
import com.example.fundapp.remote.TransactionDataSource
import com.example.fundapp.repository.TransactionRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class TransactionViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val transactionRepository = TransactionRepository(TransactionDataSource(firestore))
    val transactionHistory: MutableLiveData<List<TransactionUser?>> = MutableLiveData()
    val withdrawalRequests: MutableLiveData<List<TransactionUser?>> = MutableLiveData()

    // Function to submit a deposit transaction
    fun submitDeposit(transaction: TransactionUser) {
        viewModelScope.launch {
            transactionRepository.depositAmount(transaction)
        }
    }

    // Function to submit a withdrawal transaction
    fun withdrawAmount(transaction: TransactionUser) {
        viewModelScope.launch {
            transactionRepository.withdrawAmount(transaction)
        }
    }

    // Function to retrieve the transaction history for a specific user
    fun getTransactionHistory(userId: String) {
        viewModelScope.launch {
            val history = transactionRepository.getTransactionHistory(userId)
            transactionHistory.value = history
        }
    }

    // Function to listen for realtime updates to the users transaction history
    fun startTransactionListener(userId: String) {
        transactionRepository.addTransactionListener(userId) { transactions ->
            transactionHistory.postValue(transactions)
        }
    }

    // Function to remove the transaction listener when no longer needed
    fun stopTransactionListener() {
        transactionRepository.removeTransactionListener()
    }

    fun getApprovalWithdrawalRequests(userId: String) {
        viewModelScope.launch {
            val requests = transactionRepository.getAllWithdrawRequests(userId)
            withdrawalRequests.value = requests
        }
    }

}
