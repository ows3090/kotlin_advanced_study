package ows.kotlinstudy.subway_application.data.repository

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ows.kotlinstudy.subway_application.data.api.StationApi
import ows.kotlinstudy.subway_application.data.api.StationArrivalsApi
import ows.kotlinstudy.subway_application.data.api.response.mapper.toArrivalInformation
import ows.kotlinstudy.subway_application.data.db.StationDao
import ows.kotlinstudy.subway_application.data.db.entity.StationSubwayCrossRefEntity
import ows.kotlinstudy.subway_application.data.db.entity.mapper.toStations
import ows.kotlinstudy.subway_application.data.preference.PreferenceManager
import ows.kotlinstudy.subway_application.domain.ArrivalInformation
import ows.kotlinstudy.subway_application.domain.Station
import java.lang.RuntimeException

class StationRepositoryImpl(
    private val stationArrivalsApi: StationArrivalsApi,
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
            stationDao.insertStationSubways(stationApi.getStationSubways())
            preferenceManager.putLong(KEY_LAST_DATEBASE_UPDATED_TIME_MILLIS, fileUpdatedTimeMillis)
        }
    }

    override suspend fun getStationArrivals(stationName: String): List<ArrivalInformation> = withContext(dispatcher){
        stationArrivalsApi.getRealtimeStationArrivals(stationName)
            .body()
            ?.realtimeArrivalList
            ?.toArrivalInformation()
            ?.distinctBy { it.direction }
            ?.sortedBy { it.subway }
            ?: throw RuntimeException("도착 정보를 불러오는 데에 실패했습니다")
    }

    companion object{
        private const val KEY_LAST_DATEBASE_UPDATED_TIME_MILLIS = "KEY_LAST_DATEBASE_UPDATED_TIME_MILLIS"
    }
}