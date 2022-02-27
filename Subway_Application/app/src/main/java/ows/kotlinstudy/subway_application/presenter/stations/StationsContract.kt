package ows.kotlinstudy.subway_application.presenter.stations

import ows.kotlinstudy.subway_application.domain.Station
import ows.kotlinstudy.subway_application.presenter.BasePresenter
import ows.kotlinstudy.subway_application.presenter.BaseView

interface StationsContract {

    interface View : BaseView<Presenter> {

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showStations(stations: List<Station>)
    }

    interface Presenter : BasePresenter {
        fun filterStations(query: String)
    }
}