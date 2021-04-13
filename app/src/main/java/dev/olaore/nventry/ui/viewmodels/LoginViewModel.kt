package dev.olaore.nventry.ui.viewmodels

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.olaore.nventry.models.domain.FormErrors
import dev.olaore.nventry.repositories.UserRepository
import dev.olaore.nventry.utils.isValidEmail

class LoginViewModel(
    val userRepository: UserRepository
) : ViewModel() {

    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()

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
    }

}