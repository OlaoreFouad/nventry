package dev.olaore.nventry.ui.viewmodels

import androidx.lifecycle.ViewModel
import dev.olaore.nventry.repositories.BusinessRepository
import dev.olaore.nventry.repositories.UserRepository

class BusinessesViewModel(
    val userRepository: UserRepository,
    val businessRepository: BusinessRepository
) : ViewModel() {



}