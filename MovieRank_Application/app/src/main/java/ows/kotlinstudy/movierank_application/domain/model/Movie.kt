package ows.kotlinstudy.movierank_application.domain.model

import com.google.firebase.firestore.DocumentId

/**
 * FireStore에서 사용하는 객체는 default 생성자가 존재해야 한다.
 */
data class Movie(
    @DocumentId
    val id: String? = null,

    /**
     * 파이어베이스가 Boolean 필드명을 임의로 변경
     * 변경을 막기 위해 @field:JvmField -> getter/setter 생성 금지
     */
    @field:JvmField
    val isFeatured: Boolean? = null,

    val title: String? = null,
    val actors: String? = null,
    val country: String? = null,
    val director: String? = null,
    val genre: String? = null,
    val posterUrl: String? = null,
    val rating: String? = null,
    val averageScore: Float? = null,
    val numberOfScore: Int? = null,
    val releaseYear: Int? = null,
    val runtime: Int? = null
)
