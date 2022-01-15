package ows.kotlinstudy.shopping_application.data.response

data class ProductsResponse(
    val items : List<ProductsResponse>,
    val count: Int
)
