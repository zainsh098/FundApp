package com.example.fundapp.fragments.googlelogin.home

import com.example.fundapp.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

class HomeViewModel : UserViewModel() {

    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    private val dateNow = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    init {
        getUser(currentUserId)
        getAllUsers()
    }

    private fun setMessage(): String {
        return when (dateNow) {
            in 0..5 -> "Good Night"
            in 6..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..23 -> "Good Evening"
            else -> "Invalid time"
        }
    }
}