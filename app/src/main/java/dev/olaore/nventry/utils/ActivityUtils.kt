package dev.olaore.nventry.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.olaore.nventry.NventryApplication
import dev.olaore.nventry.database.getUsersDatabase
import dev.olaore.nventry.repositories.UserRepository
import dev.olaore.nventry.ui.viewmodels.ViewModelFactory

fun <T : ViewModel> Fragment.obtainViewModel(modelClass: Class<T>): T {

    val userRepository = ((requireActivity().application) as NventryApplication).userRepository
    val businessRepository = ((requireActivity().application) as NventryApplication).businessRepository

    val viewModelFactory = ViewModelFactory(userRepository, businessRepository)

    return ViewModelProvider(this, viewModelFactory).get(modelClass)

}

fun <T : ViewModel> AppCompatActivity.obtainViewModel(modelClass: Class<T>): T {

    val userRepository = (this.application as NventryApplication).userRepository
    val businessRepository = (this.application as NventryApplication).businessRepository

    val viewModelFactory = ViewModelFactory(userRepository, businessRepository)

    return ViewModelProvider(this, viewModelFactory).get(modelClass)

}