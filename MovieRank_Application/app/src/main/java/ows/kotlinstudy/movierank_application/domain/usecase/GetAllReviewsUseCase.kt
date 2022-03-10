package ows.kotlinstudy.movierank_application.domain.usecase

import ows.kotlinstudy.movierank_application.data.repository.ReviewRepository
import ows.kotlinstudy.movierank_application.domain.model.Review

class GetAllReviewsUseCase(private val reviewRepository: ReviewRepository) {

    suspend operator fun invoke(movieId: String): List<Review> =
        reviewRepository.getAllReviews(movieId)
}