package dev.olaore.nventry.network

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.io.File

object Storage {

    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }
    private val storageReference = storage.reference

    fun uploadImage(businessName: String, fileId: String, fileUri: Uri, extension: String): UploadTask {
        storageReference.child("businessImages/$businessName-$fileId.$extension")
        return storageReference.putFile(fileUri)
    }

}