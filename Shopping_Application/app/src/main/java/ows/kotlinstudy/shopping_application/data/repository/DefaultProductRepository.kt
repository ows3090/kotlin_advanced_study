package ows.kotlinstudy.shopping_application.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ows.kotlinstudy.shopping_application.data.entity.product.ProductEntity
import ows.kotlinstudy.shopping_application.data.network.ProductApiService

class DefaultProductRepository(
    private val productApi: ProductApiService,
    private val ioDispatcher: CoroutineDispatcher
): ProductRepository{
    override suspend fun getProductList(): List<ProductEntity> = withContext(ioDispatcher){
        TODO("Not yet implemented")
    }

    override suspend fun getLocalProductList(): List<ProductEntity> = withContext(ioDispatcher){
        TODO("Not yet implemented")
    }

    override suspend fun insertProductItem(productItem: ProductEntity): Long = withContext(ioDispatcher){
        TODO("Not yet implemented")
    }

    override suspend fun insertProductList(productList: List<ProductEntity>) = withContext(ioDispatcher){
        TODO("Not yet implemented")
    }

    override suspend fun updateProductItem(productItem: ProductEntity) = withContext(ioDispatcher){
        TODO("Not yet implemented")
    }

    override suspend fun getProductItem(itemId: Long): ProductEntity? = withContext(ioDispatcher){
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() = withContext(ioDispatcher){
        TODO("Not yet implemented")
    }

    override suspend fun deleteProductItem(id: Long) = withContext(ioDispatcher){
        TODO("Not yet implemented")
    }
}