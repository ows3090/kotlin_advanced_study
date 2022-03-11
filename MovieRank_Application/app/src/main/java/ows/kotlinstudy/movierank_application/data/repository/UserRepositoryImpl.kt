package ows.kotlinstudy.movierank_application.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ows.kotlinstudy.movierank_application.data.api.UserApi
import ows.kotlinstudy.movierank_application.data.preference.PreferenceManager
import ows.kotlinstudy.movierank_application.domain.model.User

class UserRepositoryImpl(
    private val userApi: UserApi,
    private val preferenceManager: PreferenceManager,
    private val dispatcher: CoroutineDispatcher
) : UserRepository {

    override suspend fun getUser(): User? = withContext(dispatcher) {
        preferenceManager.getString(KEY_USER_ID)?.let { User(it) }
    }

    override suspend fun saveUser(user: User) = withContext(dispatcher) {
        val newUser = userApi.saveUser(user)
        preferenceManager.putString(KEY_USER_ID, newUser.id!!)
    }

    companion object {
        private const val KEY_USER_ID = "KEY_USER_ID"
    }
}