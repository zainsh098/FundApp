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

/**
 * ViewModel for managing user-related operations and data.
 */
open class UserViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val userRepository = UserRepository(FirebaseDataSource(firestore))

    // LiveData for holding user-related data
    val currentUser: MutableLiveData<User?> = MutableLiveData()
    val orgBalance: MutableLiveData<Int> = MutableLiveData(0)
    val allUsers: MutableLiveData<List<User>> = MutableLiveData()
    val userBalance: MutableLiveData<Double?> = MutableLiveData()

    /**
     * Saves a user to the repository.
     * @param user The user to be saved.
     */
    fun saveUser(user: User) {
        viewModelScope.launch {
            userRepository.saveUser(user)
        }
    }

    /**
     * Retrieves a user by ID and updates LiveData.
     * @param userId The ID of the user to be retrieved.
     */
    fun getUser(userId: String) {
        viewModelScope.launch {
            val user = userRepository.getUser(userId)
            currentUser.value = user
            user?.let {
                SessionManager.setRole(it.role)
                Log.d("UserViewModel", "User Role: ${it.role}")
            }
        }
    }

    /**
     * Retrieves all users and calculates the total organization balance.
     */
    fun getAllUsers() {
        viewModelScope.launch {
            val users = userRepository.getAllUsers()
            allUsers.value = users
            orgBalance.value = users.sumOf { it.currentBalance ?: 0 }
        }
    }

    /**
     * Retrieves the current balance of a user by ID.
     * @param userId The ID of the user whose balance is to be retrieved.
     */
    fun getUserCurrentBalance(userId: String) {
        viewModelScope.launch {
            val balance = userRepository.getUserBalance(userId)
            userBalance.value = balance
        }
    }
}
