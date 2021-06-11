package dev.olaore.nventry.network

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dev.olaore.nventry.models.domain.Business
import dev.olaore.nventry.models.network.NetworkBusiness
import dev.olaore.nventry.models.network.NetworkUser
import dev.olaore.nventry.models.network.Product

object Network {

    private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }

    fun getRandomId(): String {
        return db.collection("users").document().id
    }

    fun saveUser(user: NetworkUser, id: String): Task<Void> {
        user.userId = id
        return db.collection("users").document(id).set(user)
    }

    fun getUser(email: String): Task<QuerySnapshot> {
        return db.collection("users").whereEqualTo("email", email).get()
    }

    fun saveBusiness(business: NetworkBusiness, id: String): Task<Void> {
        business.businessId = id
        return db.collection("businesses").document(id).set(business)
    }

    fun createProduct(product: Product, id: String): Task<Void> {
        product.id = id
        return db.collection("products").document(id).set(product);
    }

    fun getAllProducts(businessId: String): Task<QuerySnapshot> {
        return db.collection("products").whereEqualTo(
            "businessId", businessId
        ).get()
    }

    fun updateProduct(product: Product): Task<Void> {
        val productRef = db.collection("products").document(product.id)
        return productRef.update(mapOf(
            "category" to product.category,
            "description" to product.description,
            "sharingText" to product.sharingText,
            "price" to product.price,
            "name" to product.name,
            "quantity" to product.quantity,
            "imageUrls" to product.imageUrls
        ))
    }

    fun getProduct(id: String): Task<QuerySnapshot> {
        return db.collection("products").whereEqualTo("id", id).get()
    }

    fun updateProductQuantity(productId: String, newQuantity: Int): Task<Void> {
        val productRef = db.collection("products").document(productId)
        return productRef.update(mapOf(
            "quantity" to newQuantity
        ))
    }

    fun updateBusiness(business: Business): Task<Void> {
        val businessRef = db.collection("businesses").document(business.businessId)
        return businessRef.update(mapOf(
            "name" to business.name, "description" to business.description, "logoUrl" to business.logoUrl
        ))
    }

    fun getAllBusinesses(userId: String): Task<QuerySnapshot> {
        return db.collection("businesses").whereEqualTo(
            "userId", userId
        ).get()
    }

    fun getBusiness(id: String): Task<QuerySnapshot> {
        return db.collection("businesses").whereEqualTo("businessId", id).get()
    }

    fun deleteBusiness(businessId: String): Task<Void> {
        return db.collection("businesses").document(businessId).delete()
    }

}

class Resource<T>(
    var data: T?,
    var message: String? = null,
    var status: Status
) {

    companion object {

        fun <T> success(
            data: T
        ) = Resource<T>(data, status = Status.SUCCESS)

        fun <T> error(
            message: String
        ) = Resource<T>(null, message, Status.ERROR)

        fun <T> loading() = Resource<T>(null, status = Status.LOADING)

    }

}

enum class Status { LOADING, SUCCESS, ERROR }
