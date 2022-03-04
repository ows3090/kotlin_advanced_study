package ows.kotlinstudy.subway_application.data.db.entity

import androidx.room.Entity

/**
 * 다대다 매핑 관계일 경우에는 상호 참조 테이블이 필요
 * 상호 참조 테이블에는 각 테이블의 기본키가 열에 존재
 */
@Entity(primaryKeys = ["stationName","subwayId"])
data class StationSubwayCrossRefEntity(
    val stationName: String,
    val subwayId: Int
)
