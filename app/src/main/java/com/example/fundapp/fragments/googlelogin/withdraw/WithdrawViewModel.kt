package com.example.fundapp.fragments.googlelogin.withdraw

import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.fundapp.model.TransactionUser
import com.example.fundapp.viewmodel.TransactionViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar
import java.util.UUID

class WithdrawViewModel(application: Application) : AndroidViewModel(application) {
    var dateLiveData: MutableLiveData<String> = MutableLiveData()
    private var transactionViewModel = TransactionViewModel()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val withdrawSuccess = MutableLiveData<Boolean>()


    fun requestWithdrawal(
        withdrawAmount: Int,
        withdrawReason: String,
        dateWithdraw: String,
        context: Context
    ) {
        val transaction = TransactionUser(
            photoUrl = auth.currentUser!!.photoUrl.toString(),
            name = auth.currentUser!!.displayName!!,
            amount = withdrawAmount,
            reason = withdrawReason,
            dateWithdraw = dateWithdraw,
            type = "withdraw",
            transactionId = UUID.randomUUID().toString(),
            userId = auth.currentUser!!.uid,
            status = "pending"
        )
        transactionViewModel.withdrawAmount(transaction)
        Toast.makeText(context, "Withdrawal successful", Toast.LENGTH_SHORT).show()
        withdrawSuccess.value = true
    }


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

}