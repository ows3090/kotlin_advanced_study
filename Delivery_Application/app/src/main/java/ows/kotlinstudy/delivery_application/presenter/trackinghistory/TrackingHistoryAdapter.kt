package ows.kotlinstudy.delivery_application.presenter.trackinghistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ows.kotlinstudy.delivery_application.data.entity.TrackingDetail
import ows.kotlinstudy.delivery_application.databinding.ItemTrackingHistoryBinding

class TrackingHistoryAdapter: RecyclerView.Adapter<TrackingHistoryAdapter.ViewHolder>() {

    var data: List<TrackingDetail> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackingHistoryAdapter.ViewHolder = ViewHolder(
        ItemTrackingHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: TrackingHistoryAdapter.ViewHolder, position: Int) {
        holder.bind(data.get(position))
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val binding: ItemTrackingHistoryBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(info: TrackingDetail){
            binding.timeStampTextView.text = info.timeString
            binding.stateTextView.text = info.kind
            binding.locationTextView.text = "@${info.where}"
        }
    }
}