package ows.kotlinstudy.shopping_application.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ows.kotlinstudy.shopping_application.data.preference.PreferenceManager
import ows.kotlinstudy.shopping_application.presentation.BaseViewModel

internal class ProfileViewModel(
    private val preferenceManager: PreferenceManager
): BaseViewModel() {

    private var _profileStateLiveData = MutableLiveData<ProfileState>(ProfileState.Uninitialized)
    val profileStateLiveData: LiveData<ProfileState> = _profileStateLiveData

    override fun fetchData(): Job = viewModelScope.launch{
        setState(ProfileState.Loading)
        preferenceManager.getIdToken()?.let {
            setState(
                ProfileState.Login(it)
            )
        }?: kotlin.run{
            setState(
                ProfileState.Success.NotRegistered
            )
        }
    }

    private fun setState(state: ProfileState){
        _profileStateLiveData.postValue(state)
    }
}