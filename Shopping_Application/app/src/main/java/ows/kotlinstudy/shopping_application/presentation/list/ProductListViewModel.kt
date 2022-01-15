package ows.kotlinstudy.shopping_application.presentation.list

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ows.kotlinstudy.shopping_application.presentation.BaseViewModel

internal class ProductListViewModel: BaseViewModel() {

    override fun fetchData(): Job = viewModelScope.launch{

    }
}