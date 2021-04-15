package dev.olaore.nventry.models.mappers

import dev.olaore.nventry.models.database.DatabaseUser
import dev.olaore.nventry.models.domain.TemporaryUser
import dev.olaore.nventry.models.network.NetworkUser

fun TemporaryUser.asNetworkUser(): NetworkUser {
    return NetworkUser("", this.username, this.email, this.createdOn, listOf())
}

fun TemporaryUser.asDatabaseUser(userId: String): DatabaseUser {
    return DatabaseUser(userId, this.username, this.password, this.email, this.createdOn, "")
}

fun NetworkUser.asDatabaseUser(id: String, password: String): DatabaseUser {
    return DatabaseUser(
        id, this.username, password, this.email, this.createdOn, this.businesses.joinToString("---")
    )
}