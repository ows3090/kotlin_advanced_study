package ows.kotlinstudy.movierank_application.presentation.reviews

import ows.kotlinstudy.movierank_application.domain.model.Movie
import ows.kotlinstudy.movierank_application.domain.model.Review
import ows.kotlinstudy.movierank_application.presentation.BasePresenter
import ows.kotlinstudy.movierank_application.presentation.BaseView

interface MovieReviewsContract {

    interface View: BaseView<Presenter>{

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showErrorDescription(message: String)

        fun showMovieInformation(movie: Movie)

        fun showReviews(reviews: List<Review>)
    }

    interface Presenter: BasePresenter{

        val movie: Movie
    }
}