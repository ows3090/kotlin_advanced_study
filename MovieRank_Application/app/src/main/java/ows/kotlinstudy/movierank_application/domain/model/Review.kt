package ows.kotlinstudy.movierank_application.domain.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Review(
    @DocumentId
    val id: String? = null,

    /**
     * 데이터를 서버에 저장하거나 서버로부터 가져올 때 자동으로 timestamp를 Date 타입으로 가져옴
     */
    @ServerTimestamp
    val createdAt: Date? = null,

    val userId: String? = null,
    val movieId: String? = null,
    val content: String? = null,
    val score: Float? = null
)
