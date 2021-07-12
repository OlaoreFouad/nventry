package dev.olaore.nventry.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.olaore.nventry.models.database.SharedProduct
import dev.olaore.nventry.repositories.BusinessRepository
import kotlinx.coroutines.launch

class ShareViewModel(
    private val businessRepository: BusinessRepository
) : ViewModel() {

    // variable to hold list of shared products
    var sharedProducts = MutableLiveData<List<SharedProduct>>()

    // retrieve shared products
    fun getSharedProducts() {

        viewModelScope.launch {
            // update state with list of shared products
            sharedProducts.postValue(
                businessRepository.getSharedProducts()
            )
        }

    }

}