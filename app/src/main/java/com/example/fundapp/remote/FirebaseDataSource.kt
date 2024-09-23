package com.example.fundapp.remote

import com.example.fundapp.constants.TransactionConstant
import com.example.fundapp.model.User
import com.example.fundapp.userrole.organizationManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Data source class for interacting with Firebase Firestore for user-related operations.
 * @property firestore The Firestore instance used to access the database.
 */
class FirebaseDataSource(private val firestore: FirebaseFirestore) {

    private  val getOrgName=organizationManager.getName()
    /**
     * Saves a user to the Firestore database.
     * @param user The user object containing user details to be saved.
     */

    suspend fun saveUser(user: User) {
        // Access the "O3" collection, then the "Users" collection, and create a document with userId containing the user details
        firestore.collection(getOrgName!!)
            .document(TransactionConstant.KEY_USERS)  // Access the "Users" document under the "O3" collection
            .collection(TransactionConstant.KEY_USERS_IDS)  // Access the "userIds" collection inside the "Users" document
            .document(user.userId)  // Create a document with the userId
            .set(user)  // Store the user details inside this document
            .await()  // Use await to make it a suspend function
    }

    /**
     * Retrieves a user from Firestore by their userId.
     * @param userId The ID of the user to retrieve.
     * @return The user object, or null if the user does not exist.
     */
    suspend fun getUser(userId: String): User? {
        // Fetch user data from the "userIds" collection under the "Users" document
        return firestore.collection(getOrgName!!).document(TransactionConstant.KEY_USERS).collection(TransactionConstant.KEY_USERS_IDS).document(userId)
            .get().await().toObject(User::class.java)
    }

    /**
     * Retrieves all users from Firestore.
     * @return A list of all user objects.
     */
    suspend fun getAllUsers(): List<User> {
        // Fetch all users from the "userIds" collection
        return firestore.collection(getOrgName!!).document(TransactionConstant.KEY_USERS).collection(TransactionConstant.KEY_USERS_IDS).get()
            .await().documents.mapNotNull { it.toObject(User::class.java) }
    }

    /**
     * Retrieves the current balance of a user.
     * @param userId The ID of the user whose balance is to be retrieved.
     * @return The current balance of the user, or null if not available.
     */
    suspend fun getUserCurrentBalance(userId: String): Double? {
        // Fetch current balance for a user from the "userIds" collection
        val documentSnapshot =
            firestore.collection(getOrgName!!).document(TransactionConstant.KEY_USERS).collection(TransactionConstant.KEY_USERS_IDS).document(userId)
                .get()
                .await()
        return documentSnapshot.getDouble("currentBalance")
    }
}
