package dev.olaore.nventry.ui.viewmodels

import androidx.lifecycle.ViewModel
import dev.olaore.nventry.repositories.BusinessRepository
import dev.olaore.nventry.repositories.UserRepository

class UpsertBusinessViewModel(
    private val userRepository: UserRepository,
    private val businessRepository: BusinessRepository
) : ViewModel() {

    init {

    }

}