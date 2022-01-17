package ows.kotlinstudy.shopping_application.di

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ows.kotlinstudy.shopping_application.data.db.provideDB
import ows.kotlinstudy.shopping_application.data.db.provideToDao
import ows.kotlinstudy.shopping_application.data.network.buildOkHttpClient
import ows.kotlinstudy.shopping_application.data.network.provideGsonConverterFactory
import ows.kotlinstudy.shopping_application.data.network.provideProductApiService
import ows.kotlinstudy.shopping_application.data.network.provideProductRetrofit
import ows.kotlinstudy.shopping_application.data.repository.DefaultProductRepository
import ows.kotlinstudy.shopping_application.data.repository.ProductRepository
import ows.kotlinstudy.shopping_application.domain.GetProductItemUseCase
import ows.kotlinstudy.shopping_application.domain.GetProductListUseCase
import ows.kotlinstudy.shopping_application.domain.OrderProductItemUseCase
import ows.kotlinstudy.shopping_application.presentation.detail.ProductDetailViewModel
import ows.kotlinstudy.shopping_application.presentation.list.ProductListViewModel
import ows.kotlinstudy.shopping_application.presentation.main.MainViewModel
import ows.kotlinstudy.shopping_application.presentation.profile.ProfileViewModel


val appModule = module {

    single { provideGsonConverterFactory() }
    single { buildOkHttpClient() }
    single { provideProductRetrofit(get(), get()) }
    single { provideProductApiService(get()) }

    // Coroutines Dispatcher
    single { Dispatchers.IO }
    single { Dispatchers.Main }

    // Repository
    single<ProductRepository> { DefaultProductRepository(get(), get(), get()) }

    // UseCases
    factory { GetProductItemUseCase(get()) }
    factory { GetProductListUseCase(get()) }
    factory { OrderProductItemUseCase(get()) }

    // ViewModels
    viewModel { MainViewModel() }
    viewModel { ProductListViewModel(get()) }
    viewModel { ProfileViewModel() }
    viewModel { (productId: Long) -> ProductDetailViewModel(productId, get(), get()) }

    // Database
    single { provideDB(androidApplication()) }
    single { provideToDao(get()) }

}