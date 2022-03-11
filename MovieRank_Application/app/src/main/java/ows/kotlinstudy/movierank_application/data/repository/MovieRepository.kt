package ows.kotlinstudy.movierank_application.data.repository

import ows.kotlinstudy.movierank_application.domain.model.Movie

interface MovieRepository {
    suspend fun getAllMovies(): List<Movie>

    suspend fun getMovies(movieIds: List<String>): List<Movie>
}