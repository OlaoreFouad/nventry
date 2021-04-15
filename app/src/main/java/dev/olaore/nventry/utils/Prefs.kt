package dev.olaore.nventry.utils

import android.content.Context

object Prefs {

    private const val PREFS_TAG = "nventry_prefs"
    private const val AUTHENTICATED_KEY = "key_authenticated"
    private const val ID_KEY = "key_id"

    fun saveAuthenticatedUser(context: Context, userId: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE)
        val sharedPreferencesEditor = sharedPreferences.edit()

        sharedPreferencesEditor.putBoolean(AUTHENTICATED_KEY, true)
        sharedPreferencesEditor.putString(ID_KEY, userId)
        sharedPreferencesEditor.apply()
    }

    fun removeAuthenticatedUser(context: Context, userId: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE)
        val sharedPreferencesEditor = sharedPreferences.edit()

        sharedPreferencesEditor.remove(AUTHENTICATED_KEY)
        sharedPreferencesEditor.remove(ID_KEY)
        sharedPreferencesEditor.apply()
    }

    fun isUserAuthenticated(context: Context, userId: String): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(AUTHENTICATED_KEY, false)
    }

    fun getUserId(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE)
        return sharedPreferences.getString(ID_KEY, null)
    }

}