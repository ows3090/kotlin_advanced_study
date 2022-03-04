package ows.kotlinstudy.subway_application.data.api

import ows.kotlinstudy.subway_application.data.db.entity.StationEntity
import ows.kotlinstudy.subway_application.data.db.entity.SubwayEntity

interface StationApi {

    /**
     * firebaseStorage에 저장된 station_data.csv의 변경된 시간 반환
     */
    suspend fun getStationDataUpdatedTimeMillis(): Long

    /**
     * firebaseSotrage에 저장된 station_data.csv 파싱하여 StationEntity, SubwayEntity 쌍으로 연결된 리스트 반환
     */
    suspend fun getStationSubways(): List<Pair<StationEntity, SubwayEntity>>
}