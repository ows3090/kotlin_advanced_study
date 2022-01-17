package ows.kotlinstudy.shopping_application.presentation.detail

import ows.kotlinstudy.shopping_application.data.entity.product.ProductEntity

sealed class ProductDetailState{

    object Uninitialized : ProductDetailState()

    object Loading: ProductDetailState()

    data class Success(
        val productEntity: ProductEntity
    ): ProductDetailState()

    object Order: ProductDetailState()

    object Error: ProductDetailState()
}
