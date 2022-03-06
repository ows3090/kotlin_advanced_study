package ows.kotlinstudy.delivery_application.presenter.addtrackingitem

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ows.kotlinstudy.delivery_application.data.entity.ShippingCompany
import ows.kotlinstudy.delivery_application.data.entity.TrackingItem
import ows.kotlinstudy.delivery_application.data.repository.ShippingCompanyRepository
import ows.kotlinstudy.delivery_application.data.repository.TrackingItemRepository
import kotlin.Exception

class AddTrackingItemPresenter(
    private val view: AddTrackingItemsContract.View,
    private val shippingCompanyRepository: ShippingCompanyRepository,
    private val trackingItemRepository: TrackingItemRepository
): AddTrackingItemsContract.Presenter {

    override val scope: CoroutineScope = MainScope()

    override var invoice: String? = null
    override var shippingCompanies: List<ShippingCompany>? = null
    override var selectedShippingCompany: ShippingCompany? = null

    override fun onViewCreated() {
        fetchShippingCompanies()
    }

    override fun onDestroyView() {}

    override fun fetchShippingCompanies() {
        scope.launch {
            view.showShippingCompaniesLoadingIndicator()
            if(shippingCompanies.isNullOrEmpty()){
                shippingCompanies = shippingCompanyRepository.getShippingCompanies()
            }

            shippingCompanies?.let { view.showCompanies(it) }
            view.hideShippingCompaniesLoadingIndicator()
        }
    }

    override fun changeSelectedShippingCompany(companyName: String) {
        selectedShippingCompany = shippingCompanies?.find { it.name == companyName }
        enableSaveButtonIfAvailable()
    }

    override fun changeShippingInvoice(invoice: String) {
        Log.d("msg","changeShippingInvoice ${invoice}")
        this.invoice = invoice
        enableSaveButtonIfAvailable()
    }

    override fun saveTrackingItem() {
        scope.launch {
            try{
                view.showSaveTrackingItemIndicator()
                trackingItemRepository.saveTrackingItem(
                    TrackingItem(
                        invoice!!,
                        selectedShippingCompany!!
                    )
                )

                view.finish()
            }catch (exception: Exception){
                view.showErrorToast(exception.message ?: "서비스에 문제가 생겨서 운송장을 추가하지 못했어요")
            }finally {
                view.hideSaveTrackingItemIndicator()
            }
        }
    }

    private fun enableSaveButtonIfAvailable(){
        Log.d("msg","enableSaveButtonIfAvailable ${invoice}, ${selectedShippingCompany}")
        if(!invoice.isNullOrBlank() && selectedShippingCompany != null){
            view.enableSaveButton()
        }else{
            view.disableSaveButton()
        }
    }
}