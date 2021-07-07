package dev.olaore.nventry.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.olaore.nventry.models.database.SharedProduct

@Dao
interface SharedProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSharedProduct(sharedProduct: SharedProduct)

    @Query("SELECT * FROM shared_products_table ORDER BY sharedAt DESC LIMIT 5")
    suspend fun getSharedProducts(): List<SharedProduct>

}