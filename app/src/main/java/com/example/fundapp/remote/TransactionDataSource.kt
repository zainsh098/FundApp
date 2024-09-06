package com.example.fundapp.remote

import com.example.fundapp.model.TransactionUser
import com.example.fundapp.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TransactionDataSource {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun getAllUsers(): List<User> {
        return firestore.collection("users").get().await().documents.mapNotNull {
            it.toObject(User::class.java)
        }
    }

    suspend fun getUser(userId: String): User? {
        return firestore.collection("users").document(userId).get().await().toObject(User::class.java)
    }

    suspend fun updateUserBalance(user: User) {
        firestore.collection("users").document(user.userId).set(user).await()
    }

    suspend fun depositAmount(transaction: TransactionUser) {
        firestore.collection("transactions").document(transaction.transactionId).set(transaction).await()
    }

    suspend fun withdrawAmount(transaction: TransactionUser) {
        firestore.collection("transactions").document(transaction.transactionId).set(transaction).await()
    }

    suspend fun getTransactionHistoryData(userId: String): List<TransactionUser> {
        return firestore.collection("transactions")
            .whereEqualTo("userId", userId)
            .get()
            .await()
            .documents.mapNotNull { it.toObject(TransactionUser::class.java) }
    }

    suspend fun getAllWithdrawRequests(userId: String): List<TransactionUser> {
        return firestore.collection("transactions")
            .whereEqualTo("userId", userId)
            .whereEqualTo("type", "withdraw")
            .whereEqualTo("status", "pending")
            .get()
            .await()
            .documents.mapNotNull { it.toObject(TransactionUser::class.java) }
    }

    suspend fun updateRequestStatus(transactionId: String, status: String) {
        firestore.collection("transactions").document(transactionId)
            .update("status", status).await()
    }
}
