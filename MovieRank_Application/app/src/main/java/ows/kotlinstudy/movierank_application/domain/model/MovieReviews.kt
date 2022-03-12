package ows.kotlinstudy.movierank_application.domain.model

data class MovieReviews(
    val myReview: Review?,
    val othersReviews: List<Review>
)
