package ows.kotlinstudy.subway_application.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ows.kotlinstudy.subway_application.data.db.entity.StationEntity
import ows.kotlinstudy.subway_application.data.db.entity.StationSubwayCrossRefEntity
import ows.kotlinstudy.subway_application.data.db.entity.StationWithSubwaysEntity
import ows.kotlinstudy.subway_application.data.db.entity.SubwayEntity

@Dao
interface StationDao {

    // Transacion : 원자적으로 처리
    // 이 메서드는 2가지의 쿼리를 실 : StationEntity 쿼리, StationEntity를 SubwayEntity 참조 쿼리
    // Flow vs Suspend
    // Suspend : 1회성 호출
    // Flow : Observable하게 계속해서 db변경 시마다 호출
    @Transaction
    @Query("SELECT * FROM StationEntity")
    fun getStationWithSubways(): Flow<List<StationWithSubwaysEntity>>

    // 내부적으로 Transaction 처리
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(station: List<StationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubways(subways: List<SubwayEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossReference(reference : List<StationSubwayCrossRefEntity>)

    @Transaction
    suspend fun insertStationSubways(stationsSubways: List<Pair<StationEntity, SubwayEntity>>) {
        insertStations(stationsSubways.map { it.first })
        insertSubways(stationsSubways.map { it.second })
        insertCrossReference(
            stationsSubways.map { (station,subway) ->
                StationSubwayCrossRefEntity(
                    station.stationName,
                    subway.subwayId
                )
            }
        )
    }
}