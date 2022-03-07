package ows.kotlinstudy.delivery_application.presenter.trackinghistory

import ows.kotlinstudy.delivery_application.data.entity.TrackingInformation
import ows.kotlinstudy.delivery_application.data.entity.TrackingItem
import ows.kotlinstudy.delivery_application.presenter.BasePresenter
import ows.kotlinstudy.delivery_application.presenter.BaseView

class TrackingHistoryContract {

    interface View: BaseView<Presenter>{

        fun hideLoadingIndicator()

        fun showTrackingItemInformation(trackingItem: TrackingItem, trackingInformation: TrackingInformation)

        fun finish()
    }

    interface Presenter: BasePresenter{

        fun refresh()

        fun deleteTrackingItem()
    }
}