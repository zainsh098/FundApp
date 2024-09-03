package com.example.fundapp.fragments.googlelogin.deposit

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundapp.model.TransactionUser
import com.example.fundapp.viewmodel.TransactionViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.util.Calendar
import java.util.UUID

class DepositViewModel : ViewModel() {


    private val storage = FirebaseStorage.getInstance()
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var dateLiveData: MutableLiveData<String> = MutableLiveData()
    val fileUriLiveData: MutableLiveData<Uri?> = MutableLiveData()
    private var transactionViewModel = TransactionViewModel()
    val isLoading = MutableLiveData<Boolean>()
    val depositSuccess = MutableLiveData<Boolean>()


    fun selectDate(context: Context) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                dateLiveData.value = date
            }, year, month, day
        )
        datePickerDialog.show()
    }

    fun uploadFileToFirestore(fileUri: Uri, onComplete: (String) -> Unit) {
        val storageRef = storage.reference.child("deposits/${UUID.randomUUID()}")
        val uploadTask = storageRef.putFile(fileUri)

        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                onComplete(uri.toString())
            }

        }
    }


    fun submitDeposit(
        depositAmount: Int,
        dateDepositAmount: String,
        selectedFileUri: Uri?,
        context: Context
    ) {
        if (selectedFileUri != null) {
            isLoading.value = true
            uploadFileToFirestore(selectedFileUri) { fileUrl ->
                val transaction = TransactionUser(
                    name = auth.currentUser!!.displayName!!,
                    transactionId = UUID.randomUUID().toString(),
                    userId = auth.currentUser!!.uid,
                    type = "deposit",
                    amount = depositAmount,
                    dateDeposit = dateDepositAmount,
                    proofOfDeposit = fileUrl,
                    photoUrl = auth.currentUser!!.photoUrl.toString(),
                )
                try {
                    transactionViewModel.submitDeposit(transaction)
                    depositSuccess.value = true
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Deposit failed. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()


                }
                isLoading.value = false

            }
        } else {
            Toast.makeText(
                context,
                "Please Select a File !",
                Toast.LENGTH_SHORT
            ).show()
            isLoading.value = false

        }
    }
}