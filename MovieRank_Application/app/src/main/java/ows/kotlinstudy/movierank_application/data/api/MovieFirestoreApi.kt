package ows.kotlinstudy.movierank_application.data.api

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import ows.kotlinstudy.movierank_application.domain.model.Movie

class MovieFirestoreApi(
    private val firestore: FirebaseFirestore
): MovieApi {

    override suspend fun getAllMovies(): List<Movie> =
        /**
         * get() : 모든 데이터를 가져온 뒤, addOnCompleteListener를 통해 콜백 호출
         * 코루틴을 사용할 것이기에 await 메소드 사용 -> coroutines-play-service 사용해야함.
         */
        firestore.collection("movies")
            .get()
            .await()
            .map { it.toObject<Movie>() }

}