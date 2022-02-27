package ows.kotlinstudy.subway_application.presenter

interface BaseView<PresenterT: BasePresenter> {
    val presenter : PresenterT
}