package dev.olaore.nventry.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.olaore.nventry.models.domain.Business
import dev.olaore.nventry.models.network.NetworkBusiness
import dev.olaore.nventry.models.network.Product
import dev.olaore.nventry.network.Resource
import dev.olaore.nventry.repositories.BusinessRepository
import dev.olaore.nventry.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val userRepository: UserRepository,
    private val businessRepository: BusinessRepository
) : ViewModel() {

    var products = MutableLiveData<Resource<List<Product>>>()

    fun getAllProducts(businessId: String) {
        products.postValue(Resource.loading())

        viewModelScope.launch(Dispatchers.IO) {
            try {
                businessRepository.getAllProducts(businessId).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val response: List<Product> = it.result?.documents?.map { doc ->
                            doc.toObject(Product::class.java)!!
                        }!!.map { product -> product }
                        products.postValue(Resource.success(response))
                    } else {
                        products.postValue(Resource.error(it.exception?.message!!))
                    }
                }
            } catch (ex: Exception) {
                products.postValue(Resource.error("Error occurred: " + ex.message))
            }
        }

    }

}