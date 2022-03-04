package ows.kotlinstudy.subway_application.data.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 *  Station에 매핑되는 Subway 목록들을 쿼리하기 위한 클래스
 *  @Embedded Entity로 정의된 클래스의 열들을 분해하여 해당 클래스에 포함시킴
 *  @Relation Station -> parent, Subway -> entity
 *  Subway에 매핑되는 Station 목록들을 쿼리하기 위해서는 SubwayWithStationEntity 클래스 생성
 */
data class StationWithSubwaysEntity(
    @Embedded val station: StationEntity,
    @Relation(
        parentColumn = "stationName",
        entityColumn = "subwayId",
        associateBy = Junction(StationSubwayCrossRefEntity::class)
    )
    val subways: List<SubwayEntity>
)
