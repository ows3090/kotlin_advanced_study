package ows.kotlinstudy.subway_application.di

import android.app.Activity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ows.kotlinstudy.subway_application.data.api.StationApi
import ows.kotlinstudy.subway_application.data.api.StationStorageApi
import ows.kotlinstudy.subway_application.data.db.AppDatabase
import ows.kotlinstudy.subway_application.data.preference.PreferenceManager
import ows.kotlinstudy.subway_application.data.preference.SharedPreferenceManager
import ows.kotlinstudy.subway_application.data.repository.StationRepository
import ows.kotlinstudy.subway_application.data.repository.StationRepositoryImpl

val appModule = module {

    single { Dispatchers.IO }

    // Database
    single { AppDatabase.build(androidApplication())
    single { get<AppDatabase>().stationDao() }}

    // Preference
    single { androidContext().getSharedPreferences("preference",Activity.MODE_PRIVATE) }
    single<PreferenceManager> { SharedPreferenceManager(get())}

    // Api
    single<StationApi> { StationStorageApi(Firebase.storage)}

    // Repository
    single<StationRepository> { StationRepositoryImpl(get(), get(), get(), get()) }
}