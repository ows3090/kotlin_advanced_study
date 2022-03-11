package ows.kotlinstudy.movierank_application.presentation.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ows.kotlinstudy.movierank_application.databinding.ItemReviewedMovieBinding
import ows.kotlinstudy.movierank_application.domain.model.Movie
import ows.kotlinstudy.movierank_application.domain.model.ReviewedMovie
import ows.kotlinstudy.movierank_application.extension.toDecimalFormatString

class MyPageAdapter : RecyclerView.Adapter<MyPageAdapter.ViewHolder>() {

    var reviewdMovies: List<ReviewedMovie> = emptyList()
    var onMovieClickListener: ((Movie) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageAdapter.ViewHolder =
        ViewHolder(
            ItemReviewedMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(reviewdMovies.get(position))
    }

    override fun getItemCount(): Int = reviewdMovies.size

    inner class ViewHolder(private val binding: ItemReviewedMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onMovieClickListener?.invoke(reviewdMovies[adapterPosition].movie)
            }
        }

        fun bind(item: ReviewedMovie){
            Glide.with(binding.root)
                .load(item.movie.posterUrl)
                .into(binding.posterImageView)

            binding.myScoreTextView.text = item.myReview.score?.toDecimalFormatString("0.0")
        }
    }
}
