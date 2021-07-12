package dev.olaore.nventry.ui.viewmodels

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.olaore.nventry.models.database.DatabaseUser
import dev.olaore.nventry.models.domain.FormErrors
import dev.olaore.nventry.models.domain.TemporaryUser
import dev.olaore.nventry.models.mappers.asDatabaseUser
import dev.olaore.nventry.models.network.NetworkUser
import dev.olaore.nventry.network.Auth
import dev.olaore.nventry.network.Network
import dev.olaore.nventry.network.Resource
import dev.olaore.nventry.repositories.UserRepository
import dev.olaore.nventry.utils.isValidEmail
import kotlinx.coroutines.launch

class SignupViewModel (
    val userRepository: UserRepository
) : ViewModel() {

    var username = MutableLiveData<String>("")
    var email = MutableLiveData<String>("")
    var password = MutableLiveData<String>("")

    var createdAccount = MutableLiveData<Resource<DatabaseUser>>()

    var enabled = MediatorLiveData<Boolean>().apply {
        addSource(username) {
            value = isFormValid()
        }
        addSource(email) {
            value = isFormValid()
        }
        addSource(password) {
            value = isFormValid()
        }
    }

    var formErrors = ObservableArrayList<FormErrors>()

    private fun isFormValid(): Boolean {
        formErrors.clear()
        if (username.value!!.isNullOrEmpty()) {
            formErrors.add(FormErrors.MISSING_NAME)
        }
        if (!isValidEmail(email.value!!) && email.value!!.isNotBlank()) {
            formErrors.add(FormErrors.INVALID_EMAIL)
        }
        if ((password.value!!.length < 8) && (password.value!!.isNotBlank())) {
            formErrors.add(FormErrors.INVALID_PASSWORD)
        }

        return formErrors.isEmpty()
    }

    // create account logic
    fun createAccount() {
        // create temporary user with details passed in from the UI
        val user = TemporaryUser(
            username.value!!, password.value!!, email.value!!, System.currentTimeMillis()
        )
        // update UI state to "LOADING"
        createdAccount.postValue(Resource.loading())
        viewModelScope.launch {
            try {
                // make call to firebase authentication to create user
                // based on the details entered
                Auth.auth.createUserWithEmailAndPassword(
                    user.email, user.password
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        // save user details if account creation succeeded
                        saveUserDetails(user)
                    } else {
                        // update UI with error if process failed
                        createdAccount.postValue(Resource.error(it.exception?.message!!))
                    }
                }
            } catch (ex: Exception) {
                // update UI state to "ERROR" with error message if exception occurs.
                createdAccount.postValue(Resource.error(ex.message!!))
            }
        }

    }

    private fun saveUserDetails(temporaryUser: TemporaryUser) {
        val userId = Auth.auth.currentUser!!.uid
        viewModelScope.launch {
            userRepository.addUser(temporaryUser, userId).addOnCompleteListener {
                if (it.isSuccessful) {
                    val createdUser = temporaryUser.asDatabaseUser(userId)
                    createdAccount.postValue(Resource.success(createdUser))
                } else {
                    createdAccount.postValue(Resource.error(it.exception?.message!!))
                }
            }
        }
    }

    fun saveUserDetailsToDatabase(databaseUser: DatabaseUser) {
        viewModelScope.launch {
            userRepository.addUserToDatabase(databaseUser)
        }
    }

}