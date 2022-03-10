package ows.kotlinstudy.movierank_application.data.api

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import ows.kotlinstudy.movierank_application.domain.model.Review

class ReviewFirestoreApi(
    private val firestore: FirebaseFirestore
): ReviewApi {

    /**
     * 복합 indexing 이슈 발생!!
     * 단일 indexing에 대해서는 firestore에서 구축
     * but 여러 개의 필드로 모두 indexing을 지원하기에 특정 공간에 저장하기에 너무 많은 저장소 소요 ex) 12필드에서 2개의 필드로 indexing할 경우 12x12 = 144 경우의 수 생김
     * 따라서 복한 indexing이 필요한 경우에는 요청
     * firestore에서는 에러가 나타날 경우 복합 색인 자동 생성
     */
    override suspend fun getLatestReview(movieId: String): Review? =
        firestore.collection("reviews")
            .whereEqualTo("movieId", movieId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()
            .map { it.toObject<Review>() }
            .firstOrNull()

    override suspend fun getAllReviews(movieId: String): List<Review> =
        firestore.collection("reviews")
            .whereEqualTo("movieId", movieId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .await()
            .map { it.toObject<Review>() }
}