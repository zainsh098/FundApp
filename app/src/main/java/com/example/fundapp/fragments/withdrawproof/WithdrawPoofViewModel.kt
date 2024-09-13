package com.example.fundapp.fragments.withdrawproof

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundapp.viewmodel.TransactionViewModel
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class WithdrawProofViewModel : ViewModel() {
    private val storage = FirebaseStorage.getInstance()

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> get() = _successMessage

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage
    private val transactionViewModel = TransactionViewModel()

    fun submitWithdraw(
        selectedFileUri: Uri?,
        transactionID: String,
        userID: String,
        withdrawAmount: Int
    ) {
        if (selectedFileUri == null) {
            _errorMessage.value = "Please attach a proof file."
            return
        }

        _loading.value = true
        uploadFileToFirestore(selectedFileUri) { fileUrl ->
            transactionViewModel.updateTransactionProof(transactionID, fileUrl)
            transactionViewModel.acceptRequest(transactionID, userID, withdrawAmount)
            _successMessage.value = "Transaction updated successfully."
            _loading.value = false
        }
    }

    private fun uploadFileToFirestore(fileUri: Uri, onComplete: (String) -> Unit) {
        val storageRef = storage.reference.child("withdraw/${UUID.randomUUID()}")
        val uploadTask = storageRef.putFile(fileUri)

        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                onComplete(uri.toString())
            }
        }.addOnFailureListener {
            _errorMessage.value = "File upload failed."
            _loading.value = false
        }
    }
}