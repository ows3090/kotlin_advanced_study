package ows.kotlinstudy.subway_application.data.repository

import kotlinx.coroutines.flow.Flow
import ows.kotlinstudy.subway_application.domain.ArrivalInformation
import ows.kotlinstudy.subway_application.domain.Station

interface StationRepository {

    val stations: Flow<List<Station>>

    suspend fun refreshStations()

    suspend fun getStationArrivals(stationName: String) : List<ArrivalInformation>
}