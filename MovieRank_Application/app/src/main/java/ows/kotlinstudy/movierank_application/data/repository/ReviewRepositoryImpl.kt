package ows.kotlinstudy.movierank_application.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ows.kotlinstudy.movierank_application.data.api.ReviewApi
import ows.kotlinstudy.movierank_application.domain.model.Review

class ReviewRepositoryImpl(
    private val reviewApi: ReviewApi,
    private val dispatchers: CoroutineDispatcher
): ReviewRepository {

    override suspend fun getLatestReview(movieId: String): Review? = withContext(dispatchers){
        reviewApi.getLatestReview(movieId)
    }
}