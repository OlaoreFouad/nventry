package dev.olaore.nventry.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import dev.olaore.nventry.database.NventryDatabase
import dev.olaore.nventry.database.UserDatabase
import dev.olaore.nventry.models.database.DatabaseUser
import dev.olaore.nventry.models.domain.TemporaryUser
import dev.olaore.nventry.models.mappers.asNetworkUser
import dev.olaore.nventry.models.network.NetworkUser
import dev.olaore.nventry.network.Network

class UserRepository(
    val database: UserDatabase
) {

    suspend fun addUser(temporaryUser: TemporaryUser, userId: String): Task<Void> {
        return Network.saveUser(temporaryUser.asNetworkUser(), userId)
    }

    suspend fun addUserToDatabase(databaseUser: DatabaseUser) {
        database.usersDao.addUser(databaseUser)
    }

    suspend fun getUserFromDatabase(userId: String): DatabaseUser {
        return database.usersDao.getUser(userId)
    }

    suspend fun getUser(email: String): Task<QuerySnapshot> {
        return Network.getUser(email)
    }

}