package com.example.fundapp.remote

import com.example.fundapp.model.TransactionUser
import com.example.fundapp.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await

class TransactionDataSource(private val firestore: FirebaseFirestore) {

    private lateinit var listener: ListenerRegistration
    suspend fun depositAmount(transaction: TransactionUser) {
        firestore.collection("transactions").document(transaction.transactionId)
            .set(transaction).await()
    }

    suspend fun withdrawAmount(transaction: TransactionUser) {
        firestore.collection("transactions").document(transaction.transactionId)
            .set(transaction).await()
    }

    suspend fun getUser(userId: String): User? {
        return firestore.collection("users").document(userId).get().await()
            .toObject(User::class.java)
    }

    suspend fun updateUserBalance(user: User) {
        firestore.collection("users").document(user.userId).set(user).await()
    }

    suspend fun getTransactionHistory(userId: String): List<TransactionUser?> {
        return firestore.collection("transactions")
            .whereEqualTo("userId", userId)
            .get()
            .await()
            .documents
            .map { it.toObject(TransactionUser::class.java) }
    }

    // Function to add a snapshot listener for real-time updates
  fun addTransactionListener(userId: String, onTransactionChanged: (List<TransactionUser?>) -> Unit) {
        listener = firestore.collection("transactions")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Handle the error
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val transactions = snapshot.documents.map { it.toObject(TransactionUser::class.java) }
                    onTransactionChanged(transactions)
                } else {
                    onTransactionChanged(emptyList())
                }
            }
    }
    fun removeTransactionListener() {
        if (::listener.isInitialized) {
            listener.remove()
        }
    }
}
