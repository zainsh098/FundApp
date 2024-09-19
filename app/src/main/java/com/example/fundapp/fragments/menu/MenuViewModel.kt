package com.example.fundapp.fragments.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * ViewModel for the MenuFragment.
 *
 * Manages data related to the user's photo URL.
 */
class MenuViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _photoUrl: MutableLiveData<String> = MutableLiveData()
    val photoUrl: LiveData<String> = _photoUrl

    init {
        // Initialize photo URL from Firebase Auth user
        auth.currentUser?.photoUrl?.let {
            _photoUrl.value = it.toString()
        }
    }
}
