package com.example.fundapp.helper

import android.content.Context
import android.content.SharedPreferences

class SharedPrefHelper(context: Context) {

    companion object {
        private const val PREF_NAME = "FundAppOrgName"
        private const val ORG_NAME_KEY = "organame"
    }
private   val sharedPref:SharedPreferences=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)

 fun saveOrganizationName(orgName:String)
 {

     sharedPref.edit().putString(ORG_NAME_KEY, orgName).apply()

 }
    fun getOrganizationName(): String? {
        return sharedPref.getString(ORG_NAME_KEY, null)
    }

}