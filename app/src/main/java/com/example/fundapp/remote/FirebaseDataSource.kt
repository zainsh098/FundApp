package com.example.fundapp.remote

import com.example.fundapp.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseDataSource(private val firestore: FirebaseFirestore) {

    suspend fun saveUser(user: User) {
        // Access the "O3" collection, then the "Users" collection, and create a document with userId containing the user details
        firestore.collection("O3")
            .document("Users")  // Access the "Users" document under the "O3" collection
            .collection("userIds")  // Access the "userIds" collection inside the "Users" document
            .document(user.userId)  // Create a document with the userId
            .set(user)  // Store the user details inside this document
            .await()  // Use await to make it a suspend function
    }

    suspend fun getUser(userId: String): User? {
        // Fetch user data from the "userIds" collection under the "Users" document
        return firestore.collection("O3").document("Users").collection("userIds").document(userId)
            .get().await().toObject(User::class.java)
    }

    suspend fun getAllUsers(): List<User> {
        // Fetch all users from the "userIds" collection
        return firestore.collection("O3").document("Users").collection("userIds").get()
            .await().documents.mapNotNull { it.toObject(User::class.java) }
    }

    suspend fun getUserCurrentBalance(userId: String): Double? {
        // Fetch current balance for a user from the "userIds" collection
        val documentSnapshot =
            firestore.collection("O3").document("Users").collection("userIds").document(userId)
                .get()
                .await()
        return documentSnapshot.getDouble("currentBalance")
    }
}
