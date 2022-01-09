package ows.kotlinstudy.todo_application.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ows.kotlinstudy.todo_application.data.repository.TestToDoRepository
import ows.kotlinstudy.todo_application.data.repository.ToDoRepository
import ows.kotlinstudy.todo_application.domain.todo.*
import ows.kotlinstudy.todo_application.presentation.detail.DetailMode
import ows.kotlinstudy.todo_application.presentation.detail.DetailViewModel
import ows.kotlinstudy.todo_application.presentation.list.ListViewModel

internal val appTestModule = module {

    // ViewModel
    viewModel { ListViewModel(get(), get(), get()) }
    viewModel { (detailMode: DetailMode, id: Long) ->
        DetailViewModel(
            detailMode = detailMode,
            id = id,
            get(),
            get(),
            get(),
            get()
        )
    }

    // UseCase
    factory { GetToDoListUseCase(get()) }
    factory { InsertToDoListUseCase(get()) }
    factory { InsertToDoItemUseCase(get()) }
    factory { UpdateToDoUseCase(get()) }
    factory { GetToDoItemUseCase(get()) }
    factory { DeleteAllToDoItemUseCase(get()) }
    factory { DeleteToDoItemUseCase(get()) }


    // Repository
    single<ToDoRepository> { TestToDoRepository() }

}