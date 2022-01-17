package ows.kotlinstudy.shopping_application.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ows.kotlinstudy.shopping_application.data.entity.product.ProductEntity
import ows.kotlinstudy.shopping_application.domain.GetProductItemUseCase
import ows.kotlinstudy.shopping_application.domain.OrderProductItemUseCase
import ows.kotlinstudy.shopping_application.presentation.BaseViewModel

internal class ProductDetailViewModel(
    private val productId: Long,
    private val getProductItemUseCase: GetProductItemUseCase,
    private val orderProductItemUseCase: OrderProductItemUseCase
): BaseViewModel() {

    private val _productDetailStateLiveData = MutableLiveData<ProductDetailState>(ProductDetailState.Uninitialized)
    val productDetailStateLiveData: LiveData<ProductDetailState> = _productDetailStateLiveData

    private lateinit var productEntity: ProductEntity

    override fun fetchData(): Job  = viewModelScope.launch{
        setState(ProductDetailState.Loading)
        getProductItemUseCase(productId)?.let { product ->
            productEntity = product
            setState(ProductDetailState.Success(product))
        } ?: kotlin.run{
            setState(ProductDetailState.Error)
        }
    }

    fun orderProduct() = viewModelScope.launch {
        if(::productEntity.isInitialized){
            val productId = orderProductItemUseCase(productEntity)
            if(productEntity.id == productId){
                setState(ProductDetailState.Order)
            }
        }else{
            setState(ProductDetailState.Error)
        }
    }

    private fun setState(state: ProductDetailState){
        _productDetailStateLiveData.postValue(state)
    }
}