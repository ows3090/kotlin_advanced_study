package ows.kotlinstudy.subway_application.data.db.entity.mapper

import ows.kotlinstudy.subway_application.data.db.entity.StationEntity
import ows.kotlinstudy.subway_application.data.db.entity.StationWithSubwaysEntity
import ows.kotlinstudy.subway_application.data.db.entity.SubwayEntity
import ows.kotlinstudy.subway_application.domain.Station
import ows.kotlinstudy.subway_application.domain.Subway


fun StationWithSubwaysEntity.toStation() = Station(
    name = station.stationName,
    isFavorited = station.isFavorited,
    connectedSubways = subways.toSubways()
)

fun Station.toStationEntity() =
    StationEntity(
        stationName = name,
        isFavorited = isFavorited
    )

fun List<StationWithSubwaysEntity>.toStations() = map { it.toStation() }

fun List<SubwayEntity>.toSubways(): List<Subway> = map { Subway.findById(it.subwayId) }