package dev.olaore.nventry.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.olaore.nventry.models.database.SharedProduct
import dev.olaore.nventry.models.domain.Business
import dev.olaore.nventry.models.network.NetworkBusiness
import dev.olaore.nventry.models.network.Product
import dev.olaore.nventry.network.Resource
import dev.olaore.nventry.repositories.BusinessRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ProductViewModel(
    private var businessRepository: BusinessRepository
) : ViewModel() {

    var product = MutableLiveData<Resource<Product>>()
    var productUpdated = MutableLiveData<Resource<Boolean>>()
    var productDeleted = MutableLiveData<Resource<Boolean>>()
    var productId: String = ""

    fun getProduct() {

        product.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                businessRepository.getProduct(productId).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val lProduct = it.result?.documents?.map {  doc ->
                            doc.toObject(Product::class.java)
                        }!![0]
                        product.postValue(Resource.success(lProduct!!))
                    } else {
                        product.postValue(Resource.error("Error occurred: ${ it.exception?.message }"))
                    }
                }
            } catch (ex: Exception) {
                product.postValue(Resource.error("Error occurred: ${ ex.message }"))
            }
        }

    }

    fun updateQuantity(quantity: Int) {

        this.productUpdated.postValue(Resource.loading())

        viewModelScope.launch(Dispatchers.IO) {
            try {
                businessRepository.updateProductQuantity(productId, quantity).addOnCompleteListener {
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

    fun deleteProduct(productId: String) {
        productDeleted.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                businessRepository.deleteProduct(productId).addOnCompleteListener {
                    if (it.isSuccessful) {
                        productDeleted.postValue(Resource.success(true))
                    } else {
                        productDeleted.postValue(Resource.error(it.exception?.message!!))
                    }
                }
            } catch (ex: Exception) {
                productDeleted.postValue(Resource.error(ex.message!!))
            }
        }

    }

    // ViewModel logic to save sharedproduct
    fun saveSharedProduct(name: String, imageUrl: String, sharingText: String) {
        val sharedProduct = SharedProduct(
            UUID.randomUUID().toString(), name, System.currentTimeMillis(), imageUrl, sharingText
        )

        viewModelScope.launch {
            businessRepository.saveSharedProduct(sharedProduct)
        }
    }

}