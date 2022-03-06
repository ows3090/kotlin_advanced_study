package ows.kotlinstudy.delivery_application.presenter.trackingitems

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ows.kotlinstudy.delivery_application.R
import ows.kotlinstudy.delivery_application.data.entity.Level
import ows.kotlinstudy.delivery_application.data.entity.ShippingCompany
import ows.kotlinstudy.delivery_application.data.entity.TrackingInformation
import ows.kotlinstudy.delivery_application.data.entity.TrackingItem
import ows.kotlinstudy.delivery_application.databinding.ItemTrackingBinding
import ows.kotlinstudy.delivery_application.extension.setTextColorRes
import ows.kotlinstudy.delivery_application.extension.toReadableDateString
import java.util.*

class TrackingItemsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: List<Pair<TrackingItem, TrackingInformation>> = emptyList()
    var onClickItemListener: ((TrackingItem, TrackingInformation) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            ItemTrackingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val (item, trackingInformation) = data[position]

        (holder as ViewHolder).bind(item.company, trackingInformation)
    }

    override fun getItemCount(): Int  = data.size

    inner class ViewHolder(private val binding: ItemTrackingBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                data.getOrNull(adapterPosition)?.let { (item, information) ->
                    onClickItemListener?.invoke(item, information)
                }
            }
        }

        fun bind(company: ShippingCompany, information: TrackingInformation){
            binding.updatedAtTextView.text =
                Date(information.lastDetail?.time ?: System.currentTimeMillis()).toReadableDateString()

            binding.levelLabelTextView.text = information.level?.label

            when(information.level){
                Level.COMPLETE -> {
                    binding.levelLabelTextView.setTextColor(R.attr.colorPrimary)
                    binding.root.alpha = 0.5f
                }
                Level.PREPARE -> {
                    binding.levelLabelTextView.setTextColorRes(R.color.orange)
                    binding.root.alpha = 1f
                }
                else -> {
                    binding.levelLabelTextView.setTextColorRes(R.color.green)
                    binding.root.alpha = 1f
                }
            }

            binding.invoiceTextView.text = information.invoiceNo

            if(information.itemName.isNullOrBlank()){
                binding.itemNameTextView.text = "이름 없음"
                binding.itemNameTextView.setTextColorRes(R.color.gray)
            }else{
                binding.itemNameTextView.text = information.itemName
                binding.itemNameTextView.setTextColorRes(R.color.black)
            }

            binding.lastStateTextView.text = information.lastDetail?.let { it.kind + " @${it.where}" }

            binding.companyNameTextView.text = company.name
        }

    }
}

