package ows.kotlinstudy.shopping_application

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import ows.kotlinstudy.shopping_application.di.appModule

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@ShoppingApplication)
            modules(appModule)
        }
    }
}