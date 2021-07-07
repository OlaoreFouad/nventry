package dev.olaore.nventry.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.olaore.nventry.repositories.BusinessRepository
import dev.olaore.nventry.repositories.UserRepository
import java.lang.IllegalArgumentException

@Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
class ViewModelFactory(
    private val userRepository: UserRepository,
    private val businessRepository: BusinessRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            SignupViewModel::class.java -> SignupViewModel(userRepository)
            LoginViewModel::class.java -> LoginViewModel(userRepository)
            BusinessesViewModel::class.java -> BusinessesViewModel(userRepository, businessRepository)
            UpsertBusinessViewModel::class.java -> UpsertBusinessViewModel(userRepository, businessRepository)
            UpsertProductViewModel::class.java -> UpsertProductViewModel(userRepository, businessRepository)
            BusinessViewModel::class.java -> BusinessViewModel(userRepository, businessRepository)
            ProductsViewModel::class.java -> ProductsViewModel(userRepository, businessRepository)
            ProductViewModel::class.java -> ProductViewModel(businessRepository)
            ShareViewModel::class.java -> ShareViewModel(businessRepository)
            else -> throw IllegalArgumentException("Argument passed in must be of type ViewModel")
        } as T
    }

}