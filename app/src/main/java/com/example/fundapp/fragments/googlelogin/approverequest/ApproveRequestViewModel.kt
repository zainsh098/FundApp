package com.example.fundapp.fragments.googlelogin.approverequest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fundapp.model.TransactionUser
import com.example.fundapp.remote.TransactionDataSource
import com.example.fundapp.repository.TransactionRepository
import com.example.fundapp.viewmodel.TransactionViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ApproveRequestViewModel : TransactionViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val _withdrawalRequests = MutableLiveData<List<TransactionUser>>()
    val withdrawalRequests: LiveData<List<TransactionUser>> get() = _withdrawalRequests
    private val transactionRepository = TransactionRepository(TransactionDataSource(firestore))


    init {
        getAllWithdrawRequests()
    }


    fun getAllWithdrawRequests() {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val requests = transactionRepository.getAllWithdrawRequests()
                _withdrawalRequests.value = requests
                isLoading.value = false
            } catch (e: Exception) {
                Log.e("TransactionViewModel", "Error fetching all withdrawal requests", e)
            }
        }
    }

}