package ows.kotlinstudy.subway_application.presenter.stations

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ows.kotlinstudy.subway_application.data.repository.StationRepository
import ows.kotlinstudy.subway_application.domain.Station
import kotlin.coroutines.CoroutineContext

/**
 * View에서 Presenter로 생명주기 또는 클릭 이벤트 통지
 * 1. onViewCreated()
 * 2. onDestoryView()
 * 3. filterStations(query: String)
 */
class StationsPresenter(
    private val view: StationsContract.View,
    private val stationRepository: StationRepository
): StationsContract.Presenter {

    override val scope: CoroutineScope = MainScope()

    private val queryString: MutableStateFlow<String> = MutableStateFlow("")
    private val stations: MutableStateFlow<List<Station>> = MutableStateFlow(emptyList())

    init {
        observeStations()
    }

    override fun onViewCreated() {
        scope.launch {
            view.showStations(stations.value)
            stationRepository.refreshStations()
        }
    }

    override fun onDestroyView() {}

    override fun filterStations(query: String) {
        scope.launch {
            queryString.emit(query)
        }
    }

    private fun observeStations(){
        stationRepository
            .stations
            .combine(queryString){ stations, query ->
                if(query.isBlank()) stations
                else stations.filter { it.name.contains(query) }
            }
            .onStart { view.showLoadingIndicator() }
            .onEach {
                if(it.isNotEmpty()) view.hideLoadingIndicator()
                stations.value = it
                view.showStations(it)
            }
            .catch {
                it.printStackTrace()
                view.hideLoadingIndicator()
            }
            .launchIn(scope)

    }

    override fun toggleStationFavorite(station: Station) {
        scope.launch {
            stationRepository.updateStation(station.copy(isFavorited = !station.isFavorited))
        }
    }
}