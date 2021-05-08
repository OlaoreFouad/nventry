package dev.olaore.nventry.network

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dev.olaore.nventry.models.network.NetworkBusiness
import dev.olaore.nventry.models.network.NetworkUser

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

    fun getBusiness(id: String): Task<QuerySnapshot> {
        return db.collection("businesses").whereEqualTo("businessId", id).get()
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
