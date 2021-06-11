package dev.olaore.nventry.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.olaore.nventry.models.domain.Business
import dev.olaore.nventry.models.network.NetworkBusiness
import dev.olaore.nventry.models.network.Product
import dev.olaore.nventry.network.Resource
import dev.olaore.nventry.repositories.BusinessRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(
    private var businessRepository: BusinessRepository
) : ViewModel() {

    var product = MutableLiveData<Resource<Product>>()
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

}