package com.example.fundapp.userrole

import android.util.Log

/**
 * Manages user session and role information.
 */
object SessionManager {

    // Stores the current user's role
    private var currentUserRole: String? = null

    /**
     * Sets the role of the current user.
     * @param role The role to be set.
     */
    fun setRole(role: String?) {
        currentUserRole = role
        Log.d("SessionManager", "Role set to: $currentUserRole")
    }

    /**
     * Retrieves the role of the current user.
     * @return The current user role.
     */
    fun getRole(): String? {
        return currentUserRole
    }
}

object organizationManager {
    private var nameOrg: String? = null


    fun setName(name: String?) {
        nameOrg = name

    }

    fun getName(): String? {

        return nameOrg

    }


}
