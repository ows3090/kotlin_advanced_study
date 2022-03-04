package ows.kotlinstudy.subway_application.data.repository

import kotlinx.coroutines.flow.Flow
import ows.kotlinstudy.subway_application.domain.ArrivalInformation
import ows.kotlinstudy.subway_application.domain.Station

interface StationRepository {

    /**
     * DB에 저장된 StationWithSubwayEntity를 화면에 보여질 Station으로 mapping
     */
    val stations: Flow<List<Station>>

    /**
     * station_data.csv 파싱하여 StationEntity, SubwayEntity 갱신
     */
    suspend fun refreshStations()


    /**
     * Station에 실시간 도착정보 반환
     */
    suspend fun getStationArrivals(stationName: String) : List<ArrivalInformation>

    /**
     * Station -> StationEntity로 매핑 후 DB 업데이트
     */
    suspend fun updateStation(station: Station)
}