package com.example.fundapp.remote

import com.example.fundapp.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseDataSource(private val firestore: FirebaseFirestore) {

    suspend fun saveUser(user: User) {
        firestore.collection("users").document(user.userId).set(user).await()
    }

    suspend fun getUser(userId: String): User? {
        return firestore.collection("users").document(userId).get().await()
            .toObject(User::class.java)
    }

    suspend fun getAllUsers(): List<User> {
        return firestore.collection("users").get()
            .await().documents.map { it.toObject(User::class.java)!! }
    }
}