package com.example.fundapp.fragments.approvedeposit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fundapp.model.TransactionUser
import com.example.fundapp.remote.TransactionDataSource
import com.example.fundapp.repository.TransactionRepository
import com.example.fundapp.viewmodel.TransactionViewModel
import kotlinx.coroutines.launch

class ApproveDepositViewModel : TransactionViewModel() {

    private val _depositRequests = MutableLiveData<List<TransactionUser>>()
    val depositRequests: LiveData<List<TransactionUser>> get() = _depositRequests
    private val transactionRepository = TransactionRepository(TransactionDataSource())


    init {
        getAllDepositRequests()
    }

     fun getAllDepositRequests() {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val requests = transactionRepository.getAllDepositRequests()
                _depositRequests.value = requests
                isLoading.value = false
            } catch (e: Exception) {
                Log.e("TransactionViewModel", "Error fetching all Deposit requests", e)
            }
        }
    }

}

