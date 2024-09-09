package com.example.fundapp.fragments.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class MenuViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private var _photoUrl: MutableLiveData<String> = MutableLiveData()
    val photoUrl: LiveData<String> = _photoUrl

    init {
        auth.currentUser?.photoUrl.let {
            _photoUrl.value = it.toString()


        }
    }
}