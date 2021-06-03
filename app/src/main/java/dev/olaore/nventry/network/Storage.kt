package dev.olaore.nventry.network

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.io.File

object Storage {

    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }
    private val storageReference = storage.reference
    private val businessImagesReference = storageReference.child("businessImages")
    private val productImagesReference = storageReference.child("productImages")

    fun uploadBusinessImage(businessName: String, fileId: String, fileUri: Uri): Pair<UploadTask, StorageReference> {
        val reference = businessImagesReference.child("$businessName-$fileId")
        return reference.putFile(fileUri) to reference
    }

    fun uploadProductImage(productName: String, fileId: String, fileUri: Uri): Pair<UploadTask, StorageReference> {
        val reference = productImagesReference.child("$productName-$fileId")
        return reference.putFile(fileUri) to reference
    }

}