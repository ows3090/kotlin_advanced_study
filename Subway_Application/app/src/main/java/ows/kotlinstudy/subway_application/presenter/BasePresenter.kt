package ows.kotlinstudy.subway_application.presenter

import androidx.annotation.CallSuper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

interface BasePresenter {

    val scope: CoroutineScope
        get() = MainScope()

    fun onViewCreated()

    fun onDestroyView()

    // @CallSuper : 상속받은 메소드에 super method를 invoke 강제화 적
    @CallSuper
    fun onDestroy(){
        scope.cancel()
    }
}