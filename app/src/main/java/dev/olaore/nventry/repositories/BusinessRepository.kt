package dev.olaore.nventry.repositories

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import dev.olaore.nventry.database.NventryDatabase
import dev.olaore.nventry.models.domain.Business
import dev.olaore.nventry.models.network.NetworkBusiness
import dev.olaore.nventry.network.Network
import dev.olaore.nventry.network.Storage

class BusinessRepository(
    val database: NventryDatabase
) {

    suspend fun uploadBusinessImage(businessName: String, fileUri: Uri, fileId: String): Pair<UploadTask, StorageReference> {
        return Storage.uploadBusinessImage(businessName, fileId, fileUri)
    }

    suspend fun createBusiness(networkBusiness: NetworkBusiness): Task<Void> {
        return Network.saveBusiness(networkBusiness, networkBusiness.businessId)
    }

    suspend fun getAllBusinesses(userId: String): Task<QuerySnapshot> {
        return Network.getAllBusinesses(userId)
    }

    suspend fun updateBusiness(business: Business): Task<Void> {
        return Network.updateBusiness(business)
    }

}