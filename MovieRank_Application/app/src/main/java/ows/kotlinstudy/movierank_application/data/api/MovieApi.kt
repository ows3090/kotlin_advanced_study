package ows.kotlinstudy.movierank_application.data.api

import ows.kotlinstudy.movierank_application.domain.model.Movie

interface MovieApi {
    suspend fun getAllMovies(): List<Movie>

    suspend fun getMovies(movieIds: List<String>): List<Movie>
}