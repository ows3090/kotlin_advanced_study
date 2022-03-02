package ows.kotlinstudy.subway_application.presenter.stationsArrivals

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ows.kotlinstudy.subway_application.data.repository.StationRepository
import ows.kotlinstudy.subway_application.domain.Station
import java.lang.Exception

class StationArrivalsPresenter(
    private val view: StationArrivalsContract.View,
    private val station: Station,
    private val stationRepository: StationRepository
): StationArrivalsContract.Presenter{

    override val scope: CoroutineScope = MainScope()

    override fun onViewCreated() {
        fetchStationArrivals()
    }

    override fun onDestroyView() {}

    override fun fetchStationArrivals() {
        scope.launch {
            try{
                view.showLoadingIndicator()
                view.showStationArrivals(stationRepository.getStationArrivals(station.name))
            }catch (exception: Exception){
                exception.printStackTrace()
                view.showErrorDescription(exception.message ?: "알 수 없는 문제가 발생했습니다")
            }finally {
                view.hidLoadingIndicator()
            }
        }
    }

    override fun toggleStationFavorite() {
        scope.launch {
            stationRepository.updateStation(station.copy(isFavorited = !station.isFavorited))
        }
    }
}