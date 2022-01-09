package ows.kotlinstudy.todo_application.viewmodel.todo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.test.inject
import ows.kotlinstudy.todo_application.ViewModelTest
import ows.kotlinstudy.todo_application.data.entity.ToDoEntity
import ows.kotlinstudy.todo_application.domain.todo.GetToDoItemUseCase
import ows.kotlinstudy.todo_application.domain.todo.InsertToDoListUseCase
import ows.kotlinstudy.todo_application.presentation.list.ListViewModel
import ows.kotlinstudy.todo_application.presentation.list.ToDoListState

/**
 * [ListViewModel]을 테스트 하기 위한 Unit Test Class.
 *
 * 1. initData()
 * 2. test viewmodel fetch
 * 3. test Item Update
 * 4. test Item Delete All
 *
 * TestClass는 @Test Method를 호출할 때마다 새로운 TestClass 객체 생성
 */
@ExperimentalCoroutinesApi
internal class ListViewModelTest: ViewModelTest(){

    private val viewModel: ListViewModel by inject()

    private val insertToDoListUseCase: InsertToDoListUseCase by inject()
    private val getToDoItemUseCase: GetToDoItemUseCase by inject()

    private val mockList = (0 until 10).map {
        ToDoEntity(
            id = it.toLong(),
            title = "title ${it}",
            description = "description ${it}",
            hasCompleted = false
        )
    }

    /**
     * 필요한 UserCase
     * 1. InsertToDoListUseCase
     * 2. GetToDoItemUseCase
     */
    @Before
    fun init(){
        initData()
    }

    private fun initData() = runBlockingTest {
        insertToDoListUseCase(mockList)
    }

    // Test : 입력된 데이터블 불러와서 검증한다.
    @Test
    fun `test viewModel fetch`(): Unit = runBlockingTest {
        val testObservable = viewModel.toDoListLiveData.test()
        viewModel.fetchData()
        testObservable.assertValueSequence(
            listOf(
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(mockList)
            )
        )
    }

    // Test : 데이터를 업데이트 했을 때 잘 반영되는가
    @Test
    fun `test Item Update`(): Unit = runBlockingTest {
        val todo = ToDoEntity(
            id = 1,
            title = "title 1",
            description = "description 1",
            hasCompleted = true
        )
        viewModel.updateEntity(todo)
        assert(getToDoItemUseCase(todo.id)?.hasCompleted ?: false == todo.hasCompleted)
    }

    // Test : 데이터를 다 날렸을 때 빈상태로 보여지는가
    @Test
    fun `test Item Delete All`(): Unit = runBlockingTest {
        val testObservable = viewModel.toDoListLiveData.test()
        viewModel.deleteAll()
        testObservable.assertValueSequence(
            listOf(
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(listOf())
            )
        )
    }

}