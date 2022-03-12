package ows.kotlinstudy.movierank_application.domain.usecase

import ows.kotlinstudy.movierank_application.data.repository.ReviewRepository
import ows.kotlinstudy.movierank_application.domain.model.Review

class DeleteReviewUseCase(
    private val reviewRepository: ReviewRepository
) {

    suspend operator fun invoke(review: Review) =
        reviewRepository.removeReview(review)
}