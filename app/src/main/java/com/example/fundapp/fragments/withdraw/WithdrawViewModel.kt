package com.example.fundapp.fragments.withdraw

import android.app.DatePickerDialog
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fundapp.R
import com.example.fundapp.constants.TransactionConstant
import com.example.fundapp.extensions.percentOf
import com.example.fundapp.model.TransactionUser
import com.example.fundapp.viewmodel.TransactionViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar
import java.util.UUID

class WithdrawViewModel : TransactionViewModel() {

    var dateLiveData: MutableLiveData<String> = MutableLiveData()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val withdrawSuccess = MutableLiveData<Boolean>()
    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> get() = _successMessage
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun requestWithdrawal(
        currentBalance: Int,
        orgBalance: Int,
        withdrawAmount: Int,
        withdrawReason: String,
        dateWithdraw: String,
    ) {
        val transaction = TransactionUser(
            photoUrl = auth.currentUser!!.photoUrl.toString(),
            name = auth.currentUser!!.displayName!!,
            amount = withdrawAmount,
            reason = withdrawReason,
            date = dateWithdraw,
            type = TransactionConstant.KEY_WITHDRAW,
            transactionId = UUID.randomUUID().toString(),
            userId = auth.currentUser!!.uid,
            status = TransactionConstant.KEY_PENDING
        )
        val maxUserWithdrawal = currentBalance + (currentBalance.percentOf(50))
        if (withdrawAmount > maxUserWithdrawal) {
            _errorMessage.value = "Amount exceeds your allowable balance"
        } else if (withdrawAmount > orgBalance) {
            _errorMessage.value = "Withdrawal amount exceeds the organization's available balance"
        } else {
            withdrawAmount(transaction)
            _successMessage.value = "Withdrawal request is submitted"
            withdrawSuccess.value = true
        }
    }

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
}
