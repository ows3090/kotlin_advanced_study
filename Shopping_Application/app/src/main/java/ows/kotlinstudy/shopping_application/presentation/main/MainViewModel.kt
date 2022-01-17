package ows.kotlinstudy.shopping_application.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ows.kotlinstudy.shopping_application.presentation.BaseViewModel

internal class MainViewModel: BaseViewModel() {

    override fun fetchData(): Job = viewModelScope.launch{ }

    private var _mainStateLiveData = MutableLiveData<MainState>()
    val mainStateLiveData: LiveData<MainState> = _mainStateLiveData

    fun refreshOrderList() = viewModelScope.launch{
        _mainStateLiveData.postValue(MainState.RefreshOrderList)
    }
}