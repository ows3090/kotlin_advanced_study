package ows.kotlinstudy.movierank_application.data.api

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import ows.kotlinstudy.movierank_application.domain.model.Movie
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


    override suspend fun getAllUserReviews(userId: String): List<Review> =
        firestore.collection("reviews")
            .whereEqualTo("userId",userId)
            .orderBy("createdAt",Query.Direction.DESCENDING)
            .get()
            .await()
            .map { it.toObject<Review>() }

    /**
     * reivew를 작성 시에 Score 점수로 인해 averageScore와 numberOfScore 점수 반영으로 인해 트랜잭션 필요
     */
    override suspend fun addReview(review: Review): Review {
        val newReviewReference = firestore.collection("reviews").document()
        val movieReference = firestore.collection("movies").document(review.movieId!!)

        /**
         * 트랙잭션 : 1 이상의 문서에 대한 읽기 및 쓰기 작업의 집합, 여러 개의 get(), set(), update(), delete() 여러 작업을 하나의 범위에서 진행
         * 이 중에서 하나라도 실패 시 전체 롤백
         */
        firestore.runTransaction { transaction ->
            val movie = transaction.get(movieReference).toObject<Movie>()!!

            val oldAverageScore = movie.averageScore ?: 0f
            val oldNumberOfScore = movie.numberOfScore ?: 0
            val oldTotalScore = oldAverageScore * oldNumberOfScore

            val newNumberOfScore = oldNumberOfScore + 1
            val newAverageScore = (oldTotalScore + (review.score ?: 0f)) / newNumberOfScore

            transaction.set(
                movieReference,
                movie.copy(
                    numberOfScore = newNumberOfScore,
                    averageScore = newAverageScore
                )
            )

            transaction.set(
                newReviewReference,
                review,
                SetOptions.merge()
            )
        }.await()

        return newReviewReference.get().await().toObject<Review>()!!
    }

    /**
     * TODO firestore 데이터 추가
     * 1. add
     * 2. set -> Reference를 먼저 가져온 후, 데이터 setting
     */
    override suspend fun removeReview(review: Review) {
        val reviewReference = firestore.collection("reviews").document(review.id!!)
        val movieReference = firestore.collection("movies").document(review.movieId!!)

        firestore.runTransaction { transaction ->
            val movie = transaction
                .get(movieReference)
                .toObject<Movie>()!!

            val oldAverageScore = movie.averageScore ?: 0f
            val oldNumberOfScore = movie.numberOfScore ?: 0
            val oldTotalScore = oldAverageScore * oldNumberOfScore

            val newNumberOfScore = (oldNumberOfScore -1).coerceAtLeast(0)
            val newAverageScore = if(newNumberOfScore > 0){
                (oldTotalScore - (review.score ?: 0f))/newNumberOfScore
            }else{
                0f
            }

            transaction.set(
                movieReference,
                movie.copy(
                    numberOfScore = newNumberOfScore,
                    averageScore = newAverageScore
                )
            )

            transaction.delete(reviewReference)
        }
    }
}