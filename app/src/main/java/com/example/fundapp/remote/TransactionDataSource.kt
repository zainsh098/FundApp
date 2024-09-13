package com.example.fundapp.remote

import com.example.fundapp.constants.TransactionConstant
import com.example.fundapp.model.TransactionUser
import com.example.fundapp.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TransactionDataSource() {
    private val firestore: FirebaseFirestore=FirebaseFirestore.getInstance()
    // Fetch all users from "O3 -> Users -> userIds"
    suspend fun getAllUsers(): List<User> {
        return firestore.collection("O3").document("Users").collection("userIds")
            .get().await().documents.mapNotNull { it.toObject(User::class.java) }
    }

    // Fetch a specific user from "O3 -> Users -> userIds"
    suspend fun getUser(userId: String): User? {
        return firestore.collection("O3").document("Users").collection("userIds")
            .document(userId).get().await().toObject(User::class.java)
    }

    // Update the user's balance in "O3 -> Users -> userIds"
    suspend fun updateUserBalance(user: User) {
        firestore.collection("O3").document("Users").collection("userIds")
            .document(user.userId).set(user).await()
    }

    // Deposit a transaction under "O3 -> Transactions -> transactionId"
    suspend fun depositAmount(transaction: TransactionUser) {
        firestore.collection("O3").document("Transactions").collection("transactionIds")
            .document(transaction.transactionId).set(transaction).await()
    }

    // Withdraw a transaction under "O3 -> Transactions -> transactionId"
    suspend fun withdrawAmount(transaction: TransactionUser) {
        firestore.collection("O3").document("Transactions").collection("transactionIds")
            .document(transaction.transactionId).set(transaction).await()
    }

    // Get transaction history of a user
    suspend fun getTransactionHistoryData(userId: String): List<TransactionUser> {
        return firestore.collection("O3").document("Transactions").collection("transactionIds")
            .whereEqualTo(TransactionConstant.USER_ID, userId)
            .get().await().documents.mapNotNull { it.toObject(TransactionUser::class.java) }
    }

    // Get all pending withdrawal requests for a user
    suspend fun getAllWithdrawRequests(userId: String): List<TransactionUser> {
        return firestore.collection("O3").document("Transactions").collection("transactionIds")
            .whereEqualTo(TransactionConstant.USER_ID, userId)
            .whereEqualTo(TransactionConstant.KEY_TYPE, TransactionConstant.KEY_WITHDRAW)
            .whereEqualTo(TransactionConstant.KEY_STATUS, TransactionConstant.KEY_PENDING)
            .get().await().documents.mapNotNull { it.toObject(TransactionUser::class.java) }
    }

    // Get all pending deposit requests for a user
    suspend fun getAllDepositRequest(userId: String): List<TransactionUser> {
        return firestore.collection("O3").document("Transactions").collection("transactionIds")
            .whereEqualTo(TransactionConstant.USER_ID, userId)
            .whereEqualTo(TransactionConstant.KEY_TYPE, TransactionConstant.KEY_DEPOSIT)
            .whereEqualTo(TransactionConstant.KEY_STATUS, TransactionConstant.KEY_PENDING)
            .get().await().documents.mapNotNull { it.toObject(TransactionUser::class.java) }
    }

    // Update the status of a transaction
    suspend fun updateRequestStatus(transactionId: String, status: String) {
        firestore.collection("O3").document("Transactions").collection("transactionIds")
            .document(transactionId)
            .update(TransactionConstant.KEY_STATUS, status).await()
    }
    suspend fun updateDepositRequestStatus(transactionId: String, status: String) {
        firestore.collection("O3").document("Transactions").collection("transactionIds")
            .document(transactionId)
            .update(TransactionConstant.KEY_STATUS, status).await()
    }

    // Update the withdrawal proof for a transaction
    suspend fun updateTransaction(transactionId: String, withdrawProof: String) {
        firestore.collection("O3").document("Transactions").collection("transactionIds")
            .document(transactionId)
            .update(TransactionConstant.KEY_PROOF_WITHDRAW, withdrawProof).await()
    }
}
