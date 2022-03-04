package ows.kotlinstudy.delivery_application

import android.app.Application
import android.os.Build
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ows.kotlinstudy.delivery_application.di.appModule

class DeliveryApplication: Application() {

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
}