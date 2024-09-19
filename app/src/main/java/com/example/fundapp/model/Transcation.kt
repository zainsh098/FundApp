package com.example.fundapp.model

/**
 * Data class representing a transaction for a user.
 *
 * @property name The name of the user associated with the transaction.
 * @property transactionId A unique identifier for the transaction.
 * @property userId The ID of the user who initiated the transaction.
 * @property amount The amount of money involved in the transaction.
 * @property date The date when the transaction was initiated.
 * @property type The type of transaction (e.g., deposit, withdrawal).
 * @property status The current status of the transaction (e.g., pending, accepted, rejected).
 * @property photoUrl An optional URL to a photo associated with the transaction.
 * @property proofOfDeposit An optional URL to the proof of deposit for the transaction.
 * @property proofOfWithdraw An optional URL to the proof of withdrawal for the transaction.
 * @property reason An optional reason for the transaction.
 */
data class TransactionUser(
    val name: String = "",
    val transactionId: String = "",
    val userId: String = "",
    val amount: Int = 0,
    val date: String = "",
    val type: String = "",
    val status: String = "",
    val photoUrl: String? = null,
    val proofOfDeposit: String? = null,
    val proofOfWithdraw: String? = null,
    val reason: String? = null,
)
