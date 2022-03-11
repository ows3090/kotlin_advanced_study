package ows.kotlinstudy.movierank_application.data.repository

import ows.kotlinstudy.movierank_application.domain.model.User

interface UserRepository {

    suspend fun getUser(): User?

    suspend fun saveUser(user: User)
}