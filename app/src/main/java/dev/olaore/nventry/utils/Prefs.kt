package dev.olaore.nventry.utils

import android.content.Context

const val PRODUCT_NAME = "PRODUCT_NAME"
const val PRODUCT_IMAGE = "PRODUCT_IMAGE"
const val PRODUCT_SHARING_TEXT = "PRODUCT_SHARING_TEXT"

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

    fun isUserAuthenticated(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(AUTHENTICATED_KEY, false)
    }

    fun getUserId(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE)
        return sharedPreferences.getString(ID_KEY, null)
    }

    fun saveProductDetails(context: Context, name: String, sharingText: String, imageUrl: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString(PRODUCT_IMAGE, imageUrl)
        editor.putString(PRODUCT_NAME, name)
        editor.putString(PRODUCT_SHARING_TEXT, sharingText)

        editor.apply()

    }

    fun getSharedProductDetails(context: Context): TemporaryDetails {
        val sharedPreferences = context.getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE)
        return TemporaryDetails(
            sharedPreferences.getString(PRODUCT_NAME, "")!!,
            sharedPreferences.getString(PRODUCT_IMAGE, "")!!,
            sharedPreferences.getString(PRODUCT_SHARING_TEXT, "")!!
        )
    }

    fun clearSharedProducts(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.remove(PRODUCT_SHARING_TEXT)
        editor.remove(PRODUCT_IMAGE)
        editor.remove(PRODUCT_NAME)
    }
}

data class TemporaryDetails(
    val name: String, val image: String, val sharingText: String
)