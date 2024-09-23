package com.example.fundapp.remote

import android.util.Log
import com.example.fundapp.constants.TransactionConstant
import com.example.fundapp.model.TransactionUser
import com.example.fundapp.model.User
import com.example.fundapp.userrole.organizationManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Data source class for interacting with Firebase Firestore for transaction-related operations.
 * Provides methods to perform CRUD operations on transactions and user data stored in Firestore.
 */
class TransactionDataSource() {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val getOrgName = organizationManager.getName()


    /**
     * Retrieves all users from Firestore.
     *
     * Fetches all user documents from the "userIds" collection, which is nested under the "Users" document
     * in the "O3" collection.
     *
     * @return A list of all user objects.
     */
    suspend fun getAllUsers(): List<User> {
        return firestore.collection(getOrgName!!)
            .document(TransactionConstant.KEY_USERS)
            .collection(TransactionConstant.KEY_USERS_IDS)
            .get().await().documents.mapNotNull { it.toObject(User::class.java) }
    }

    /**
     * Retrieves a user from Firestore by their userId.
     *
     * Fetches the user document from the "userIds" collection, which is nested under the "Users" document
     * in the "O3" collection.
     *
     * @param userId The ID of the user to retrieve.
     * @return The user object, or null if the user does not exist.
     */
    suspend fun getUser(userId: String): User? {
        return firestore.collection(getOrgName!!)
            .document(TransactionConstant.KEY_USERS)
            .collection(TransactionConstant.KEY_USERS_IDS)
            .document(userId).get().await().toObject(User::class.java)
    }

    /**
     * Updates the balance of a user in Firestore.
     *
     * Sets the updated user data, including the current balance, in the "userIds" collection under the
     * "Users" document in the "O3" collection.
     *
     * @param user The user object containing updated balance information.
     */
    suspend fun updateUserBalance(user: User) {
        firestore.collection(getOrgName!!).document(TransactionConstant.KEY_USERS)
            .collection(TransactionConstant.KEY_USERS_IDS)
            .document(user.userId).set(user).await()
    }

    /**
     * Saves a deposit transaction to Firestore.
     *
     * Stores the transaction details in a document identified by the transactionId within the "transactionIds"
     * collection, which is nested under the "Transactions" document in the "O3" collection.
     *
     * @param transaction The transaction object containing deposit details to be saved.
     */
    suspend fun depositAmount(transaction: TransactionUser) {
        firestore.collection(getOrgName!!)
            .document(TransactionConstant.KEY_TRANSACTIONS)
            .collection(TransactionConstant.KEY_TRANSACTIONS_IDS)
            .document(transaction.transactionId).set(transaction).await()
    }

    /**
     * Saves a withdrawal transaction to Firestore.
     *
     * Stores the transaction details in a document identified by the transactionId within the "transactionIds"
     * collection, which is nested under the "Transactions" document in the "O3" collection.
     *
     * @param transaction The transaction object containing withdrawal details to be saved.
     */
    suspend fun withdrawAmount(transaction: TransactionUser) {
        firestore.collection(getOrgName!!)
            .document(TransactionConstant.KEY_TRANSACTIONS)
            .collection(TransactionConstant.KEY_TRANSACTIONS_IDS)
            .document(transaction.transactionId).set(transaction).await()
    }

    /**
     * Retrieves the transaction history for a specific user.
     *
     * Fetches all transactions associated with a userId from the "transactionIds" collection, which is
     * nested under the "Transactions" document in the "O3" collection.
     *
     * @param userId The ID of the user whose transaction history is to be retrieved.
     * @return A list of transaction objects related to the user.
     */
    suspend fun getTransactionHistoryData(userId: String): List<TransactionUser> {
        return firestore.collection(getOrgName!!)
            .document(TransactionConstant.KEY_TRANSACTIONS)
            .collection(TransactionConstant.KEY_TRANSACTIONS_IDS)
            .whereEqualTo(TransactionConstant.USER_ID, userId)
            .get().await().documents.mapNotNull { it.toObject(TransactionUser::class.java) }
    }

