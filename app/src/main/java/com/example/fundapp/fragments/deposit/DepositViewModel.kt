package com.example.fundapp.fragments.deposit

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fundapp.R
import com.example.fundapp.constants.TransactionConstant
import com.example.fundapp.model.TransactionUser
import com.example.fundapp.viewmodel.TransactionViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.util.Calendar
import java.util.UUID

/**
 * ViewModel for handling deposit operations.
 *
 * This ViewModel manages the state and business logic for deposit operations,
 * including selecting a date, uploading files, and submitting deposit requests.
 */
class DepositViewModel : TransactionViewModel() {

    private val storage = FirebaseStorage.getInstance()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var dateLiveData: MutableLiveData<String> = MutableLiveData()
    val fileUriLiveData: MutableLiveData<Uri?> = MutableLiveData()

    private val _depositSuccess = MutableLiveData<Boolean>()
    val depositSuccess: LiveData<Boolean> get() = _depositSuccess

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> get() = _successMessage

    /**
     * Opens a date picker dialog for the user to select a date.
     *
     * @param context The context in which the date picker dialog is shown.
     */
    fun selectDate(context: Context) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            R.style.DialogTheme,
            { _, selectedYear, selectedMonth, selectedDay ->
                val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                dateLiveData.value = date
            }, year, month, day
        )
        datePickerDialog.show()
    }

    /**
     * Uploads a file to Firebase Storage and returns the download URL.
     *
     * @param fileUri The URI of the file to upload.
     * @param onComplete A callback function that receives the download URL of the uploaded file.
     */
    fun uploadFileToFirestore(fileUri: Uri, onComplete: (String) -> Unit) {
        val storageRef = storage.reference.child("deposits/${UUID.randomUUID()}")
        val uploadTask = storageRef.putFile(fileUri)

        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                onComplete(uri.toString())
            }
        }.addOnFailureListener {
            _errorMessage.value = "Failed to upload file"
        }
    }

    /**
     * Submits a deposit request.
     *
     * This method checks if a file is selected, uploads the file, and then creates
     * a `TransactionUser` object with the deposit details. It then attempts to submit
     * the deposit request and updates the ViewModel state based on success or failure.
     *
     * @param depositAmount The amount to deposit.
     * @param dateDepositAmount The date of the deposit.
     * @param selectedFileUri The URI of the selected file.
     * @param context The context used to show error messages.
     */
    fun submitDeposit(
        depositAmount: Int,
        dateDepositAmount: String,
        selectedFileUri: Uri?,
    ) {
        if (selectedFileUri != null) {
            isLoading.value = true
            uploadFileToFirestore(selectedFileUri) { fileUrl ->
                val transaction = TransactionUser(
                    name = auth.currentUser?.displayName ?: "Unknown",
                    transactionId = UUID.randomUUID().toString(),
                    userId = auth.currentUser?.uid ?: "",
                    type = TransactionConstant.KEY_DEPOSIT,
                    amount = depositAmount,
                    date = dateDepositAmount,
                    proofOfDeposit = fileUrl,
                    status = TransactionConstant.KEY_STATUS,
                    photoUrl = auth.currentUser?.photoUrl.toString()
                )
                try {
                    submitDeposit(transaction)
                    _depositSuccess.value = true
                    _successMessage.value = "Deposit request submitted"
                } catch (e: Exception) {
                    _errorMessage.value = "Deposit failed. Please try again."
                }
                isLoading.value = false
            }
        } else {
            _errorMessage.value = "Please select a file!"
        }
    }
}
