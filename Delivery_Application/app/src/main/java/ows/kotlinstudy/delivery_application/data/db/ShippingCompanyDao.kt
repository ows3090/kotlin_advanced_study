package ows.kotlinstudy.delivery_application.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ows.kotlinstudy.delivery_application.data.entity.ShippingCompany

@Dao
interface ShippingCompanyDao {

    @Query("SELECT * FROM ShippingCompany")
    suspend fun getAll(): List<ShippingCompany>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: List<ShippingCompany>)
}