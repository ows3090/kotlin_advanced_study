package ows.kotlinstudy.shopping_application.presentation.list

import ows.kotlinstudy.shopping_application.data.entity.product.ProductEntity

/**
 * sealed class는 객체를 관리하는 집합 클래스, enum class는 상수들을 관리하는 클래스
 * 일반적으로 super class를 상속하는 child class의 종류는 컴파일러가 알지 못하는데
 * sealed class로 상속받은 child class들은 제한을 두어 컴파일러가 알 수 있다.
 *
  */
sealed class ProductListState{
    object UnInitialized: ProductListState()

    object Loading: ProductListState()

    data class Success(
        val productList: List<ProductEntity>
    ): ProductListState()

    object Error: ProductListState()
}
