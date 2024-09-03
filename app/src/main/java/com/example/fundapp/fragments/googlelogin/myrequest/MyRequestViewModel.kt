package com.example.fundapp.fragments.googlelogin.myrequest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fundapp.model.TransactionUser
import com.example.fundapp.remote.TransactionDataSource
import com.example.fundapp.repository.TransactionRepository
import com.example.fundapp.viewmodel.TransactionViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class MyRequestViewModel : TransactionViewModel() {


    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val transactionRepository = TransactionRepository(TransactionDataSource(firestore))
    private val _gettransactionHistory1: MutableLiveData<List<TransactionUser?>> = MutableLiveData()
    val getTransactionHistory1: MutableLiveData<List<TransactionUser?>> = _gettransactionHistory1

    init {
        getTransactionHistoryUser(auth.currentUser!!.uid)
    }

    fun getTransactionHistoryUser(userId: String) {

        viewModelScope.launch {
            val history = transactionRepository.getTransactionHistory(userId)
            getTransactionHistory1.value = history
        }

    }
}