package ows.kotlinstudy.todo_application.viewmodel.todo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.koin.core.parameter.parametersOf
import org.koin.test.inject
import ows.kotlinstudy.todo_application.ViewModelTest
import ows.kotlinstudy.todo_application.data.entity.ToDoEntity
import ows.kotlinstudy.todo_application.presentation.detail.DetailMode
import ows.kotlinstudy.todo_application.presentation.detail.DetailViewModel
import ows.kotlinstudy.todo_application.presentation.detail.ToDoDetailState
import ows.kotlinstudy.todo_application.presentation.list.ListViewModel
import ows.kotlinstudy.todo_application.presentation.list.ToDoListState


/**
 * [DetailViewModel]을 테스트 하기 위한 Unit Test Class.
 *
 * 1. test viewModel fetch
 * 2. test insert todo
 */
@ExperimentalCoroutinesApi
internal class DetailViewModelForWriteTest: ViewModelTest() {
    private val id = 0L
    private val detailViewModel by inject<DetailViewModel> { parametersOf(DetailMode.WRITE, id) }
    private val listViewModel: ListViewModel by inject()

    private val todo = ToDoEntity(
        id = id,
        title = "title $id",
        description = "description $id",
        hasCompleted = false
    )

    @Test
    fun `test viewModel fetch`() = runBlockingTest {
        val testObservable = detailViewModel.toDoDetailLiveData.test()

        detailViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnInitialized,
                ToDoDetailState.Write
            )
        )
    }

    @Test
    fun `test insert todo`() = runBlockingTest {
        val detailTestObservable = detailViewModel.toDoDetailLiveData.test()
        val listTestObservable = listViewModel.toDoListLiveData.test()

        detailViewModel.writeToDo(
            title = todo.title,
            description = todo.description
        )

        detailTestObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnInitialized,
                ToDoDetailState.Loading,
                ToDoDetailState.Success(todo)
            )
        )

        assert(detailViewModel.detailMode == DetailMode.DETAIL)
        assert(detailViewModel.id == id)

        // 뒤로나가서 리스트 보기
        listViewModel.fetchData()
        listTestObservable.assertValueSequence(
            listOf(
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(listOf(
                    todo
                ))
            )
        )
    }
}