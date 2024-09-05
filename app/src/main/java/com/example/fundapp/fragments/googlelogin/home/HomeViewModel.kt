package com.example.fundapp.fragments.googlelogin.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fundapp.model.User
import com.example.fundapp.userrole.SessionManager
import com.example.fundapp.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

class HomeViewModel : UserViewModel() {

    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    private val dateNow = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    private val _message: MutableLiveData<String> = MutableLiveData()
    val user = User()
    val message: LiveData<String> = _message


    init {
        getUser(currentUserId)
        getAllUsers()
        getUserCurrentBalance(currentUserId)
        SessionManager.setRole(user.role)
        setMessage()
    }

    private fun setMessage() {
        _message.value = when (dateNow) {
            in 0..5 -> "Good Night"
            in 6..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..23 -> "Good Evening"
            else -> "Invalid time"
        }
    }
}