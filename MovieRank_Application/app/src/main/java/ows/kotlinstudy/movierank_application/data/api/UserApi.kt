package ows.kotlinstudy.movierank_application.data.api

import ows.kotlinstudy.movierank_application.domain.model.User

interface UserApi {
    suspend fun saveUser(user: User): User
}