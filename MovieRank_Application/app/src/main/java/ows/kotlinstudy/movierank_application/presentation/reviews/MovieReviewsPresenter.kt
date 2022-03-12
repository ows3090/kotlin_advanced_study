package ows.kotlinstudy.movierank_application.presentation.reviews

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ows.kotlinstudy.movierank_application.domain.model.Movie
import ows.kotlinstudy.movierank_application.domain.model.MovieReviews
import ows.kotlinstudy.movierank_application.domain.model.Review
import ows.kotlinstudy.movierank_application.domain.usecase.DeleteReviewUseCase
import ows.kotlinstudy.movierank_application.domain.usecase.GetAllReviewsUseCase
import ows.kotlinstudy.movierank_application.domain.usecase.SubmitReviewUseCase

class MovieReviewsPresenter(
    private val view: MovieReviewsContract.View,
    override val movie: Movie,
    private val getAllReviews: GetAllReviewsUseCase,
    private val submitReview: SubmitReviewUseCase,
    private val deleteReview: DeleteReviewUseCase
): MovieReviewsContract.Presenter {

    override val scope: CoroutineScope = MainScope()

    private var movieReviews: MovieReviews = MovieReviews(null, emptyList())

    override fun onViewCreated() {
        view.showMovieInformation(movie)
        fetchReviews()
    }

    override fun onDestoryView() {}

    private fun fetchReviews() = scope.launch {
        try{
            view.showLoadingIndicator()
            movieReviews = getAllReviews(movie.id!!)
            view.showReviews(movieReviews)
        }catch (exception: Exception){
            exception.printStackTrace()
            view.showErrorDescription("에러가 발생했어요")
        }finally {
            view.hideLoadingIndicator()
        }
    }

    override fun requestAddReview(content: String, score: Float) {
        scope.launch {
            try{
                view.showLoadingIndicator()
                val submitReview = submitReview(movie, content, score)
                view.showReviews(movieReviews.copy(myReview = submitReview))
            }catch (exception: Exception){
                view.showErrorToast("리뷰 등록에 실패 했어요 ")
            }finally {
                view.hideLoadingIndicator()
            }
        }
    }

    override fun requestRemoveReview(review: Review) {
        scope.launch {
            try{
                view.showLoadingIndicator()
                deleteReview(review)
                view.showReviews(movieReviews.copy(myReview = null))
            }catch (exception: Exception){
                view.showErrorToast("리뷰 삭제를 실패했어요")
            }finally {
                view.hideLoadingIndicator()
            }
        }
    }
}