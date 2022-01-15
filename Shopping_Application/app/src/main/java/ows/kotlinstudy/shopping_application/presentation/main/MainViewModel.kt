package ows.kotlinstudy.shopping_application.presentation.main

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ows.kotlinstudy.shopping_application.presentation.BaseViewModel

internal class MainViewModel: BaseViewModel() {

    override fun fetchData(): Job = viewModelScope.launch{

    }
}