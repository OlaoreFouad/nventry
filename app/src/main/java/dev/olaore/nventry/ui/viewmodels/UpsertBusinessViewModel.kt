package dev.olaore.nventry.ui.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.olaore.nventry.models.domain.Business
import dev.olaore.nventry.models.domain.UploadProcess
import dev.olaore.nventry.models.network.NetworkBusiness
import dev.olaore.nventry.network.Auth
import dev.olaore.nventry.network.Network
import dev.olaore.nventry.network.Resource
import dev.olaore.nventry.network.Storage
import dev.olaore.nventry.repositories.BusinessRepository
import dev.olaore.nventry.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpsertBusinessViewModel(
    private val userRepository: UserRepository,
    private val businessRepository: BusinessRepository
) : ViewModel() {

    var fileUploadComplete = MutableLiveData<Resource<UploadProcess>>()
    var businessCreated = MutableLiveData<Resource<Boolean>>()
    var businessUpdated = MutableLiveData<Resource<Boolean>>()

    init {

    }

    // create business logic
    fun createBusiness(
        businessName: String, businessDescription: String, logoUrl: String
    ) {
        // gather entered business details
        val newBusiness = NetworkBusiness(
            Network.getRandomId(),
            Auth.auth.currentUser!!.uid,
            businessName,
            businessDescription,
            logoUrl,
            createdOn = System.currentTimeMillis()
        )
        // update loading state on UI
        businessCreated.postValue(Resource.loading())

        viewModelScope.launch {
            try {
                // create business
                businessRepository.createBusiness(newBusiness).addOnCompleteListener {
                    if (it.isSuccessful) {
                        // respond with TRUE if business created successfully
                        businessCreated.postValue(Resource.success(true))
                    } else {
                        // respond with error message if process failed
                        businessCreated.postValue(Resource.error(it.exception?.message!!))
                    }
                }
            } catch (ex: Exception) {
                // respond with error message if process failed
                businessCreated.postValue(Resource.error(ex.message!!))
            }
        }
    }

    fun updateBusiness(
        business: Business
    ) {
        // update loading state on UI
        businessUpdated.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // call update business logic
                businessRepository.updateBusiness(business).addOnCompleteListener {
                    if (it.isSuccessful) {
                        // send TRUE if business update completed successfully
                        businessUpdated.postValue(Resource.success(true))
                    } else {
                        // send error message if exception occurred
                        businessUpdated.postValue(Resource.error(it.exception?.message!!))
                    }
                }
            } catch (ex: Exception) {
                // send error message if exception occurred
                businessUpdated.postValue(Resource.error(ex.message!!))
            }
        }
    }

    fun uploadImage(businessName: String, fileId: String, uri: Uri) {

        fileUploadComplete.postValue(Resource.loading())

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val uploadTask =
                    businessRepository.uploadBusinessImage(businessName, uri, fileId)
                val urlTask = uploadTask.first.continueWithTask {
                    if (!it.isSuccessful) {
                        it.exception?.let { ex ->
                            throw ex
                        }
                    }
                    uploadTask.second.downloadUrl
                }.addOnCompleteListener {
                    if (it.isSuccessful) {
                        val downloadUrl = it.result
                        fileUploadComplete.postValue(
                            Resource.success(UploadProcess(downloadUrl.toString(), ""))
                        )
                    } else {
                        it.exception?.let { ex ->
                            throw ex
                        }
                    }
                }
            } catch (ex: Exception) {
                fileUploadComplete.postValue(Resource.error(ex.message!!))
            }
        }
    }

}