package ows.kotlinstudy.movierank_application.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import ows.kotlinstudy.movierank_application.data.api.MovieApi
import ows.kotlinstudy.movierank_application.data.api.MovieFirestoreApi
import ows.kotlinstudy.movierank_application.data.api.ReviewApi
import ows.kotlinstudy.movierank_application.data.api.ReviewFirestoreApi
import ows.kotlinstudy.movierank_application.data.repository.MovieRepository
import ows.kotlinstudy.movierank_application.data.repository.MovieRepositoryImpl
import ows.kotlinstudy.movierank_application.data.repository.ReviewRepository
import ows.kotlinstudy.movierank_application.data.repository.ReviewRepositoryImpl
import ows.kotlinstudy.movierank_application.domain.model.Movie
import ows.kotlinstudy.movierank_application.domain.usecase.GetAllMoviesUseCase
import ows.kotlinstudy.movierank_application.domain.usecase.GetAllReviewsUseCase
import ows.kotlinstudy.movierank_application.domain.usecase.GetRandomFeaturedMovieUseCase
import ows.kotlinstudy.movierank_application.presentation.home.HomeContract
import ows.kotlinstudy.movierank_application.presentation.home.HomeFragment
import ows.kotlinstudy.movierank_application.presentation.home.HomePresenter
import ows.kotlinstudy.movierank_application.presentation.reviews.MovieReviewsContract
import ows.kotlinstudy.movierank_application.presentation.reviews.MovieReviewsFragment
import ows.kotlinstudy.movierank_application.presentation.reviews.MovieReviewsPresenter

val appModule = module {
    single { Dispatchers.IO }
}

val dataModule = module {
    single { Firebase.firestore }

    single<MovieApi> { MovieFirestoreApi(get()) }
    single<ReviewApi> { ReviewFirestoreApi(get()) }

    single<MovieRepository> { MovieRepositoryImpl(get(), get()) }
    single<ReviewRepository> { ReviewRepositoryImpl(get(), get()) }
}

val domainModule = module {
    factory { GetRandomFeaturedMovieUseCase(get(), get()) }
    factory { GetAllMoviesUseCase(get()) }
    factory { GetAllReviewsUseCase(get()) }
}

val presenterModule = module {
    scope<HomeFragment> {
        scoped<HomeContract.Presenter> { HomePresenter(getSource(), get(), get()) }
    }
    scope<MovieReviewsFragment> {
        scoped<MovieReviewsContract.Presenter> { (movie: Movie) -> MovieReviewsPresenter(getSource(),movie, get()) }
    }
}