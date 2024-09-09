package com.example.fundapp.userrole

import android.util.Log

object SessionManager {
    private var currentUserRole: String? = null

    fun setRole(role: String?) {
        currentUserRole = role
        Log.d("SessionManager", "Role set to: $currentUserRole")
    }

    fun getRole(): String? {
        return currentUserRole
    }
}
