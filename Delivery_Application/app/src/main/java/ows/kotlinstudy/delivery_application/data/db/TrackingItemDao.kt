package ows.kotlinstudy.delivery_application.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ows.kotlinstudy.delivery_application.data.entity.TrackingItem

@Dao
interface TrackingItemDao {

    /**
     * DB의 데이터가 변경이 될 때 마다 Observable하게 탐지 가능
     * 1. Add Tracking Item
     * 2. Delete Tracking Item
     */
    @Query("SELECT * FROM TrackingItem")
    fun allTrackingItems(): Flow<List<TrackingItem>>

    @Query("SELECT * FROM TrackingItem")
    suspend fun getAll(): List<TrackingItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: TrackingItem)

}