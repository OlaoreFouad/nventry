package dev.olaore.nventry.repositories

import dev.olaore.nventry.database.NventryDatabase
import dev.olaore.nventry.database.UserDatabase

class UserRepository(
    val database: UserDatabase
)