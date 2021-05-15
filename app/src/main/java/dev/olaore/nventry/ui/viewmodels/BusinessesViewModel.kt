package dev.olaore.nventry.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import dev.olaore.nventry.models.domain.Business
import dev.olaore.nventry.models.network.NetworkBusiness
import dev.olaore.nventry.network.Auth
import dev.olaore.nventry.network.Resource
import dev.olaore.nventry.repositories.BusinessRepository
import dev.olaore.nventry.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BusinessesViewModel(
    private val userRepository: UserRepository,
    private val businessRepository: BusinessRepository
) : ViewModel() {

    val businesses = MutableLiveData<Resource<List<Business>>>()
    val onBusinessDeleted = MutableLiveData<Resource<Boolean>>()

    init {
        getAllBusinesses()
    }

    fun deleteBusiness(businessId: String) {

        onBusinessDeleted.postValue(Resource.loading())

        viewModelScope.launch(Dispatchers.IO) {
            try {
                businessRepository.deleteBusiness(businessId)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            onBusinessDeleted.postValue(Resource.success(true))
                        } else {
                            onBusinessDeleted.postValue(Resource.error(it.exception?.message!!))
                        }
                    }
            } catch(ex: Exception) {
                onBusinessDeleted.postValue(Resource.error(ex.message!!))
            }
        }
    }

    private fun getAllBusinesses() {

        businesses.postValue(Resource.loading())

        viewModelScope.launch(Dispatchers.IO) {

            try {

                businessRepository.getAllBusinesses(
                    Auth.auth.currentUser!!.uid
                ).addOnCompleteListener {
                    if (it.isSuccessful) {

                        val response: List<Business> = it.result?.documents?.map {  doc ->
                            doc.toObject(NetworkBusiness::class.java)!!
                        }!!.map { networkBusiness -> Business(networkBusiness) }

                        businesses.postValue(Resource.success(response))


                    } else {
                        businesses.postValue(Resource.error(it.exception?.message!!))
                    }
                }

            } catch (ex: Exception) {
                businesses.postValue(Resource.error(ex.message!!))
            }

        }

    }

    fun refresh() {
        this.getAllBusinesses()
    }

}