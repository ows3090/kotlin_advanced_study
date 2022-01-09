package ows.kotlinstudy.todo_application.presentation.list

import ows.kotlinstudy.todo_application.data.entity.ToDoEntity

sealed class ToDoListState {
    object UnInitialized: ToDoListState()

    object Loading: ToDoListState()

    data class Success(
        val toDoList: List<ToDoEntity>
    ): ToDoListState()

    object Error: ToDoListState()
}
