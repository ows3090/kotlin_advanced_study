package ows.kotlinstudy.movierank_application.presentation.home

import ows.kotlinstudy.movierank_application.domain.model.FeaturedMovie
import ows.kotlinstudy.movierank_application.domain.model.Movie
import ows.kotlinstudy.movierank_application.presentation.BasePresenter
import ows.kotlinstudy.movierank_application.presentation.BaseView

interface HomeContract {

    interface View: BaseView<Presenter>{

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showErrorDescription(message: String)

        fun showMovies(
            featuredMovie: FeaturedMovie?,
            movies: List<Movie>
        )

    }

    interface Presenter: BasePresenter
}