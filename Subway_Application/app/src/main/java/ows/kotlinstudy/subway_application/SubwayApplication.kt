package ows.kotlinstudy.subway_application

import android.app.Application
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ows.kotlinstudy.subway_application.di.appModule

class SubwayApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(
                if(BuildConfig.DEBUG) Level.DEBUG
                else Level.NONE
            )
            androidContext(this@SubwayApplication)
            modules(appModule)
        }
    }
}