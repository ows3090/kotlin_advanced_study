package ows.kotlinstudy.shopping_application.presentation.profile

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ows.kotlinstudy.shopping_application.presentation.BaseViewModel

internal class ProfileViewModel: BaseViewModel() {
    override fun fetchData(): Job = viewModelScope.launch{

    }
}