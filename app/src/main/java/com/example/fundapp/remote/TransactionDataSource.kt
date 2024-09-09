package com.example.fundapp.remote

import com.example.fundapp.model.TransactionUser
import com.example.fundapp.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class TransactionDataSource {

    companion object {
        private const val KEY_PROOF_WITHDRAW = "proofOfWithdraw"
        private const val USER_ID = "userId"
        private const val KEY_WITHDRAW = "withdraw"
        private const val KEY_PENDING = "pending"
        private const val KEY_TYPE = "type"
        private const val KEY_STATUS = "status"
        private const val KEY_USERS = "users"
        private const val KEY_TRANSACTIONS = "transactions"
    }

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun getAllUsers(): List<User> {
        return firestore.collection(KEY_USERS).get().await().documents.mapNotNull {
            it.toObject(User::class.java)
        }
    }

    suspend fun getUser(userId: String): User? {
        return firestore.collection(KEY_USERS).document(userId).get().await()
            .toObject(User::class.java)
    }

    suspend fun updateUserBalance(user: User) {
        firestore.collection(KEY_USERS).document(user.userId).set(user).await()
    }

    suspend fun depositAmount(transaction: TransactionUser) {
        firestore.collection(KEY_TRANSACTIONS).document(transaction.transactionId).set(transaction)
            .await()
    }

    suspend fun withdrawAmount(transaction: TransactionUser) {
        firestore.collection(KEY_TRANSACTIONS).document(transaction.transactionId).set(transaction)
            .await()
    }

    suspend fun getTransactionHistoryData(userId: String): List<TransactionUser> {
        return firestore.collection(KEY_TRANSACTIONS)
            .whereEqualTo(USER_ID, userId)
            .get()
            .await()
            .documents.mapNotNull { it.toObject(TransactionUser::class.java) }
    }

    suspend fun getAllWithdrawRequests(userId: String): List<TransactionUser> {
        return firestore.collection(KEY_TRANSACTIONS)
            .whereEqualTo(USER_ID, userId)
            .whereEqualTo(KEY_TYPE, KEY_WITHDRAW)
            .whereEqualTo(KEY_STATUS, KEY_PENDING)
            .get()
            .await()
            .documents.mapNotNull { it.toObject(TransactionUser::class.java) }
    }

    suspend fun updateRequestStatus(transactionId: String, status: String) {
        firestore.collection(KEY_TRANSACTIONS).document(transactionId)
            .update(KEY_STATUS, status).await()
    }

    suspend fun updateTransaction(transactionId: String, withdrawProof: String) {
        firestore.collection(KEY_TRANSACTIONS).document(transactionId)
            .update(KEY_PROOF_WITHDRAW, withdrawProof).await()


    }

}
