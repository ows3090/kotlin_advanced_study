package ows.kotlinstudy.movierank_application.presentation.reviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import ows.kotlinstudy.movierank_application.databinding.ItemMovieInformationBinding
import ows.kotlinstudy.movierank_application.databinding.ItemMyReviewBinding
import ows.kotlinstudy.movierank_application.databinding.ItemReviewBinding
import ows.kotlinstudy.movierank_application.databinding.ItemReviewFormBinding
import ows.kotlinstudy.movierank_application.domain.model.Movie
import ows.kotlinstudy.movierank_application.domain.model.Review
import ows.kotlinstudy.movierank_application.extension.toAbbreviatedString
import ows.kotlinstudy.movierank_application.extension.toDecimalFormatString

/**
 * RecyclerView에 다른 View들을 추가
 * -> index 수치를 조정하여 계산
 *
 * Actual Data -> Adapter View
 *              0: Header
 * 0: review -> 1: review
 * 1: review -> 2: review
 * 2: review -> 3: review
 * 3: review -> 4: review
 */
class MovieReviewsAdapter(private val movie: Movie) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var myReview: Review? = null
    var reviews: List<Review> = emptyList()

    var onReviewSubmitButtonClickListener: ((content: String, score: Float) -> Unit)? = null
    var onReviewDeleteButtonClickListener: ((Review) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            // Movie 정보를 나타내는 ViewHolder 클래스
            ITEM_VIEW_TYPE_HEADER -> {
                MovieInformationViewHolder(
                    ItemMovieInformationBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }

            // 다른 사람의 Review List의 item을 나타내는 ViewHolder 클래스
            ITEM_VIEW_TYPE_ITEM -> {
                ReviewViewHolder(parent)
            }

            // 내가 리뷰를 하지 않을 때 리뷰 작성을 위해 나타내는 ViewHolder 클래스
            ITEM_VIEW_TYPE_REVIEW_FROM -> {
                ReviewFormViewHolder(
                    ItemReviewFormBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }

            // 나의 리뷰를 보여주는 ViewHolder 클래스
            ITEM_VIEW_TYPE_MY_REVIEW -> {
                MyReviewViewHolder(
                    ItemMyReviewBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            else -> throw RuntimeException("알 수 없는 ViewType 입니다.")
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieInformationViewHolder -> {
                holder.bind(movie)
            }
            is ReviewViewHolder -> {
                holder.bind(reviews[position - 2])
            }
            is MyReviewViewHolder -> {
                myReview ?: return
                holder.bind(myReview!!)
            }
            is ReviewFormViewHolder -> Unit
            else -> throw RuntimeException("알 수 없는 ViewHolder 입니다.")
        }
    }

    override fun getItemCount(): Int = reviews.size + 2

    override fun getItemViewType(position: Int): Int =
        when (position) {
            0 -> ITEM_VIEW_TYPE_HEADER
            1 -> {
                if (myReview == null) {
                    ITEM_VIEW_TYPE_REVIEW_FROM
                } else {
                    ITEM_VIEW_TYPE_MY_REVIEW
                }
            }
            else -> ITEM_VIEW_TYPE_ITEM
        }

    /**
     * Movie 정보를 보여주기 위한 ViewHolder
     */
    inner class MovieInformationViewHolder(private val binding: ItemMovieInformationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Movie) {
            Glide.with(binding.root)
                .load(item.posterUrl)
                .into(binding.posterImageView)

            item.let {
                binding.averageScoreTextView.text =
                    "평점 ${it.averageScore?.toDecimalFormatString("0.0")} (${it.numberOfScore?.toAbbreviatedString()}"
                binding.titleTextView.text = it.title
                binding.additionalInformationTextView.text = "${it.releaseYear}*${it.country}"
                binding.relationsTextView.text = "감독: ${it.director}\n출연진: ${it.actors}"
                binding.genreChipGroup.removeAllViews()
                it.genre?.split(" ")?.forEach { genre ->
                    binding.genreChipGroup.addView(
                        Chip(binding.root.context).apply {
                            isClickable = false
                            text = genre
                        }
                    )
                }
            }
        }
    }

    /**
     * Others Review Item들을 보여주기 위한 ViewHolder
     */
    inner class ReviewViewHolder(
        parent: ViewGroup,
        private val binding: ItemReviewBinding = ItemReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Review) {
            item.let {
                binding.authorIdTextView.text = "${it.userId?.take(3)}***"
                binding.scoreTextView.text = it.score?.toDecimalFormatString("0.0")
                binding.contentsTextView.text = "\"${it.content}\""
            }
        }
    }

    /**
     * Review 작성을 위한 ViewHolder
     */
    inner class ReviewFormViewHolder(private val binding: ItemReviewFormBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.submitButton.setOnClickListener {
                onReviewSubmitButtonClickListener?.invoke(
                    binding.reviewFieldEditText.text.toString(),
                    binding.ratingBar.rating
                )
            }
            binding.reviewFieldEditText.addTextChangedListener { editable ->
                binding.contentLimitTextView.text = "(${editable?.length ?: 0}/50"
                binding.submitButton.isEnabled = (editable?.length ?: 0) >= 5
            }
        }
    }

    /**
     * My Review를 보기 위한 ViewHolder
     */
    inner class MyReviewViewHolder(private val binding: ItemMyReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.deleteButton.setOnClickListener {
                onReviewDeleteButtonClickListener?.invoke(myReview!!)
            }
        }

        fun bind(item: Review) {
            item.let {
                binding.scoreTextView.text = it.score?.toDecimalFormatString("0.0")
                binding.contentsTextView.text = "\"${it.content}\""
            }
        }
    }

    companion object {
        const val ITEM_VIEW_TYPE_HEADER = 0
        const val ITEM_VIEW_TYPE_ITEM = 1
        const val ITEM_VIEW_TYPE_REVIEW_FROM = 2
        const val ITEM_VIEW_TYPE_MY_REVIEW = 3
    }
}