package ows.kotlinstudy.movierank_application.di

import android.app.Activity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ows.kotlinstudy.movierank_application.data.api.*
import ows.kotlinstudy.movierank_application.data.preference.PreferenceManager
import ows.kotlinstudy.movierank_application.data.preference.SharedPreferenceManager
import ows.kotlinstudy.movierank_application.data.repository.*
import ows.kotlinstudy.movierank_application.domain.model.Movie
import ows.kotlinstudy.movierank_application.domain.usecase.GetAllMoviesUseCase
import ows.kotlinstudy.movierank_application.domain.usecase.GetAllReviewsUseCase
import ows.kotlinstudy.movierank_application.domain.usecase.GetMyReviewedMoviesUseCase
import ows.kotlinstudy.movierank_application.domain.usecase.GetRandomFeaturedMovieUseCase
import ows.kotlinstudy.movierank_application.presentation.home.HomeContract
import ows.kotlinstudy.movierank_application.presentation.home.HomeFragment
import ows.kotlinstudy.movierank_application.presentation.home.HomePresenter
import ows.kotlinstudy.movierank_application.presentation.mypage.MyPageContract
import ows.kotlinstudy.movierank_application.presentation.mypage.MyPageFragment
import ows.kotlinstudy.movierank_application.presentation.mypage.MyPagePresenter
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
    single<UserApi> { UserFirestoreApi(get()) }

    single<MovieRepository> { MovieRepositoryImpl(get(), get()) }
    single<ReviewRepository> { ReviewRepositoryImpl(get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get(), get()) }

    single { androidContext().getSharedPreferences("preference", Activity.MODE_PRIVATE) }
    single<PreferenceManager> { SharedPreferenceManager(get()) }
}

val domainModule = module {
    factory { GetRandomFeaturedMovieUseCase(get(), get()) }
    factory { GetAllMoviesUseCase(get()) }
    factory { GetAllReviewsUseCase(get()) }
    factory { GetMyReviewedMoviesUseCase(get(), get(), get()) }
}

val presenterModule = module {
    scope<HomeFragment> {
        scoped<HomeContract.Presenter> { HomePresenter(getSource(), get(), get()) }
    }
    scope<MovieReviewsFragment> {
        scoped<MovieReviewsContract.Presenter> { (movie: Movie) ->
            MovieReviewsPresenter(
                getSource(),
                movie,
                get()
            )
        }
    }

    scope<MyPageFragment> {
        scoped<MyPageContract.Presenter> { MyPagePresenter(getSource(), get()) }
    }
}