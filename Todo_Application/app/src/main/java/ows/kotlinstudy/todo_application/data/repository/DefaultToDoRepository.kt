package ows.kotlinstudy.todo_application.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ows.kotlinstudy.todo_application.data.entity.ToDoEntity
import ows.kotlinstudy.todo_application.data.local.db.dao.ToDoDao

class DefaultToDoRepository(
    private val toDoDao: ToDoDao,
    private val ioDispatcher: CoroutineDispatcher
) : ToDoRepository {

    override suspend fun getToDoList(): List<ToDoEntity> = withContext(ioDispatcher){
        toDoDao.getAll()
    }

    override suspend fun getToDoItem(itemId: Long): ToDoEntity? = withContext(ioDispatcher){
        toDoDao.getById(itemId)
    }

    override suspend fun insertToDoList(toDoList: List<ToDoEntity>) = withContext(ioDispatcher){
        toDoDao.insert(toDoList)
    }

    override suspend fun insertToDoItem(toDoItem: ToDoEntity): Long = withContext(ioDispatcher){
        toDoDao.insert(toDoItem)
    }

    override suspend fun updateToDoItem(toDoEntity: ToDoEntity) = withContext(ioDispatcher){
        toDoDao.update(toDoEntity)
    }

    override suspend fun deleteAll() = withContext(ioDispatcher){
        toDoDao.deleteAll()
    }

    override suspend fun deleteToDoItem(id: Long) = withContext(ioDispatcher){
        toDoDao.delete(id)
    }
}