package ows.kotlinstudy.movierank_application.data.api

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import ows.kotlinstudy.movierank_application.domain.model.User

class UserFirestoreApi(
    private val firestore: FirebaseFirestore
): UserApi {

    override suspend fun saveUser(user: User): User =
        firestore.collection("users")
            .add(user)
            .await()
            .let { User(it.id) }
}