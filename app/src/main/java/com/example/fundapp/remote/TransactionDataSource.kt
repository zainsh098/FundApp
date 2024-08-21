package com.example.fundapp.remote

import com.example.fundapp.model.TransactionUser
import com.example.fundapp.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TransactionDataSource(private val firestore: FirebaseFirestore) {

    suspend fun depositAmount(transaction: TransactionUser) {

        firestore.collection("transactions").document(transaction.transactionId).set(transaction)
            .await()
    }

    suspend fun getUser(userId: String): User? {
        return firestore.collection("users").document(userId).get().await()
            .toObject(User::class.java)
    }

    suspend fun updateUserBalance(user: User) {
        firestore.collection("users").document(user.userId).set(user).await()
    }


}
