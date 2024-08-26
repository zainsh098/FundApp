package com.example.fundapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundapp.model.User
import com.example.fundapp.remote.FirebaseDataSource
import com.example.fundapp.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val userRepository = UserRepository(FirebaseDataSource(firestore))

    val user: MutableLiveData<User?> = MutableLiveData()
    val users: MutableLiveData<List<User>> = MutableLiveData()
    val userIds: MutableLiveData<List<String>> = MutableLiveData()
    val userBalance: MutableLiveData<Double?> = MutableLiveData()

    fun saveUser(user: User) {
        viewModelScope.launch {
            userRepository.saveUser(user)
        }
    }

//    fun getUserDetails(user: User) {
//        viewModelScope.launch {
//            usersDetails.value = listOf(userRepository.getUserData(user.userId.toString()))
//        }
//    }

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

    fun getAllUserIds() {
        viewModelScope.launch {
            val userList = userRepository.getAllUsers()
            val ids = userList.map { it.userId }
            userIds.value = ids
        }
    }

    fun getUserCurrentBalance(userId: String) {

        viewModelScope.launch {
            val balance = userRepository.getUserBalance(userId)
            userBalance.value=balance

        }
    }


}



