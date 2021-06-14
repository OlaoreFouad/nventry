package dev.olaore.nventry.repositories

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import dev.olaore.nventry.database.NventryDatabase
import dev.olaore.nventry.models.domain.Business
import dev.olaore.nventry.models.network.NetworkBusiness
import dev.olaore.nventry.models.network.Product
import dev.olaore.nventry.network.Network
import dev.olaore.nventry.network.Storage

class BusinessRepository(
    val database: NventryDatabase
) {

    suspend fun uploadBusinessImage(businessName: String, fileUri: Uri, fileId: String): Pair<UploadTask, StorageReference> {
        return Storage.uploadBusinessImage(businessName, fileId, fileUri)
    }

    suspend fun uploadProductImage(productName: String, fileUri: Uri, fileId: String): Pair<UploadTask, StorageReference> {
        return Storage.uploadProductImage(productName, fileId, fileUri)
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

    suspend fun deleteBusiness(businessId: String): Task<Void> {
        return Network.deleteBusiness(businessId)
    }

    suspend fun getAllProducts(businessId: String): Task<QuerySnapshot> {
        return Network.getAllProducts(businessId)
    }

    suspend fun createProduct(product: Product, id: String): Task<Void> {
        return Network.createProduct(product, id)
    }

    suspend fun updateProduct(product: Product): Task<Void> {
        return Network.updateProduct(product)
    }

    suspend fun updateProductQuantity(productId: String, newQuantity: Int): Task<Void> {
        return Network.updateProductQuantity(productId, newQuantity)
    }

    suspend fun getProduct(id: String): Task<QuerySnapshot> {
        return Network.getProduct(id)
    }

    suspend fun deleteProduct(productId: String): Task<Void> {
        return Network.deleteProduct(productId)
    }

}