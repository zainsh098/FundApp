package com.example.fundapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundapp.model.User
import com.example.fundapp.remote.FirebaseDataSource
import com.example.fundapp.remote.TransactionDataSource
import com.example.fundapp.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val userRepository = UserRepository(FirebaseDataSource(firestore))
    private  val transactionDataSource=TransactionDataSource(firestore)

    val user: MutableLiveData<User?> = MutableLiveData()
    val users: MutableLiveData<List<User>> = MutableLiveData()

    fun saveUser(user: User) {
        viewModelScope.launch {
            userRepository.saveUser(user)
        }
    }

    fun getUser(userId: String) {
        viewModelScope.launch {
            user.value = userRepository.getUser(userId)
        }
    }

    fun getAllUsers() {
        viewModelScope.launch {
            users.value = userRepository.getAllUsers()
        }
    }



}