package ows.kotlinstudy.delivery_application.presenter.trackingitems

import kotlinx.coroutines.launch
import ows.kotlinstudy.delivery_application.data.entity.TrackingInformation
import ows.kotlinstudy.delivery_application.data.entity.TrackingItem
import ows.kotlinstudy.delivery_application.data.repository.TrackingItemRepository
import java.lang.Exception

class TrackingItemsPresenter(
    private val view: TrackingItemsContract.View,
    private val trackingItemRepository: TrackingItemRepository
): TrackingItemsContract.Presenter{

    override var trackingItemInformation: List<Pair<TrackingItem, TrackingInformation>> = emptyList()

    override fun onViewCreated() {
        fetchTrackingInformation()
    }

    override fun onDestroyView() {}

    override fun refresh() {
        fetchTrackingInformation(true)
    }

    private fun fetchTrackingInformation(forceFetch: Boolean = false) = scope.launch {
        try{
            view.showLoadingIndicator()

            if(trackingItemInformation.isEmpty() || forceFetch){
                trackingItemInformation = trackingItemRepository.getTrackingItemInformation()
            }

            if(trackingItemInformation.isEmpty()){
                view.showNoDataDescription()
            }else{
                view.showTrackingItemInformation(trackingItemInformation)
            }
        }catch (exception: Exception){
            exception.printStackTrace()
        }finally {
            view.hideLoadingIndicator()
        }
    }
}