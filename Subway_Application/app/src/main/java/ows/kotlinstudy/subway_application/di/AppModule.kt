package ows.kotlinstudy.subway_application.di

import android.app.Activity
import com.google.firebase.ktx.BuildConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ows.kotlinstudy.subway_application.data.api.StationApi
import ows.kotlinstudy.subway_application.data.api.StationArrivalsApi
import ows.kotlinstudy.subway_application.data.api.StationStorageApi
import ows.kotlinstudy.subway_application.data.api.Url
import ows.kotlinstudy.subway_application.data.db.AppDatabase
import ows.kotlinstudy.subway_application.data.preference.PreferenceManager
import ows.kotlinstudy.subway_application.data.preference.SharedPreferenceManager
import ows.kotlinstudy.subway_application.data.repository.StationRepository
import ows.kotlinstudy.subway_application.data.repository.StationRepositoryImpl
import ows.kotlinstudy.subway_application.presenter.stations.StationsContract
import ows.kotlinstudy.subway_application.presenter.stations.StationsFragment
import ows.kotlinstudy.subway_application.presenter.stations.StationsPresenter
import ows.kotlinstudy.subway_application.presenter.stationsArrivals.StationArrivalsContract
import ows.kotlinstudy.subway_application.presenter.stationsArrivals.StationArrivalsFragment
import ows.kotlinstudy.subway_application.presenter.stationsArrivals.StationArrivalsPresenter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

val appModule = module {

    single { Dispatchers.IO }

    // Database
    single { AppDatabase.build(androidApplication()) }
    single { get<AppDatabase>().stationDao() }

    // Preference
    single { androidContext().getSharedPreferences("preference",Activity.MODE_PRIVATE) }
    single<PreferenceManager> { SharedPreferenceManager(get())}

    // Api
    single {
        OkHttpClient()
            .newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if(BuildConfig.DEBUG){
                        HttpLoggingInterceptor.Level.BODY
                    }else{
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
    }
    single<StationArrivalsApi> {
        Retrofit.Builder().baseUrl(Url.SEOUL_DATA_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create()
    }
    single<StationApi> { StationStorageApi(Firebase.storage)}

    // Repository
    single<StationRepository> { StationRepositoryImpl(get(),get(), get(), get(), get()) }

    // Presentation
    scope<StationsFragment> {
        scoped<StationsContract.Presenter> { StationsPresenter(getSource(), get()) }
    }

    scope<StationArrivalsFragment> {
        scoped<StationArrivalsContract.Presenter> { StationArrivalsPresenter(getSource(), get(), get()) }
    }
}