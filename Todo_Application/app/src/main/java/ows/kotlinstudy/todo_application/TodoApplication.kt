package ows.kotlinstudy.todo_application

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ows.kotlinstudy.todo_application.di.appModule

class TodoApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        // TODO Koin Trigger
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@TodoApplication)
            modules(appModule)
        }
    }
}