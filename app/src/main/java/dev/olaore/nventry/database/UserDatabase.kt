package dev.olaore.nventry.database

import android.content.Context
import android.service.autofill.UserData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.olaore.nventry.database.dao.UsersDao
import dev.olaore.nventry.models.database.DatabaseUser

@Database(
    entities = [ DatabaseUser::class ],
    version = 1,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {

    abstract val usersDao: UsersDao

}

private lateinit var INSTANCE: UserDatabase

fun getUsersDatabase(context: Context): UserDatabase {
    return if (!::INSTANCE.isInitialized) {
        Room.databaseBuilder(
            context, UserDatabase::class.java, "users_database"
        ).fallbackToDestructiveMigration().build()
    } else {
        INSTANCE
    }
}