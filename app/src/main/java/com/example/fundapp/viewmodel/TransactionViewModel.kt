package com.example.fundapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundapp.model.TransactionUser
import com.example.fundapp.remote.TransactionDataSource
import com.example.fundapp.repository.TransactionRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

open class TransactionViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val transactionRepository = TransactionRepository(TransactionDataSource())
    val transactionHistory: MutableLiveData<List<TransactionUser?>> = MutableLiveData()
    val allTransactionHistoryUsers: MutableLiveData<List<TransactionUser>> = MutableLiveData()
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


    fun getAllUsersTransactionHistory(userId: String) {
        viewModelScope.launch {
            val history = transactionRepository.getAllUsersTransactions()
            allTransactionHistoryUsers.value = history
        }
    }

    fun acceptRequest(transactionId: String, userId: String, withdrawAmount: Int) {
        viewModelScope.launch {
            transactionRepository.acceptWithdrawalRequest(transactionId, userId, withdrawAmount)
            Log.d(
                "TransactionViewModel",
                "Withdrawal accepted for user: $userId, amount: $withdrawAmount"
            )
            transactionRepository.updateOrganizationBalance(withdrawAmount)
            Log.d(
                "TransactionViewModel",
                "Updated organization balance with deduction: $withdrawAmount"
            )
        }
    }

    fun rejectRequest(transactionId: String) {
        viewModelScope.launch {
            transactionRepository.rejectWithdrawalRequest(transactionId)
        }
    }

}
