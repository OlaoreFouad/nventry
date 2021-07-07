package dev.olaore.nventry

import android.app.Application
import dev.olaore.nventry.database.getNventryDatabase
import dev.olaore.nventry.database.getUsersDatabase
import dev.olaore.nventry.repositories.BusinessRepository
import dev.olaore.nventry.repositories.UserRepository

class NventryApplication : Application() {

    val userRepository: UserRepository
        get() = UserRepository(getUsersDatabase(applicationContext))

    val businessRepository: BusinessRepository
        get() = BusinessRepository(getNventryDatabase(applicationContext))
}