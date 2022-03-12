package ows.kotlinstudy.movierank_application.domain.usecase

import ows.kotlinstudy.movierank_application.data.repository.ReviewRepository
import ows.kotlinstudy.movierank_application.data.repository.UserRepository
import ows.kotlinstudy.movierank_application.domain.model.MovieReviews
import ows.kotlinstudy.movierank_application.domain.model.Review
import ows.kotlinstudy.movierank_application.domain.model.User

class GetAllReviewsUseCase(
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository
) {

    suspend operator fun invoke(movieId: String): MovieReviews {
        val reviews = reviewRepository.getAllReviews(movieId)
        val user = userRepository.getUser()

        if(user == null){
            userRepository.saveUser(User())

            return MovieReviews(null, reviews)
        }
        return MovieReviews(
            reviews.find { it.userId == user.id },
            reviews.filter { it.userId != user.id}
        )
    }
}