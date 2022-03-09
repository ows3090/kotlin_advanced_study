package ows.kotlinstudy.delivery_application.work

import androidx.work.DelegatingWorkerFactory
import kotlinx.coroutines.CoroutineDispatcher
import ows.kotlinstudy.delivery_application.data.repository.TrackingItemRepository

class AppWorkerFactory(
    trackingItemRepository: TrackingItemRepository,
    dispatcher: CoroutineDispatcher
): DelegatingWorkerFactory() {

    init {
        addFactory(TrackingCheckWorkerFactory(trackingItemRepository, dispatcher))
    }
}