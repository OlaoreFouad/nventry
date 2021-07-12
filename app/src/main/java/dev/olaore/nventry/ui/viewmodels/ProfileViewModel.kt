package dev.olaore.nventry.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.olaore.nventry.models.database.DatabaseUser
import dev.olaore.nventry.repositories.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val usersRepository: UserRepository
) : ViewModel() {

    var username = MutableLiveData<String>()

    fun getUsername(id: String) {
        viewModelScope.launch {
            username.postValue(
                usersRepository.getUserFromDatabase(id).username
            )
        }
    }

}