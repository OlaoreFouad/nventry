package dev.olaore.nventry.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shared_products_table")
data class SharedProduct(

    @PrimaryKey
    var id: String,

    var name: String,

    var sharedAt: Long,

    var image: String,

    var sharingText: String

)