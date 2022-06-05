package ows.kotlinstudy.movierank_application.data.api

import com.google.firebase.firestore.FieldPath
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
            .map { it.toObject() }

    /**
     * whereIn : 해당 컬렉션 안에 포함되어 있는지를 쿼리해주는 메소드
     * documentId로 쿼리 하기위해서는 FieldPath.documentId() 사용
     */
    override suspend fun getMovies(movieIds: List<String>): List<Movie> =
        firestore.collection("movies")
            .whereIn(FieldPath.documentId(), movieIds)
            .get()
            .await()
            .map { it.toObject() }
}