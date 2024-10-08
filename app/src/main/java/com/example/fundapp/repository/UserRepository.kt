package com.example.fundapp.repository

import com.example.fundapp.model.User
import com.example.fundapp.remote.FirebaseDataSource

class UserRepository(private val dataSource: FirebaseDataSource) {

    suspend fun saveUser(user: User) {
        dataSource.saveUser(user)
    }
    suspend fun getUser(userId: String): User? {
        return dataSource.getUser(userId)
    }


    suspend fun getUserBalance(userId: String): Double? {
        return dataSource.getUserCurrentBalance(userId)
    }

    suspend fun getAllUsers(): List<User> {
        return dataSource.getAllUsers()
    }
}