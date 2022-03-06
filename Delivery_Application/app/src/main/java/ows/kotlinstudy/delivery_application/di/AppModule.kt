package ows.kotlinstudy.delivery_application.di

import android.content.Context.MODE_PRIVATE
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ows.kotlinstudy.delivery_application.data.api.SweetTrackerApi
import ows.kotlinstudy.delivery_application.data.api.Url
import ows.kotlinstudy.delivery_application.data.db.AppDatabase
import ows.kotlinstudy.delivery_application.data.preference.PreferenceManager
import ows.kotlinstudy.delivery_application.data.preference.SharedPreferenceManager
import ows.kotlinstudy.delivery_application.data.repository.ShippingCompanyRepository
import ows.kotlinstudy.delivery_application.data.repository.ShippingCompanyRepositoryImpl
import ows.kotlinstudy.delivery_application.data.repository.TrackingItemRepository
import ows.kotlinstudy.delivery_application.data.repository.TrackingItemRepositoryImpl
import ows.kotlinstudy.delivery_application.presenter.addtrackingitem.AddTrackingItemFragment
import ows.kotlinstudy.delivery_application.presenter.addtrackingitem.AddTrackingItemPresenter
import ows.kotlinstudy.delivery_application.presenter.addtrackingitem.AddTrackingItemsContract
import ows.kotlinstudy.delivery_application.presenter.trackingitems.TrackingItemsContract
import ows.kotlinstudy.delivery_application.presenter.trackingitems.TrackingItemsFragment
import ows.kotlinstudy.delivery_application.presenter.trackingitems.TrackingItemsPresenter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


val appModule = module {

    // IO Dispatcher
    single { Dispatchers.IO }

    // Database
    single { AppDatabase.build(androidApplication()) }
    single { get<AppDatabase>().trackingItemDao() }
    single { get<AppDatabase>().shippingCompanyDao() }

    // Api
    single {
        OkHttpClient()
            .newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
    }

    single<SweetTrackerApi> {
        Retrofit.Builder().baseUrl(Url.SWEET_TRACKER_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create()
    }

    // Preference
    single { androidContext().getSharedPreferences("preference", MODE_PRIVATE) }
    single<PreferenceManager> { SharedPreferenceManager(get()) }

    // Repository
    single<TrackingItemRepository> { TrackingItemRepositoryImpl(get(), get(), get()) }
    single<ShippingCompanyRepository> { ShippingCompanyRepositoryImpl(get(), get(), get(), get()) }


    // Presenter
    scope<TrackingItemsFragment> {
        scoped<TrackingItemsContract.Presenter> { TrackingItemsPresenter(getSource(), get()) }
    }
    scope<AddTrackingItemFragment> {
        scoped<AddTrackingItemsContract.Presenter> { AddTrackingItemPresenter(getSource(),get(), get()) }
    }
}