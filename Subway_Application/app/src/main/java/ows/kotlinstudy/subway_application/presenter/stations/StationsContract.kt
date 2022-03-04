package ows.kotlinstudy.subway_application.presenter.stations

import ows.kotlinstudy.subway_application.domain.Station
import ows.kotlinstudy.subway_application.presenter.BasePresenter
import ows.kotlinstudy.subway_application.presenter.BaseView

interface StationsContract {

    interface View : BaseView<Presenter> {

        // ProgressBar 표시
        fun showLoadingIndicator()

        // ProgressBar 숨김
        fun hideLoadingIndicator()

        // Station List 목록 보기
        fun showStations(stations: List<Station>)
    }

    interface Presenter : BasePresenter {
        // 검색 query 입력하여 Station 변환
        fun filterStations(query: String)

        // 즐겨찾기 추가
        fun toggleStationFavorite(station: Station)
    }
}