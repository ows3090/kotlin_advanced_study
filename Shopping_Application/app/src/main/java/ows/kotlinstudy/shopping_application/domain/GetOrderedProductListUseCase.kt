package ows.kotlinstudy.shopping_application.domain

import ows.kotlinstudy.shopping_application.data.entity.product.ProductEntity
import ows.kotlinstudy.shopping_application.data.repository.ProductRepository

class GetOrderedProductListUseCase(
    private val productRepository: ProductRepository
): UseCase {

    suspend operator fun invoke(): List<ProductEntity>{
        return productRepository.getLocalProductList()
    }
}