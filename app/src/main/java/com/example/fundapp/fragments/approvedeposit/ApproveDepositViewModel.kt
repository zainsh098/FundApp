package com.example.fundapp.fragments.approvedeposit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fundapp.model.TransactionUser
import com.example.fundapp.repository.TransactionRepository
import com.example.fundapp.viewmodel.TransactionViewModel
import kotlinx.coroutines.launch

/**
 * ViewModel for managing deposit approval requests.
 *
 * This ViewModel handles the retrieval of deposit requests and exposes them to the UI. It also
 * manages the loading state and handles any errors that occur during data fetching.
 */
class ApproveDepositViewModel : TransactionViewModel() {

    private val _depositRequests = MutableLiveData<List<TransactionUser>>()
    /**
     * LiveData for observing the list of deposit requests.
     */
    val depositRequests: LiveData<List<TransactionUser>> get() = _depositRequests

    private val transactionRepository = TransactionRepository()

    init {
        getAllDepositRequests()
    }

    /**
     * Fetches all deposit requests from the repository and updates the LiveData.
     */
    fun getAllDepositRequests() {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val requests = transactionRepository.getAllDepositRequests()
                _depositRequests.value = requests
                isLoading.value = false
            } catch (e: Exception) {
                Log.e("ApproveDepositViewModel", "Error fetching all deposit requests", e)
                isLoading.value = false
            }
        }
    }
}
