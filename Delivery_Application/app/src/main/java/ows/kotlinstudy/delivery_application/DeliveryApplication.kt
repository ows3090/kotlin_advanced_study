package ows.kotlinstudy.delivery_application

import android.app.Application
import androidx.work.Configuration
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ows.kotlinstudy.delivery_application.di.appModule
import ows.kotlinstudy.delivery_application.work.AppWorkerFactory

class DeliveryApplication: Application(), Configuration.Provider {

    private val workerFactory: AppWorkerFactory by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(
                if(BuildConfig.DEBUG) Level.DEBUG
                else Level.NONE
            )
            androidContext(this@DeliveryApplication)
            modules(appModule)
        }
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(
                if(BuildConfig.DEBUG){
                    android.util.Log.DEBUG
                }else{
                    android.util.Log.INFO
                }
            )
            .setWorkerFactory(workerFactory)
            .build()
}