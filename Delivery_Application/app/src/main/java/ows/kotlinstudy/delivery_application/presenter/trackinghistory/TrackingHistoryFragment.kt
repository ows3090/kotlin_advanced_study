package ows.kotlinstudy.delivery_application.presenter.trackinghistory

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.scope.ScopeFragment
import org.koin.core.parameter.parametersOf
import ows.kotlinstudy.delivery_application.data.entity.TrackingInformation
import ows.kotlinstudy.delivery_application.data.entity.TrackingItem
import ows.kotlinstudy.delivery_application.databinding.FragmentTrackingHistoryBinding

class TrackingHistoryFragment : ScopeFragment(), TrackingHistoryContract.View {

    override val presenter: TrackingHistoryContract.Presenter by inject {
        parametersOf(arguments.item, arguments.information)
    }

    private var binding: FragmentTrackingHistoryBinding? = null

    private val arguments: TrackingHistoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTrackingHistoryBinding.inflate(inflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        bindViews()
        presenter.onViewCreated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    private fun initViews() {
        /**
         * NestedScrollView 내부의 RecyclerView 가 있을 경우 ViewHolder를 재사용 하지 못하는 이슈 발생
         * -> 즉, onCreatViewHolder가 한번에 호출되어 모두 생성
         */
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = TrackingHistoryAdapter()
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }
    }

    private fun bindViews(){
        binding?.refreshLayout?.setOnRefreshListener {
            presenter.refresh()
        }

        binding?.deleteTrackingItemButton?.setOnClickListener {
            presenter.deleteTrackingItem()
        }
    }

    override fun hideLoadingIndicator() {
        binding?.refreshLayout?.isRefreshing = false
    }

    @SuppressLint("SetTextI18n")
    override fun showTrackingItemInformation(
        trackingItem: TrackingItem,
        trackingInformation: TrackingInformation
    ) {
        binding?.resultTextView?.text = trackingInformation.level?.label
        binding?.invoiceTextView?.text = "${trackingInformation.invoiceNo} (${trackingItem.company.name})"

        binding?.itemNameTextView?.text =
            if(trackingInformation.itemName.isNullOrBlank()){
                "이름 없음"
            }else{
                trackingInformation.itemName
            }

        (binding?.recyclerView?.adapter as TrackingHistoryAdapter)?.run{
            data = trackingInformation.trackingDetails ?: emptyList()
            notifyDataSetChanged()
        }
    }

    override fun finish() {
        findNavController().popBackStack()
    }
}