package ows.kotlinstudy.subway_application.data.api

import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import ows.kotlinstudy.subway_application.data.db.entity.StationEntity
import ows.kotlinstudy.subway_application.data.db.entity.SubwayEntity

class StationStorageApi(
    firebaseStorage: FirebaseStorage
) : StationApi {

    private val sheetReference = firebaseStorage.reference.child(STATION_DATA_FILE_NAME)

    // Firebase의 반환 값은 대부분 Task, addOnCompleteListener와 같이 콜백 리스너를 통해 구현
    // Firebase play service 추가하면 await 활용 가능 ->  Deferred 객체 반환
    override suspend fun getStationDataUpdatedTimeMillis(): Long =
        sheetReference.metadata.await().updatedTimeMillis

    override suspend fun getStationSubways(): List<Pair<StationEntity, SubwayEntity>> {
        val downloadSizeBytes = sheetReference.metadata.await().sizeBytes
        val byteArray = sheetReference.getBytes(downloadSizeBytes).await()

        return byteArray.decodeToString()
            .lines()
            .drop(1)
            .map { it.split(",") }
            .map { StationEntity(it[1]) to SubwayEntity(it[0].toInt()) }
    }

    companion object {
        private const val STATION_DATA_FILE_NAME = "station_data.csv"
    }
}