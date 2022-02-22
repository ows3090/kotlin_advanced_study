package ows.kotlinstudy.sns_application.photo

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ows.kotlinstudy.camera_application.extensions.loadCenterCrop
import ows.kotlinstudy.sns_application.databinding.ViewholderPhotoItemBinding

class PhotoListAdapter(
    private val removePhotoListener: (Uri) -> Unit
): RecyclerView.Adapter<PhotoListAdapter.ProductItemViewHolder>() {

    private var imageUriList: List<Uri> = listOf()

    inner class ProductItemViewHolder(
        private val binding: ViewholderPhotoItemBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bindData(data: Uri) = with(binding){
            photoImageView.loadCenterCrop(data.toString(), 8f)
        }

        fun bindViews(data: Uri) = with(binding){
            closeButton.setOnClickListener{
                removePhotoListener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        val view = ViewholderPhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductItemViewHolder, position: Int) {
        holder.bindData(imageUriList.get(position))
        holder.bindViews(imageUriList.get(position))
    }

    override fun getItemCount(): Int = imageUriList.size

    fun setPhotoList(imageUriList: List<Uri>){
        this.imageUriList = imageUriList
        notifyDataSetChanged()
    }
}