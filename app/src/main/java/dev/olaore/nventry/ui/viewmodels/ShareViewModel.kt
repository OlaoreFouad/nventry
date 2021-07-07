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

    var sharedProducts = MutableLiveData<List<SharedProduct>>()

    fun getSharedProducts() {

        viewModelScope.launch {
            sharedProducts.postValue(
                businessRepository.getSharedProducts()
            )
        }

    }

}