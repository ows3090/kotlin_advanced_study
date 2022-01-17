package ows.kotlinstudy.shopping_application.data.db

import android.content.Context
import androidx.room.Room

internal fun provideDB(context: Context): ProductDatabase =
    Room.databaseBuilder(context, ProductDatabase::class.java, ProductDatabase.DB_NAME).build()

internal fun provideToDao(productDatabase: ProductDatabase) = productDatabase.productDao()