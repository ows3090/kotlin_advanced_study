package ows.kotlinstudy.todo_application.data.repository

import ows.kotlinstudy.todo_application.data.entity.ToDoEntity

/**
 * 1. insertToDoList
 * 2. getToDoList
 * 3. udateToDoItem
 */
interface ToDoRepository {

    suspend fun getToDoList(): List<ToDoEntity>

    suspend fun insertToDoItem(toDoItem: ToDoEntity): Long

    suspend fun insertToDoList(toDoList: List<ToDoEntity>)

    suspend fun updateToDoItem(toDoEntity: ToDoEntity): Boolean

    suspend fun getToDoItem(itemId: Long): ToDoEntity?

    suspend fun deleteAll()

    suspend fun deleteToDoItem(id: Long): Boolean

}