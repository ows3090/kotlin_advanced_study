package ows.kotlinstudy.movierank_application.domain.model

data class FeaturedMovie(
    val movie: Movie,
    val latestReview: Review?
)
