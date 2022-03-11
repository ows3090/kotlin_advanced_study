package ows.kotlinstudy.movierank_application.domain.usecase

import android.util.Log
import ows.kotlinstudy.movierank_application.data.repository.MovieRepository
import ows.kotlinstudy.movierank_application.data.repository.ReviewRepository
import ows.kotlinstudy.movierank_application.data.repository.UserRepository
import ows.kotlinstudy.movierank_application.domain.model.ReviewedMovie
import ows.kotlinstudy.movierank_application.domain.model.User

class GetMyReviewedMoviesUseCase(
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository,
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(): List<ReviewedMovie> {
        val user = userRepository.getUser()

        if (user == null) {
            userRepository.saveUser(User())
            return emptyList()
        }

        Log.d("msg","${user.id!!} ${reviewRepository.getAllUserReviews(user.id!!)}")
        val reviews = reviewRepository.getAllUserReviews(user.id!!)
            .filter { it.movieId.isNullOrBlank().not() }

        if (reviews.isNullOrEmpty()) {
            Log.d("msg","GetMyReviewedMoviesUseCase reviews null")
            return emptyList()
        }

        return movieRepository
            .getMovies(reviews.map { it.movieId!! })
            .mapNotNull { movie ->
                val relatedReview = reviews.find { it.movieId == movie.id }
                relatedReview?.let { ReviewedMovie(movie, it) }
            }
    }
}