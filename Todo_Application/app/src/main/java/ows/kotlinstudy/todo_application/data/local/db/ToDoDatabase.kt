package ows.kotlinstudy.todo_application.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ows.kotlinstudy.todo_application.data.entity.ToDoEntity
import ows.kotlinstudy.todo_application.data.local.db.dao.ToDoDao

@Database(
    entities = [ToDoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ToDoDatabase : RoomDatabase() {

    companion object{
        const val DB_NAME = "ToDoDataBase.db"
    }
    abstract fun toDoDao(): ToDoDao
}