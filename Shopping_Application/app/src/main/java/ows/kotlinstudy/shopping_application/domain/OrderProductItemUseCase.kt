package ows.kotlinstudy.shopping_application.domain

import ows.kotlinstudy.shopping_application.data.entity.product.ProductEntity
import ows.kotlinstudy.shopping_application.data.repository.ProductRepository

class OrderProductItemUseCase(
    private val productRepository: ProductRepository
): UseCase {

    suspend operator fun invoke(productEntity: ProductEntity): Long {
        return productRepository.insertProductItem(productEntity)
    }
}