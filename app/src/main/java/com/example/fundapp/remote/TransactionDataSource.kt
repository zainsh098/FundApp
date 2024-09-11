package com.example.fundapp.remote

import com.example.fundapp.constants.TransactionConstant
import com.example.fundapp.model.TransactionUser
import com.example.fundapp.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class TransactionDataSource {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun getAllUsers(): List<User> {
        return firestore.collection(TransactionConstant.KEY_USERS).get()
            .await().documents.mapNotNull {
                it.toObject(User::class.java)
            }
    }

    suspend fun getUser(userId: String): User? {
        return firestore.collection(TransactionConstant.KEY_USERS).document(userId).get().await()
            .toObject(User::class.java)
    }

    suspend fun updateUserBalance(user: User) {
        firestore.collection(TransactionConstant.KEY_USERS).document(user.userId).set(user).await()
    }

    suspend fun depositAmount(transaction: TransactionUser) {
        firestore.collection(TransactionConstant.KEY_TRANSACTIONS)
            .document(transaction.transactionId).set(transaction)
            .await()
    }

    suspend fun withdrawAmount(transaction: TransactionUser) {
        firestore.collection(TransactionConstant.KEY_TRANSACTIONS)
            .document(transaction.transactionId).set(transaction)
            .await()
    }

    suspend fun getTransactionHistoryData(userId: String): List<TransactionUser> {
        return firestore.collection(TransactionConstant.KEY_TRANSACTIONS)
            .whereEqualTo(TransactionConstant.USER_ID, userId)
            .get()
            .await()
            .documents.mapNotNull { it.toObject(TransactionUser::class.java) }
    }

    suspend fun getAllWithdrawRequests(userId: String): List<TransactionUser> {
        return firestore.collection(TransactionConstant.KEY_TRANSACTIONS)
            .whereEqualTo(TransactionConstant.USER_ID, userId)
            .whereEqualTo(TransactionConstant.KEY_TYPE, TransactionConstant.KEY_WITHDRAW)
            .whereEqualTo(TransactionConstant.KEY_STATUS, TransactionConstant.KEY_PENDING)
            .get()
            .await()
            .documents.mapNotNull { it.toObject(TransactionUser::class.java) }
    }


    suspend fun getAllDepositRequest(userId: String): List<TransactionUser> {
        return firestore.collection(TransactionConstant.KEY_TRANSACTIONS)
            .whereEqualTo(TransactionConstant.USER_ID, userId)
            .whereEqualTo(TransactionConstant.KEY_TYPE, TransactionConstant.KEY_DEPOSIT)
            .whereEqualTo(TransactionConstant.KEY_STATUS, TransactionConstant.KEY_PENDING)
            .get()
            .await()
            .documents.mapNotNull { it.toObject(TransactionUser::class.java) }


    }


    suspend fun updateRequestStatus(transactionId: String, status: String) {
        firestore.collection(TransactionConstant.KEY_TRANSACTIONS).document(transactionId)
            .update(TransactionConstant.KEY_STATUS, status).await()
    }

    suspend fun updateTransaction(transactionId: String, withdrawProof: String) {
        firestore.collection(TransactionConstant.KEY_TRANSACTIONS).document(transactionId)
            .update(TransactionConstant.KEY_PROOF_WITHDRAW, withdrawProof).await()


    }

}
