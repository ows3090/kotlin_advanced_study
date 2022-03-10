package ows.kotlinstudy.movierank_application.presentation.reviews

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ows.kotlinstudy.movierank_application.domain.model.Movie
import ows.kotlinstudy.movierank_application.domain.usecase.GetAllReviewsUseCase

class MovieReviewsPresenter(
    private val view: MovieReviewsContract.View,
    override val movie: Movie,
    private val getAllReviews: GetAllReviewsUseCase
): MovieReviewsContract.Presenter {

    override val scope: CoroutineScope = MainScope()

    override fun onViewCreated() {
        view.showMovieInformation(movie)
        fetchReviews()
    }

    override fun onDestoryView() {}

    private fun fetchReviews() = scope.launch {
        try{
            view.showLoadingIndicator()
            view.showReviews(getAllReviews(movie.id!!))
        }catch (exception: Exception){
            exception.printStackTrace()
            view.showErrorDescription("에러가 발생했어요")
        }finally {
            view.hideLoadingIndicator()
        }
    }
}