package ows.kotlinstudy.movierank_application.presentation

import androidx.annotation.CallSuper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

interface BasePresenter {

    val scope: CoroutineScope

    fun onViewCreated()

    fun onDestoryView()

    @CallSuper
    fun onDestory(){
        scope.cancel()
    }
}