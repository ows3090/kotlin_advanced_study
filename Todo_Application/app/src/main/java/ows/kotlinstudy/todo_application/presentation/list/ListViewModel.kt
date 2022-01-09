package ows.kotlinstudy.todo_application.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ows.kotlinstudy.todo_application.data.entity.ToDoEntity
import ows.kotlinstudy.todo_application.domain.todo.DeleteAllToDoItemUseCase
import ows.kotlinstudy.todo_application.domain.todo.GetToDoListUseCase
import ows.kotlinstudy.todo_application.domain.todo.UpdateToDoUseCase
import ows.kotlinstudy.todo_application.presentation.BaseViewModel

/**
 * 필요한 UseCase
 * 1. [GetTodoListUseCase]
 * 2. [UpdateToDoUseCase]
 * 3. [DeleteAllToDoItemUseCase]
 */
internal class ListViewModel(
    private val getToDoListUseCase: GetToDoListUseCase,
    private val updateToDoUseCase: UpdateToDoUseCase,
    private val deleteAllToDoItemUseCase: DeleteAllToDoItemUseCase
) : BaseViewModel() {
    private var _toDoListLiveData = MutableLiveData<ToDoListState>(ToDoListState.UnInitialized)
    val toDoListLiveData: LiveData<ToDoListState> = _toDoListLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        _toDoListLiveData.postValue(ToDoListState.Loading)
        _toDoListLiveData.postValue(ToDoListState.Success(getToDoListUseCase()))
    }

    fun updateEntity(todoEntity: ToDoEntity) = viewModelScope.launch {
        updateToDoUseCase(todoEntity)
    }

    fun deleteAll() = viewModelScope.launch{
        _toDoListLiveData.postValue(ToDoListState.Loading)
        deleteAllToDoItemUseCase()
        _toDoListLiveData.postValue(ToDoListState.Success(getToDoListUseCase()))
    }
}