package ows.kotlinstudy.camera_application.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ows.kotlinstudy.camera_application.databinding.ViewholderImageBinding
import ows.kotlinstudy.camera_application.extensions.loadCenterCrop

class ImageViewPagerAdapter(
    var uriList: List<Uri>
):RecyclerView.Adapter<ImageViewPagerAdapter.ImageViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(ViewholderImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bindData(uriList.get(position))
    }

    override fun getItemCount(): Int = uriList.size

    class ImageViewHolder(
        private val binding: ViewholderImageBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bindData(uri: Uri) = with(binding){
            imageView.loadCenterCrop(uri.toString())
        }
    }
}