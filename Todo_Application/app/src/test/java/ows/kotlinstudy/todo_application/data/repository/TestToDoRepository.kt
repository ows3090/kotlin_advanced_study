package ows.kotlinstudy.todo_application.data.repository

import ows.kotlinstudy.todo_application.data.entity.ToDoEntity

class TestToDoRepository : ToDoRepository {

    private val toDoList: MutableList<ToDoEntity> = mutableListOf()
    
    override suspend fun getToDoList(): List<ToDoEntity> {
        return toDoList
    }

    override suspend fun insertToDoList(toDoList: List<ToDoEntity>) {
        this.toDoList.addAll(toDoList)
    }

    override suspend fun insertToDoItem(toDoItem: ToDoEntity): Long {
        this.toDoList.add(toDoItem)
        return toDoItem.id
    }

    override suspend fun updateToDoItem(toDoEntity: ToDoEntity): Boolean {
        val foundToDoEntity = toDoList.find { it.id == toDoEntity.id }
        if(foundToDoEntity == null) return false
        else{
            this.toDoList[toDoList.indexOf(foundToDoEntity)] = toDoEntity
            return true
        }
    }

    override suspend fun getToDoItem(itemId: Long): ToDoEntity? {
        return toDoList.find { it.id == itemId}
    }

    override suspend fun deleteAll() {
        toDoList.clear()
    }

    override suspend fun deleteToDoItem(id: Long): Boolean {
        val foundToDoEntity = toDoList.find { it.id == id }
        if(foundToDoEntity == null) return false
        else{
            this.toDoList.removeAt(toDoList.indexOf(foundToDoEntity))
            return true
        }
    }
}