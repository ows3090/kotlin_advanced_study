package ows.kotlinstudy.delivery_application.presenter.addtrackingitem

import ows.kotlinstudy.delivery_application.data.entity.ShippingCompany
import ows.kotlinstudy.delivery_application.presenter.BasePresenter
import ows.kotlinstudy.delivery_application.presenter.BaseView

class AddTrackingItemsContract {

    interface View: BaseView<Presenter>{

        fun showShippingCompaniesLoadingIndicator()

        fun hideShippingCompaniesLoadingIndicator()

        fun showSaveTrackingItemIndicator()

        fun hideSaveTrackingItemIndicator()

        fun showCompanies(companies: List<ShippingCompany>)

        fun enableSaveButton()

        fun disableSaveButton()

        fun showErrorToast(message: String)

        fun finish()
    }

    interface Presenter: BasePresenter{

        var invoice: String?
        var shippingCompanies: List<ShippingCompany>?
        var selectedShippingCompany: ShippingCompany?

        fun fetchShippingCompanies()

        fun changeSelectedShippingCompany(companyName: String)

        fun changeShippingInvoice(invoice: String)

        fun saveTrackingItem()
    }
}