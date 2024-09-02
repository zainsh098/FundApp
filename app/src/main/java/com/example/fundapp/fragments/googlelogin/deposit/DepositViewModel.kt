package com.example.fundapp.fragments.googlelogin.deposit

import android.app.AlertDialog
import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.fundapp.databinding.AlertDialogBinding
import com.example.fundapp.model.TransactionUser
import com.example.fundapp.viewmodel.TransactionViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.util.Calendar
import java.util.UUID

class DepositViewModel(application: Application) : AndroidViewModel(application) {


    private val storage = FirebaseStorage.getInstance()
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var dateLiveData: MutableLiveData<String> = MutableLiveData()
    val fileUriLiveData: MutableLiveData<Uri?> = MutableLiveData()
    private var transactionViewModel = TransactionViewModel()
    private lateinit var binding: AlertDialogBinding


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
        }.addOnFailureListener {
            Toast.makeText(
                getApplication(),
                "Some Exception Caused.File Not Uploaded",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    fun submitDeposit(
        depositAmount: Int,
        dateDepositAmount: String,
        selectedFileUri: Uri?,
        context: Context
    ) {
        if (selectedFileUri != null) {
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
                    showDepositSuccessDialog(context)
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Deposit failed. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(
                context,
                "Please Select a File !",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun showDepositSuccessDialog(context: Context) {
        binding = AlertDialogBinding.inflate(LayoutInflater.from(context))
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(binding.root)
        val dialog = dialogBuilder.create()
        dialog.show()
        binding.txtCloseButton.setOnClickListener {
            dialog.dismiss()

        }
    }
}