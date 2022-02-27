package ows.kotlinstudy.subway_application.data.api

import ows.kotlinstudy.subway_application.data.db.entity.StationEntity
import ows.kotlinstudy.subway_application.data.db.entity.SubwayEntity

interface StationApi {

    suspend fun getStationDataUpdatedTimeMillis(): Long

    suspend fun getStationSubways(): List<Pair<StationEntity, SubwayEntity>>
}