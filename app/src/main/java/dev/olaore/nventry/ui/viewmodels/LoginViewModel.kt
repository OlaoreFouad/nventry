package dev.olaore.nventry.ui.viewmodels

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.rpc.context.AttributeContext
import dev.olaore.nventry.models.database.DatabaseUser
import dev.olaore.nventry.models.domain.FormErrors
import dev.olaore.nventry.models.domain.TemporaryUser
import dev.olaore.nventry.models.mappers.asDatabaseUser
import dev.olaore.nventry.models.network.NetworkUser
import dev.olaore.nventry.network.Auth
import dev.olaore.nventry.network.Resource
import dev.olaore.nventry.repositories.UserRepository
import dev.olaore.nventry.utils.Prefs
import dev.olaore.nventry.utils.isValidEmail
import kotlinx.coroutines.launch

/*
* 1. check if id and authenticated is set to true
* 2. if so, retrieve user from database and fill inputs
* 3. on-click, manage login process
* 4. get current user details (by email)
* 5. save user details to database
* */

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    var email = MutableLiveData<String>("")
    var password = MutableLiveData<String>("")
    lateinit var databaseUser: DatabaseUser

    var loggedInUser = MutableLiveData<Resource<DatabaseUser>>()

    var enabled = MediatorLiveData<Boolean>().apply {
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
        if (!isValidEmail(email.value!!) && email.value!!.isNotBlank()) {
            formErrors.add(FormErrors.INVALID_EMAIL)
        }
        if ((password.value!!.length < 8) && (password.value!!.isNotBlank())) {
            formErrors.add(FormErrors.INVALID_PASSWORD)
        }

        return formErrors.isEmpty()
    }

    fun login() {
        Log.d("LoginViewModel", "Email: ${ email.value }, Password: ${ password.value }")

        loggedInUser.postValue(Resource.loading())

        try {

            Auth.auth.signInWithEmailAndPassword(email.value!!, password.value!!)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        getUserDetails(email.value!!)
                    } else {
                        loggedInUser.postValue(Resource.error("Error occurred while trying to log you in: " + it.exception?.message))
                    }
                }

        } catch (ex: Exception) {
            loggedInUser.postValue(Resource.error("Error occurred while trying to log you in: " + ex.message))
        }

    }

    private fun getUserDetails(email: String) {
        viewModelScope.launch {
            userRepository.getUser(email).addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    loggedInUser.postValue(Resource.error("Error Occurred: User does not exist"))
                } else {
                    val user: NetworkUser = documents.first().toObject(NetworkUser::class.java)
                    val dbUser = user.asDatabaseUser(user.userId, databaseUser.password)

                    loggedInUser.postValue(Resource.success(dbUser))
                }
            }.addOnFailureListener {
                loggedInUser.postValue(Resource.error(it.message!!))
            }
        }
    }

    fun getDatabaseUser(id: String) {
        viewModelScope.launch {
            databaseUser = userRepository.getUserFromDatabase(id)
            refreshInputs()
        }
    }

    fun saveUserDetailsToDatabase(databaseUser: DatabaseUser) {
        viewModelScope.launch {
            userRepository.addUserToDatabase(databaseUser)
        }
    }

    private fun refreshInputs() {
        if (databaseUser.email != null) {
            email.postValue(databaseUser.email)
        }
    }

}