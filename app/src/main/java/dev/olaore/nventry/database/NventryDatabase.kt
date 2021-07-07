package dev.olaore.nventry.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.olaore.nventry.database.dao.SharedProductsDao
import dev.olaore.nventry.database.dao.UsersDao
import dev.olaore.nventry.models.database.DatabaseUser
import dev.olaore.nventry.models.database.SharedProduct

@Database(
    entities = [ SharedProduct::class ],
    version = 1,
    exportSchema = false
)
abstract class NventryDatabase : RoomDatabase() {

    abstract val sharedProductsDao: SharedProductsDao

}

private lateinit var INSTANCE: NventryDatabase

fun getNventryDatabase(context: Context): NventryDatabase {
    return if (!::INSTANCE.isInitialized) {
        Room.databaseBuilder(
            context, NventryDatabase::class.java, "nventry_database"
        ).fallbackToDestructiveMigration().build()
    } else {
        INSTANCE
    }
}
