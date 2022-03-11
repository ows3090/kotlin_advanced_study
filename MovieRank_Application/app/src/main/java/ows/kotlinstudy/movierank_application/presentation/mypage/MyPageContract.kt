package ows.kotlinstudy.movierank_application.presentation.mypage

import ows.kotlinstudy.movierank_application.domain.model.ReviewedMovie
import ows.kotlinstudy.movierank_application.presentation.BasePresenter
import ows.kotlinstudy.movierank_application.presentation.BaseView

interface MyPageContract {

    interface View: BaseView<Presenter>{

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showNoDataDescription(message: String)

        fun showErrorDescription(message: String)

        fun showReviewedMovies(reviewedMovies: List<ReviewedMovie>)
    }

    interface Presenter: BasePresenter
}