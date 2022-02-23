package ows.kotlinstudy.sns_application.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ows.kotlinstudy.sns_application.databinding.ItemArticleBinding
import java.text.SimpleDateFormat
import java.util.*

class ArticleAdapter(val onItemClicked: (ArticleModel) -> Unit) : ListAdapter<ArticleModel, ArticleAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(articleModel: ArticleModel) {

            val format = SimpleDateFormat("MM월 dd일")
            val date = Date(articleModel.createAt)

            binding.titleTextView.text = articleModel.title
            binding.dateTextview.text = format.format(date)
            binding.priceTextView.text = articleModel.content

            if (articleModel.imageUrlList.isNotEmpty()) {
                Glide.with(binding.thumbnailImageView)
                    .load(articleModel.imageUrlList.first())
                    .into(binding.thumbnailImageView)
            }

            binding.root.setOnClickListener {
                onItemClicked(articleModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    // adpater의 submitList로 전달해준 리스트와 차이를 계산하고 변화를 처리해준다.
    // ListAdapter 내부에 AsyncListDiffer가 존재하여 처리
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ArticleModel>() {

            // 객체가 동일한지 -> 아이템이 삭제된다던가, 추가되면서 id값이 전체적으로 다 다르면 새롭게 생성, 삭제
            override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                return oldItem.createAt == newItem.createAt
            }

            // 아이템의 데이터가 동일한 지, 객체는 동일하지만 내용이 변경되었을 때
            override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}