    /**
     * Retrieves all pending withdrawal requests for a specific user.
     *
     * Fetches all withdrawal transactions that are pending for the given userId from the "transactionIds"
     * collection, which is nested under the "Transactions" document in the "O3" collection.
     *
     * @param userId The ID of the user whose pending withdrawal requests are to be retrieved.
     * @return A list of pending withdrawal transaction objects.
     */
    suspend fun getAllWithdrawRequests(userId: String): List<TransactionUser> {
        return firestore.collection(getOrgName!!)
            .document(TransactionConstant.KEY_TRANSACTIONS)
            .collection(TransactionConstant.KEY_TRANSACTIONS_IDS)
            .whereEqualTo(TransactionConstant.USER_ID, userId)
            .whereEqualTo(TransactionConstant.KEY_TYPE, TransactionConstant.KEY_WITHDRAW)
            .whereEqualTo(TransactionConstant.KEY_STATUS, TransactionConstant.KEY_PENDING)
            .get().await().documents.mapNotNull { it.toObject(TransactionUser::class.java) }
    }

    /**
     * Retrieves all pending deposit requests for a specific user.
     *
     * Fetches all deposit transactions that are pending for the given userId from the "transactionIds"
     * collection, which is nested under the "Transactions" document in the "O3" collection.
     *
     * @param userId The ID of the user whose pending deposit requests are to be retrieved.
     * @return A list of pending deposit transaction objects.
     */
    suspend fun getAllDepositRequest(userId: String): List<TransactionUser> {
        Log.d("Name oRG ", getOrgName!!)

        return firestore.collection(getOrgName)
            .document(TransactionConstant.KEY_TRANSACTIONS)
            .collection(TransactionConstant.KEY_TRANSACTIONS_IDS)
            .whereEqualTo(TransactionConstant.USER_ID, userId)
            .whereEqualTo(TransactionConstant.KEY_TYPE, TransactionConstant.KEY_DEPOSIT)
            .whereEqualTo(TransactionConstant.KEY_STATUS, TransactionConstant.KEY_PENDING)
            .get().await().documents.mapNotNull {
                it.toObject(TransactionUser::class.java)
            }


    }


    /**
     * Updates the status of a specific transaction.
     *
     * Changes the status field of a transaction document identified by transactionId in the "transactionIds"
     * collection, which is nested under the "Transactions" document in the "O3" collection.
     *
     * @param transactionId The ID of the transaction whose status is to be updated.
     * @param status The new status to be set for the transaction.
     */
    suspend fun updateRequestStatus(transactionId: String, status: String) {
        firestore.collection(getOrgName!!)
            .document(TransactionConstant.KEY_TRANSACTIONS)
            .collection(TransactionConstant.KEY_TRANSACTIONS_IDS)
            .document(transactionId)
            .update(TransactionConstant.KEY_STATUS, status).await()
    }

    /**
     * Updates the deposit request status for a specific transaction.
     *
     * Changes the status field of a deposit request transaction document identified by transactionId in the
     * "transactionIds" collection, which is nested under the "Transactions" document in the "O3" collection.
     *
     * @param transactionId The ID of the transaction whose deposit request status is to be updated.
     * @param status The new status to be set for the deposit request.
     */
    suspend fun updateDepositRequestStatus(transactionId: String, status: String) {
        firestore.collection(getOrgName!!)
            .document(TransactionConstant.KEY_TRANSACTIONS)
            .collection(TransactionConstant.KEY_TRANSACTIONS_IDS)
            .document(transactionId)
            .update(TransactionConstant.KEY_STATUS, status).await()
    }

    /**
     * Updates the withdrawal proof for a specific transaction.
     *
     * Adds or updates the withdrawal proof URL for a transaction document identified by transactionId
     * in the "transactionIds" collection, which is nested under the "Transactions" document in the "O3" collection.
     *
     * @param transactionId The ID of the transaction whose proof URL is to be updated.
     * @param withdrawProof The URL of the withdrawal proof to be set.
     */
    suspend fun updateTransaction(transactionId: String, withdrawProof: String) {
        firestore.collection(getOrgName!!)
            .document(TransactionConstant.KEY_TRANSACTIONS)
            .collection(TransactionConstant.KEY_TRANSACTIONS_IDS)
            .document(transactionId)
            .update(TransactionConstant.KEY_PROOF_WITHDRAW, withdrawProof).await()
    }
}
