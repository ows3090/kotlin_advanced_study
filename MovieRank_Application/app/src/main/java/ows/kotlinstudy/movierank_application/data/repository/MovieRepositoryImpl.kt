package ows.kotlinstudy.movierank_application.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ows.kotlinstudy.movierank_application.data.api.MovieApi
import ows.kotlinstudy.movierank_application.domain.model.Movie

class MovieRepositoryImpl(
    private val movieApi: MovieApi,
    private val dispachers: CoroutineDispatcher
): MovieRepository {

    override suspend fun getAllMovies(): List<Movie> = withContext(dispachers){
        movieApi.getAllMovies()
    }

    override suspend fun getMovies(movieIds: List<String>): List<Movie> = withContext(dispachers) {
        movieApi.getMovies(movieIds)
    }
}