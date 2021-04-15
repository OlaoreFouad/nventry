package dev.olaore.nventry.network

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object Auth {

    val auth by lazy {
        Firebase.auth
    }

}