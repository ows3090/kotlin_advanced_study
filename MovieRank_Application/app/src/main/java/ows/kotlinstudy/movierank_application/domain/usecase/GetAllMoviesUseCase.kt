package ows.kotlinstudy.movierank_application.domain.usecase

import ows.kotlinstudy.movierank_application.data.repository.MovieRepository
import ows.kotlinstudy.movierank_application.domain.model.Movie

class GetAllMoviesUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(): List<Movie> = movieRepository.getAllMovies()
}