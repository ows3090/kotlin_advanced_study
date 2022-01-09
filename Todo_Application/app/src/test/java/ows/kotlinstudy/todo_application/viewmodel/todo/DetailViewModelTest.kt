package ows.kotlinstudy.todo_application.viewmodel.todo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.koin.core.parameter.parametersOf
import org.koin.test.inject
import ows.kotlinstudy.todo_application.ViewModelTest
import ows.kotlinstudy.todo_application.data.entity.ToDoEntity
import ows.kotlinstudy.todo_application.domain.todo.InsertToDoItemUseCase
import ows.kotlinstudy.todo_application.presentation.detail.DetailMode
import ows.kotlinstudy.todo_application.presentation.detail.DetailViewModel
import ows.kotlinstudy.todo_application.presentation.detail.ToDoDetailState
import ows.kotlinstudy.todo_application.presentation.list.ListViewModel
import ows.kotlinstudy.todo_application.presentation.list.ToDoListState

/**
 * [DetailViewModel]을 테스트 하기 위한 Unit Test Class.
 *
 * 1. initData()
 * 2. test viewModel fetch
 * 3. test delete todo
 * 4. test update todo
 */
@ExperimentalCoroutinesApi
internal class DetailViewModelTest: ViewModelTest() {

    private val id = 1L

    private val detailViewModel by inject<DetailViewModel> { parametersOf(DetailMode.DETAIL, id) }
    private val listViewModel: ListViewModel by inject()

    private val insertToDoItemUseCase: InsertToDoItemUseCase by inject()

    private val todo = ToDoEntity(
        id = id,
        title = "title $id",
        description = "description $id",
        hasCompleted = false
    )

    @Before
    fun init(){
        initData()
    }

    private fun initData() = runBlockingTest {
        insertToDoItemUseCase(todo)
    }

    @Test
    fun `test viewModel fetch`() = runBlockingTest {
        val testObservable = detailViewModel.toDoDetailLiveData.test()
        detailViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnInitialized,
                ToDoDetailState.Loading,
                ToDoDetailState.Success(todo)
            )
        )
    }

    @Test
    fun `test delete todo`() = runBlockingTest {
        val detailTestObservable = detailViewModel.toDoDetailLiveData.test()
        detailViewModel.deleteToDo()

        detailTestObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnInitialized,
                ToDoDetailState.Loading,
                ToDoDetailState.Delete
            )
        )

        val listTestObservable = listViewModel.toDoListLiveData.test()
        listViewModel.fetchData()
        listTestObservable.assertValueSequence(
            listOf(
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(listOf())
            )
        )
    }

    @Test
    fun `test update todo`() = runBlockingTest {
        val testObservable = detailViewModel.toDoDetailLiveData.test()

        val updateTitle = "title 1 update"
        val updateDescription = "description 1 update"

        val updateToDo = todo.copy(
            title = updateTitle,
            description = updateDescription
        )
        detailViewModel.writeToDo(
            title = updateTitle,
            description = updateDescription
        )

        testObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnInitialized,
                ToDoDetailState.Loading,
                ToDoDetailState.Success(updateToDo)
            )
        )
    }
}