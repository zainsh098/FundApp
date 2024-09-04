package com.example.fundapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
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
    private val transactionRepository = TransactionRepository(TransactionDataSource(firestore))
    val transactionHistory: MutableLiveData<List<TransactionUser?>> = MutableLiveData()


    private val _withdrawalRequests = MutableLiveData<List<TransactionUser>>()
    val withdrawalRequests: LiveData<List<TransactionUser>> get() = _withdrawalRequests


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
            val history = transactionRepository.getTransactionHistory(userId)
            transactionHistory.value = history
        }
    }

    fun getAllWithdrawRequests() {
        viewModelScope.launch {
            try {
                val requests = transactionRepository.getAllWithdrawRequests()
                _withdrawalRequests.postValue(requests)
            } catch (e: Exception) {
                Log.e("TransactionViewModel", "Error fetching all withdrawal requests", e)
            }
        }
    }


}
