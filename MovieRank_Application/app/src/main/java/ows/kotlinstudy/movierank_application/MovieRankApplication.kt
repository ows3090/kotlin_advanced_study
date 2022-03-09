package ows.kotlinstudy.movierank_application

import android.app.Application
import android.os.Build
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ows.kotlinstudy.movierank_application.di.appModule

class MovieRankApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MovieRankApplication)
            androidLogger(
                if(BuildConfig.DEBUG){
                    Level.DEBUG
                }else{
                    Level.NONE
                }
            )
            modules(appModule)
        }
    }
}