package com.example.fundapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundapp.model.User
import com.example.fundapp.remote.FirebaseDataSource
import com.example.fundapp.repository.UserRepository
import com.example.fundapp.userrole.SessionManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

open class UserViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val userRepository = UserRepository(FirebaseDataSource(firestore))

    val currentUser: MutableLiveData<User?> = MutableLiveData()
    val allUsers: MutableLiveData<List<User>> = MutableLiveData()
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
            val user = userRepository.getUser(userId)
            currentUser.value = userRepository.getUser(userId)


            user?.let {

                SessionManager.setRole(it.role)
                Log.d("UserViewModel User View Model", "User Role: User View Model ${it.role}")

            }


        }
    }

    fun getAllUsers() {
        viewModelScope.launch {
            allUsers.value = userRepository.getAllUsers()
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
            userBalance.value = balance

        }
    }


}



