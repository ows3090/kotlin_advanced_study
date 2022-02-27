package ows.kotlinstudy.subway_application.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ows.kotlinstudy.subway_application.data.api.StationApi
import ows.kotlinstudy.subway_application.data.db.StationDao
import ows.kotlinstudy.subway_application.data.db.entity.StationSubwayCrossRefEntity
import ows.kotlinstudy.subway_application.data.db.entity.mapper.toStations
import ows.kotlinstudy.subway_application.data.preference.PreferenceManager
import ows.kotlinstudy.subway_application.domain.Station

class StationRepositoryImpl(
    private val stationApi: StationApi,
    private val stationDao: StationDao,
    private val preferenceManager: PreferenceManager,
    private val dispatcher: CoroutineDispatcher
): StationRepository {

    // distinctUntilChanged : 실제 쿼리 결과가 변경될 떄 호출
    // flowOn : 어떤 스레드에서 호출할 것인가를 결정
    override val stations: Flow<List<Station>> =
        stationDao.getStationWithSubways()
            .distinctUntilChanged()
            .map { it.toStations() }
            .flowOn(dispatcher)

    override suspend fun refreshStations() = withContext(dispatcher){
        val fileUpdatedTimeMillis = stationApi.getStationDataUpdatedTimeMillis()
        val lastDatabaseUpdatedTimeMillis = preferenceManager.getLong(KEY_LAST_DATEBASE_UPDATED_TIME_MILLIS)

        if(lastDatabaseUpdatedTimeMillis == null || fileUpdatedTimeMillis > lastDatabaseUpdatedTimeMillis){
            val stationSubways = stationApi.getStationSubways()
            stationDao.insertStations(stationSubways.map{ it.first })
            stationDao.insertSubways(stationSubways.map { it.second })
            stationDao.insertCrossReference(
                stationSubways.map { (station, subway) ->
                    StationSubwayCrossRefEntity(
                        station.stationName,
                        subway.subwayId
                    )
                }
            )
            preferenceManager.putLong(KEY_LAST_DATEBASE_UPDATED_TIME_MILLIS, fileUpdatedTimeMillis)
        }
    }

    companion object{
        private const val KEY_LAST_DATEBASE_UPDATED_TIME_MILLIS = "KEY_LAST_DATEBASE_UPDATED_TIME_MILLIS"
    }
}