package dev.olaore.nventry.ui.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.olaore.nventry.models.domain.Business
import dev.olaore.nventry.models.domain.UploadProcess
import dev.olaore.nventry.models.network.NetworkBusiness
import dev.olaore.nventry.models.network.Product
import dev.olaore.nventry.network.Auth
import dev.olaore.nventry.network.Network
import dev.olaore.nventry.network.Resource
import dev.olaore.nventry.network.Storage
import dev.olaore.nventry.repositories.BusinessRepository
import dev.olaore.nventry.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpsertProductViewModel(
    private val userRepository: UserRepository,
    private val businessRepository: BusinessRepository
) : ViewModel() {

    var fileUploadComplete = MutableLiveData<Resource<UploadProcess>>()
    var productCreated = MutableLiveData<Resource<Boolean>>()
    var productUpdated = MutableLiveData<Resource<Boolean>>()

    var product = MutableLiveData<Product>(Product())

    fun createProduct(newProduct: Product) {

        productCreated.postValue(Resource.loading())

        viewModelScope.launch {
            try {
                businessRepository.createProduct(newProduct, Network.getRandomId()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        productCreated.postValue(Resource.success(true))
                    } else {
                        productCreated.postValue(Resource.error(it.exception?.message!!))
                    }
                }
            } catch (ex: Exception) {
                productCreated.postValue(Resource.error(ex.message!!))
            }
        }

    }

    fun updateProduct(
        product: Product
    ) {

        productUpdated.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                businessRepository.updateProduct(product).addOnCompleteListener {
                    if (it.isSuccessful) {
                        productUpdated.postValue(Resource.success(true))
                    } else {
                        productUpdated.postValue(Resource.error(it.exception?.message!!))
                    }
                }

            } catch (ex: Exception) {
                productUpdated.postValue(Resource.error(ex.message!!))
            }

        }

    }

    fun setProduct(prod: Product) {
        this.product.postValue(prod)
    }

    fun uploadImage(productName: String, fileId: String, uri: Uri) {

        fileUploadComplete.postValue(Resource.loading())

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val uploadTask = businessRepository.uploadProductImage(productName, uri, fileId)
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