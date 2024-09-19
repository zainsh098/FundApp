package com.example.fundapp.fragments.approverequest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fundapp.model.TransactionUser
import com.example.fundapp.repository.TransactionRepository
import com.example.fundapp.viewmodel.TransactionViewModel
import kotlinx.coroutines.launch

/**
 * ViewModel for managing and processing withdrawal requests.
 *
 * This ViewModel is responsible for fetching withdrawal requests from the repository,
 * updating the UI with the list of requests, and managing loading state.
 */
class ApproveRequestViewModel : TransactionViewModel() {

    private val _withdrawalRequests = MutableLiveData<List<TransactionUser>>()
    /**
     * LiveData representing the list of withdrawal requests.
     */
    val withdrawalRequests: LiveData<List<TransactionUser>> get() = _withdrawalRequests

    private val transactionRepository = TransactionRepository()

    init {
        // Fetch all withdrawal requests when the ViewModel is initialized
        getAllWithdrawRequests()
    }

    /**
     * Fetches all withdrawal requests from the repository.
     *
     * Launches a coroutine to perform the network operation and updates the LiveData
     * with the list of withdrawal requests. Also manages the loading state.
     */
    fun getAllWithdrawRequests() {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val requests = transactionRepository.getAllWithdrawRequests()
                _withdrawalRequests.value = requests
                isLoading.value = false
            } catch (e: Exception) {
                Log.e("ApproveRequestViewModel", "Error fetching all withdrawal requests", e)
            }
        }
    }
}
