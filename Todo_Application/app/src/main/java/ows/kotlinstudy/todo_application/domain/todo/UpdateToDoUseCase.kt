package ows.kotlinstudy.todo_application.domain.todo

import ows.kotlinstudy.todo_application.data.entity.ToDoEntity
import ows.kotlinstudy.todo_application.data.repository.ToDoRepository
import ows.kotlinstudy.todo_application.domain.UseCase

internal class UpdateToDoUseCase(
    private val toDoRepository: ToDoRepository
) : UseCase{

    suspend operator fun invoke(toDoEntity: ToDoEntity): Boolean{
        return toDoRepository.updateToDoItem(toDoEntity)
    }
}