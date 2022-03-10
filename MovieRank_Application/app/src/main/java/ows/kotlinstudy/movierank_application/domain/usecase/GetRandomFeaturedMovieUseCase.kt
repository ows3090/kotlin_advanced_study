package ows.kotlinstudy.movierank_application.domain.usecase

import android.util.Log
import ows.kotlinstudy.movierank_application.data.repository.MovieRepository
import ows.kotlinstudy.movierank_application.data.repository.ReviewRepository
import ows.kotlinstudy.movierank_application.domain.model.FeaturedMovie

class GetRandomFeaturedMovieUseCase(
    private val movieRepository: MovieRepository,
    private val reviewRepository: ReviewRepository
) {

    suspend operator fun invoke(): FeaturedMovie? {
        val featuredMovies = movieRepository.getAllMovies()
            .filter { it.id.isNullOrBlank().not() }
            .filter { it.isFeatured == true }

        Log.d("msg","GetRandomFeaturedMovieUseCase ${featuredMovies}")

        if(featuredMovies.isNullOrEmpty()){
            return null
        }

        return featuredMovies.random()
            .let { movie ->
                val latesReview = reviewRepository.getLatestReview(movie.id!!)
                Log.d("msg","latesReview ${latesReview}")
                FeaturedMovie(movie, latesReview)
            }
    }
}