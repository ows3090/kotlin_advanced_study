package ows.kotlinstudy.subway_application.presenter.stationsArrivals

import ows.kotlinstudy.subway_application.domain.ArrivalInformation
import ows.kotlinstudy.subway_application.presenter.BasePresenter
import ows.kotlinstudy.subway_application.presenter.BaseView

interface StationArrivalsContract {

    interface View : BaseView<Presenter> {
        fun showLoadingIndicator()

        fun hidLoadingIndicator()

        fun showErrorDescription(message: String)

        fun showStationArrivals(arrivalInformation: List<ArrivalInformation>)
    }

    interface Presenter : BasePresenter {
        fun fetchStationArrivals()

        fun toggleStationFavorite()
    }
}