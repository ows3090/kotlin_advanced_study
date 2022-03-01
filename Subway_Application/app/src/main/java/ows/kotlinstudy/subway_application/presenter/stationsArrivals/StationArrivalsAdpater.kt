package ows.kotlinstudy.subway_application.presenter.stationsArrivals

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ows.kotlinstudy.subway_application.databinding.ItemArrivalBinding
import ows.kotlinstudy.subway_application.domain.ArrivalInformation

class StationArrivalsAdpater: RecyclerView.Adapter<StationArrivalsAdpater.ViewHolder>() {

    var data: List<ArrivalInformation> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemArrivalBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data.get(position))
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val viewBinding: ItemArrivalBinding): RecyclerView.ViewHolder(viewBinding.root){
        fun bind(arrival: ArrivalInformation){
            viewBinding.labelTextView.badgeColor = arrival.subway.color
            viewBinding.labelTextView.text = "${arrival.subway.label} - ${arrival.destination}"
            viewBinding.destinationTextView.text = "\uD83D\uDEA9 ${arrival.destination}"
            viewBinding.arrivalMessageTextView.text = arrival.message
            viewBinding.arrivalMessageTextView.setTextColor(if(arrival.message.contentEquals("당역")) Color.RED else Color.DKGRAY)
            viewBinding.updatedTimeTextView.text = "측정 시간 : ${arrival.updatedAt}"
        }
    }
}