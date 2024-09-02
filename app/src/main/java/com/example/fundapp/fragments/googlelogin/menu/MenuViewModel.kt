package com.example.fundapp.fragments.googlelogin.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class MenuViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private var _photolUrl: MutableLiveData<String> = MutableLiveData()
    val photolUrl: LiveData<String> = _photolUrl

    init {
        auth.currentUser?.photoUrl.let {
            _photolUrl.value = it.toString()


        }
    }
}