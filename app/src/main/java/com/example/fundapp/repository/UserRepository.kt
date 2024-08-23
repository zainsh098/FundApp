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

    suspend fun getAllUserIds(): List<String> {
        return dataSource.getAllUserIds()
    }

//    suspend fun  getUserData(user: String):User{
//        return dataSource.getUserData(user)
//
//
//    }

    suspend fun getAllUsers(): List<User> {
        return dataSource.getAllUsers()
    }
}