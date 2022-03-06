package ows.kotlinstudy.delivery_application.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ows.kotlinstudy.delivery_application.data.entity.TrackingItem

@Dao
interface TrackingItemDao {

    @Query("SELECT * FROM TrackingItem")
    suspend fun getAll(): List<TrackingItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: TrackingItem)
}