package ows.kotlinstudy.movierank_application.presentation

interface BaseView<PresenterT: BasePresenter> {

    val presenter: PresenterT
}