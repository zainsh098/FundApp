package com.example.fundapp.repository

import com.example.fundapp.model.User
import com.example.fundapp.remote.FirebaseDataSource

/**
 * Repository class for managing user data operations.
 * @property dataSource The data source for accessing Firebase data.
 */
class UserRepository(private val dataSource: FirebaseDataSource) {

    /**
     * Saves a user to the data source.
     * @param user The user to be saved.
     */
    suspend fun saveUser(user: User) {
        dataSource.saveUser(user)
    }

    /**
     * Retrieves a user by their ID.
     * @param userId The ID of the user to be retrieved.
     * @return The user if found, otherwise null.
     */
    suspend fun getUser(userId: String): User? {
        return dataSource.getUser(userId)
    }

    /**
     * Retrieves the balance of a user.
     * @param userId The ID of the user whose balance is to be retrieved.
     * @return The user balance if available, otherwise null.
     */
    suspend fun getUserBalance(userId: String): Double? {
        return dataSource.getUserCurrentBalance(userId)
    }

    /**
     * Retrieves all users from the data source.
     * @return A list of all users.
     */
    suspend fun getAllUsers(): List<User> {
        return dataSource.getAllUsers()
    }
}
