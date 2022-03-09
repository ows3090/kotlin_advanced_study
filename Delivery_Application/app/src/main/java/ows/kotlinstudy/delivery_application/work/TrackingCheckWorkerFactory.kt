package ows.kotlinstudy.delivery_application.work

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineDispatcher
import ows.kotlinstudy.delivery_application.data.repository.TrackingItemRepository

class TrackingCheckWorkerFactory(
    private val trackingItemRepository: TrackingItemRepository,
    private val dispatcher: CoroutineDispatcher
): WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? = when(workerClassName){
        TrackingCheckWorker::class.java.name -> {
            TrackingCheckWorker(
                appContext,
                workerParameters,
                trackingItemRepository,
                dispatcher
            )
        }else ->{
            null
        }
    }
}