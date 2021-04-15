package dev.olaore.nventry.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class DatabaseUser(

    @PrimaryKey
    var userId: String,

    var username: String,

    var password: String,

    var email: String,

    var createdOn: Long,

    var businesses: String
)