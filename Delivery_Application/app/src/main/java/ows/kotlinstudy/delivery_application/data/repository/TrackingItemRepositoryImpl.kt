package ows.kotlinstudy.delivery_application.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext
import ows.kotlinstudy.delivery_application.data.api.SweetTrackerApi
import ows.kotlinstudy.delivery_application.data.db.TrackingItemDao
import ows.kotlinstudy.delivery_application.data.entity.TrackingInformation
import ows.kotlinstudy.delivery_application.data.entity.TrackingItem
import java.lang.RuntimeException

class TrackingItemRepositoryImpl(
    private val trackerApi: SweetTrackerApi,
    private val trackingItemDao: TrackingItemDao,
    private val dispatcher: CoroutineDispatcher
) : TrackingItemRepository {

    override val trackingItems: Flow<List<TrackingItem>> =
        trackingItemDao.allTrackingItems().distinctUntilChanged()

    override suspend fun getTrackingItemInformation(): List<Pair<TrackingItem, TrackingInformation>> =
        withContext(dispatcher) {
            trackingItemDao.getAll()
                .mapNotNull { trackingItem ->
                    val relatedTrackingInfo = trackerApi.getTrackingInformation(
                        trackingItem.company.code,
                        trackingItem.invoice
                    ).body()

                    if (relatedTrackingInfo?.invoiceNo.isNullOrBlank()) {
                        null
                    } else {
                        trackingItem to relatedTrackingInfo!!
                    }
                }
                .sortedWith(
                    compareBy(
                        { it.second.level },  // 오름차순
                        { -(it.second.lastDetail?.time ?: Long.MAX_VALUE) } // 내림차순
                    )
                )
        }

    override suspend fun saveTrackingItem(trackingItem: TrackingItem) = withContext(dispatcher){
        val trackingInformation = trackerApi.getTrackingInformation(
            trackingItem.company.code,
            trackingItem.invoice
        ).body()

        if(!trackingInformation!!.errorMessage.isNullOrBlank()){
            throw RuntimeException(trackingInformation.errorMessage)
        }

        trackingItemDao.insert(trackingItem)
    }

}