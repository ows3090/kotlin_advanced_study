package ows.kotlinstudy.movierank_application.presentation.home

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ows.kotlinstudy.movierank_application.domain.usecase.GetAllMoviesUseCase
import ows.kotlinstudy.movierank_application.domain.usecase.GetRandomFeaturedMovieUseCase

class HomePresenter(
    private val view: HomeContract.View,
    private val getRandomFeaturedMovie: GetRandomFeaturedMovieUseCase,
    private val getAllMovies: GetAllMoviesUseCase
): HomeContract.Presenter {

    override val scope: CoroutineScope = MainScope()

    override fun onViewCreated() {
        fetchMovies()
    }

    override fun onDestoryView() {}

    private fun fetchMovies() = scope.launch {
        try{
            view.showLoadingIndicator()
            val featuredMovie = getRandomFeaturedMovie()
            val movies = getAllMovies()

            view.showMovies(featuredMovie, movies)
        }catch (exception: Exception){
            exception.printStackTrace()
            view.showErrorDescription("에러가 발생했어요")
        }finally {
            view.hideLoadingIndicator()
        }
    }
}