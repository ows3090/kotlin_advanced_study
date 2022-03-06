package ows.kotlinstudy.delivery_application.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ows.kotlinstudy.delivery_application.data.entity.TrackingItem

@Database(
    entities = [TrackingItem::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun trackingItemDao(): TrackingItemDao

    companion object{

        private const val DATABASE_NAME = "tracking.db"

        fun build(context: Context): AppDatabase =
            Room.databaseBuilder(context,AppDatabase::class.java, DATABASE_NAME).build()
    }
}