package ows.kotlinstudy.todo_application.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

internal abstract class BaseViewModel: ViewModel() {
    abstract fun fetchData(): Job
}