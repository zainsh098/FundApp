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
    private val depositAmount: MutableLiveData<TransactionUser?> = MutableLiveData()

    fun submitDeposit(transaction: TransactionUser) {
        viewModelScope.launch {

            transactionRepository.depositAmount(transaction)

        }

    }

}


