package ows.kotlinstudy.delivery_application.presenter

interface BaseView<PresenterT: BasePresenter> {
    val presenter: PresenterT
}