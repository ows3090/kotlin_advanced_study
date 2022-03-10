package ows.kotlinstudy.movierank_application.data.repository

import ows.kotlinstudy.movierank_application.domain.model.Review

interface ReviewRepository {

    suspend fun getLatestReview(movieId: String): Review?

    suspend fun getAllReviews(movieId: String): List<Review>
}