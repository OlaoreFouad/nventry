package dev.olaore.nventry.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.olaore.nventry.models.database.DatabaseUser

@Dao
public interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: DatabaseUser)

    @Query("SELECT * FROM users_table WHERE userId = :id")
    suspend fun getUser(id: String): DatabaseUser

    @Query("DELETE FROM users_table")
    suspend fun deleteAll()

}