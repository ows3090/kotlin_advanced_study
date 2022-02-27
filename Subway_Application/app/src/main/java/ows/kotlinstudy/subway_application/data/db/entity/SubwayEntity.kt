package ows.kotlinstudy.subway_application.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// StationEntity와는 N:M 매핑
@Entity
data class SubwayEntity(
    @PrimaryKey val subwayId: Int
)