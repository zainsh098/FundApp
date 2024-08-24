package com.example.fundapp.remote

import com.example.fundapp.model.TransactionUser
import com.example.fundapp.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TransactionDataSource(private val firestore: FirebaseFirestore) {

    suspend fun depositAmount(transaction: TransactionUser) {
        try {
            firestore.collection("transactions").document(transaction.transactionId)
                .set(transaction).await()
            println("Deposit transaction added successfully: $transaction")
        } catch (e: Exception) {
            // Log or handle the exception
            println("Error adding deposit transaction: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun withdrawAmount(transaction: TransactionUser) {
        try {
            firestore.collection("transactions").document(transaction.transactionId)
                .set(transaction).await()
            println("Withdraw transaction added successfully: $transaction")
        } catch (e: Exception) {
            // Log or handle the exception
            println("Error adding withdraw transaction: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun getUser(userId: String): User? {
        return try {
            firestore.collection("users").document(userId).get().await()
                .toObject(User::class.java)
        } catch (e: Exception) {
            // Log or handle the exception
            println("Error fetching user: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    suspend fun updateUserBalance(user: User) {
        try {
            firestore.collection("users").document(user.userId).set(user).await()
            println("User balance updated successfully: $user")
        } catch (e: Exception) {
            // Log or handle the exception
            println("Error updating user balance: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun getTransactionHistory(userId: String): List<TransactionUser?> {
        return try {
            firestore.collection("transactions")
                .whereEqualTo("userId", userId)
                .get()
                .await()
                .documents
                .map { it.toObject(TransactionUser::class.java) }
        } catch (e: Exception) {
            // Log or handle the exception
            println("Error fetching transaction history: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }
}
