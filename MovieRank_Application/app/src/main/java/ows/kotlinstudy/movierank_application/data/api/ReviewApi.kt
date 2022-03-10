package ows.kotlinstudy.movierank_application.data.api

import ows.kotlinstudy.movierank_application.domain.model.Review

interface ReviewApi {

    suspend fun getLatestReview(movieId: String): Review?

    suspend fun getAllReviews(movieId: String): List<Review>
}