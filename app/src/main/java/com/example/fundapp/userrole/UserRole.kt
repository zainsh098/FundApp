package com.example.fundapp.userrole

import com.example.fundapp.viewmodel.UserViewModel

object SessionManager {
    private var currentUserRole: String = ""


    fun setRole(role: String) {

        currentUserRole =role

    }

    fun getRole(): String {

        return currentUserRole

    }
}