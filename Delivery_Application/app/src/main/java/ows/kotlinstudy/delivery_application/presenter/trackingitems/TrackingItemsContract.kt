package ows.kotlinstudy.delivery_application.presenter.trackingitems

import ows.kotlinstudy.delivery_application.data.entity.TrackingInformation
import ows.kotlinstudy.delivery_application.data.entity.TrackingItem
import ows.kotlinstudy.delivery_application.presenter.BasePresenter
import ows.kotlinstudy.delivery_application.presenter.BaseView

class TrackingItemsContract {

    interface View: BaseView<Presenter>{

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showNoDataDescription()

        fun showTrackingItemInformation(trackingItemInformation: List<Pair<TrackingItem, TrackingInformation>>)
    }

    interface Presenter: BasePresenter{

        var trackingItemInformation: List<Pair<TrackingItem, TrackingInformation>>

        fun refresh()
    }
}